package com.inseoul.make_plan

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R

class ViewPagerAdpater_recommand (
    val c: Context,
    var listener:ViewPagerAdapterEventListener,
    val itemlist:ArrayList<Drawable?>,
    val parentView:View,
    val parentPosition:Int
)
    : RecyclerView.Adapter<ViewPagerAdpater_recommand.ViewHolder>() {


    interface ViewPagerAdapterEventListener {
        fun onClick(view: View, position:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.slide_image_view, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = itemlist.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var img = itemlist[position]
        holder.previewImg.setImageDrawable(img)
        holder.itemView.setOnClickListener {
            listener!!.onClick(parentView, parentPosition)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var previewImg: ImageView
        init{
            previewImg = itemView.findViewById(R.id.previewImg)
        }
    }
}