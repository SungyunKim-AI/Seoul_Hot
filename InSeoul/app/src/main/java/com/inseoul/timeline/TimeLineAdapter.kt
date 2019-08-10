package com.inseoul.timeline

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import com.inseoul.home.HomeItem

class TimeLineAdapter(val context: Context,
                      var listener:RecyclerViewAdapterEventListener,
                      var items:ArrayList<TimeLineItem>
): RecyclerView.Adapter<TimeLineAdapter.ViewHolder>() {

    interface RecyclerViewAdapterEventListener {
        fun onClick(view: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_line, parent, false)
        return ViewHolder(v)
    }

    fun getData(position: Int): TimeLineItem? {
        return if (items == null || items.size < position) {
            null
        } else items[position]

    }

    override fun getItemCount(): Int {
        if(items==null)
            return 0
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items!!.get(position)

        if(position % 3 == 0){
            holder.thumbnail.setImageResource(R.drawable.sample1)
        }
        if(position % 3 == 1){
            holder.thumbnail.setImageResource(R.drawable.sample2)

        }
        if(position % 3 == 2){
            holder.thumbnail.setImageResource(R.drawable.sample3)

        }


        holder.item_title.text = data.title
        holder.item_content.text = data.preview

        holder.itemView.setOnClickListener {
            listener!!.onClick(it)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView
        var item_title: TextView
        var item_content: TextView
        init {
            item_title = itemView.findViewById(R.id.item_title)
            thumbnail = itemView.findViewById(R.id.item_img)
            item_content = itemView.findViewById(R.id.item_content)
        }
    }



}

