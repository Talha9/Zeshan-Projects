package com.earthmap.satellite.map.location.map.splashModule

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.earthmap.satellite.map.location.map.Ads.BillingHelper
import com.earthmap.satellite.map.location.map.Ads.MyAppAds
import com.earthmap.satellite.map.location.map.MainActivity
import com.earthmap.satellite.map.location.map.PremiumActivity
import com.earthmap.satellite.map.location.map.databinding.ActivityPrivicyBinding
import com.earthmap.satellite.map.location.map.home.Home
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class PrivicyActivity : AppCompatActivity() {
    lateinit var binding: ActivityPrivicyBinding
    private var sharedLiveStreetViewPreferences: SharedPreferences? = null
    private var listOfKeys = ArrayList<String>()
    var billingHelper: BillingHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialization()
        onClickListeners()
        fillMyList()
    }

    private fun initialization() {
        sharedLiveStreetViewPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)

        val isSceondTimet = sharedLiveStreetViewPreferences!!.getBoolean("is2ndTime", false)

        if (isSceondTimet) {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        } else {
            billingHelper = BillingHelper(this)
            billingHelper?.setBillingPreferences(true)
        }

    }

    private fun onClickListeners() {

        binding.privacyMoveToMainBtn.setOnClickListener {
            if (binding.privacyCheck.isChecked) {
                sharedLiveStreetViewPreferences!!.edit()
                    .putBoolean("is2ndTime", true).apply()
                startActivity(Intent(this, Home::class.java))
                sharedLiveStreetViewPreferences!!.edit()
                    .putString("MAPBOX_LiveStreetView_APP_KEY", getKeyFromCounterCheck()).apply()
                updateCounterVarFromFirebase()
                finish()
            } else {
                Toast.makeText(this, "Please Checked box!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.readMoreBtn.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://liveearthmapsnavigation.blogspot.com/2023/01/live-earth-map-and-navigation.html")
                )
            )
        }
    }

    private fun fillMyList() {
        listOfKeys.add(MyAppAds.mapbox_access_token_1)
        listOfKeys.add(MyAppAds.mapbox_access_token_2)
        listOfKeys.add(MyAppAds.mapbox_access_token_3)
        listOfKeys.add(MyAppAds.mapbox_access_token_4)
        listOfKeys.add(MyAppAds.mapbox_access_token_5)
        listOfKeys.add(MyAppAds.mapbox_access_token_6)
        listOfKeys.add(MyAppAds.mapbox_access_token_7)
        listOfKeys.add(MyAppAds.mapbox_access_token_8)
    }

    @SuppressLint("LongLogTag")
    private fun getKeyFromCounterCheck(): String? {
        var myKey: String? = null
        when (MyAppAds.current_counter) {
            1.0 -> {
                myKey = MyAppAds.mapbox_access_token_1
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 1.0)
            }
            2.0 -> {
                myKey = MyAppAds.mapbox_access_token_2
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 2.0)
            }
            3.0 -> {
                myKey = MyAppAds.mapbox_access_token_3
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 3.0)
            }
            4.0 -> {
                myKey = MyAppAds.mapbox_access_token_4
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 4.0)
            }
            5.0 -> {
                myKey = MyAppAds.mapbox_access_token_5
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 5.0)
            }
            6.0 -> {
                myKey = MyAppAds.mapbox_access_token_6
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 6.0)
            }
            7.0 -> {
                myKey = MyAppAds.mapbox_access_token_7
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 7.0)
            }
            8.0 -> {
                myKey = MyAppAds.mapbox_access_token_8
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 8.0)
            }
            else -> {
                myKey = listOfKeys.get(Random.nextInt(listOfKeys.size))
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + myKey)

            }
        }
        return myKey
    }

    fun updateCounterVarFromFirebase() {
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("LiveEarthMapNavigation")
        if (MyAppAds.haveGotSnapshot) {
            if (MyAppAds.current_counter >= 8.0) {
                MyAppAds.current_counter = 1.0
            } else {
                MyAppAds.current_counter++
            }
            MyAppAds.haveGotSnapshot = false
            databaseReference.child("RelesAds")
                .child("current_counter").setValue(
                    MyAppAds.current_counter
                )
        }
    }


    override fun onBackPressed() {
        finish()
        finishAffinity()
    }

}