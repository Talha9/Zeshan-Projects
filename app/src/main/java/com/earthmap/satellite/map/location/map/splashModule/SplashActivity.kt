package com.earthmap.satellite.map.location.map.splashModule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.MyAppClass
import com.earthmap.satellite.map.location.map.databinding.ActivitySplashBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import java.util.*

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    private val myHandler = Handler()
    private var isFromPlay = true
    private val TAG = "SplashLog:"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "isFromPlay: True ")
        MyAppAds.preLoadAds(this)
        MyAppAds.preloadMax(this)


        //  addModelToFirebase(this)
        iniatRemoteConfig()


        binding.animationSplashScreen.setAnimation(R.raw.splash_anim)
        binding.goBtn.setOnClickListener { moveToNext() }

    }
    private fun iniatRemoteConfig() {
        MyAppAds.mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(1)
            .build()
        MyAppAds.mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        MyAppAds.mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(
                this,
                OnCompleteListener<Boolean> { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Log.d("ConfigTAG", "Config params updated: $updated")
                    } else {
                        Log.d("ConfigTAG", "Fetch failed")
                    }
                    // ...
                })


    }




    override fun onResume() {
        super.onResume()
        myHandler.postDelayed({
            binding.goBtn.visibility = View.VISIBLE
        }, 6000)


    }

    override fun onPause() {
        myHandler.removeCallbacksAndMessages(null)
        super.onPause()
    }


    private fun moveToNext() {
        moveToMain()
    }

    private fun moveToMain() {
        if (MyAppClass.appOpenAdsManager != null) {
            MyAppClass.appOpenAdsManager!!.showAppOpenAdForSplash(this,
                object : FullScreenContentCallback() {
                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        super.onAdFailedToShowFullScreenContent(p0)
                        val intent =
                            Intent(this@SplashActivity, PrivicyActivity::class.java)
                        startActivity(intent)
                        finish()
                    }



                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        val intent =
                            Intent(this@SplashActivity, PrivicyActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })
        } else {
            val intent = Intent(this@SplashActivity, PrivicyActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun isFromPlayStore(context: Context): Boolean {
        val validInstallers: List<String> =
            ArrayList(Arrays.asList("com.android.vending", "com.google.android.feedback"))
        val installer: String? =
            context.packageManager.getInstallerPackageName(context.packageName).toString()
        Log.d("verifyInstallerId", "Installed from : $installer")
        return installer != null && validInstallers.contains(installer)
    }
}