package com.inseoul.forecast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R

class Forecast_adapter (
    val context: Context,
    var items:ArrayList<Forecast_item>
): RecyclerView.Adapter<Forecast_adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_forecast_item, parent, false)
        return ViewHolder(v)

    }

    fun getData(position: Int): Forecast_item? {
        return if (items == null || items.size < position) {
            null
        } else items[position]

    }

    override fun getItemCount(): Int {
        if (items == null)
            return 0
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items!!.get(position)

        when(data.weather_am){
            "맑음"->{
                holder.w_icon_am.setImageResource(R.drawable.w_sun)
            }
            "구름많음", "흐림"->{
                holder.w_icon_am.setImageResource(R.drawable.w_cloud)
            }
            "구름많고 비", "흐리고 비", "흐리고 비/눈", "구름많고 비/눈"->{
                holder.w_icon_am.setImageResource(R.drawable.w_rain)
            }
            "구름많고 눈/비", "흐리고 눈", "흐리고 눈/비", "구름많고 눈"->{
                holder.w_icon_am.setImageResource(R.drawable.w_snow)
            }
        }
        when(data.weather_pm){
            "맑음"->{
                holder.w_icon_pm.setImageResource(R.drawable.w_sun)
            }
            "구름많음", "흐림"->{
                holder.w_icon_pm.setImageResource(R.drawable.w_cloud)
            }
            "구름많고 비", "흐리고 비", "흐리고 비/눈", "구름많고 비/눈"->{
                holder.w_icon_pm.setImageResource(R.drawable.w_rain)
            }
            "구름많고 눈/비", "흐리고 눈", "흐리고 눈/비", "구름많고 눈"->{
                holder.w_icon_pm.setImageResource(R.drawable.w_snow)
            }
        }
        holder.date.text = data.date
        holder.t_high.text = data.t_high.toString() + "℃"
        holder.t_low.text = data.t_low.toString() + "℃"

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var w_icon_am: ImageView
        var w_icon_pm: ImageView
        var date: TextView
        var t_high: TextView
        var t_low: TextView

        init {
            w_icon_am = itemView.findViewById(R.id.w_icon_am)
            w_icon_pm = itemView.findViewById(R.id.w_icon_pm)

            date = itemView.findViewById(R.id.w_date)
            t_high = itemView.findViewById(R.id.t_high)
            t_low = itemView.findViewById(R.id.t_low)

        }
    }
}
