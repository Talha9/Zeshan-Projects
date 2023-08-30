package com.earthmap.satellite.map.location.map.Utils.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.earthmap.satellite.map.location.map.Utils.callbacks.MapStylesDialogCallback
import com.earthmap.satellite.map.location.map.databinding.MapStylesDialogBinding


class MapStylesDialog(var mContext: Context,var callback: MapStylesDialogCallback): Dialog(mContext) {
var binding: MapStylesDialogBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= MapStylesDialogBinding.inflate(layoutInflater)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding!!.root)

        onClickLiters()
    }

    private fun onClickLiters() {
       binding!!.defaultStyleMap.setOnClickListener {
           callback.onMapDefaultMapClick()
       }
        binding!!.nightStyleMap.setOnClickListener {
            callback.onMapNightMapClick()
        }
        binding!!.satelliteStyleMap.setOnClickListener {
            callback.onMapSatelliteMapClick()
        }
        binding!!.dialogCloseBtn.setOnClickListener {
            try {
                dismiss()
            } catch (e: Exception) {
            }
        }
    }
}