package com.inseoul.add_place

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R

class AddPlaceAdapter(val context: Context,
                    var listener:RecyclerViewAdapterEventListener,
                    var items:ArrayList<PlaceItem>
): RecyclerView.Adapter<AddPlaceAdapter.ViewHolder>() {

    interface RecyclerViewAdapterEventListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return ViewHolder(v)
    }

    fun getData(position: Int): PlaceItem? {
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
        holder.item_content.text = data.preview

        holder.itemView.setOnClickListener {
            listener!!.onClick(it, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView
        var item_title: TextView
        var item_content: TextView
        var selectBtn: Button

        init {
            item_title = itemView.findViewById(R.id.search_title)
            thumbnail = itemView.findViewById(R.id.search_thumbnail)
            item_content = itemView.findViewById(R.id.search_preview)
            selectBtn = itemView.findViewById(R.id.selectBtn)


            itemView.setOnClickListener(View.OnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    Log.d("alert",pos.toString())
                }
            })
        }

    }
}
