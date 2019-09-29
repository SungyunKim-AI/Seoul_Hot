package com.inseoul.register_review

import android.content.Context
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.review.ReviewItem
import androidx.appcompat.widget.AppCompatEditText
import android.text.style.ImageSpan
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.hootsuite.nachos.NachoTextView
import com.hootsuite.nachos.terminator.ChipTerminatorHandler
import com.inseoul.R


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
            .inflate(com.inseoul.R.layout.activity_register_review_page, parent, false)
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
//
//        var chip = Chip(c)
//        chip.text = "#"
//        chip.setOnCloseIconClickListener {
//            holder.hashTag.removeView(chip as View)
//        }

        var last_index = 0;
        var text_length = 0;
        var lastChar: CharSequence = ""

        holder.hashTag.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR)
        holder.hashTag.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);

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
        var hashTag: NachoTextView
        init{
            place = itemView.findViewById(com.inseoul.R.id.place)
            trip_comment = itemView.findViewById(com.inseoul.R.id.trip_comment)
            addPhoto = itemView.findViewById(com.inseoul.R.id.addPhoto)
            addGallery = itemView.findViewById(com.inseoul.R.id.addGallery)
            imgList = itemView.findViewById(com.inseoul.R.id.recyclerView_addPlace)
            hashTag = itemView.findViewById(com.inseoul.R.id.hashtag)
        }
    }




}
