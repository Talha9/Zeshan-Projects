package com.earthmap.satellite.map.location.map.myLocationModule

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.ClipboardManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.Ads.MyAppShowAds
import com.earthmap.satellite.map.location.map.Geocoders.ConvertLatLngToPlace
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.*
import com.earthmap.satellite.map.location.map.Utils.callbacks.LocationDialogCallback
import com.earthmap.satellite.map.location.map.Utils.callbacks.MapStylesDialogCallback
import com.earthmap.satellite.map.location.map.Utils.dialogs.InternetDialog
import com.earthmap.satellite.map.location.map.Utils.dialogs.LocationDialog
import com.earthmap.satellite.map.location.map.Utils.dialogs.MapStylesDialog
import com.earthmap.satellite.map.location.map.databinding.ActivityMyLocationMainBinding
import com.mapbox.android.core.location.*
import com.mapbox.mapboxsdk.Mapbox
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
import java.lang.Double
import java.lang.ref.WeakReference

class MyLocationMainActivity : AppCompatActivity(), LocationDialogCallback, NetworkStateReceiver.NetworkStateReceiverListener,
    OnMapReadyCallback {
    var binding: ActivityMyLocationMainBinding?=null
    lateinit var mFetchLocation: LocationClass
    lateinit var mLocationService: LocationService
    var gpsEnableDialog: LocationDialog? = null
    var position: CameraPosition? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog?=null
    var oMapboxMap:MapboxMap?=null
    private var mBuildingPlugin: BuildingPlugin? = null
    var trafficShowingOnMap = false
    var trafficPlugin: TrafficPlugin? = null
    var mMapLayersDialog: MapStylesDialog?=null
    private var locationComponent: LocationComponent? = null
    private var locationEngine: LocationEngine? = null
    var onTimeLocationCheck = true
    private var is2D = 0
    private var clipGlobeEarthMapManager: ClipboardManager? = null
    var mConvertLatLngToPlaceName: ConvertLatLngToPlace? = null
    private var callback: LocationChangeListeningActivityLocationCallback =
        LocationChangeListeningActivityLocationCallback(
            this
        )
    private val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    private val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Mapbox.getInstance(
                this,
                constants.mapboxApiKey
            )
        } catch (e: Exception) {
        }
        binding= ActivityMyLocationMainBinding.inflate(layoutInflater)
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
        binding!!.currentLocationBtn.setOnClickListener {
            onTimeLocationCheck=true
            if (constants.mLatitude!=0.0  && constants.mLongitude!=0.0){
                getCurrent(LatLng(constants.mLatitude,constants.mLongitude))
            }
        }

        binding!!.myLocationShareBtn.setOnClickListener {
            val address =
                "${constants.countryName}\nhttps://maps.google.com/maps?q=@${constants.mLatitude},${constants.mLongitude}"
            shareMyLocation(address)
        }

        binding!!.map2d3dBtn.setOnClickListener {
        if(is2D==0){
          is2D= UtilsFunctionClass.animateCamIn3DAngle(LatLng(constants.mLatitude,constants.mLongitude),oMapboxMap!!,mBuildingPlugin!!)
        }else{
            is2D=UtilsFunctionClass.animateCamIn2DAngle(LatLng(constants.mLatitude,constants.mLongitude),oMapboxMap!!,mBuildingPlugin!!)
        }
        }
        binding!!.mapLayersBtn.setOnClickListener {
            MyAppShowAds.meidationForClickSimpleLiveStreetView(
                this,
                MyAppAds.admobInterstitialAd,MyAppAds.maxInterstitialAdLiveEarth
            ){
                mMapLayersDialog= MapStylesDialog(this,object : MapStylesDialogCallback {
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

        binding!!.myLocationAddressTxt.setOnClickListener {
            if (binding!!.myLocationAddressTxt.text.isNotEmpty() && constants.mLatitude!=0.0 && constants.mLongitude!=0.0) {
                val address = "I am here now" + "\nLatitude: ${constants.mLatitude}\nLongitude: ${constants.mLongitude}\n" + "\"${binding!!.myLocationAddressTxt.text}\nhttps://maps.google.com/maps?q=@${constants.mLatitude},${constants.mLongitude}"
                clipGlobeEarthMapManager!!.text = address
                Toast.makeText(this, "Text Copy", Toast.LENGTH_SHORT)
                    .show()
            }else{
                Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onBackPressed() {
        MyAppShowAds.mediationBackPressedSimple(
            this,
            MyAppAds.admobInterstitialAd,MyAppAds.maxInterstitialAdLiveEarth
        ){
            super.onBackPressed()
        }
    }

    private fun initializers() {
        clipGlobeEarthMapManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        binding!!.header.headerBarTitleTxt.text="My Location"
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        callback = LocationChangeListeningActivityLocationCallback(this)
        binding!!.mapView.onStart()
        binding!!.mapView.getMapAsync(this)
        internetDialog= InternetDialog(this)
        binding!!.mapView.getMapAsync(this)
        mFetchLocation = LocationClass(this)
        mFetchLocation.initLocationRequest()
        gpsEnableDialog= LocationDialog(this,this)
        mLocationService=LocationService(this, gpsEnableDialog!!)
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
        oMapboxMap=mapboxMap
        mapboxMap.uiSettings.isCompassEnabled = false
        oMapboxMap!!.setStyle(Style.SATELLITE_STREETS, object : Style.OnStyleLoaded {
            override fun onStyleLoaded(style: Style) {
                enableLocationComponent(style)
                trafficPlugin = TrafficPlugin(binding!!.mapView, mapboxMap, style)
                mBuildingPlugin = BuildingPlugin(binding!!.mapView, mapboxMap, style)
                try {
                    mBuildingPlugin!!.setOpacity(1.0f)
                    mBuildingPlugin!!.setMinZoomLevel(10f)
                } catch (e: Exception) {
                }

            }
        })
    }
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
            locationComponent = oMapboxMap!!.locationComponent
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

    private class LocationChangeListeningActivityLocationCallback internal constructor(activity: MyLocationMainActivity) :
        LocationEngineCallback<LocationEngineResult?> {
        var location: Location? = null
        private val activityWeakReference: WeakReference<MyLocationMainActivity>
        override fun onSuccess(p0: LocationEngineResult?) {
            val activity: MyLocationMainActivity? =
                activityWeakReference.get()
            if (activity != null) {
                location = p0?.lastLocation
                if (location == null) {
                    return
                }

                if (p0?.lastLocation != null) {
                    activity.oMapboxMap!!.locationComponent
                        .forceLocationUpdate(p0.lastLocation)
                    if (location != null) {
                        Log.d("LocationCheckingLogs", "onSuccess: " + location)
                        activity.getCurrent(LatLng(location!!.latitude,location!!.longitude))
                        constants.mLatitude=location!!.latitude
                        constants.mLongitude=location!!.longitude
                    }
                }
            }
        }

        override fun onFailure(exception: java.lang.Exception) {
            val activity: MyLocationMainActivity? =
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
                WeakReference<MyLocationMainActivity>(
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
        mFetchLocation.stopLocationRequest()
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

    private fun getCurrent(location: LatLng) {
        Log.d("LocationCheckingLogs", "getcurrent: " + location)
        if (onTimeLocationCheck) {
            getLocation(LatLng(location.latitude, location.longitude))
            position = CameraPosition.Builder()
                .target(
                    LatLng(
                        Double.valueOf(location.latitude),
                        Double.valueOf(location.longitude)
                    )
                ) // Sets the new camera position
                .zoom(10.0) // Sets the zoom
                .bearing(180.0) // Rotate the camera
                .tilt(30.0) // Set the camera tilt
                .build() // Creates a CameraPosition from the builder


            oMapboxMap!!.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(position!!), 3000
            )
            binding!!.progressMainBg.visibility=View.GONE
            onTimeLocationCheck = false
        }

    }

    private fun getLocation(mLatLngs: LatLng) {
        mConvertLatLngToPlaceName = ConvertLatLngToPlace(
            this,
            mLatLngs,
            object : ConvertLatLngToPlace.GeoTaskCallbackPlace {
                override fun onSuccessLocationFetched(fetchedAddress: String?) {
                    if (fetchedAddress != null) {
                        constants.countryName = fetchedAddress
                        binding!!.myLocationAddressTxt.text=fetchedAddress
                    } else {
                        Toast.makeText(
                            this@MyLocationMainActivity,
                            "Internet ERROR!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailedLocationFetched() {
                    Toast.makeText(
                        this@MyLocationMainActivity,
                        "Internet ERROR!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        mConvertLatLngToPlaceName!!.execute()
    }

    private fun shareMyLocation(loc: String) {
        try {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.type = "text/plain"
            val shareSub = "This is my Location!"
            val shareBody = loc
            shareAppIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(shareAppIntent, "Share location using..."))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun mBannerAdsSmall() {
            MyAppAds.loadEarthMapBannerForMainMediation(
                binding!!.smallAd.adContainer,this
            )
    }

}