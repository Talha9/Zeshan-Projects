package com.earthmap.satellite.map.location.map.nearBy.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.Ads.MyAppShowAds
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.*
import com.earthmap.satellite.map.location.map.Utils.MapNavigation.MapNavigationActivity
import com.earthmap.satellite.map.location.map.Utils.MapNavigation.model.NavigationModel
import com.earthmap.satellite.map.location.map.Utils.callbacks.LocationDialogCallback
import com.earthmap.satellite.map.location.map.Utils.callbacks.MapStylesDialogCallback
import com.earthmap.satellite.map.location.map.Utils.fourSquareApi.Result
import com.earthmap.satellite.map.location.map.Utils.dialogs.InternetDialog
import com.earthmap.satellite.map.location.map.Utils.dialogs.LocationDialog
import com.earthmap.satellite.map.location.map.Utils.dialogs.MapStylesDialog
import com.earthmap.satellite.map.location.map.Utils.fourSquareApi.ApiModel
import com.earthmap.satellite.map.location.map.Utils.fourSquareApi.Api_Instance
import com.earthmap.satellite.map.location.map.Utils.fourSquareApi.Api_Interface
import com.earthmap.satellite.map.location.map.databinding.ActivityNearByBinding
import com.earthmap.satellite.map.location.map.nearBy.models.NearByModel
import com.google.gson.Gson
import com.mapbox.android.core.location.*
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.OnLocationClickListener
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin
import com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin
import retrofit2.Call
import retrofit2.Response
import java.lang.ref.WeakReference

class NearByActivity : AppCompatActivity(), NetworkStateReceiver.NetworkStateReceiverListener,
    OnMapReadyCallback,
    LocationDialogCallback {
    var binding: ActivityNearByBinding? = null
    private val radius_fourSquare: Int = 10000
    private val limit_fourSquare: Int = 50
    var dataModel: NearByModel? = null
    var onTimeLocationCheck = true
    var currentLocation: Location? = null
    lateinit var oMapboxMap: MapboxMap
    private var mBuildingPlugin: BuildingPlugin? = null
    var mLocationDialog: LocationDialog? = null
    var mMapLayersDialog: MapStylesDialog? = null
    private var locationComponent: LocationComponent? = null
    private var locationEngine: LocationEngine? = null
    private var callback: LocationChangeListeningActivityLocationCallback =
        LocationChangeListeningActivityLocationCallback(
            this
        )
    var trafficShowingOnMap = false
    var trafficPlugin: TrafficPlugin? = null
    lateinit var mLocationService: LocationService
    var position: CameraPosition? = null
    private val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    private val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Mapbox.getInstance(
                this,
                constants.mapboxApiKey
            )
        } catch (e: Exception) {
        }
        binding = ActivityNearByBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.mapView.onCreate(savedInstanceState)

        initialization()
        onClickListeners()
        setUpHeader()
        mBannerAdsSmall()
        if (intent.getParcelableExtra<NearByModel>("near_By") != null) {
            try {
                val model = intent.getParcelableExtra<NearByModel>("near_By")
                if (model != null) {
                    dataModel = model
                    Log.d(
                        "ModelLogCheckTAG",
                        "onItemClick: " + model.nearByCatIcon + "," + model.nearByCatId + "," + model.nearByCatName
                    )

                    apiCalling(constants.mLatitude, constants.mLongitude, dataModel!!.nearByCatId)

                }
            } catch (e: Exception) {
            }
        }

    }

    private fun onClickListeners() {
        binding!!.currentLocationBtn.setOnClickListener {
            if (currentLocation != null) {
                if (currentLocation!!.latitude != 0.0 && currentLocation!!.longitude != 0.0) {
                    getcurrent(currentLocation!!)
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
                        oMapboxMap.setStyle(Style.MAPBOX_STREETS)
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
                        oMapboxMap.setStyle(Style.DARK)
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
                        oMapboxMap.setStyle(Style.SATELLITE_STREETS)

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
        MyAppShowAds.mediationBackPressedSimple(
            this,
            MyAppAds.admobInterstitialAd,MyAppAds.maxInterstitialAdLiveEarth
        ) {
            super.onBackPressed()
        }
    }

    private fun initialization() {
        mLocationDialog = LocationDialog(this, this)
        mLocationService = LocationService(this, mLocationDialog!!)
        try {
            val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
            registerReceiver(mLocationService, filter)
        } catch (e: Exception) {
        }
        callback = LocationChangeListeningActivityLocationCallback(this)
        binding!!.mapView.onStart()
        binding!!.mapView.getMapAsync(this)
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

    private fun apiCalling(
        latGPS: Double,
        lngGPS: Double,
        nearByCatId: String
    ) {
        val lataLng = "$latGPS,$lngGPS"
        val apiFourSquareService =
            Api_Instance.getRetrofitInstance()!!.create(
                Api_Interface::class.java
            )
        val callFourSqureModel = apiFourSquareService.getPlaces(
            lataLng,
            constants.client_id_fourSquare,
            constants.client_secret_fourSquare,
            constants.time_fourSquare,
            radius_fourSquare,
            nearByCatId,
            limit_fourSquare,
            constants.token_fourSquare
        )
        callFourSqureModel.enqueue(object : retrofit2.Callback<ApiModel?> {
            override fun onResponse(call: Call<ApiModel?>, response: Response<ApiModel?>) {
                try {
                    if (response.isSuccessful && response.body() != null) {
                        val mainResponsebody = response.body()
                        if (mainResponsebody!!.results != null && mainResponsebody.results.size > 0) {
                            convertMarkersListToModel(mainResponsebody.results)
                            binding!!.progressMainBg.visibility = View.GONE
                            Log.d(
                                "callFourSqureModelTAG",
                                "onResponse: " + mainResponsebody.results[0].categories[0].name + "," +
                                        mainResponsebody.results[0].distance + "," +
                                        mainResponsebody.results[0].geocodes.main.latitude + "," +
                                        mainResponsebody.results[0].geocodes.main.latitude + "," +
                                        mainResponsebody.results[0].location.address + "," +
                                        mainResponsebody.results[0].location.locality
                            )
                        }

                    }


                } catch (e: Exception) {
                }
            }

            override fun onFailure(call: Call<ApiModel?>, t: Throwable) {
                binding!!.progressMainBg.visibility = View.GONE
                Log.d("callFourSqureModelTAG", "onFailure: " + t.localizedMessage)
            }

        })
    }

    fun convertMarkersListToModel(results: MutableList<com.earthmap.satellite.map.location.map.Utils.fourSquareApi.Result>) {
        if (results != null && results.size > 0) {

            for (i in results) {

                generateMarkers(i)
            }
            val latLngList = ArrayList<LatLng>()
            for (i in results) {
                latLngList.add(LatLng(i.geocodes.main.latitude, i.geocodes.main.longitude))
            }
            UtilsFunctionClass.animateToBounds(oMapboxMap, latLngList)
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


    override fun onMapReady(mapboxMap: MapboxMap) {

        oMapboxMap = mapboxMap
        mapboxMap.uiSettings.isCompassEnabled = false
        mapboxMap.setStyle(
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
            if (currentLocation != null) {
                if (currentLocation!!.latitude != 0.0 && currentLocation!!.longitude != 0.0) {
                    getcurrent(currentLocation!!)
                }
            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val customOptions = LocationComponentOptions.builder(this)
                .elevation(5f)
                .foregroundTintColor(getColor(R.color.ThemeColor))
                .accuracyColor(getColor(R.color.themetrans))
                .accuracyAlpha(0.3f)
                .accuracyAnimationEnabled(true)
                .build()

            // Get an instance of the component
            locationComponent = oMapboxMap.locationComponent
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .useDefaultLocationEngine(false)
                    .locationComponentOptions(customOptions)
                    .build()


            // Activate with options
            locationComponent?.activateLocationComponent(locationComponentActivationOptions)
            // Enable to make component visible
            locationComponent?.setLocationComponentEnabled(true)


            // Set the component's camera mode
            locationComponent?.setCameraMode(CameraMode.TRACKING)

            // Set the component's render mode
            initLocationEngine()


            // Add the location icon click listener
            locationComponent?.addOnLocationClickListener(OnLocationClickListener {
                if (locationComponent?.getLastKnownLocation() != null) {
                    Toast.makeText(
                        this, String.format(
                            "Current Location",
                            locationComponent?.getLastKnownLocation()!!.latitude,
                            locationComponent?.getLastKnownLocation()!!.longitude
                        ), Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                44
            )
        }
    }

    private class LocationChangeListeningActivityLocationCallback internal constructor(activity: NearByActivity) :
        LocationEngineCallback<LocationEngineResult?> {
        var location: Location? = null
        private val activityWeakReference: WeakReference<NearByActivity>
        override fun onSuccess(p0: LocationEngineResult?) {
            val activity: NearByActivity? =
                activityWeakReference.get()
            if (activity != null) {
                location = p0?.lastLocation
                if (location == null) {
                    return
                }

                if (p0?.lastLocation != null) {
                    activity.oMapboxMap.locationComponent
                        .forceLocationUpdate(p0.lastLocation)
                    if (location != null) {
                        Log.d("LocationCheckingLogs", "onSuccess: " + location)
                        activity.currentLocation = location as Location
                    }
                }
            }
        }

        override fun onFailure(exception: java.lang.Exception) {
            val activity: NearByActivity? =
                activityWeakReference.get()
            if (activity != null) {
                Toast.makeText(
                    activity, exception.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        init {
            activityWeakReference =
                WeakReference<NearByActivity>(
                    activity
                )
        }

    }

    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        val request =
            LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
                .build()
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
        locationEngine!!.requestLocationUpdates(request, callback, mainLooper)
        locationEngine!!.getLastLocation(callback)
    }

    private fun generateMarkers(markersModel: com.earthmap.satellite.map.location.map.Utils.fourSquareApi.Result) {
        // markers!!.clear()
        val factory = IconFactory.getInstance(this)
        val icon = factory.fromResource(R.drawable.location_marker)
        val gson = Gson()
        val markerInfoString = gson.toJson(markersModel)
        oMapboxMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    markersModel.geocodes.main.latitude,
                    markersModel.geocodes.main.longitude
                )
            ).setSnippet(markerInfoString).icon(icon)
        )

        oMapboxMap.setInfoWindowAdapter { marker ->
            val gson = Gson()
            val aMarkerInfo =
                gson.fromJson(marker.snippet, Result::class.java)
            val v = LayoutInflater.from(this@NearByActivity)
                .inflate(R.layout.custom_info_window_nearby_place, null)
            val myImage = v!!.findViewById<ImageView>(R.id.thumnail)
            val myText = v.findViewById<TextView>(R.id.detailInfo)
            val myLoading = v.findViewById<ProgressBar>(R.id.progress)
            val myNavBtn = v.findViewById<ImageView>(R.id.moveToNext)

            Log.d(
                "getInfoWindowTag",
                "getInfoWindow: " + dataModel!!.nearByCatName + "," + dataModel!!.nearByCatIcon
            )
            UtilsFunctionClass.setImageInGlideFromDrawable(
                this@NearByActivity,
                getDrawable(dataModel!!.nearByCatIcon)!!,
                myLoading,
                myImage
            )
            myText.text = aMarkerInfo.name
            myNavBtn.setOnClickListener {
                if (currentLocation != null) {
                    if (currentLocation!!.latitude != 0.0 && currentLocation!!.longitude != 0.0) {
                        val intent = Intent(this@NearByActivity, MapNavigationActivity::class.java)
                        intent.putExtra(
                            "navigation_model",
                            NavigationModel(
                                currentLocation!!.latitude,
                                currentLocation!!.longitude,
                                aMarkerInfo.geocodes.main.latitude,
                                aMarkerInfo.geocodes.main.longitude,
                                0
                            )
                        )
                        /* if (Build.VERSION.SDK_INT < 31) {*/
                        startActivity(intent)
                        /* } else {
                             Toast.makeText(
                                 this@NearByActivity,
                                 "Feature Not Available!",
                                 Toast.LENGTH_SHORT
                             ).show()

                         }*/
                    }
                }
            }

            v
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
                mLocationDialog!!.show()
            } catch (e: Exception) {
            }
        } else {
            try {
                mLocationDialog!!.dismiss()
            } catch (e: Exception) {
            }
        }
    }

    private fun getcurrent(location: Location) {
        Log.d("LocationCheckingLogs", "getcurrent: " + location)
        position = CameraPosition.Builder()
            .target(
                LatLng(
                    java.lang.Double.valueOf(location.latitude),
                    java.lang.Double.valueOf(location.longitude)
                )
            ) // Sets the new camera position
            .zoom(10.0) // Sets the zoom
            .bearing(180.0) // Rotate the camera
            .tilt(30.0) // Set the camera tilt
            .build() // Creates a CameraPosition from the builder


        oMapboxMap.animateCamera(
            CameraUpdateFactory
                .newCameraPosition(position!!), 5000
        )
        onTimeLocationCheck = false
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

    private fun mBannerAdsSmall() {
        MyAppAds.loadEarthMapBannerForMainMediation(
            binding!!.smallAd.adContainer,  this
        )
    }

}