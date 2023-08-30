package com.earthmap.satellite.map.location.map.satelliteTrackerModule.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.earthmap.satellite.map.location.map.adapters.SatelliteTrackerMainAdapter
import com.earthmap.satellite.map.location.map.databinding.DialogSatelliteTrackerBinding
import com.earthmap.satellite.map.location.map.satelliteTrackerModule.callbacks.SateliteSelectedCallBack
import com.earthmap.satellite.map.location.map.satelliteTrackerModule.callbacks.SatelliteTrackerDialogCallback
import com.earthmap.satellite.map.location.map.satelliteTrackerModule.helper.SatelliteTrackerMianHelper
import com.earthmap.satellite.map.location.map.satelliteTrackerModule.model.SatelliteTrackerMainModel


class GlobeEarthMapSatteliteSelectionDialog(var mContext:Context,var callback: SatelliteTrackerDialogCallback):Dialog(mContext) {
   lateinit var binding: DialogSatelliteTrackerBinding
var adapter: SatelliteTrackerMainAdapter?=null
    var manager:LinearLayoutManager?=null
   var list=ArrayList<SatelliteTrackerMainModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding= DialogSatelliteTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listFiller()
        setUpAdapter()
    }

    private fun setUpAdapter() {
        manager= LinearLayoutManager(mContext)
        binding.satelliteRecyclerView.layoutManager=manager
        if (list.size>0) {
            try {
                adapter= SatelliteTrackerMainAdapter(mContext,list,object :
                    SateliteSelectedCallBack {
                    override fun onSatelliteClick(model: SatelliteTrackerMainModel) {
                        callback.onItemSelected(model)
                        dismiss()
                    }
                })
                binding.satelliteRecyclerView.adapter=adapter
            } catch (e: Exception) {
            }
        }
    }

    private fun listFiller() {
        list= SatelliteTrackerMianHelper.satelliteListFiller()
    }
}