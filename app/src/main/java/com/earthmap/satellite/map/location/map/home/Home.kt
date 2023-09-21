package com.earthmap.satellite.map.location.map.home

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.earthmap.satellite.map.location.map.PremiumActivity
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.LocationClass
import com.earthmap.satellite.map.location.map.Utils.LocationService
import com.earthmap.satellite.map.location.map.Utils.UtilsFunctionClass
import com.earthmap.satellite.map.location.map.Utils.callbacks.LocationDialogCallback
import com.earthmap.satellite.map.location.map.Utils.constants
import com.earthmap.satellite.map.location.map.Utils.dialogs.LocationDialog
import com.earthmap.satellite.map.location.map.Utils.dialogs.RateUsDailog
import com.earthmap.satellite.map.location.map.databinding.ActivityHomeBinding

class Home : AppCompatActivity(), LocationDialogCallback {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    lateinit var mFetchLocation: LocationClass
    private var sharedPreferences: SharedPreferences? = null
    lateinit var mLocationService: LocationService
    var mRateUsDialog: RateUsDailog? = null
    var gpsEnableDialog: LocationDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialization()
        setSupportActionBar(binding.appBarHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_rateUs -> {
                    mRateUsDialog = RateUsDailog(this)
                    mRateUsDialog?.show()
                    drawerLayout.close()
                    true
                }
                R.id.nav_share -> {
                    val uri =
                        "https://play.google.com/store/apps/details?id=${packageName}"
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    val shareSub = packageName
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
                    startActivity(Intent.createChooser(sharingIntent, "Share via"))
                    drawerLayout.close()
                    true
                }
                R.id.nav_privicy -> {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://liveearthmapsnavigation.blogspot.com/2023/01/live-earth-map-and-navigation.html")
                        )
                    )
                    drawerLayout.close()
                    true
                }
                else -> {
                    true
                }
            }
        }

      /*  binding.appBarHome.premiumBtn.setOnClickListener {
            startActivity(Intent(this, PremiumActivity::class.java))
        }*/

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initialization() {
        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        constants.mapboxApiKey =
            sharedPreferences!!.getString("MAPBOX_LiveStreetView_APP_KEY", constants.mapboxApiKey)!!
        mFetchLocation = LocationClass(this)
        mFetchLocation.initLocationRequest()
        gpsEnableDialog = LocationDialog(this, this)

       // binding.appBarHome.premiumBtn.setAnimation(R.raw.premium_anim)

        mLocationService = LocationService(this, gpsEnableDialog!!)
        try {
            val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
            registerReceiver(mLocationService, filter)
        } catch (e: Exception) {
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
    }

    override fun onEnabledGPS() {
        try {
            val callGPSSettingIntent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            startActivity(callGPSSettingIntent)
        } catch (e: Exception) {
        }
    }

    override fun onDestroy() {
        unregisterReceiver(mLocationService)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsEnableDialog!!.show()
        } else {
            UtilsFunctionClass.askForPermissions(this)
            Log.d("onResumeTAG", "onResume: ")
        }
    }
}