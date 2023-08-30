package com.earthmap.satellite.map.location.map.isoCodesModule.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.earthmap.satellite.map.location.map.Ads.MyAppNativeAds
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.Utils.restCountriesApi.CountriesModel
class IsoCodeMainAdapter(
    var mContext: Context,
    var list: ArrayList<CountriesModel>
) :
    RecyclerView.Adapter<IsoCodeMainAdapter.IsoMainViewHolder>() {
    var typeAds = 0
    var typePost = 1
    var empty = 2

    inner class IsoMainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt: TextView? = null
        var countrytxt: TextView? = null
        var flgImg: ImageView? = null

        init {
            txt = itemView.findViewById(R.id.isoCodetxt)
            countrytxt = itemView.findViewById(R.id.isoCountrytxt)
            flgImg = itemView.findViewById(R.id.isoCodesflagImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IsoMainViewHolder {
        var v: View? = null
        if (viewType == typePost) {
            v = LayoutInflater.from(mContext)
                .inflate(R.layout.single_iso_codeitem_design, parent, false)
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
        return IsoMainViewHolder(v!!)
    }

    override fun onBindViewHolder(holder: IsoMainViewHolder, position: Int) {
        if (position == 0) {
            return
        }
        if (position % 7 == 0) return
        val newPosition = position - (position / 7 + 1)
        val model = list[newPosition]
        try {
            val flage =
                "https://flagpedia.net/data/flags/normal/${model.alpha2Code.toLowerCase()}.png"
            Log.i("flagezzz", ": " + flage)
            Glide.with(mContext).load(flage).into(holder.flgImg!!)
        } catch (e: Exception) {
        }

        holder.txt!!.text = "+ " + model.callingCodes[0]
        holder.countrytxt!!.text = model.name

    }

    override fun getItemCount(): Int {
        val itemCount = list.size
        return itemCount + (itemCount / 6 + 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            empty
        } else {
            if (position % 7 == 0) {
                typeAds
            } else {
                typePost
            }
        }
    }

    fun setIsoCodesList(isoCodesListFiltered: ArrayList<CountriesModel>) {
        list = isoCodesListFiltered
        notifyDataSetChanged()
    }
}