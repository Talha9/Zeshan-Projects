package com.earthmap.satellite.map.location.map.satelliteTrackerModule

import android.content.DialogInterface
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.Ads.MyAppShowAds

import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.NetworkStateReceiver
import com.earthmap.satellite.map.location.map.Utils.constants
import com.earthmap.satellite.map.location.map.Utils.dialogs.InternetDialog
import com.earthmap.satellite.map.location.map.Utils.getAdaptiveAdSize
import com.earthmap.satellite.map.location.map.Utils.satelliteTrackerApi.SatelliteTrackerRetrofit
import com.earthmap.satellite.map.location.map.Utils.satelliteTrackerApi.SatelliteTrackerRetrofitInstance
import com.earthmap.satellite.map.location.map.Utils.satelliteTrackerApi.mvvm.SatteliteTrackerModelFactory
import com.earthmap.satellite.map.location.map.Utils.satelliteTrackerApi.mvvm.SatteliteTrackerRepository
import com.earthmap.satellite.map.location.map.Utils.satelliteTrackerApi.mvvm.SatteliteTrackerViewModel
import com.earthmap.satellite.map.location.map.databinding.ActivitySatteliteTrackerMainBinding
import com.earthmap.satellite.map.location.map.satelliteTrackerModule.callbacks.SatelliteTrackerDialogCallback
import com.earthmap.satellite.map.location.map.satelliteTrackerModule.dialogs.GlobeEarthMapSatteliteSelectionDialog
import com.earthmap.satellite.map.location.map.satelliteTrackerModule.model.SatelliteMarkersModel
import com.earthmap.satellite.map.location.map.satelliteTrackerModule.model.SatelliteTrackerMainModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style

class SatteliteTrackerMainActivity : AppCompatActivity(),
    NetworkStateReceiver.NetworkStateReceiverListener, OnMapReadyCallback {
    private lateinit var mSatelliteViewModel: SatteliteTrackerViewModel
    lateinit var oMapboxMap: MapboxMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    var satelliteDialog: GlobeEarthMapSatteliteSelectionDialog? = null
    lateinit var binding: ActivitySatteliteTrackerMainBinding
    var position: CameraPosition? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    var markersList = ArrayList<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Mapbox.getInstance(
                this,
                constants.mapboxApiKey
            )
        } catch (e: Exception) {
        }
        binding = ActivitySatteliteTrackerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mBannerAdsSmall()
        initLiveGlobeQuizeViews(savedInstanceState)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.bottomSheetDrawer)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        onClickListeners()
        apiCalling(1) //by Default Api Calling By id 1
        setUpHeader()
    }

    private fun onClickListeners() {
        binding.selectCountry.setOnClickListener {
            MyAppShowAds.meidationForClickSimpleLiveStreetView(
                this,
                MyAppAds.admobInterstitialAd, MyAppAds.maxInterstitialAdLiveEarth
            ) {
                satelliteTrackerDialog()
            }
        }

    }

    private fun initLiveGlobeQuizeViews(savedInstanceState: Bundle?) {
        binding!!.header.headerBarTitleTxt.text = "Satellite Tracker"
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onStart()
        binding.mapView.getMapAsync(this)
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog = InternetDialog(this)
    }

    private fun setUpHeader() {
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.oMapboxMap = mapboxMap
        mapboxMap.uiSettings.isCompassEnabled = false
        mapboxMap.setStyle(Style.SATELLITE_STREETS, object : Style.OnStyleLoaded {
            override fun onStyleLoaded(style: Style) {
            }
        })

    }

    private fun satelliteTrackerDialog() {
        satelliteDialog = GlobeEarthMapSatteliteSelectionDialog(this, object :
            SatelliteTrackerDialogCallback {
            override fun onItemSelected(model: SatelliteTrackerMainModel) {
                MyAppShowAds.meidationForClickSimpleLiveStreetView(
                    this@SatteliteTrackerMainActivity,
                    MyAppAds.admobInterstitialAd, MyAppAds.maxInterstitialAdLiveEarth
                ) {
                    binding.progressMainBg.visibility = View.VISIBLE
                    binding.satelliteName.text = model.satelliteCategoryName
                    apiCalling(model.id)
                }
            }
        })
        try {
            satelliteDialog!!.show()
        } catch (e: Exception) {
        }
    }

    fun apiCalling(id: Int) {
        if (markersList.size > 0) {
            Log.d("apiCallingTAG", "before: " + markersList.size)
            for (i in markersList) {
                oMapboxMap!!.removeMarker(i)
            }
            markersList.removeAll(markersList)
            Log.d("apiCallingTAG", "after: " + markersList.size)
            oMapboxMap!!.removeAnnotations()
        }

        val retrofitInstance = SatelliteTrackerRetrofitInstance.getInstance().create(
            SatelliteTrackerRetrofit::class.java
        )
        val mainRepository = SatteliteTrackerRepository(retrofitInstance)
        mSatelliteViewModel =
            ViewModelProvider(this, SatteliteTrackerModelFactory(mainRepository)).get(
                SatteliteTrackerViewModel::class.java
            )
        mSatelliteViewModel.callForData(id)
        mSatelliteViewModel.satelliteTrackerData.observe(this) {
            if (it != null && it.above.size > 0) {
                position = CameraPosition.Builder()
                    .target(LatLng(it.above[0].satlat, it.above[0].satlng))
                    .zoom(2.0) // Sets the zoom
                    .bearing(180.0) // Rotate the camera
                    .tilt(30.0) // Set the camera tilt
                    .build() // Creates a CameraPosition from the builder

                oMapboxMap.animateCamera(
                    CameraUpdateFactory
                        .newCameraPosition(position!!), 5000
                )
                for (i in it.above.indices) {
                    try {
                        val model = it.above[i]
                        generateMarkers(
                            SatelliteMarkersModel(
                                model.satname,
                                model.launchDate,
                                model.satlat,
                                model.satlng
                            )
                        )
                        binding.progressMainBg.visibility = View.GONE
                    } catch (e: Exception) {
                    }
                }

            } else {
                Toast.makeText(this, "No Satellite Found!", Toast.LENGTH_SHORT).show()
            }
        }
        mSatelliteViewModel.errorMessage.observe(this) {
            // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        mSatelliteViewModel.loading.observe(this) {
            if (it) {
                binding.bottomSheet.progressBarPlayerCenter.visibility = View.VISIBLE
            } else {
                binding.bottomSheet.progressBarPlayerCenter.visibility = View.GONE
            }
        }

    }

    private fun generateMarkers(model: SatelliteMarkersModel) {

        val factory = IconFactory.getInstance(this)
        val icon = factory.fromResource(R.drawable.satellite_marker)
        val gson = Gson()
        val markerInfoString = gson.toJson(model)
        markersList.add(
            oMapboxMap!!.addMarker(
                MarkerOptions().position(LatLng(model.latitude, model.longitude))
                    .setSnippet(markerInfoString).icon(icon)
            )
        )
        oMapboxMap!!.setOnMarkerClickListener { marker ->
            val gson = Gson()
            val aMarkerInfo: SatelliteMarkersModel =
                gson.fromJson(marker.snippet, SatelliteMarkersModel::class.java)
            binding.bottomSheet.SatelliteLaunchedDate.text = aMarkerInfo.launchedDate
            binding.bottomSheet.SatelliteNameTxt.text = aMarkerInfo.satelliteName
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            position =
                CameraPosition.Builder().target(LatLng(aMarkerInfo.latitude, aMarkerInfo.longitude))
                    .build()
            oMapboxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position!!), 3000)
            true
        }

    }

    override fun networkAvailable() {
        try {
            internetDialog!!.dismiss()
        } catch (e: Exception) {
        }
    }

    override fun networkUnavailable() {
        try {
            internetDialog!!.show()
            internetDialog!!.setOnKeyListener(DialogInterface.OnKeyListener { dialogInterface, i, keyEvent ->
                if (i == KeyEvent.KEYCODE_BACK) {
                    onBackPressed()
                }
                return@OnKeyListener false
            })
        } catch (e: Exception) {
        }
    }

    override fun onStart() {
        super.onStart()
        binding!!.mapView.onStart()
    }

    override fun onPause() {
        binding!!.mapView.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding!!.mapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        binding!!.mapView.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        binding!!.mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroy() {
        networkStateReceiver!!.removeListener(this)
        unregisterReceiver(networkStateReceiver)
        binding!!.mapView.onDestroy()
        super.onDestroy()
    }

    private fun mBannerAdsSmall() {
        MyAppAds.loadEarthMapBannerForMainMediation(
            binding!!.smallAd.adContainer, this
        )
    }

}