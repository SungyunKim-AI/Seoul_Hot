package com.inseoul.review

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R

class Review_ImageViewPagerAdapter(
    val itemlist:ArrayList<Drawable?>?
    ) : RecyclerView.Adapter<Review_ImageViewPagerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_img, parent, false)
        return ViewHolder(v)
    }
    override fun getItemCount():Int{
        if(itemlist != null){
            return itemlist.size
        } else{
            return 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        if(itemlist != null){
            var img = itemlist[position]
            holder.image.setImageDrawable(img)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var image: ImageView
        init{
            image = itemView.findViewById(R.id.review_image_item)
        }
    }
}