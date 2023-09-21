package com.earthmap.satellite.map.location.map.Utils

import android.Manifest
import android.R
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import java.io.InputStream
import java.net.URL


class UtilsFunctionClass {

    companion object {

        fun setImageInGlideFromString(mContext: Context, i: String, p: ProgressBar, v: ImageView) {
            try {
                Glide.with(mContext).load(i).listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        p.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        p.visibility = View.GONE
                        return false
                    }
                }).into(v)
            } catch (e: Exception) {
            }
        }
        fun setImageInGlideFromDrawable(mContext: Context, i: Drawable, p: ProgressBar, v: ImageView) {
            try {
                Glide.with(mContext).load(i).listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        p.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        p.visibility = View.GONE
                        return false
                    }
                }).into(v)
            } catch (e: Exception) {
            }
        }


        fun hideKeyboard(activity: Activity) {
            val view = activity.findViewById<View>(R.id.content)
            if (view != null) {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun LoadImageFromWebURL(url: String?): Drawable? {
            return try {
                val iStream: InputStream = URL(url).getContent() as InputStream
                Drawable.createFromStream(iStream, "src name")
            } catch (e: java.lang.Exception) {
                null
            }
        }


        fun askForPermissions(mContext: Context){
            var checkLocation=false
            var checkStorage=false
            val permissions = arrayOf<String>(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            val rationale = "Please provide permission so that you can Use Module..."
            val options: Permissions.Options = Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning")

            Permissions.check(
                mContext /*context*/,
                permissions,
                rationale,
                options,
                object : PermissionHandler() {
                    override fun onGranted() {
                       // Toast.makeText(mContext,"Permission Allowed!",Toast.LENGTH_SHORT).show()
                        constants.storageModule=true
                        constants.locationModule=true
                    }
                    override fun onDenied(context: Context?, deniedPermissions: ArrayList<String?>?) {
                        for(i in 0 until deniedPermissions!!.size){
                            if(deniedPermissions[i]== Manifest.permission.ACCESS_FINE_LOCATION || deniedPermissions[i]== Manifest.permission.ACCESS_COARSE_LOCATION){
                                if (!checkLocation) {
                                    Toast.makeText(mContext,"Location Permission Not Allowed!",Toast.LENGTH_SHORT).show()
                                     checkLocation=true
                                    constants.locationModule=false
                                }
                            }
                           /* if(deniedPermissions[i]== Manifest.permission.READ_EXTERNAL_STORAGE || deniedPermissions[i]== Manifest.permission.WRITE_EXTERNAL_STORAGE){
                                if (!checkStorage) {
                                    Toast.makeText(mContext,"Storage Permission Not Allowed!",Toast.LENGTH_SHORT).show()
                                    checkStorage=true
                                    constants.storageModule=false
                                }
                            }*/
                        }
                    }
                })
        }

        fun animateToBounds(mapBoxInstance:MapboxMap,list: ArrayList<LatLng>) {
            val latLngList: MutableList<LatLng> = java.util.ArrayList()
            for (i in list.indices) {
                val LocLat: Double = list[i].latitude
                val LocLang: Double = list[i].longitude
                if (LocLat != 0.0 && LocLang != 0.0) {
                    latLngList.add(LatLng(LocLat, LocLang))
                }
            }
            val latLngBounds = LatLngBounds.Builder()
                .includes(latLngList)
                .build()
            try {
                mapBoxInstance.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(latLngBounds, 50),
                    5000
                )
            } catch (e: java.lang.Exception) {
            }
        }

         fun animateCamIn3DAngle(mLatLng: LatLng,mapboxMap: MapboxMap,mBuildingPlugin: BuildingPlugin):Int {
            val camPosition = CameraPosition.Builder()
                .target(mLatLng)
                .tilt(60.0)
                .bearing(-10.0)
                .zoom(16.0)
                .build()
            try {
                mBuildingPlugin.setVisibility(true)
            } catch (e: Exception) {
            }
            try {
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition))
            } catch (e: Exception) {
            }

            return 1
        }

         fun animateCamIn2DAngle(mLatLng: LatLng,mapboxMap: MapboxMap,mBuildingPlugin: BuildingPlugin):Int {
            val camPosition = CameraPosition.Builder()
                .target(mLatLng)
                .tilt(15.0)
                .bearing(1.0)
                .zoom(16.0)
                .build()
            try {
                mBuildingPlugin.setVisibility(false)
            } catch (e: Exception) {
            }
            try {
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition))
            } catch (e: Exception) {
            }
            return 0
        }





    }
}