package com.earthmap.satellite.map.location.map.navigationModule.fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.earthmap.satellite.map.location.map.Geocoders.ConvertPlaceNameToLatLng
import com.earthmap.satellite.map.location.map.Utils.MapNavigation.MapNavigationActivity
import com.earthmap.satellite.map.location.map.Utils.MapNavigation.model.NavigationModel
import com.earthmap.satellite.map.location.map.Utils.constants
import com.earthmap.satellite.map.location.map.databinding.FragmentVoiceNavigationBinding
import com.earthmap.satellite.map.location.map.navigationModule.callbacks.onPlaceVoiceCallback
import com.earthmap.satellite.map.location.map.navigationModule.models.PlaceResultModel
import com.mapbox.mapboxsdk.geometry.LatLng

class VoiceNavigation(var callback: onPlaceVoiceCallback) : Fragment() {
    var binding: FragmentVoiceNavigationBinding? = null
    var bundle: Bundle? = null
    var mContext: Context? = null
    var currentLat = 0.0
    var currentLng = 0.0
    var destinationLat = 0.0
    var destinationLng = 0.0
    var mConvertPlaceNameToLatLng: ConvertPlaceNameToLatLng? = null
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoiceNavigationBinding.inflate(layoutInflater)

        onClickListeners()

        return binding!!.root
    }

    private fun onClickListeners() {
        binding!!.currentLocateBtn.setOnClickListener {
            binding!!.navOriginTxt.setText(constants.countryName)
        }
        binding!!.navigationSpeakBtn.setOnClickListener {
            voiceDestination()
        }
        binding!!.navDestinationTxt.setOnClickListener {
            voiceDestination()
        }
        binding!!.RouteBtn.setOnClickListener {
            if (currentLat != 0.0 && currentLng != 0.0 && destinationLat != 0.0 && destinationLng != 0.0) {
            callback.onVoiceTextGet(fetchCoordinates(currentLat,currentLng,destinationLat,destinationLng))
            binding!!.RouteBtn.visibility=View.GONE
            binding!!.NavigateBtn.visibility=View.VISIBLE
        }
    }
        binding!!.NavigateBtn.setOnClickListener {
            binding!!.NavigateBtn.visibility=View.GONE
            binding!!.RouteBtn.visibility=View.VISIBLE
            binding!!.navOriginTxt.text=""
            binding!!.navDestinationTxt.text=""
                if(currentLat!=0.0 && currentLng!=0.0 && destinationLat!=0.0 && destinationLng!=0.0){
                    val intent=Intent(mContext, MapNavigationActivity::class.java)
                    Log.d("NavigateBtnTAG", "onClickListeners: OKKKK")
                    intent.putExtra("navigation_model",
                        NavigationModel(currentLat,currentLng,destinationLat,destinationLng,0)
                    )
                    startActivity(intent)
                }else{
                    Toast.makeText(mContext, "Location not Found..!", Toast.LENGTH_SHORT).show()
                }

        }
    }


    private fun fetchCoordinates(
        mLatitude: Double,
        mLongitude: Double,
        latitude1: Double,
        longitude1: Double
    ): PlaceResultModel {
        var mPlaceResultModel=PlaceResultModel()
        if (mLatitude != 0.0 && mLongitude != 0.0 && latitude1 != 0.0 && longitude1 != 0.0) {
            mPlaceResultModel = PlaceResultModel(mLatitude, mLongitude, latitude1, longitude1)
        } else {
            Toast.makeText(mContext, "Please Enter Location...!", Toast.LENGTH_SHORT).show()
        }
        return mPlaceResultModel
    }

    private fun voiceDestination() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Starting Point")
        try {
            startActivityForResult(intent, 1)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                mContext, "Speech not recognized",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {

                if (resultCode == Activity.RESULT_OK) {

                    try {
                        val listResults =
                            data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        val resultText = listResults!!.get(0)
                        mConvertPlaceNameToLatLng =
                            ConvertPlaceNameToLatLng(
                                mContext!!,
                                resultText,
                                object : ConvertPlaceNameToLatLng.GeoTaskCallbackLatlngs {
                                    override fun onSuccessLocationFetched(fetchedLatLngs: LatLng?) {
                                        if (fetchedLatLngs != null) {
                                            try {
                                                binding!!.navDestinationTxt.setText(resultText)
                                                destinationLat = fetchedLatLngs.latitude
                                                destinationLng = fetchedLatLngs.longitude
                                                currentLat = constants.mLatitude
                                                currentLng = constants.mLongitude
                                                if (currentLat != 0.0 && currentLng != 0.0 && destinationLat != 0.0 && destinationLng != 0.0) {
                                                    callback.onVoiceTextGet(
                                                        fetchCoordinates(
                                                            currentLat,
                                                            currentLng,
                                                            destinationLat,
                                                            destinationLng
                                                        )
                                                    )
                                                    binding!!.RouteBtn.visibility = View.GONE
                                                    binding!!.NavigateBtn.visibility = View.VISIBLE
                                                }

                                            } catch (e: Exception) {
                                            }
                                        } else {
                                            Toast.makeText(
                                                mContext, "Location Not Found!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onFailedLocationFetched() {
                                        Toast.makeText(
                                            mContext, "Location Not Found!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                })
                        mConvertPlaceNameToLatLng!!.execute()
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }
}