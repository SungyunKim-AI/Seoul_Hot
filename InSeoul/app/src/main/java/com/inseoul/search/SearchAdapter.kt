package com.inseoul.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R


class SearchAdapter(
    val context: Context,
    var listener: RecyclerViewAdapterEventListener,
    var items: ArrayList<SearchItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface RecyclerViewAdapterEventListener {
        fun onClick(view: View, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_place_search, parent, false)
            return ViewHolder2(v)
        } else {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search, parent, false)
            return ViewHolder1(v)
        }
    }

    fun getData(position: Int): SearchItem? {
        return if (items == null || items.size < position) {
            null
        } else items[position]

    }

    override fun getItemCount(): Int {
        if (items == null)
            return 0
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        //return super.getItemViewType(position)
        return position % 2 * 2
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHolder1) {
            val data = items!!.get(position)

            holder.thumbnail.setImageResource(data.placeIcon)
            holder.item_title.text = data.placeNm

            holder.itemView.setOnClickListener {
                listener!!.onClick(it, position)
            }
        } else if (holder is ViewHolder2) {
            val data = items!!.get(position)

            holder.thumbnail.setImageResource(data.placeIcon)
            holder.item_title.text = data.placeNm

            holder.itemView.setOnClickListener {
                listener!!.onClick(it, position)
            }

            holder.itemView.selectBtn.setOnClickListener {
                listener!!.onClick(it, position)
            }

        }

    }


    //일반 search에서의 뷰
    inner class ViewHolder1 : RecyclerView.ViewHolder {

        constructor(itemView: View) : super(itemView)

        var thumbnail: ImageView
        var item_title: TextView

        init {
            item_title = itemView.findViewById(R.id.search_title)
            thumbnail = itemView.findViewById(R.id.search_thumbnail)
        }
    }

    //add_place 에서의 뷰
    inner class ViewHolder2 : RecyclerView.ViewHolder {
        constructor(itemView: View) : super(itemView)

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
