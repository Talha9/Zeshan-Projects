package com.earthmap.satellite.map.location.map.Utils.MapNavigation

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.MapNavigation.model.NavigationModel
import com.earthmap.satellite.map.location.map.Utils.constants
import com.earthmap.satellite.map.location.map.databinding.ActivityMapNavigationBinding
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.bindgen.Expected
import com.mapbox.geojson.Point
import com.mapbox.geojson.Point.fromLngLat
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.maps.*
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.RouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.replay.MapboxReplayer
import com.mapbox.navigation.core.replay.ReplayLocationEngine
import com.mapbox.navigation.core.replay.route.ReplayProgressObserver
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.core.trip.session.VoiceInstructionsObserver
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.maps.route.line.model.RouteLine
import com.mapbox.navigation.ui.tripprogress.api.MapboxTripProgressApi
import com.mapbox.navigation.ui.tripprogress.model.*
import com.mapbox.navigation.ui.voice.api.MapboxSpeechApi
import com.mapbox.navigation.ui.voice.api.MapboxVoiceInstructionsPlayer
import com.mapbox.navigation.ui.voice.model.SpeechAnnouncement
import com.mapbox.navigation.ui.voice.model.SpeechError
import com.mapbox.navigation.ui.voice.model.SpeechValue
import com.mapbox.navigation.ui.voice.model.SpeechVolume
import java.util.*
import kotlin.collections.ArrayList


class MapNavigationActivity : AppCompatActivity() {
    private var routeType: Int?=0
    private var dataModel: NavigationModel?=null
    lateinit var binding: ActivityMapNavigationBinding
    private val TAG = "MapBoxNavigation"

    private var routeList:Boolean=false
    private companion object {
        private const val BUTTON_ANIMATION_DURATION = 1500L
    }

    private var destination: Point? = null
    private var multan: Point? = null
    private var origin: Point? = null
    private val mapboxReplayer = MapboxReplayer()
    private val replayLocationEngine = ReplayLocationEngine(mapboxReplayer)
    private val replayProgressObserver = ReplayProgressObserver(mapboxReplayer)
    private lateinit var mapboxMap: MapboxMap
    private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var navigationCamera: NavigationCamera
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource
    private val pixelDensity = Resources.getSystem().displayMetrics.density

    private val overviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            140.0 * pixelDensity,
            40.0 * pixelDensity,
            120.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeOverviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            20.0 * pixelDensity
        )
    }
    private val followingPadding: EdgeInsets by lazy {
        EdgeInsets(
            180.0 * pixelDensity,
            40.0 * pixelDensity,
            150.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeFollowingPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private lateinit var maneuverApi: MapboxManeuverApi
    private lateinit var tripProgressApi: MapboxTripProgressApi
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()
    private lateinit var routeArrowView: MapboxRouteArrowView
    private var isVoiceInstructionsMuted = false
        set(value) {
            field = value
            if (value) {
                binding.soundButton.muteAndExtend(BUTTON_ANIMATION_DURATION)
                voiceInstructionsPlayer.volume(SpeechVolume(0f))
            } else {
                binding.soundButton.unmuteAndExtend(BUTTON_ANIMATION_DURATION)
                voiceInstructionsPlayer.volume(SpeechVolume(1f))
            }
        }
    private lateinit var speechApi: MapboxSpeechApi
    private lateinit var voiceInstructionsPlayer: MapboxVoiceInstructionsPlayer
    private val voiceInstructionsObserver = VoiceInstructionsObserver { voiceInstructions ->
        speechApi.generate(voiceInstructions, speechCallback)
    }
    private val speechCallback =
        MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>> { expected ->
            expected.fold(
                { error ->
                    voiceInstructionsPlayer.play(
                        error.fallback,
                        voiceInstructionsPlayerCallback
                    )
                },
                { value ->
                    voiceInstructionsPlayer.play(
                        value.announcement,
                        voiceInstructionsPlayerCallback
                    )
                }
            )
        }
    private val voiceInstructionsPlayerCallback =
        MapboxNavigationConsumer<SpeechAnnouncement> { value ->
            speechApi.clean(value)
        }
    private val navigationLocationProvider = NavigationLocationProvider()
    private val locationObserver = object : LocationObserver {
        var firstLocationUpdateReceived = false

        override fun onNewRawLocation(rawLocation: Location) {
        }

        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
            navigationLocationProvider.changePosition(
                location = enhancedLocation,
                keyPoints = locationMatcherResult.keyPoints,
            )
            viewportDataSource.onLocationChanged(enhancedLocation)
            viewportDataSource.evaluate()
            if (!firstLocationUpdateReceived) {
                firstLocationUpdateReceived = true
                navigationCamera.requestNavigationCameraToOverview(
                    stateTransitionOptions = NavigationCameraTransitionOptions.Builder()
                        .maxDuration(0) // instant transition
                        .build()
                )
            }
        }
    }
    private val routeProgressObserver = RouteProgressObserver { routeProgress ->
        viewportDataSource.onRouteProgressChanged(routeProgress)
        viewportDataSource.evaluate()
        val style = mapboxMap.getStyle()
        if (style != null) {
            val maneuverArrowResult = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
            routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
        }
        val maneuvers = maneuverApi.getManeuvers(routeProgress)
        maneuvers.fold(
            { error ->
                Toast.makeText(
                    this,
                    error.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            },
            {
                binding.maneuverView.visibility = View.VISIBLE
                binding.maneuverView.renderManeuvers(maneuvers)
            }
        )
        binding.tripProgressView.render(
            tripProgressApi.getTripProgress(routeProgress)
        )
    }
    private val routesObserver = RoutesObserver { routeUpdateResult ->
        if (routeUpdateResult.routes.isNotEmpty()) {
            val routeLines = routeUpdateResult.routes.map { RouteLine(it, null) }

            routeLineApi.setRoutes(
                routeLines
            ) { value ->
                mapboxMap.getStyle()?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }
            viewportDataSource.onRouteChanged(routeUpdateResult.routes.first())
            viewportDataSource.evaluate()
        } else {
            val style = mapboxMap.getStyle()
            if (style != null) {
                routeLineApi.clearRouteLine { value ->
                    routeLineView.renderClearRouteLineValue(
                        style,
                        value
                    )
                }
                routeArrowView.render(style, routeArrowApi.clearArrows())
            }
            viewportDataSource.clearRouteData()
            viewportDataSource.evaluate()
        }
    }

    private var mRoutePoints = ArrayList<Point>()
    private var customRoutList: ArrayList<LatLng> = ArrayList()


    @SuppressLint("MissingPermission", "LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ResourceOptionsManager.getDefault(this, constants.mapboxApiKey).update {
            tileStoreUsageMode(TileStoreUsageMode.READ_ONLY)
        }
        binding = ActivityMapNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapboxMap = binding.mapView.getMapboxMap()

        multan = fromLngLat(71.492157,53.2524)

/*
        if (AppConstant.latitude != 0.0 && AppConstant.longitude != 0.0 && AppConstant.destinationLatitude != 0.0 && AppConstant.destinationLongitude != 0.0) {
            origin = Point.fromLngLat(AppConstant.longitude, AppConstant.latitude!!)
            destination =
                Point.fromLngLat(AppConstant.destinationLongitude, AppConstant.destinationLatitude)
        }*/

        if (intent.getParcelableExtra<NavigationModel>("navigation_model") != null) {
            try {
                val model = intent.getParcelableExtra<NavigationModel>("navigation_model")
                if (model != null) {
                    dataModel = model
                    Log.d(
                        "ModelLogCheckTAG",
                        "onItemClick: " + model.originLatitude + "," + model.originLongitude + model.destinationLatitude + "," + model.destinationLongitude
                    )

                    origin =
                        fromLngLat(dataModel!!.originLongitude, dataModel!!.originLatitude)
                    destination = fromLngLat(
                        dataModel!!.destinationLongitude,
                        dataModel!!.destinationLatitude
                    )
                    mRoutePoints.add(origin!!)
                    mRoutePoints.add(destination!!)

                    routeType = dataModel!!.routeIndex
                }
            } catch (e: Exception) {
            }
        }



        Log.d(TAG, "onCreate: ========${mRoutePoints.size}======"+routeList)


        Handler().postDelayed({
            if (destination != null) {
                findRoute(destination!!)
            }
        }, 1000)
        //destination?.let { findRoute(it) }
        binding.mapView.location.apply {
            this.locationPuck = LocationPuck2D(
                bearingImage = ContextCompat.getDrawable(
                    this@MapNavigationActivity,
                    R.drawable.location_marker
                )
            )
            setLocationProvider(navigationLocationProvider)
            enabled = true
        }
        mapboxNavigation = if (MapboxNavigationProvider.isCreated()) {
            MapboxNavigationProvider.retrieve()
        } else {
            val navigationOptions = NavigationOptions.Builder(this)
                .accessToken(constants.mapboxApiKey)
                .build()
            MapboxNavigationProvider.create(navigationOptions)
        }
        viewportDataSource = MapboxNavigationViewportDataSource(mapboxMap)
        navigationCamera = NavigationCamera(
            mapboxMap,
            binding.mapView.camera,
            viewportDataSource
        )
        binding.mapView.camera.addCameraAnimationsLifecycleListener(
            NavigationBasicGesturesHandler(navigationCamera)
        )
        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
            when (navigationCameraState) {
                NavigationCameraState.TRANSITION_TO_FOLLOWING,
                NavigationCameraState.FOLLOWING -> binding.recenter.visibility = View.INVISIBLE
                NavigationCameraState.TRANSITION_TO_OVERVIEW,
                NavigationCameraState.OVERVIEW,
                NavigationCameraState.IDLE -> binding.recenter.visibility = View.VISIBLE
            }
        }
// set the padding values depending on screen orientation and visible view layout
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.overviewPadding = landscapeOverviewPadding
        } else {
            viewportDataSource.overviewPadding = overviewPadding
        }
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.followingPadding = landscapeFollowingPadding
        } else {
            viewportDataSource.followingPadding = followingPadding
        }

// make sure to use the same DistanceFormatterOptions across different features
        val distanceFormatterOptions = mapboxNavigation.navigationOptions.distanceFormatterOptions

// initialize maneuver api that feeds the data to the top banner maneuver view
        maneuverApi = MapboxManeuverApi(
            MapboxDistanceFormatter(distanceFormatterOptions)
        )

// initialize bottom progress view
        tripProgressApi = MapboxTripProgressApi(
            TripProgressUpdateFormatter.Builder(this)
                .distanceRemainingFormatter(
                    DistanceRemainingFormatter(distanceFormatterOptions)
                )
                .timeRemainingFormatter(
                    TimeRemainingFormatter(this)
                )
                .percentRouteTraveledFormatter(
                    PercentDistanceTraveledFormatter()
                )
                .estimatedTimeToArrivalFormatter(
                    EstimatedTimeToArrivalFormatter(this, TimeFormat.NONE_SPECIFIED)
                )
                .build()
        )

// initialize voice instructions api and the voice instruction player
        speechApi = MapboxSpeechApi(
            this,
            constants.mapboxApiKey,
            Locale.US.language
        )
        voiceInstructionsPlayer = MapboxVoiceInstructionsPlayer(
            this,
            constants.mapboxApiKey,
            Locale.US.language
        )

// initialize route line, the withRouteLineBelowLayerId is specified to place
// the route line below road labels layer on the map
// the value of this option will depend on the style that you are using
// and under which layer the route line should be placed on the map layers stack
        val mapboxRouteLineOptions = MapboxRouteLineOptions.Builder(this)
            .withRouteLineBelowLayerId("road-label")
            .build()
        routeLineApi = MapboxRouteLineApi(mapboxRouteLineOptions)
        routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)

// initialize maneuver arrow view to draw arrows on the map
        val routeArrowOptions = RouteArrowOptions.Builder(this).build()
        routeArrowView = MapboxRouteArrowView(routeArrowOptions)

// load map style
        mapboxMap.loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
// add long click listener that search for a route to the clicked destination
            binding.mapView.gestures.addOnMapLongClickListener { point ->
                findRoute(destination)
                true
            }
        }

// initialize view interactions
        binding.stop.setOnClickListener {
            clearRouteAndStopNavigation()
            onBackPressed()
        }
        binding.recenter.setOnClickListener {
            navigationCamera.requestNavigationCameraToFollowing()
            binding.routeOverview.showTextAndExtend(BUTTON_ANIMATION_DURATION)
        }
        binding.routeOverview.setOnClickListener {
            navigationCamera.requestNavigationCameraToOverview()
            binding.recenter.showTextAndExtend(BUTTON_ANIMATION_DURATION)
        }
        binding.soundButton.setOnClickListener {
// mute/unmute voice instructions
            isVoiceInstructionsMuted = !isVoiceInstructionsMuted
        }

// set initial sounds button state
        binding.soundButton.unmute()

// start the trip session to being receiving location updates in free drive
// and later when a route is set also receiving route progress updates
        mapboxNavigation.startTripSession()

    }


    override fun onStart() {
        super.onStart()

// register event listeners
        mapboxNavigation.registerRoutesObserver(routesObserver)
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.registerLocationObserver(locationObserver)
        mapboxNavigation.registerVoiceInstructionsObserver(voiceInstructionsObserver)
        mapboxNavigation.registerRouteProgressObserver(replayProgressObserver)

        if (mapboxNavigation.getRoutes().isEmpty()) {
// if simulation is enabled (ReplayLocationEngine set to NavigationOptions)
// but we're not simulating yet,
// push a single location sample to establish origin
            mapboxReplayer.pushEvents(
                listOf(
                    ReplayRouteMapper.mapToUpdateLocation(
                        eventTimestamp = 0.0,
                        point = fromLngLat(-122.39726512303575, 37.785128345296805)
                    )
                )
            )
            mapboxReplayer.playFirstLocation()
        }
    }

    override fun onStop() {
        super.onStop()

// unregister event listeners to prevent leaks or unnecessary resource consumption
        mapboxNavigation.unregisterRoutesObserver(routesObserver)
        mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.unregisterLocationObserver(locationObserver)
        mapboxNavigation.unregisterVoiceInstructionsObserver(voiceInstructionsObserver)
        mapboxNavigation.unregisterRouteProgressObserver(replayProgressObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        MapboxNavigationProvider.destroy()
        clearRouteAndStopNavigation()
        mapboxReplayer.finish()
        maneuverApi.cancel()
        routeLineApi.cancel()
        routeLineView.cancel()
        speechApi.cancel()
        voiceInstructionsPlayer.shutdown()
    }

    private fun findRoute(mRoutPointList: Point?) {

        Log.d(TAG, "findRoute: =mRoutePoints size==="+mRoutePoints.size)
        val originLocation = navigationLocationProvider.lastLocation
        val originPoint = originLocation?.let {
            fromLngLat(it.longitude, it.latitude)
        } ?: return

// execute a route request
// it's recommended to use the
// applyDefaultNavigationOptions and applyLanguageAndVoiceUnitOptions
// that make sure the route request is optimized
// to allow for support of all of the Navigation SDK features

//            .coordinatesList(mRoutePoints)
//            .coordinatesList(listOf(originPoint, destination))

        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(this)
                .coordinatesList(listOf(originPoint, destination))
// provide the bearing for the origin of the request to ensure
// that the returned route faces in the direction of the current user movement
                .bearingsList(
                    listOf(
                        Bearing.builder()
                            .angle(originLocation.bearing.toDouble())
                            .degrees(45.0)
                            .build(),
                        null
                    )
                )
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            object : RouterCallback {
                override fun onRoutesReady(
                    routes: List<DirectionsRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    setRouteAndStartNavigation(routes)
                }

                override fun onFailure(
                    reasons: List<RouterFailure>,
                    routeOptions: RouteOptions
                ) {
// no impl
                }

                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
// no impl
                }
            }
        )
    }

    private fun setRouteAndStartNavigation(routes: List<DirectionsRoute>) {
// set routes, where the first route in the list is the primary route that
// will be used for active guidance
        mapboxNavigation.setRoutes(routes)

// start location simulation along the primary route

        try {
            startSimulation(routes.first())
        } catch (e: Exception) {
        }

// show UI elements
        binding.soundButton.visibility = View.VISIBLE
        binding.routeOverview.visibility = View.VISIBLE
        binding.tripProgressCard.visibility = View.VISIBLE

// move the camera to overview when new route is available
        navigationCamera.requestNavigationCameraToOverview()
    }

    private fun clearRouteAndStopNavigation() {
// clear
        mapboxNavigation.setRoutes(listOf())

// stop simulation
        mapboxReplayer.stop()

// hide UI elements
        binding.soundButton.visibility = View.INVISIBLE
        binding.maneuverView.visibility = View.INVISIBLE
        binding.routeOverview.visibility = View.INVISIBLE
        binding.tripProgressCard.visibility = View.INVISIBLE
    }

    private fun startSimulation(route: DirectionsRoute) {
        try {
            mapboxReplayer.run {
                stop()
                clearEvents()
                val replayEvents = ReplayRouteMapper().mapDirectionsRouteGeometry(route)
                pushEvents(replayEvents)
                seekTo(replayEvents.first())
                play()
            }
        } catch (e: Exception) {
        }
    }















/*
    private var origin: Point? = null
    private var destination: Point? = null
    var dataModel: NavigationModel? = null
    var navigateCheck = false
    private lateinit var navigationMapboxMap: NavigationMapboxMap
    private lateinit var mapboxNavigation: MapboxNavigation
    private var routeType = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Mapbox.getInstance(this, constants.mapboxApiKey)
        } catch (e: Exception) {
        }
        binding = ActivityMapNavigationBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (intent.getParcelableExtra<NavigationModel>("navigation_model") != null) {
            try {
                val model = intent.getParcelableExtra<NavigationModel>("navigation_model")
                if (model != null) {
                    dataModel = model
                    Log.d(
                        "ModelLogCheckTAG",
                        "onItemClick: " + model.originLatitude + "," + model.originLongitude + model.destinationLatitude + "," + model.destinationLongitude
                    )

                    origin =
                        Point.fromLngLat(dataModel!!.originLongitude, dataModel!!.originLatitude)
                    destination = Point.fromLngLat(
                        dataModel!!.destinationLongitude,
                        dataModel!!.destinationLatitude
                    )
                    routeType = dataModel!!.routeIndex
                }
            } catch (e: Exception) {
            }
        }

        initialization(savedInstanceState)


    }

    private fun initialization(savedInstanceState: Bundle?) {
        val initialPosition =
            CameraPosition.Builder()
                .target(
                    LatLng(
                        origin!!.latitude(),
                        origin!!.longitude()
                    )
                )
                .zoom(15.0)
                .build()
        binding!!.navigationView.onCreate(savedInstanceState)
        binding!!.navigationView.initialize(this, initialPosition)
    }

    override fun onNavigationReady(isRunning: Boolean) {
        Log.d("NavigationRunningTag", "onNavigationReady: $isRunning")
        if (!isRunning && !::navigationMapboxMap.isInitialized
            && !::mapboxNavigation.isInitialized
        ) {
            if (binding!!.navigationView.retrieveNavigationMapboxMap() != null
            ) {
                Log.d("NavigationRunningTag", "onNavigationReady: Both are not null")
                navigationMapboxMap = binding!!.navigationView.retrieveNavigationMapboxMap()!!
                val navigationOptions = MapboxNavigation
                    .defaultNavigationOptionsBuilder(this, "")
                    .build()
                mapboxNavigation = MapboxNavigationProvider.create(navigationOptions)
                fetchRoute()
            } else {
                Log.d("NavigationRunningTag", "onNavigationReady:  Both are null")
            }
        }
    }

    private fun fetchRoute() {
        Log.d("RouteFetchTag", "fetchRoute: ")
        if (origin != null && destination != null) {
            mapboxNavigation.requestRoutes(
                RouteOptions.builder()
                    .accessToken(constants.mapboxApiKey)
                    .coordinates(listOf(origin, destination))
                    .geometries(RouteUrl.GEOMETRY_POLYLINE6)
                    .alternatives(true)
                    .profile(RouteUrl.PROFILE_DRIVING_TRAFFIC)
                    .user(RouteUrl.PROFILE_DEFAULT_USER)
                    .baseUrl(RouteUrl.BASE_URL)
                    .requestUuid("1")
                    .build(), object : RoutesRequestCallback {
                    override fun onRoutesReady(routes: List<DirectionsRoute>) {
                        Log.d("RouteFetchTag", "onRoutesReady: ")
                        if (routes.isNotEmpty()) {
                            val directionsRoute = routes[routeType]
                            startNavigation(directionsRoute)
                            navigateCheck = true
                        }
                    }

                    override fun onRoutesRequestCanceled(routeOptions: RouteOptions) {
                        Log.d("RouteFetchTag", "onRoutesRequestCanceled: ")
                        navigateCheck = true
                    }

                    override fun onRoutesRequestFailure(
                        throwable: Throwable,
                        routeOptions: RouteOptions
                    ) {
                        navigateCheck = true
                        Log.d(
                            "RouteFetchTag",
                            "onRoutesRequestFailure: " + throwable.localizedMessage
                        )
                    }

                }
            )

        }
    }

    private fun startNavigation(directionsRoute: DirectionsRoute) {
        Log.d("startNavigationTag", "startNavigation: ")
        val optionsBuilder = NavigationViewOptions.builder(this)
        optionsBuilder.navigationListener(this)
        optionsBuilder.directionsRoute(directionsRoute)
        optionsBuilder.shouldSimulateRoute(false)
        binding!!.navigationView.startNavigation(optionsBuilder.build())

    }

    override fun onNavigationRunning() {
        Log.d("startNavigationTag", "onNavigationRunning: ")
    }

    override fun onNavigationFinished() {
        Log.d("startNavigationTag", "onNavigationFinished: ")
        if (navigateCheck) {
            finish()
        } else {
            Toast.makeText(this, "Please Wait Route is Being Ready!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCancelNavigation() {
        if (navigateCheck) {
            Log.d("startNavigationTag", "onCancelNavigation: ")
            binding!!.navigationView.stopNavigation()
            finish()
        } else {
            Toast.makeText(this, "Please Wait Route is Being Ready!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onLowMemory() {
        super.onLowMemory()
        binding!!.navigationView.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
        binding!!.navigationView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding!!.navigationView.onResume()
    }

    override fun onStop() {
        binding!!.navigationView.onStop()
        super.onStop()
    }

    override fun onPause() {
        binding!!.navigationView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding!!.navigationView.onDestroy()
        mapboxNavigation.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding!!.navigationView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding!!.navigationView.onRestoreInstanceState(savedInstanceState)
    }

    override fun onBackPressed() {
        if (navigateCheck) {
            super.onBackPressed()
        }else {
            Toast.makeText(this, "Please Wait Route is Being Ready!", Toast.LENGTH_SHORT).show()
        }
    }*/


}