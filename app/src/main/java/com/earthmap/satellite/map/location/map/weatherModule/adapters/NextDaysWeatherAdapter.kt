package com.earthmap.satellite.map.location.map.weatherModule.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.databinding.WeatherNextDaysItemStyleBinding
import java.text.ParseException
import java.text.SimpleDateFormat

class NextDaysWeatherAdapter(
    var mContext: Context,
    var list: List<com.earthmap.satellite.map.location.map.weatherModule.api_weather.List>
) : RecyclerView.Adapter<NextDaysWeatherAdapter.NextDaysWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextDaysWeatherViewHolder {
        val binding = WeatherNextDaysItemStyleBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return NextDaysWeatherViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NextDaysWeatherViewHolder, position: Int) {
        val dataModel=list.get(position)
        val input: String = dataModel.getDtTxt()
        try {
            @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val date = sdf.parse(input)
            val outFormat = SimpleDateFormat("EE")
            val goal = outFormat.format(date)
            holder.DayTxt!!.text = goal
            val outFormat2 = SimpleDateFormat("dd MMM yy")
            val goal2 = outFormat2.format(date)
            holder.DayTxt!!.setText(goal2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.TempTxt!!.text = Math.round(dataModel.getMain().getTemp()).toString() + "\u00B0"
        val strIconWeather: String = dataModel.weather.get(0).icon
        getWeatherConditions(strIconWeather,holder)

    }

    private fun getWeatherConditions(cond: String, holder: NextDaysWeatherViewHolder){
        when (cond) {
            "04d" ->
                try {
                    Glide.with(mContext).load(R.drawable.broken_weather_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "04n" ->
                try {
                    Glide.with(mContext).load(R.drawable.broken_weather_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "10d" ->
                try {
                    Glide.with(mContext).load(R.drawable.rain_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "10n" ->
                try {
                    Glide.with(mContext).load(R.drawable.rain_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "11d" ->
                try {
                    Glide.with(mContext).load(R.drawable.thunderstorm_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "11n" ->
                try {
                    Glide.with(mContext).load(R.drawable.thunderstorm_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "02d" ->
                try {
                    Glide.with(mContext).load(R.drawable.few_clouds_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "02n" ->
                try {
                    Glide.with(mContext).load(R.drawable.few_clouds_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "09d" ->
                try {
                    Glide.with(mContext).load(R.drawable.shower_rain_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "09n" ->
                try {
                    Glide.with(mContext).load(R.drawable.shower_rain_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "01d" ->
                try {
                    Glide.with(mContext).load(R.drawable.clear_sky_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }

            "01n" ->
                try {
                    Glide.with(mContext).load(R.drawable.clear_sky_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }

            "03d" ->
                try {
                    Glide.with(mContext).load(R.drawable.scattered_clouds_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "03n" ->
                try {
                    Glide.with(mContext).load(R.drawable.scattered_clouds_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }

            "13d" ->
                try {
                    Glide.with(mContext).load(R.drawable.snow_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "13n" ->
                try {
                    Glide.with(mContext).load(R.drawable.snow_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "50d" ->
                try {
                    Glide.with(mContext).load(R.drawable.mist_day)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }
            "50n" ->
                try {
                    Glide.with(mContext).load(R.drawable.mist_night)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.Loading!!.visibility = View.GONE
                                return false
                            }
                        }).into(holder.itemImg!!)
                } catch (e: Exception) {
                }

        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class NextDaysWeatherViewHolder(bindingItemView: WeatherNextDaysItemStyleBinding) :
        RecyclerView.ViewHolder(bindingItemView.root) {
        var itemImg: ImageView? = null
        var DayTxt: TextView? = null
        var Loading: ProgressBar? = null
        var TempTxt: TextView? = null

        init {
            itemImg = bindingItemView.NextItemImg
            DayTxt=bindingItemView.NextitemDateTxt
            Loading=bindingItemView.NextItemProgress
            TempTxt=bindingItemView.NextitemTemptxt
        }


    }
}