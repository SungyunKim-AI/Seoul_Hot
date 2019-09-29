package com.inseoul.register_review

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inseoul.R

class RegisterReviewRecyclerViewAdapter(
    val c: Context,
    val itemlist:ArrayList<String?>
): RecyclerView.Adapter<RegisterReviewRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_register_review_item, parent, false)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        holder.setIsRecyclable(false)
//        holder.img.setImageDrawable(itemlist[position])
        var img = itemlist[position]
//            holder.image.setImageResource(R.drawable.sample2)
//        val url = "http://ksun1234.cafe24.com/" + img
        Log.d("powerTlqkf", img)
        Glide.with(c).load(img).thumbnail(0.1f).placeholder(R.drawable.logo).into(holder.img)

        holder.cancel.setOnClickListener {

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val img: ImageView
        var cancel: TextView
        init{
            img = itemView.findViewById(R.id.img)
            cancel = itemView.findViewById(R.id.cancel)
        }
    }
}