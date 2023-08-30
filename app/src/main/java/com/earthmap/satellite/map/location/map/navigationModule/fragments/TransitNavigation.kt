package com.earthmap.satellite.map.location.map.navigationModule.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.Ads.MyAppShowAds
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.MapNavigation.MapNavigationActivity
import com.earthmap.satellite.map.location.map.Utils.MapNavigation.model.NavigationModel
import com.earthmap.satellite.map.location.map.Utils.constants
import com.earthmap.satellite.map.location.map.databinding.FragmentTransitNavigationBinding
import com.earthmap.satellite.map.location.map.navigationModule.adapters.NavigationTransitAdapter
import com.earthmap.satellite.map.location.map.navigationModule.callbacks.NavigationTransitCallback
import com.earthmap.satellite.map.location.map.navigationModule.callbacks.onRouteTransitCallback
import com.earthmap.satellite.map.location.map.navigationModule.models.TransitRoutesModel
import com.earthmap.satellite.map.location.map.navigationModule.models.TransitWayPointModel
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions

class TransitNavigation(var callback: NavigationTransitCallback) : Fragment() {
    var binding: FragmentTransitNavigationBinding?=null
    var bundle: Bundle? = null
    var currentLat = 0.0
    var currentLng = 0.0
    var destinationLat = 0.0
    var destinationLng = 0.0
    var mContext:Context?=null
    var editTxtList=ArrayList<TransitRoutesModel>()
    var list=ArrayList<TransitRoutesModel>()
    var adapter: NavigationTransitAdapter?=null
    var manager:LinearLayoutManager?=null
    var editTxtCheck=0
    var wayPointIndex=0
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext=activity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentTransitNavigationBinding.inflate(layoutInflater)

        onClickListeners()
        setupAdapter()
        return binding!!.root
    }

    private fun setupAdapter() {
        manager= LinearLayoutManager(mContext)
        binding!!.AddViewRecView.layoutManager=manager
        adapter= NavigationTransitAdapter(mContext!!,editTxtList,object : onRouteTransitCallback {
            override fun onItemRemovedClick(position: Int) {
                editTxtList.removeAt(position)
                adapter!!.notifyDataSetChanged()
            }

            override fun onEditTxtClick(position: Int, model: TransitRoutesModel) {
                wayPointIndex=position
                editTxtCheck=3
                val placeOptions =
                    PlaceOptions.builder().backgroundColor(resources.getColor(R.color.white))
                        .build(PlaceOptions.MODE_FULLSCREEN)

                val intent = PlaceAutocomplete.IntentBuilder()
                    .placeOptions(placeOptions)
                    .accessToken(constants.mapboxApiKey)
                    .build(activity)
                someActivityResultLauncher.launch(intent)

            }
        })
        binding!!.AddViewRecView.adapter=adapter
    }

    private fun onClickListeners() {
        binding!!.currentLocateBtn.setOnClickListener {
            binding!!.navOriginTxt.setText(constants.countryName)
            currentLat = constants.mLatitude
            currentLng = constants.mLongitude
        }

        binding!!.navDestinationTxt.setOnClickListener {
            editTxtCheck=1
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
            editTxtCheck=0
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
                callback.gettingWaypointsData(TransitWayPointModel(currentLat,currentLng,destinationLat,destinationLng,editTxtList))
            }else{
                Toast.makeText(mContext, "Coordinates Missing...!", Toast.LENGTH_SHORT).show()
            }
        }
        binding!!.NavigateBtn.setOnClickListener {
            binding!!.navOriginTxt.text = ""
            binding!!.navDestinationTxt.text = ""

          /*  if (Build.VERSION.SDK_INT < 31) {*/
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
            currentLat=0.0
            currentLng=0.0
            destinationLat=0.0
            destinationLng=0.0
        }

        binding!!.AddViewBtn.setOnClickListener {
            editTxtList.add(TransitRoutesModel("Enter Way Point","0.0","0.0"))
            adapter!!.notifyDataSetChanged()
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
                        when(editTxtCheck){
                            0->{
                                currentLat=lats
                                currentLng=lngs
                                binding!!.navOriginTxt.text=Cityname
                            }
                            1->{
                                destinationLat=lats
                                destinationLng=lngs
                                binding!!.navDestinationTxt.text=Cityname
                            }
                            3->{
                                editTxtList.set(wayPointIndex,
                                    TransitRoutesModel(Cityname,lats.toString(),lngs.toString())
                                )
                                adapter!!.notifyDataSetChanged()
                            }
                        }

                    }
                }
            } else {
                Toast.makeText(mContext, "Location not Found..!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}