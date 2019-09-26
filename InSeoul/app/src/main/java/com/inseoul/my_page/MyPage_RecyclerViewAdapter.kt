package com.inseoul.my_page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import com.inseoul.timeline.TimeLineAdapter
import com.inseoul.timeline.TimeLineItem

class MyPage_RecyclerViewAdapter(
    val context: Context,
    var listener:RecyclerViewAdapterEventListener,
    var items:ArrayList<MyPage_Item>,
    var viewType:Int
): RecyclerView.Adapter<MyPage_RecyclerViewAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    interface RecyclerViewAdapterEventListener {
        fun onClick(view: View, p:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.activity_my_page_item, parent, false)
        return ViewHolder(v)
    }

    fun getData(position: Int): MyPage_Item? {
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

        holder.item_title.text = data.title

        val start_calendar = data.date.split("-")
        var startDay = start_calendar[2]
        var startMonth = start_calendar[1]
        var startYear = start_calendar[0]

        val end_calendar = data.thumbnail!!.split("-")
        var endDay = end_calendar[2]
        var endMonth = end_calendar[1]
        var endYear = end_calendar[0]

        var resultStr:String ?= null

        if(startYear == endYear){
            resultStr = startYear + "." + startMonth + "." + startDay + " - " + endMonth + "." + endDay
        }
        if(startYear == endYear && startMonth == endMonth && startDay == endDay){
            resultStr = startYear + "." + startMonth + "." + startDay
        }


        holder.item_date.text = resultStr

        holder.itemView.setOnClickListener {
            listener!!.onClick(it, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_title: TextView
        var item_date: TextView

        init {
            item_title = itemView.findViewById(R.id.textview_recyclerview_title)
            item_date = itemView.findViewById(R.id.textview_recyclerview_date)
        }
    }

}
