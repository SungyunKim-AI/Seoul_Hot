package com.inseoul.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import kotlinx.android.synthetic.main.item_search.view.*


class SearchAdapter(val context: Context,
                    var listener:RecyclerViewAdapterEventListener,
                    var items:ArrayList<SearchItem>,
                    var flag:Boolean
): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    interface RecyclerViewAdapterEventListener {
        fun onClick1(view: View, position: Int)
        fun onClick2(view:View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return ViewHolder(v)
    }

    fun getData(position: Int): SearchItem? {
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

        holder.thumbnail.setImageResource(data.placeIcon)
        holder.item_title.text = data.placeNm


        if(flag){
            holder.itemView.selectBtn.visibility = VISIBLE
            holder.itemView.selectBtn.setOnClickListener{
                listener!!.onClick1(it, position)
            }
        }
        holder.itemView.setOnClickListener {
            listener!!.onClick2(it, position)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView
        var item_title: TextView
        var selectBtn: Button
        init {
            item_title = itemView.findViewById(R.id.search_title)
            thumbnail = itemView.findViewById(R.id.search_thumbnail)
            selectBtn = itemView.findViewById(R.id.selectBtn)
        }
    }



}
