package com.earthmap.satellite.map.location.map

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.earthmap.satellite.map.location.map.Ads.MyAppNativeAds
import com.earthmap.satellite.map.location.map.Ads.PurchaseHelper
import com.earthmap.satellite.map.location.map.databinding.ActivityPremiumBinding
import com.earthmap.satellite.map.location.map.home.Home

class PremiumActivity() : AppCompatActivity() {
    lateinit var binding: ActivityPremiumBinding
    lateinit var helper: PurchaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPremiumBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initLizers()
        clickListener()
    }

    private fun initLizers() {
        helper = PurchaseHelper(this)
    }

    private fun clickListener() {
        binding.removeads.setOnClickListener {
            helper.purchaseAdsPackage()
        }
        binding.premiumModuls.setOnClickListener {
            helper.purchaseAdsPackage()

        }
        binding.bothAdsModules.setOnClickListener {
            helper.purchaseAdsPackage()

        }
        binding.backArrow.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        binding.premiumDesc.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://liveearthmapsnavigation.blogspot.com/2023/01/live-earth-map-and-navigation.html")
                )
            )
        }

    }


}