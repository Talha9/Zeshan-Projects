package com.earthmap.satellite.map.location.map.navigationModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.navigationModule.callbacks.onRouteTransitCallback
import com.earthmap.satellite.map.location.map.navigationModule.models.TransitRoutesModel


class NavigationTransitAdapter(var mContext: Context, var list:ArrayList<TransitRoutesModel>, var callback: onRouteTransitCallback):
    RecyclerView.Adapter<NavigationTransitAdapter.NavigationTransitViewHolder>() {


    inner class NavigationTransitViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var txt:TextView?=null
        var btn:ImageView?=null
        init {
            txt=itemView.findViewById(R.id.CustomAddTxt)
            btn=itemView.findViewById(R.id.CustomAddBtn)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationTransitViewHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.single_transit_item_style,parent,false)
        return NavigationTransitViewHolder(v)
    }

    override fun onBindViewHolder(holder: NavigationTransitViewHolder, position: Int) {
        val model=list[position]
        holder.txt!!.setText(model.destinationPoint)
        holder.btn!!.setOnClickListener {
            callback.onItemRemovedClick(position)
        }
        holder.txt!!.setOnClickListener {
            callback.onEditTxtClick(position, model)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}