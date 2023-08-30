package com.earthmap.satellite.map.location.map.navigationModule.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.Ads.MyAppShowAds
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.MapNavigation.MapNavigationActivity
import com.earthmap.satellite.map.location.map.Utils.MapNavigation.model.NavigationModel
import com.earthmap.satellite.map.location.map.Utils.constants
import com.earthmap.satellite.map.location.map.databinding.FragmentTextNavigationBinding
import com.earthmap.satellite.map.location.map.navigationModule.callbacks.onPlaceTextChangeCallback
import com.earthmap.satellite.map.location.map.navigationModule.models.PlaceResultModel
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions

class TextNavigation(var callback: onPlaceTextChangeCallback) : Fragment() {
    private var destClick = true
    var mContext: Context? = null
    var binding: FragmentTextNavigationBinding? = null
    var bundle: Bundle? = null
    var currentLat = 0.0
    var currentLng = 0.0
    var destinationLat = 0.0
    var destinationLng = 0.0
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextNavigationBinding.inflate(layoutInflater)

        onClickListeners()

        return binding!!.root
    }


    private fun onClickListeners() {
        binding!!.currentLocateBtn.setOnClickListener {
            binding!!.navOriginTxt.setText(constants.countryName)
            currentLat = constants.mLatitude
            currentLng = constants.mLongitude
        }
        binding!!.navDestinationTxt.setOnClickListener {
            destClick = true
            val placeOptions =
                PlaceOptions.builder().backgroundColor(resources.getColor(R.color.white))
                    .build(PlaceOptions.MODE_FULLSCREEN)

            val intent = PlaceAutocomplete.IntentBuilder()
                .placeOptions(placeOptions)
                .accessToken(constants.mapboxApiKey)
                .build(activity)
            someActivityResultLauncher.launch(intent)
        }
        binding!!.navOriginTxt.setOnClickListener {
            destClick = false
            val placeOptions =
                PlaceOptions.builder().backgroundColor(resources.getColor(R.color.white))
                    .build(PlaceOptions.MODE_FULLSCREEN)

            val intent = PlaceAutocomplete.IntentBuilder()
                .placeOptions(placeOptions)
                .accessToken(constants.mapboxApiKey)
                .build(activity)
            someActivityResultLauncher.launch(intent)
        }
        binding!!.RouteBtn.setOnClickListener {
            if (currentLat != 0.0 && currentLng != 0.0 && destinationLat != 0.0 && destinationLng != 0.0) {
                callback.onTextChange(fetchCoordinates())
                binding!!.RouteBtn.visibility = View.GONE
                binding!!.NavigateBtn.visibility = View.VISIBLE
            }
        }
        binding!!.NavigateBtn.setOnClickListener {
            binding!!.NavigateBtn.visibility = View.GONE
            binding!!.RouteBtn.visibility = View.VISIBLE
            binding!!.navOriginTxt.text = ""
            binding!!.navDestinationTxt.text = ""
            /*if (Build.VERSION.SDK_INT < 31) {*/
                if (currentLat != 0.0 && currentLng != 0.0 && destinationLat != 0.0 && destinationLng != 0.0) {
                    val intent = Intent(mContext, MapNavigationActivity::class.java)
                    intent.putExtra(
                        "navigation_model",
                        NavigationModel(currentLat, currentLng, destinationLat, destinationLng,0)
                    )
                   MyAppShowAds.meidationForClickSimpleLiveStreetView(mContext,
                       MyAppAds.admobInterstitialAd,MyAppAds.maxInterstitialAdLiveEarth){
                       startActivity(intent)
                   }
                } else {
                    Toast.makeText(mContext, "Location not Found..!", Toast.LENGTH_SHORT).show()
                }
           /* } else {
                Toast.makeText(mContext, "Feature Not Available..!", Toast.LENGTH_SHORT).show()
            }*/
        }
    }

    var someActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            Log.d("ActivityReciveData", "onActivityResult: " + data!!.extras)
            if (data != null) {
                val feature = PlaceAutocomplete.getPlace(data)
                if (feature.center() != null) {
                    if (!feature.center()!!.coordinates().isEmpty()) {
                        val lats = feature.center()!!.coordinates()[1]
                        val lngs = feature.center()!!.coordinates()[0]
                        val Cityname = feature.text()
                        if (destClick) {
                            binding!!.navDestinationTxt.setText(Cityname)
                            destinationLat = lats
                            destinationLng = lngs
                        } else {
                            binding!!.navOriginTxt.setText(Cityname)
                            currentLat = lats
                            currentLng = lngs
                        }
                    }
                }
            } else {
                Toast.makeText(mContext, "Location not Found..!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchCoordinates(): PlaceResultModel {
        var mPlaceResultModel = PlaceResultModel()
        if (currentLat != 0.0 && currentLng != 0.0 && destinationLat != 0.0 && destinationLng != 0.0) {
            mPlaceResultModel =
                PlaceResultModel(currentLat, currentLng, destinationLat, destinationLng)
        } else {
            Toast.makeText(mContext, "Please Enter Location...!", Toast.LENGTH_SHORT).show()
        }
        return mPlaceResultModel
    }


}