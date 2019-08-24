package com.inseoul.make_plan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R

class MakePlanAdapter(val context: Context,
                    var listener:RecyclerViewAdapterEventListener,
                    var items:ArrayList<MakePlanItem>
): RecyclerView.Adapter<MakePlanAdapter.ViewHolder>() {

    interface RecyclerViewAdapterEventListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return ViewHolder(v)
    }

    fun getData(position: Int): MakePlanItem? {
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

        holder.item_title.text = data.TRIP_NAME
        holder.item_content.text = data.preview

        holder.itemView.setOnClickListener {
            listener!!.onClick(it, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView
        var item_title: TextView
        var item_content: TextView
        init {
            item_title = itemView.findViewById(R.id.search_title)
            thumbnail = itemView.findViewById(R.id.search_thumbnail)
            item_content = itemView.findViewById(R.id.search_preview)
        }
    }



}
