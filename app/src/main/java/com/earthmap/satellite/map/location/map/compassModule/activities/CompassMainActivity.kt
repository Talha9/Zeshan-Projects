package com.earthmap.satellite.map.location.map.compassModule.activities

import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.Ads.MyAppNativeAds
import com.earthmap.satellite.map.location.map.Ads.MyAppShowAds
import com.earthmap.satellite.map.location.map.Utils.getAdaptiveAdSize
import com.earthmap.satellite.map.location.map.compassModule.Callbacks.CompassCallbacks
import com.earthmap.satellite.map.location.map.compassModule.utils.CompassFormatter
import com.earthmap.satellite.map.location.map.compassModule.utils.CompassFunctionality
import com.earthmap.satellite.map.location.map.databinding.ActivityCompassMainBinding
import com.google.android.gms.ads.AdView

import java.text.DecimalFormat

class CompassMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityCompassMainBinding
    private var currentAzimuth = 0f
    var df2: DecimalFormat? = null
    var mCompassFunctionality: CompassFunctionality? = null
    var mCompassFormatter: CompassFormatter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompassMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializers()
        onClickListeners()
        mBannerAdsSmall()
        nativeAds()
    }

    private fun onClickListeners() {
        binding.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        MyAppShowAds.mediationBackPressedSimple(
            this,
            MyAppAds.admobInterstitialAd,
            MyAppAds.maxInterstitialAdLiveEarth
        ) {
            super.onBackPressed()
        }
    }

    private fun initializers() {
        binding!!.header.headerBarTitleTxt.text = "Compass"
        df2 = DecimalFormat("#.##")
        mCompassFormatter = CompassFormatter(this)
        mCompassFunctionality = CompassFunctionality(this)
        val l = getCompassListener()
        mCompassFunctionality!!.setListener(l)
    }

    override fun onStart() {
        super.onStart()
        mCompassFunctionality!!.start()
    }

    override fun onPause() {
        mCompassFunctionality!!.stop()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mCompassFunctionality!!.start()
    }

    override fun onStop() {
        mCompassFunctionality!!.stop()
        super.onStop()
    }

    private fun getCompassListener(): CompassCallbacks {
        return object : CompassCallbacks {
            override fun onNewAzimuth(azimuth: Float) {
                runOnUiThread {
                    adjustArrow(azimuth)
                    adjustSotwLabel(azimuth)
                }
            }

            override fun getXYValues(x: Float, y: Float) {
                binding.XValue.setText(df2!!.format(x.toDouble()) + 0x00B0.toChar())
                binding.YValue.setText(df2!!.format(y.toDouble()) + 0x00B0.toChar())
            }
        }
    }

    private fun adjustArrow(azimuth: Float) {
        Log.d(
            "Comp", "will set rotation from " + currentAzimuth + " to "
                    + azimuth
        )
        val an: Animation = RotateAnimation(
            -currentAzimuth, -azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuth = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true
        binding.CompassDailer.startAnimation(an)
    }

    private fun adjustSotwLabel(azimuth: Float) {
        binding.CompassText.text = mCompassFormatter!!.format(azimuth)
    }

    private fun mBannerAdsSmall() {
        MyAppAds.loadEarthMapBannerForMainMediation(binding.smallAd.adContainer, this)

    }

    private fun nativeAds() {
        val adMobFrame = binding.nativeAd.flAdplaceholder
        MyAppNativeAds.loadAdmobNativeAdPriority(this, adMobFrame)
    }
}