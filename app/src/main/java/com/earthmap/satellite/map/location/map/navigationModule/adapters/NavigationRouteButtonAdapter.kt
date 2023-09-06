package com.earthmap.satellite.map.location.map.navigationModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.navigationModule.callbacks.NavigationRouteCallback
import com.earthmap.satellite.map.location.map.navigationModule.models.NavigationRouteButtonsModel
import java.text.DecimalFormat

class NavigationRouteButtonAdapter(var mContext: Context, var list:ArrayList<NavigationRouteButtonsModel>, var callback: NavigationRouteCallback):
    RecyclerView.Adapter<NavigationRouteButtonAdapter.NavigationRouteButtonViewHolder>() {
    var selected=0

    inner class NavigationRouteButtonViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var txt:TextView?=null
        var btn:ConstraintLayout?=null

        init {
            txt=itemView.findViewById(R.id.navigationRouteDistance)
            btn=itemView.findViewById(R.id.navigationRouteBtn)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NavigationRouteButtonViewHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.single_route_navigation_btn_styles,parent,false)
        return NavigationRouteButtonViewHolder(v)
    }

    override fun onBindViewHolder(holder: NavigationRouteButtonViewHolder, position: Int) {
        val model=list[position]
        if(selected==model.currentPosition){
            holder.btn!!.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                mContext,R.drawable.bg_white_color
            ))
            holder.txt!!.setTextColor(mContext.getColor(R.color.ThemeColor))
        }else{
            holder.btn!!.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                    mContext,R.drawable.bg_theme_color
                ))
            holder.txt!!.setTextColor(mContext.getColor(R.color.ThemeColorLight))
        }
        val distanceInKm=(model.distanceTxt).toDouble()/1000

        holder.txt!!.text="Route "+model.routeIndex+" \n"+ DecimalFormat("#.#").format(distanceInKm) + " Km."
        holder.btn!!.setOnClickListener {
            selected= model.currentPosition
            notifyDataSetChanged()
            callback.onRouteButtonClick(model,position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}