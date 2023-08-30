package com.earthmap.satellite.map.location.map.Utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import com.earthmap.satellite.map.location.map.Utils.dialogs.LocationDialog

class LocationService(var activity: AppCompatActivity,var dialog: LocationDialog):
    BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (LocationManager.PROVIDERS_CHANGED_ACTION == p1!!.action) {
            val locationManager =
                activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (!isGpsEnabled) {
                dialog.show()
            } else {
              dialog.dismiss()
            }
        }
    }
}