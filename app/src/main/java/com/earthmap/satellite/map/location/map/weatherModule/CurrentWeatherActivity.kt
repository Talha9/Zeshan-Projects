package com.earthmap.satellite.map.location.map.weatherModule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.Ads.MyAppShowAds
import com.earthmap.satellite.map.location.map.Geocoders.ConvertPlaceNameToLatLng
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.*
import com.earthmap.satellite.map.location.map.Utils.callbacks.LocationDialogCallback
import com.earthmap.satellite.map.location.map.Utils.dialogs.InternetDialog
import com.earthmap.satellite.map.location.map.Utils.dialogs.LocationDialog
import com.earthmap.satellite.map.location.map.databinding.ActivityCurrentWeatherBinding
import com.earthmap.satellite.map.location.map.weatherModule.adapters.CurrentDayWeatherAdapter
import com.earthmap.satellite.map.location.map.weatherModule.adapters.NextDaysWeatherAdapter
import com.earthmap.satellite.map.location.map.weatherModule.api_weather.City
import com.earthmap.satellite.map.location.map.weatherModule.api_weather.List
import com.earthmap.satellite.map.location.map.weatherModule.api_weather.WeatherApi
import com.earthmap.satellite.map.location.map.weatherModule.api_weather.WeatherMainModel
import com.mapbox.mapboxsdk.geometry.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList

class CurrentWeatherActivity : AppCompatActivity(), LocationDialogCallback,
    NetworkStateReceiver.NetworkStateReceiverListener {
    var binding: ActivityCurrentWeatherBinding? = null
    lateinit var mFetchLocation: LocationClass
    lateinit var mLocationService: LocationService
    var gpsEnableDialog: LocationDialog? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    var apikey = "74e38bfb846cc04ec324b33eea94444a"
    private var backAD = false
    var isSearchAvailable = false
    var nxtDaysAdapter: NextDaysWeatherAdapter? = null
    var crntDayAdapter: CurrentDayWeatherAdapter? = null
    var managerForCurrentDayWeatherAdapter: LinearLayoutManager? = null
    var managerForNextDaysWeatherAdapter: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentWeatherBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        Initializers()
        onClicks()
        mBannerAdsSmall()

        val latitudeStart = constants.mLatitude
        val longitudeStart = constants.mLongitude
        GetCurrentLocationWeather(latitudeStart.toString(), longitudeStart.toString())
    }

    private fun Initializers() {
        binding!!.header.headerBarTitleTxt.text = "Weather"
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
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

        managerForNextDaysWeatherAdapter =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        managerForCurrentDayWeatherAdapter =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun onClicks() {
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }

        binding!!.header.headerBarSearchBtn.setOnClickListener {
            if (isSearchAvailable) {
                val searchTxt = binding!!.header.headerBarSearchTxt.text.toString().trim()
                if (searchTxt != null) {
                    binding!!.header.headerBarSearchTxt.setText(searchTxt)
                    fineAIGpsNavPlace(searchTxt)
                    UtilsFunctionClass.hideKeyboard(this)
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
        binding!!.header.headerBarSearchTxt.setOnEditorActionListener { v, actionId, event ->
            val searchTxt = binding!!.header.headerBarSearchTxt.text.toString().trim()
            if (searchTxt != null) {
                binding!!.header.headerBarSearchTxt.setText(searchTxt)
                fineAIGpsNavPlace(searchTxt)
                UtilsFunctionClass.hideKeyboard(this@CurrentWeatherActivity)
            } else {
                Toast.makeText(
                    this@CurrentWeatherActivity,
                    "Please Enter Place..!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }
        binding!!.header.headerBarCancelBtn.setOnClickListener {
            binding!!.header.headerBarBackBtn.visibility = View.VISIBLE
            binding!!.header.headerBarTitleTxt.visibility = View.VISIBLE
            binding!!.header.headerBarCancelBtn.visibility = View.GONE
            binding!!.header.headerBarSearchTxt.visibility = View.GONE
            isSearchAvailable = false
        }
    }

    override fun onBackPressed() {
        MyAppShowAds.mediationBackPressedSimple(this, MyAppAds.admobInterstitialAd,MyAppAds.maxInterstitialAdLiveEarth){
            super.onBackPressed()
        }
    }

    @SuppressLint("LongLogTag")
    fun GetCurrentLocationWeather(citylats: String, citylngs: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api: WeatherApi = retrofit.create(WeatherApi::class.java)

        val weathCall: Call<WeatherMainModel?>? =
            api.getForCast(citylats, citylngs, "metric", apikey)

        Log.d("CityWeatherProcessTagLogs", "doInBackground: " + weathCall!!.request().url)
        weathCall.enqueue(object : Callback<WeatherMainModel?> {
            @SuppressLint("LongLogTag")
            override fun onResponse(
                call: Call<WeatherMainModel?>,
                response: Response<WeatherMainModel?>
            ) {
                if (response.isSuccessful) {
                    val mydata = response.body()!!.list
                    val myCityData = response.body()!!.city
                    NxtDayWeatherSetAdapter(mydata)
                    WeatherOnScreenSetting(myCityData, mydata)
                    CurrentDayWeatherSetAdapter(mydata)


                }
            }

            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<WeatherMainModel?>, t: Throwable) {
                Log.d("GetCurrentLocationWeatherTagLog", "onFailure: " + t)
            }

        })


    }

    private fun WeatherOnScreenSetting(
        myCityData: City,
        mydata: MutableList<com.earthmap.satellite.map.location.map.weatherModule.api_weather.List>
    ) {
        binding!!.weatherCityTxt.setText(myCityData.name + "," + myCityData.country)
        var temp = mydata.get(0).main.temp
        binding!!.weatherTemptxt.setText(
            Math.round(temp).toString() + "\u00B0"
        )
        binding!!.weatherDayType.setText(mydata.get(0).weather.get(0).description)

        val input: String = mydata.get(0).dtTxt
        try {
            @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val date = sdf.parse(input)
            val outFormat = SimpleDateFormat("EE")
            val goal = outFormat.format(date)
            binding!!.weatherDate.setText(goal)
            val outFormat2 = SimpleDateFormat("dd MMM yy")
            val goal2 = outFormat2.format(date)
            binding!!.weatherDate.setText(goal2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        binding!!.weatherReelFeelTxt.setText(mydata.get(0).main.feelsLike.toString() + "\u00B0")
        binding!!.weatherHumidityTxt.setText(mydata.get(0).main.humidity.toString() + "%")
        binding!!.weatherWindTxt.setText(mydata.get(0).wind.speed.toString() + " mp/h")
        binding!!.weatherUVTxt.setText(mydata.get(0).visibility.toString())

        getWeatherConditions(mydata.get(0).weather.get(0).icon, this@CurrentWeatherActivity)
    }

    private fun NxtDayWeatherSetAdapter(mydata: MutableList<com.earthmap.satellite.map.location.map.weatherModule.api_weather.List>) {
        val newForcastList =
            ArrayList<com.earthmap.satellite.map.location.map.weatherModule.api_weather.List>()
        for (i in mydata.indices) {
            if (i == 0 || i == 7 || i == 15 || i == 23 || i == 31 || i == 39) {
                newForcastList.add(mydata.get(i))
            }
        }

        if (newForcastList.size > 0) {

            try {
                nxtDaysAdapter = NextDaysWeatherAdapter(this@CurrentWeatherActivity, newForcastList)
                binding!!.ForcastRecView.setAdapter(nxtDaysAdapter)
                binding!!.ForcastRecView.setLayoutManager(managerForNextDaysWeatherAdapter)
            } catch (e: Exception) {
            }
        }
    }

    @SuppressLint("LongLogTag")
    private fun CurrentDayWeatherSetAdapter(mydata: MutableList<com.earthmap.satellite.map.location.map.weatherModule.api_weather.List>) {
        val newForcastList = ArrayList<List>()
        for (i in mydata.indices) {
            val input: String = mydata.get(i).dtTxt
            @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val date = sdf.parse(input)
            val outFormat1 = SimpleDateFormat("dd MMM yy")
            val goal1 = outFormat1.format(date)
            val modelDate = goal1
            val goal2 = outFormat1.format(System.currentTimeMillis())
            val currentDate = goal2
            if (modelDate == currentDate) {
                Log.d("CurrentDayWeatherSetAdapterLogTag", "CurrentDayWeatherSetAdapter: " + i)
                newForcastList.add(mydata.get(i))
                Log.d(
                    "CurrentDayWeatherSetAdapterLogTag",
                    "CurrentDayWeatherSetAdapter: " + modelDate + "==" + currentDate
                )
            }
        }

        if (newForcastList.size > 0) {
            try {
                crntDayAdapter =
                    CurrentDayWeatherAdapter(this@CurrentWeatherActivity, newForcastList)
                binding!!.weatherRecView.setAdapter(crntDayAdapter)
                binding!!.weatherRecView.setLayoutManager(managerForCurrentDayWeatherAdapter)
            } catch (e: Exception) {
            }
        }

    }

    private fun getWeatherConditions(cond: String, mContext: Context) {
        when (cond) {
            "04d" ->
                try {
                    Glide.with(mContext).load(R.drawable.broken_weather_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "04n" ->
                try {
                    Glide.with(mContext).load(R.drawable.broken_weather_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "10d" ->
                try {
                    Glide.with(mContext).load(R.drawable.rain_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "10n" ->
                try {
                    Glide.with(mContext).load(R.drawable.rain_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "11d" ->
                try {
                    Glide.with(mContext).load(R.drawable.thunderstorm_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "11n" ->
                try {
                    Glide.with(mContext).load(R.drawable.thunderstorm_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "02d" ->
                try {
                    Glide.with(mContext).load(R.drawable.few_clouds_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "02n" ->
                try {
                    Glide.with(mContext).load(R.drawable.few_clouds_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "09d" ->
                try {
                    Glide.with(mContext).load(R.drawable.shower_rain_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "09n" ->
                try {
                    Glide.with(mContext).load(R.drawable.shower_rain_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "01d" ->
                try {
                    Glide.with(mContext).load(R.drawable.clear_sky_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }

            "01n" ->
                try {
                    Glide.with(mContext).load(R.drawable.clear_sky_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }

            "03d" ->
                try {
                    Glide.with(mContext).load(R.drawable.scattered_clouds_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "03n" ->
                try {
                    Glide.with(mContext).load(R.drawable.scattered_clouds_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }

            "13d" ->
                try {
                    Glide.with(mContext).load(R.drawable.snow_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "13n" ->
                try {
                    Glide.with(mContext).load(R.drawable.snow_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "50d" ->
                try {
                    Glide.with(mContext).load(R.drawable.mist_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg)
                } catch (e: Exception) {
                }
            "50n" ->
                try {
                    Glide.with(mContext).load(R.drawable.mist_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding!!.weatherMainProgress!!.visibility = View.GONE
                                return false
                            }
                        }).into(binding!!.weatherMainImg!!)
                } catch (e: Exception) {
                }

        }
    }

    private fun fineAIGpsNavPlace(mySearchText: String) {
        if (mySearchText != "") {
            hideAIGpsNavKeybord()

            val task = ConvertPlaceNameToLatLng(
                this,
                mySearchText,
                object : ConvertPlaceNameToLatLng.GeoTaskCallbackLatlngs {
                    override fun onSuccessLocationFetched(fetchedLatLngs: LatLng?) {
                        if (fetchedLatLngs != null) {

                            try {
                                val newlatAIGpsNav = fetchedLatLngs.latitude
                                val newlngAIGpsNav = fetchedLatLngs.longitude
                                if (newlatAIGpsNav != 0.0 && newlngAIGpsNav != 0.0) {

                                    GetCurrentLocationWeather(
                                        newlatAIGpsNav.toString(),
                                        newlngAIGpsNav.toString()
                                    )

                                } else {
                                    Toast.makeText(
                                        this@CurrentWeatherActivity,
                                        "Location Not Found",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: Exception) {
                            }

                        }
                    }

                    override fun onFailedLocationFetched() {
                        Toast.makeText(
                            this@CurrentWeatherActivity,
                            "Location Not Found",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                })
            task.execute()


        } else {
            Toast.makeText(this, "Please Enter Place Name First", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideAIGpsNavKeybord() {
        try {
            val imm: InputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        } catch (e: Exception) {
        }
    }

    private fun mBannerAdsSmall() {


        MyAppAds.loadEarthMapBannerForMainMediation(
            binding!!.smallAd.adContainer,  this
        )
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

    override fun onDestroy() {
        networkStateReceiver!!.removeListener(this)
        unregisterReceiver(networkStateReceiver)
        unregisterReceiver(mLocationService)
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
}