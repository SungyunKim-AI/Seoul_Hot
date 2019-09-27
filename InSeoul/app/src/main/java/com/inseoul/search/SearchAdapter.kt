package com.inseoul.search

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inseoul.R
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_search.view.*


class SearchAdapter(val context: Context,
                    var listener:RecyclerViewAdapterEventListener,
                    var items:ArrayList<Search_Item>,
                    var flag:Boolean,
                    var category: Int
): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    interface RecyclerViewAdapterEventListener {
        fun onClick(view: View, position: Int, categoryIndex:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return ViewHolder(v)
    }

    fun getData(position: Int): Search_Item? {
        return if (items == null || items.size < position) {
            null
        } else items[position]

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items.get(position)
//        val url = data.url!!.replace("\\","")
        val url = data.url
        Log.v("Thumbnail", data.toString())
//        Glide.with(context).load(url).placeholder(R.drawable.logo).into(holder.thumbnail)

//        if(data != null){
//            if(data.url != null){
//            } else {
//                Glide.with(context).load(R.drawable.logo).into(holder.thumbnail)
//                Log.v("Thumbnail2", "null")
//            }
//        }
        holder.item_title.text = data.title


        if(flag){
            holder.itemView.selectBtn.visibility = VISIBLE
            holder.itemView.selectBtn.setOnClickListener{
                listener!!.onClick(it, position, category)
            }
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, SearchDetail::class.java)
            intent.putExtra("data", data)
            intent.putExtra("flag",flag)        // 시작 액티비티가 search라면 false, addplace라면 true
            //Log.v("Before intent", data.posX.toString() +  ", " + data.posY.toString())
            startActivity(context, intent, null)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView
        var item_title: TextView
        var selectBtn: TextView
        init {
            item_title = itemView.findViewById(R.id.search_title)
            thumbnail = itemView.findViewById(R.id.search_thumbnail)
            selectBtn = itemView.findViewById(R.id.selectBtn)
        }
    }



}
