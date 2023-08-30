package com.earthmap.satellite.map.location.map.compassModule.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.earthmap.satellite.map.location.map.compassModule.Callbacks.CompassCallbacks

class CompassFunctionality(mContext: Context) : SensorEventListener {
    private val TAG = "Compass"
    var sensorManager: SensorManager
    var gsensor: Sensor
    var msensor: Sensor
    var mGravity = FloatArray(3)
    var mGeomagnetic = FloatArray(3)
    var R = FloatArray(9)
    var I = FloatArray(9)
    var azimuth = 0f
    var azimuthFix = 0f
    var listener: CompassCallbacks? = null


    init {
        sensorManager = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }
    fun start() {
        sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    fun setmAzimuthFix(fix: Float) {
        azimuthFix = fix
    }

    fun resetAzimuthFix() {
        setmAzimuthFix(0f)
    }

    @JvmName("setListener1")
    fun setListener(l: CompassCallbacks) {
        listener = l
    }


    override fun onSensorChanged(p0: SensorEvent?) {
        val alpha = 0.97f
        synchronized(this) {
            if (p0!!.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * p0.values.get(0) //X
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * p0.values.get(1) //Y
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * p0.values.get(2) //Z

                // mGravity = event.values;
                Log.e(TAG + " ACCELEROMETER ", java.lang.Float.toString(mGravity[0]))
                Log.e(TAG + " ACCELEROMETER ", java.lang.Float.toString(mGravity[1]))
                Log.e(TAG + " ACCELEROMETER ", java.lang.Float.toString(mGravity[2]))
            }
            if (p0.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                // mGeomagnetic = event.values;
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * p0.values.get(0) //X
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * p0.values.get(1) //Y
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * p0.values.get(2) //Z
                Log.e(TAG + " Magnetic ", java.lang.Float.toString(p0.values.get(0)))
                Log.e(
                    TAG + " Magnetic ",
                    java.lang.Float.toString(p0.values.get(0))
                )
                Log.e(TAG + " Magnetic ", java.lang.Float.toString(p0.values.get(0)))
            }
            val success = SensorManager.getRotationMatrix(
                R, I, mGravity,
                mGeomagnetic
            )
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)
                Log.d(TAG, "azimuth (rad): $azimuth")
                azimuth =
                    Math.toDegrees(orientation[0].toDouble()).toFloat() // orientation
                azimuth = (azimuth + azimuthFix + 360) % 360
                Log.d(TAG, "azimuth (deg): $azimuth")
                if (listener != null) {
                    listener!!.onNewAzimuth(azimuth)
                    listener!!.getXYValues(mGravity[0], mGravity[1])
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

}