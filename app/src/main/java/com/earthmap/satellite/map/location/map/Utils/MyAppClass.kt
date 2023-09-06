package com.earthmap.satellite.map.location.map.Utils

import android.R
import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.earthmap.satellite.map.location.map.Ads.AppOpenAdsManager
import com.earthmap.satellite.map.location.map.Ads.MyAdModel
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings


class MyAppClass : Application() {

    companion object {
        var myApplication: Application? = null
        var appOpenAdsManager: AppOpenAdsManager? = null
        fun getStr(id: Int): String {
            return myApplication!!.getString(id)
        }
    }

    override fun onCreate() {
        super.onCreate()
        myApplication = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Firebase.initialize(this)
       // getDataFromFirebase()

        try {
            MobileAds.initialize(this) { }
        } catch (e: Exception) {
        }
        appOpenAdsManager = AppOpenAdsManager(this)
        AudienceNetworkAds.initialize(this)
       // MyAppAds.preReLoadAds(this)
        /* MobileAds.openAdInspector(this) {
             // Error will be non-null if ad inspector closed due to an error.
             Log.d("adsInspection", "ad Inspect:${it?.message} ")
         }*/

    }



    private fun getDataFromFirebase() {
        try {
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("LiveEarthMapNavigation")
            databaseReference.child("RelesAds").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val model = dataSnapshot.getValue<MyAdModel>(
                        MyAdModel::class.java
                    )
                    if (model != null) {
                        MyAppAds.haveGotSnapshot = true
                        MyAppAds.interstitial_admob_inApp = model.interstitial_admob_inApp
                        MyAppAds.banner_admob_inApp = model.banner_admob_inApp
                        MyAppAds.native_admob_inApp = model.native_admob_inApp
                        MyAppAds.app_open_admob_inApp = model.app_open_admob_inApp
                        MyAppAds.shouldShowAppOpen = model.shouldShowAppOpen
                        MyAppAds.shouldShowAdmob = model.isShould_show_admob
                        MyAppAds.next_ads_time = model.next_ads_time.toLong()
                        MyAppAds.current_counter = model.current_counter
                        MyAppAds.showSplashGoAd = model.show_splash_go_ad


                        Log.i(
                            "LoadAds",
                            "ads ids:  " + "show_splash_go_ad : " + model.show_splash_go_ad + "\n" + "banner ad: " + model.banner_admob_inApp + "\n"
                        )
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.i("LoadAds", databaseError.message)
                }
            })
        } catch (e: Exception) {
        }
    }

}