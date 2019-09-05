package com.inseoul.register_review

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R

class RegisterReviewRecyclerViewAdapter(
    val itemlist:ArrayList<Drawable?>
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
        holder.img.setImageDrawable(itemlist[position])
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