package com.inseoul.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inseoul.R

class SearchDetailViewpagerAdapter(
    val c: Context,
    val itemlist:ArrayList<String>
)
    : RecyclerView.Adapter<SearchDetailViewpagerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchDetailViewpagerAdapter.ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_img, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return itemlist.size

    }

    override fun onBindViewHolder(holder: SearchDetailViewpagerAdapter.ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var data = itemlist[position]
        val url = data.replace("\\","")
        Log.v("Thumbnail", url)
        Glide.with(c).load(url).thumbnail(0.1f).placeholder(R.drawable.no_image).into(holder.img)


    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var img: ImageView
        init{
            img = itemView.findViewById(R.id.review_image_item)
        }
    }
}