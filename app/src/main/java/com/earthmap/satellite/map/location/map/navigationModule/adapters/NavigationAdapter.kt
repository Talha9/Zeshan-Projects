package com.earthmap.satellite.map.location.map.navigationModule.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.UtilsFunctionClass
import com.earthmap.satellite.map.location.map.navigationModule.callbacks.NavigationCallbacks
import com.earthmap.satellite.map.location.map.navigationModule.models.NavigationModel


class NavigationAdapter(var mContext:Context, var list:ArrayList<NavigationModel>, var callback: NavigationCallbacks):
    RecyclerView.Adapter<NavigationAdapter.NavigationViewHolder>(){
    var selectedPos=1
    inner class NavigationViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var btn:ConstraintLayout?=null
        var img:ImageView?=null
        var txt:TextView?=null
        var progress:ProgressBar?=null
        init {
            btn=itemView.findViewById(R.id.navigationBtn)
            img=itemView.findViewById(R.id.navigationImg)
            txt=itemView.findViewById(R.id.navigationTxt)
            progress=itemView.findViewById(R.id.navigationProgress)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationViewHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.single_navigation_frag_btn_design,parent,false)
        return NavigationViewHolder(v)
    }

    override fun onBindViewHolder(holder: NavigationViewHolder, position: Int) {
        val model=list[position]
        if(selectedPos==model.currentPosition){
            holder.btn!!.setBackgroundDrawable(AppCompatResources.getDrawable(
                mContext,R.drawable.navigation_item_click_design
            ))
            holder.txt!!.setTextColor(mContext.getColor(R.color.ThemeColor))
            holder.img!!.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(mContext, R.color.ThemeColor))
        }else{
            holder.btn!!.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                    mContext,
                    R.drawable.btn_background_theme_color
                )
            )
            holder.txt!!.setTextColor(mContext.getColor(R.color.white))
            holder.img!!.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(mContext, R.color.white))
        }

        UtilsFunctionClass.setImageInGlideFromDrawable(mContext,mContext.getDrawable(model.navImg)!!,holder.progress!!,holder.img!!)
        holder.txt!!.text=model.navTxt
        holder.btn!!.setOnClickListener {
            selectedPos= model.currentPosition
            notifyDataSetChanged()
            callback.onNavigationFragmentBtnClick(model.navTxt)
        }

    }

    override fun getItemCount(): Int {
      return list.size
    }
}