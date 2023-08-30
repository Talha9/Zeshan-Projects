package com.earthmap.satellite.map.location.map.nearBy.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.Ads.MyAppShowAds
import com.earthmap.satellite.map.location.map.Utils.NetworkStateReceiver
import com.earthmap.satellite.map.location.map.Utils.dialogs.InternetDialog
import com.earthmap.satellite.map.location.map.Utils.getAdaptiveAdSize
import com.earthmap.satellite.map.location.map.databinding.ActivityNearbyMainBinding
import com.earthmap.satellite.map.location.map.nearBy.adapters.NearByAdapter
import com.earthmap.satellite.map.location.map.nearBy.callbacks.NearByCallbacks
import com.earthmap.satellite.map.location.map.nearBy.helpers.NearByHelper
import com.earthmap.satellite.map.location.map.nearBy.models.NearByModel


class NearbyMainActivity : AppCompatActivity(), NetworkStateReceiver.NetworkStateReceiverListener {
    var binding: ActivityNearbyMainBinding?=null
    var list:ArrayList<NearByModel>?=null
    var manager:LinearLayoutManager?=null
    var adapter: NearByAdapter?=null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNearbyMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        listFiller()
        initialization()
        setUpHeader()
        mBannerAdsSmall()
    }

    private fun setupAdapter() {
        manager= LinearLayoutManager(this)

        binding!!.nearbyRecView.layoutManager=manager
        if (list!=null && list!!.size>0) {
            adapter= NearByAdapter(this,list!!,object : NearByCallbacks {
                override fun onNearByCategoryClick(model: NearByModel) {
                    MyAppShowAds.meidationForClickSimpleLiveStreetView(
                        this@NearbyMainActivity,
                        MyAppAds.admobInterstitialAd,MyAppAds.maxInterstitialAdLiveEarth
                    ) {
                        val intent = Intent(this@NearbyMainActivity, NearByActivity::class.java)
                        intent.putExtra("near_By", model)
                        startActivity(intent)
                    }
                }
            })
            binding!!.nearbyRecView.adapter=adapter
            binding!!.progressMainBg.visibility= View.GONE
        }
    }

    private fun listFiller() {
        list= NearByHelper.fillNearByCategoryList()
        setupAdapter()
    }

    private fun initialization() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog= InternetDialog(this)
    }
    private fun setUpHeader() {
        binding!!.header.headerBarTitleTxt.text="Near By"
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
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
    override fun onDestroy() {
        networkStateReceiver!!.removeListener(this)
        unregisterReceiver(networkStateReceiver)
        super.onDestroy()
    }

    private fun mBannerAdsSmall() {
            MyAppAds.loadEarthMapBannerForMainMediation(
                binding!!.smallAd.adContainer,this
            )
    }

}