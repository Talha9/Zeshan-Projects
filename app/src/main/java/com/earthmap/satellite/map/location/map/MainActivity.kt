package com.earthmap.satellite.map.location.map

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.earthmap.satellite.map.location.map.Utils.LocationClass
import com.earthmap.satellite.map.location.map.Utils.LocationService
import com.earthmap.satellite.map.location.map.Utils.callbacks.LocationDialogCallback
import com.earthmap.satellite.map.location.map.Utils.constants
import com.earthmap.satellite.map.location.map.Utils.dialogs.LocationDialog
import com.earthmap.satellite.map.location.map.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}