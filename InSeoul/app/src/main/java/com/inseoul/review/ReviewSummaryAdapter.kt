package com.inseoul.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R

class ReviewSummaryAdapter(
    val itemlist:MutableList<ReviewItem>
) : RecyclerView.Adapter<ReviewSummaryAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewSummaryAdapter.ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_review_summary_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        if(itemlist != null){
            return itemlist.size
        } else{
            return 0
        }
    }

    override fun onBindViewHolder(holder: ReviewSummaryAdapter.ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val data = itemlist[position]
        var image = data.imageList!![0]!!
        holder.image.setImageDrawable(image)
        holder.name.text = data.upso_name
        holder.location.text = data.location


    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var name: TextView
        var location: TextView
        var image: ImageView
        init{
            name = itemView.findViewById(R.id.item_name)
            location = itemView.findViewById(R.id.location)
            image = itemView.findViewById(R.id.summary_img)
        }
    }
}
