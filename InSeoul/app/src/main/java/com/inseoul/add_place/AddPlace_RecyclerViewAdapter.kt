package com.inseoul.add_place

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import java.util.*
import kotlin.collections.ArrayList


class AddPlace_RecyclerViewAdapter(
    val context: Context,
    var listener: RecyclerViewAdapterEventListener,
    var items: ArrayList<AddPlaceItem>,
    startDragListener: OnStartDragListener

) : RecyclerView.Adapter<AddPlace_RecyclerViewAdapter.ViewHolder>(), ItemTouchHelperCallback.OnItemMoveListener {


    var mStartDragListener = startDragListener

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(items,fromPosition,toPosition)
        notifyItemMoved(fromPosition,toPosition)

        return true
    }

    interface OnStartDragListener{
        fun onStartDrag(holder: ViewHolder)
    }


    interface RecyclerViewAdapterEventListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_place, parent, false)
        return ViewHolder(v)
    }

    fun getData(position: Int): AddPlaceItem? {
        return if (items == null || items.size < position) {
            null
        } else items[position]

    }

    override fun getItemCount(): Int {
        if (items == null)
            return 0
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val data = items!!.get(position)

        holder.placeCount.text = data.count.toString()
        holder.placeNm.text = data.PlaceNm
        val type = data.PlaceType
        // ContentType
        // 관광지 12   문화시설 14   행사/공연/축제 15   레포츠 28  숙박 32    음식점 39

        when (type) {
            12 -> {
                holder.placeType.text = "관광지"
            }
            14 -> {
                holder.placeType.text = "문화시설"
            }
            15 -> {
                holder.placeType.text = "행사/공연/축제"
            }
            28 -> {
                holder.placeType.text = "레포츠"
            }
            32 -> {
                holder.placeType.text = "숙박"
            }
            39 -> {
                holder.placeType.text = "음식점"
            }
        }

        holder.itemView.setOnClickListener {
            listener.onClick(it, position)
        }

        holder.movebtn.setOnTouchListener { view, motionEvent ->
            if(motionEvent.actionMasked == MotionEvent.ACTION_DOWN)
                mStartDragListener.onStartDrag(holder)
            return@setOnTouchListener false
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var placeCount: TextView
        var placeNm: TextView
        var placeType: TextView
        var movebtn :Button
        var deletebtn :Button

        init {
            placeCount = itemView.findViewById(R.id.tv_placeCount)
            placeNm = itemView.findViewById(R.id.tv_placeNm)
            placeType = itemView.findViewById(R.id.tv_placeType)
            movebtn = itemView.findViewById(R.id.movebtn)
            deletebtn = itemView.findViewById(R.id.deletebtn)
        }
    }


}
