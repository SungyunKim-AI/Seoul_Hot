package com.inseoul.my_page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
        fun onClick(view: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when(viewType){
            0 ->{
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.activity_my_page_item, parent, false)
                return ViewHolder(v)
            }
            1->{
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.activity_my_page_item_review, parent, false)
                return ViewHolder(v)
            }
        }
        val v = LayoutInflater.from(parent.context)
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
        holder.item_date.text = data.date

        holder.itemView.setOnClickListener {
            listener!!.onClick(it)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView
        var item_title: TextView
        var item_date: TextView

        init {
            item_title = itemView.findViewById(R.id.textview_recyclerview_title)
            thumbnail = itemView.findViewById(R.id.thumbnail)
            item_date = itemView.findViewById(R.id.textview_recyclerview_date)
        }
    }
}
