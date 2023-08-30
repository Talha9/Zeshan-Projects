package com.earthmap.satellite.map.location.map.nearBy.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.earthmap.satellite.map.location.map.Ads.MyAppNativeAds
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.nearBy.callbacks.NearByCallbacks
import com.earthmap.satellite.map.location.map.nearBy.models.NearByModel


class NearByAdapter(
    var mContext: Context,
    var list: ArrayList<NearByModel>,
    var callback: NearByCallbacks
) : RecyclerView.Adapter<NearByAdapter.NearByViewHolder>() {

    var typeAds = 0
    var typePost = 1
    var empty = 2

    inner class NearByViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var btn: ConstraintLayout? = null
        var img: ImageView? = null
        var txt: TextView? = null

        init {
            btn = itemView.findViewById(R.id.nearByItemBg)
            img = itemView.findViewById(R.id.nearByItemIcon)
            txt = itemView.findViewById(R.id.nearByItemTxt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearByViewHolder {

        var v: View? = null
        if (viewType == typePost) {
            v = LayoutInflater.from(mContext)
                .inflate(R.layout.single_nearby_cat_item_design, parent, false)
        } else if (viewType == typeAds) {
            v = LayoutInflater.from(mContext)
                .inflate(R.layout.live_streat_view_nav_native_layout_native_ads, parent, false)
            MyAppNativeAds.loadAdmobNativeAdPriority(
                mContext as Activity,
                v as FrameLayout
            )
        } else if (viewType == empty) {
            v = LayoutInflater.from(mContext).inflate(R.layout.empty_layout, parent, false)
        }

        return NearByViewHolder(v!!)
    }

    override fun onBindViewHolder(holder: NearByViewHolder, position: Int) {
        if (position == 0) {
            return
        }
        if (position % 3 == 0) return
        val newPosition = position - (position / 3 + 1)
        val model = list[newPosition]
        holder.txt!!.text = model.nearByCatName
        try {
            Glide.with(mContext).load(model.nearByCatIcon).into(holder.img!!)
        } catch (e: Exception) {
        }
        holder.btn!!.setOnClickListener {
            callback.onNearByCategoryClick(model)
        }
    }
    override fun getItemCount(): Int {
        val itemCount = list.size
        return itemCount + (itemCount / 2 + 1)

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            empty
        } else {
            if (position % 3 == 0) {
                typeAds
            } else {
                typePost
            }
        }
    }

}