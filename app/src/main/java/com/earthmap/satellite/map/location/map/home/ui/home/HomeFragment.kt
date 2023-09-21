package com.earthmap.satellite.map.location.map.home.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.Ads.MyAppShowAds
import com.earthmap.satellite.map.location.map.Utils.MyAppClass
import com.earthmap.satellite.map.location.map.compassModule.activities.CompassMainActivity
import com.earthmap.satellite.map.location.map.databinding.FragmentHomeBinding
import com.earthmap.satellite.map.location.map.earthMapModule.EarthMapMainActivity
import com.earthmap.satellite.map.location.map.home.Model.HomeItemModel
import com.earthmap.satellite.map.location.map.home.adapter.HomeAdapter
import com.earthmap.satellite.map.location.map.home.callback.HomeCallback
import com.earthmap.satellite.map.location.map.home.mvvm.HomeViewModel
import com.earthmap.satellite.map.location.map.myLocationModule.MyLocationMainActivity
import com.earthmap.satellite.map.location.map.navigationModule.activities.NavigationMainActivity
import com.earthmap.satellite.map.location.map.nearBy.activities.NearbyMainActivity
import com.earthmap.satellite.map.location.map.satelliteTrackerModule.SatteliteTrackerMainActivity
import com.earthmap.satellite.map.location.map.weatherModule.CurrentWeatherActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings


class HomeFragment : Fragment() {
    lateinit var homeViewModel: HomeViewModel
    lateinit var mContext: Context
    var adapter: HomeAdapter? = null
    var sensorManager: SensorManager? = null
    lateinit var binding: FragmentHomeBinding
    var isSensorAvailable = false
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentHomeBinding.inflate(layoutInflater)



        initialization()



        return binding.root
    }


    private fun checkSensorAvailability(sensorType: Int): Boolean {
        var isSensor = false
        if (sensorManager!!.getDefaultSensor(sensorType) != null) {
            isSensor = true
        }
        return isSensor
    }

    private fun initialization() {
        binding.homeRecView.layoutManager = GridLayoutManager(mContext, 2)
        sensorManager =
            requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        isSensorAvailable = checkSensorAvailability(Sensor.TYPE_MAGNETIC_FIELD)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.list.observe(viewLifecycleOwner) {

            adapter = HomeAdapter(mContext, it, isSensorAvailable,object : HomeCallback {
                override fun onItemClick(model: HomeItemModel, position: Int) {
                    when (position) {
                        0 -> {
                            binding.adWaitingLayout.visibility = View.VISIBLE

                            binding.adWaitingLayout.visibility = View.GONE
                            val mIntent = Intent(mContext, EarthMapMainActivity::class.java)
                            MyAppShowAds.meidationForClickLiveStreetViewWithoutCounter(
                                mContext,
                                MyAppAds.admobInterstitialAd,
                                MyAppAds.maxInterstitialAdLiveEarth,
                                mIntent
                            )
                        }

                        1 -> {
                            binding.adWaitingLayout.visibility = View.VISIBLE

                            binding.adWaitingLayout.visibility = View.GONE
                            val mIntent =
                                Intent(mContext, SatteliteTrackerMainActivity::class.java)
                            MyAppShowAds.meidationForClickLiveStreetViewWithoutCounter(
                                mContext,
                                MyAppAds.admobInterstitialAd,
                                MyAppAds.maxInterstitialAdLiveEarth,
                                mIntent
                            )
                        }

                        2 -> {
                            binding.adWaitingLayout.visibility = View.VISIBLE

                            binding.adWaitingLayout.visibility = View.GONE
                            val mIntent = Intent(mContext, NearbyMainActivity::class.java)
                            MyAppShowAds.meidationForClickLiveStreetViewWithoutCounter(
                                mContext,
                                MyAppAds.admobInterstitialAd,
                                MyAppAds.maxInterstitialAdLiveEarth,
                                mIntent
                            )
                        }

                        3 -> {
                            binding.adWaitingLayout.visibility = View.VISIBLE

                            binding.adWaitingLayout.visibility = View.GONE
                            val mIntent = Intent(mContext, CurrentWeatherActivity::class.java)
                            MyAppShowAds.meidationForClickLiveStreetViewWithoutCounter(
                                mContext,
                                MyAppAds.admobInterstitialAd,
                                MyAppAds.maxInterstitialAdLiveEarth,
                                mIntent
                            )
                        }

                        4 -> {
                            binding.adWaitingLayout.visibility = View.VISIBLE

                            binding.adWaitingLayout.visibility = View.GONE
                            val mIntent = Intent(mContext, NavigationMainActivity::class.java)
                            MyAppShowAds.meidationForClickLiveStreetViewWithoutCounter(
                                mContext,
                                MyAppAds.admobInterstitialAd,
                                MyAppAds.maxInterstitialAdLiveEarth,
                                mIntent
                            )
                        }

                        5 -> {
                            binding.adWaitingLayout.visibility = View.VISIBLE

                            binding.adWaitingLayout.visibility = View.GONE
                            val mIntent = Intent(mContext, MyLocationMainActivity::class.java)
                            MyAppShowAds.meidationForClickLiveStreetViewWithoutCounter(
                                mContext,
                                MyAppAds.admobInterstitialAd,
                                MyAppAds.maxInterstitialAdLiveEarth,
                                mIntent
                            )
                        }

                        6 -> {
                            binding.adWaitingLayout.visibility = View.VISIBLE

                            binding.adWaitingLayout.visibility = View.GONE
                            if (checkSensorAvailability(Sensor.TYPE_MAGNETIC_FIELD)) {
                                val mIntent = Intent(mContext, CompassMainActivity::class.java)
                                MyAppShowAds.meidationForClickLiveStreetViewWithoutCounter(
                                    mContext,
                                    MyAppAds.admobInterstitialAd,
                                    MyAppAds.maxInterstitialAdLiveEarth, mIntent
                                )

                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Magnetic Sensor Not available.!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

            })
            binding.homeRecView.adapter = adapter
        }
    }


}