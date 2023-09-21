package com.earthmap.satellite.map.location.map.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.home.Model.HomeItemModel
import com.earthmap.satellite.map.location.map.home.callback.HomeCallback

class HomeAdapter(
    var mContext: Context,
    var list: ArrayList<HomeItemModel>,
    var isSensorAvailable: Boolean,
    var callback: HomeCallback
) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var btn: ConstraintLayout? = null
        var itemCard: ConstraintLayout? = null
        var img: ImageView? = null
        var txt: TextView? = null

        init {
            btn = itemView.findViewById(R.id.itemBtn)
            itemCard = itemView.findViewById(R.id.itemCardColor)
            img = itemView.findViewById(R.id.itemImg)
            txt = itemView.findViewById(R.id.itemTxt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.single_home_item, parent, false)
        return HomeViewHolder(v)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val model = list[position]
        if (isSensorAvailable) {
            holder.itemCard!!.background = mContext.getDrawable(model.itemCircleColour)
            holder.txt!!.text = model.itemTxt
            Glide.with(mContext).load(model.src).into(holder.img!!)
            holder.btn!!.setOnClickListener {
                callback.onItemClick(model, position)
            }
        } else {
            if (model.itemTxt == "Compass") {
                return
            } else {
                holder.itemCard!!.background = mContext.getDrawable(model.itemCircleColour)
                holder.txt!!.text = model.itemTxt
                Glide.with(mContext).load(model.src).into(holder.img!!)
                holder.btn!!.setOnClickListener {
                    callback.onItemClick(model, position)
                }
            }
        }



    }

    override fun getItemCount(): Int {
        return list.size
    }
}