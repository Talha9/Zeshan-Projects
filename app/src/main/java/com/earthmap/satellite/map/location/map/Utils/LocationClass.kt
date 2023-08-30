package com.earthmap.satellite.map.location.map.Utils
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices


class LocationClass(var mContext: Context) : GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private lateinit var locationRequest: LocationRequest
    private lateinit var googleApiClient: GoogleApiClient
//    private var GpsEnableDialog: LocationDialog? = null

    fun CallforLocation() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                locationRequest,
                this)
        } catch (e: Exception) {
        }
    }
    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient,
                    locationRequest,
                    this
                )
            } catch (e: Exception) {
            }
        }
    }
    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(mContext, "Cannot Get Location", Toast.LENGTH_LONG).show()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(mContext, "Cannot Get Location", Toast.LENGTH_LONG).show()
    }

    override fun onLocationChanged(p0: Location) {
//        Log.d(TAG, "onLocationChanged: ${p0.latitude}")
        if (p0 != null) {
            if (p0.latitude != 0.0 && p0.longitude != 0.0) {
                constants.mLatitude = p0.latitude
                constants.mLongitude = p0.longitude
                constants.mAltitude = p0.altitude
                constants.location=p0
                constants.mSpeed= p0.speed
                Log.i(
                    "onLocationChangedTag",
                    "Latitude: " + constants.mLatitude
                )
                Log.i(
                    "onLocationChangedTag",
                    "Longitude: " + constants.mLongitude
                )
            }
        }
    }
    fun stopLocationRequest() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
        } catch (e: Exception) {
        }
    }

fun initLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 20000
        locationRequest.fastestInterval = 20000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        googleApiClient = GoogleApiClient.Builder(mContext)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        try {
            googleApiClient.connect()
        } catch (e: Exception) {
        }
    }


}