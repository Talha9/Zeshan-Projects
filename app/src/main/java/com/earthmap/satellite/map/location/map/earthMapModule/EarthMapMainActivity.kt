package com.earthmap.satellite.map.location.map.earthMapModule

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.Ads.MyAppShowAds
import com.earthmap.satellite.map.location.map.Geocoders.ConvertPlaceNameToLatLng
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.*
import com.earthmap.satellite.map.location.map.Utils.callbacks.LocationDialogCallback
import com.earthmap.satellite.map.location.map.Utils.callbacks.MapStylesDialogCallback
import com.earthmap.satellite.map.location.map.Utils.dialogs.InternetDialog
import com.earthmap.satellite.map.location.map.Utils.dialogs.LocationDialog
import com.earthmap.satellite.map.location.map.Utils.dialogs.MapStylesDialog
import com.earthmap.satellite.map.location.map.databinding.ActivityEarthMapMainBinding
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin
import com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin

class EarthMapMainActivity : AppCompatActivity(), LocationDialogCallback,
    NetworkStateReceiver.NetworkStateReceiverListener,
    OnMapReadyCallback {
    var binding: ActivityEarthMapMainBinding? = null
    lateinit var mFetchLocation: LocationClass
    lateinit var mLocationService: LocationService
    var gpsEnableDialog: LocationDialog? = null
    var position: CameraPosition? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    var oMapboxMap: MapboxMap? = null
    private var mBuildingPlugin: BuildingPlugin? = null
    var trafficShowingOnMap = false
    var trafficPlugin: TrafficPlugin? = null
    var mMapLayersDialog: MapStylesDialog? = null
    var onTimeLocationCheck = true
    var isSearchAvailable = false
    var destinationLats = 0.0
    var destinationLngs = 0.0
    private var is2D = 0
    var mConvertPlaceNameToLatLng: ConvertPlaceNameToLatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Mapbox.getInstance(
                this,
                constants.mapboxApiKey
            )
        } catch (e: Exception) {
        }
        binding = ActivityEarthMapMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.mapView.onCreate(savedInstanceState)

        onClickListeners()
        initializers()
        mBannerAdsSmall()
    }

    private fun onClickListeners() {
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding!!.header.headerBarSearchBtn.setOnClickListener {
            if (isSearchAvailable) {
                val searchTxt = binding!!.header.headerBarSearchTxt.text.toString().trim()
                if (searchTxt != null) {
                    binding!!.header.headerBarSearchTxt.setText(searchTxt)
                    generateLangFromPlaceName(searchTxt)
                    UtilsFunctionClass.hideKeyboard(this@EarthMapMainActivity)
                } else {
                    Toast.makeText(this, "Please Enter Place..!", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding!!.header.headerBarBackBtn.visibility = View.GONE
                binding!!.header.headerBarTitleTxt.visibility = View.GONE
                binding!!.header.headerBarCancelBtn.visibility = View.VISIBLE
                binding!!.header.headerBarSearchTxt.visibility = View.VISIBLE
                isSearchAvailable = true
            }
        }
        binding!!.header.headerBarSearchTxt.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                val searchTxt = binding!!.header.headerBarSearchTxt.text.toString().trim()
                if (searchTxt != null) {
                    binding!!.header.headerBarSearchTxt.setText(searchTxt)
                    generateLangFromPlaceName(searchTxt)
                    UtilsFunctionClass.hideKeyboard(this@EarthMapMainActivity)
                } else {
                    Toast.makeText(
                        this@EarthMapMainActivity,
                        "Please Enter Place..!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return true
            }
        })
        binding!!.header.headerBarCancelBtn.setOnClickListener {
            binding!!.header.headerBarBackBtn.visibility = View.VISIBLE
            binding!!.header.headerBarTitleTxt.visibility = View.VISIBLE
            binding!!.header.headerBarCancelBtn.visibility = View.GONE
            binding!!.header.headerBarSearchTxt.visibility = View.GONE
            isSearchAvailable = false
        }
        binding!!.currentLocationBtn.setOnClickListener {
            onTimeLocationCheck = true
            if (constants.mLatitude != 0.0 && constants.mLongitude != 0.0) {
                getLocation(constants.mLatitude, constants.mLongitude)
            }
        }
        binding!!.map2d3dBtn.setOnClickListener {
            MyAppShowAds.meidationForClickSimpleLiveStreetView(
                this,
                MyAppAds.admobInterstitialAd,MyAppAds.maxInterstitialAdLiveEarth
            ) {
                if (oMapboxMap != null && mBuildingPlugin != null) {
                    if (is2D == 0) {
                        is2D = UtilsFunctionClass.animateCamIn3DAngle(
                            LatLng(
                                destinationLats,
                                destinationLngs
                            ), oMapboxMap!!, mBuildingPlugin!!
                        )
                    } else {
                        is2D = UtilsFunctionClass.animateCamIn2DAngle(
                            LatLng(
                                destinationLats,
                                destinationLngs
                            ), oMapboxMap!!, mBuildingPlugin!!
                        )
                    }
                }
            }

        }
        binding!!.mapLayersBtn.setOnClickListener {
            MyAppShowAds.meidationForClickSimpleLiveStreetView(
                this,
                MyAppAds.admobInterstitialAd,MyAppAds.maxInterstitialAdLiveEarth
            ) {
                mMapLayersDialog = MapStylesDialog(this, object : MapStylesDialogCallback {
                    override fun onMapDefaultMapClick() {
                        if (trafficShowingOnMap) {
                            if (trafficPlugin != null) {
                                trafficPlugin!!.setVisibility(true)
                            }
                        } else {
                            if (trafficPlugin != null) {
                                trafficPlugin!!.setVisibility(false)
                            }
                        }
                        oMapboxMap!!.setStyle(Style.MAPBOX_STREETS)
                        try {
                            mMapLayersDialog!!.dismiss()
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }

                    }

                    override fun onMapNightMapClick() {
                        if (trafficShowingOnMap) {
                            if (trafficPlugin != null) {
                                trafficPlugin!!.setVisibility(true)
                            }
                        } else {
                            if (trafficPlugin != null) {
                                trafficPlugin!!.setVisibility(false)
                            }
                        }
                        oMapboxMap!!.setStyle(Style.DARK)
                        try {
                            mMapLayersDialog!!.dismiss()
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onMapSatelliteMapClick() {
                        if (trafficShowingOnMap) {
                            if (trafficPlugin != null) {
                                trafficPlugin!!.setVisibility(true)
                            }
                        } else {
                            if (trafficPlugin != null) {
                                trafficPlugin!!.setVisibility(false)
                            }
                        }
                        oMapboxMap!!.setStyle(Style.SATELLITE_STREETS)

                        try {
                            mMapLayersDialog!!.dismiss()
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }

                })
                try {
                    mMapLayersDialog!!.show()
                } catch (e: Exception) {
                }
            }

        }
    }

    override fun onBackPressed() {
        MyAppShowAds.mediationBackPressedSimple(this, MyAppAds.admobInterstitialAd,MyAppAds.maxInterstitialAdLiveEarth) {
            super.onBackPressed()
        }
    }

    private fun initializers() {
        binding!!.header.headerBarTitleTxt.text = "Earth Map"
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        binding!!.mapView.onStart()
        binding!!.mapView.getMapAsync(this)
        internetDialog = InternetDialog(this)
        binding!!.mapView.getMapAsync(this)
        mFetchLocation = LocationClass(this)
        mFetchLocation.initLocationRequest()
        gpsEnableDialog = LocationDialog(this, this)
        mLocationService = LocationService(this, gpsEnableDialog!!)
        try {
            val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
            registerReceiver(mLocationService, filter)
        } catch (e: Exception) {
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

    override fun onEnabledGPS() {
        try {
            val callGPSSettingIntent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            startActivity(callGPSSettingIntent)
        } catch (e: Exception) {
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        oMapboxMap = mapboxMap
        mapboxMap.uiSettings.isCompassEnabled = false
        oMapboxMap!!.setStyle(
            Style.SATELLITE_STREETS
        ) { style ->
            enableLocationComponent(style)
            trafficPlugin = TrafficPlugin(binding!!.mapView, mapboxMap, style)
            mBuildingPlugin = BuildingPlugin(binding!!.mapView, mapboxMap, style)
            try {
                mBuildingPlugin!!.setOpacity(1.0f)
                mBuildingPlugin!!.setMinZoomLevel(10f)
            } catch (e: Exception) {
            }
            getLocation(constants.mLatitude, constants.mLongitude)
        }
    }

    private fun placeMarker(latitude: Double, longitude: Double) {
        oMapboxMap!!.removeAnnotations()
        val factory = IconFactory.getInstance(this)
        val icon = factory.fromResource(com.mapbox.mapboxsdk.R.drawable.mapbox_marker_icon_default)
        oMapboxMap!!.addMarker(
            MarkerOptions().position(LatLng(latitude, longitude)).icon(icon)
        )
        binding!!.progressMainBg.visibility = View.GONE
    }

    private fun getLocation(mLatitude: Double, mLongitude: Double) {
        if (onTimeLocationCheck) {
            destinationLats = mLatitude
            destinationLngs = mLongitude
            placeMarker(mLatitude, mLongitude)
            position = CameraPosition.Builder()
                .target(
                    LatLng(
                        mLatitude,
                        mLongitude
                    )
                ) // Sets the new camera position
                .zoom(10.0) // Sets the zoom
                .bearing(180.0) // Rotate the camera
                .tilt(30.0) // Set the camera tilt
                .build() // Creates a CameraPosition from the builder


            oMapboxMap!!.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(position!!), 5000
            )
            onTimeLocationCheck = false
        }
    }

    fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponent: LocationComponent = oMapboxMap!!.locationComponent

            val customOptions = LocationComponentOptions.builder(this)
                .elevation(5f)
                .foregroundTintColor(getColor(R.color.ThemeColor))
                .accuracyColor(getColor(R.color.themetrans))
                .accuracyAlpha(0.3f)
                .accuracyAnimationEnabled(true)
                .build()


            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .locationComponentOptions(customOptions)
                    .build()
            )

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationComponent.isLocationComponentEnabled = true

            locationComponent.cameraMode = CameraMode.TRACKING

            locationComponent.renderMode = RenderMode.COMPASS
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                44
            )
        }
    }

    private fun generateLangFromPlaceName(placeName: String) {

        mConvertPlaceNameToLatLng = ConvertPlaceNameToLatLng(
            this,
            placeName,
            object : ConvertPlaceNameToLatLng.GeoTaskCallbackLatlngs {
                override fun onSuccessLocationFetched(fetchedLatLngs: LatLng?) {
                    if (fetchedLatLngs != null) {
                        if (fetchedLatLngs.latitude != null && fetchedLatLngs.longitude != null) {
                            onTimeLocationCheck = true
                            getLocation(fetchedLatLngs.latitude, fetchedLatLngs.longitude)
                            destinationLats = fetchedLatLngs.latitude
                            destinationLngs = fetchedLatLngs.longitude
                        } else {
                            Toast.makeText(
                                this@EarthMapMainActivity,
                                "Location not Found..!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@EarthMapMainActivity,
                            "Location not Found..!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailedLocationFetched() {
                    Toast.makeText(
                        this@EarthMapMainActivity,
                        "Internet ERROR..!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        try {
            mConvertPlaceNameToLatLng!!.execute()
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
        unregisterReceiver(mLocationService)
        binding!!.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                gpsEnableDialog!!.show()
            } catch (e: Exception) {
            }
        } else {
            try {
                gpsEnableDialog!!.dismiss()
            } catch (e: Exception) {
            }
        }
    }

    private fun mBannerAdsSmall() {
        MyAppAds.loadEarthMapBannerForMainMediation(
            binding!!.smallAd.adContainer, this
        )
    }

}