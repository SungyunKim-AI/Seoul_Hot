package com.inseoul.register_review

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import com.inseoul.review.ReviewItem

class RegisterReviewViewPagerAdapter(
    val c: Context,
    val listener:EventListener,
    var itemlist:ArrayList<ReviewItem>
) : RecyclerView.Adapter<RegisterReviewViewPagerAdapter.ViewHolder>(){

    interface EventListener {
        fun addPhotoOnClick(view: View, position: Int)
        fun addGalleryOnClick(view:View, positon: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterReviewViewPagerAdapter.ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_register_review_page, parent, false)
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

    override fun onBindViewHolder(holder: RegisterReviewViewPagerAdapter.ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val data = itemlist[position]
//        var image = data.imageList!![0]!!
//        holder.setIsRecyclable(false)
        holder.place.text = data.upso_name
        if(data.imageList != null){
            var iList = data.imageList
            Log.e("image", iList!!.toString())
            val adapter = RegisterReviewRecyclerViewAdapter(c, iList!!)
            holder.imgList.layoutManager = LinearLayoutManager(c, LinearLayout.HORIZONTAL, false)
            holder.imgList.adapter = adapter
        }


        holder.addPhoto.setOnClickListener {
            listener.addPhotoOnClick(it,position)
        }
        holder.addGallery.setOnClickListener {
            listener.addGalleryOnClick(it, position)
        }

        holder.trip_comment.addTextChangedListener( object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    itemlist[position].review_content = holder.trip_comment.text.toString()

                }
            }
        )
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val place:TextView
        var trip_comment: EditText
        var addPhoto: ImageButton
        var addGallery: ImageButton
        var imgList:RecyclerView
        init{
            place = itemView.findViewById(R.id.place)
            trip_comment = itemView.findViewById(R.id.trip_comment)
            addPhoto = itemView.findViewById(R.id.addPhoto)
            addGallery = itemView.findViewById(R.id.addGallery)
            imgList = itemView.findViewById(R.id.recyclerView_addPlace)
        }
    }
}
