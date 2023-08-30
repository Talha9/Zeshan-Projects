package com.earthmap.satellite.map.location.map.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.satelliteTrackerModule.callbacks.SateliteSelectedCallBack
import com.earthmap.satellite.map.location.map.satelliteTrackerModule.model.SatelliteTrackerMainModel

class SatelliteTrackerMainAdapter(
    var mContext:Context,
    var list:ArrayList<SatelliteTrackerMainModel>,
    var callback: SateliteSelectedCallBack
):
    RecyclerView.Adapter<SatelliteTrackerMainAdapter.SatelliteSelectedViewHolder>() {

    inner class SatelliteSelectedViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var txt:TextView?=null
        var btn:ConstraintLayout?=null
        init {
            txt=itemView.findViewById(R.id.satelliteName)
            btn=itemView.findViewById(R.id.selectSatellite)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SatelliteSelectedViewHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.one_satellite_tracker_item,parent,false)
        return SatelliteSelectedViewHolder(v)
    }

    override fun onBindViewHolder(holder: SatelliteSelectedViewHolder, position: Int) {
        val model=list[position]
        holder.txt!!.text=model.satelliteCategoryName
        holder.btn!!.setOnClickListener {
            callback.onSatelliteClick(model)
        }
    }

    override fun getItemCount(): Int {

        return list.size
    }
}


