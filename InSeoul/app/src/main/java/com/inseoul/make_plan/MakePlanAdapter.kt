package com.inseoul.make_plan

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.inseoul.R

class MakePlanAdapter(
    val context: Context,
    var listener:RecyclerViewAdapterEventListener,
    var items:ArrayList<MakePlanItem>,
    var imgItems:ArrayList<ImgItem>
): RecyclerView.Adapter<MakePlanAdapter.ViewHolder>() {

    interface RecyclerViewAdapterEventListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommand_review, parent, false)
        return ViewHolder(v)
    }

    fun getData(position: Int): MakePlanItem? {
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
        holder.item_title.text = data.TRIP_NAME
        holder.item_content.text = data.preview
        val img = imgItems[position].img
        val vp = ViewPagerAdpater_recommand(context, img)
        holder.view_pager2.adapter = vp


        holder.itemView.setOnClickListener {
            listener!!.onClick(it, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_title: TextView
        var item_content: TextView
        var view_pager2: ViewPager2
        init {
            item_title = itemView.findViewById(R.id.r_title)
            item_content = itemView.findViewById(R.id.r_content)
            view_pager2 = itemView.findViewById(R.id.view_pager2)
        }
    }



}
