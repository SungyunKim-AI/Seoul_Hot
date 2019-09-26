package com.inseoul.add_place

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import kotlinx.android.synthetic.main.item_add_place.view.*
import java.util.*
import kotlin.collections.ArrayList


class AddPlace_RecyclerViewAdapter(
    val context: Context,
    var listener: RecyclerViewAdapterEventListener,
    var items: ArrayList<AddPlaceItem>,
    var startDragListener: OnStartDragListener
) : RecyclerView.Adapter<AddPlace_RecyclerViewAdapter.ViewHolder>(), ItemTouchHelperCallback.OnItemMoveListener {


    interface OnStartDragListener{
        fun onStartDarg(dragHolder: ViewHolder)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int, viewHolder: RecyclerView.ViewHolder): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
//                Log.d("alert_itemchange",items.toString())


            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
//                Log.d("alert_itemchange",items.toString())
            }
        }

        for(i in 0 until items.size){
            items[i].count = i+1
        }
//        Log.d("alert_itemchange_last",items.toString())

        notifyItemMoved(fromPosition, toPosition)
        notifyDataSetChanged()
        listener.onChangeCallback(viewHolder.itemView, items)
        return true
    }


//    override fun onItemDismiss(position: Int) {
//        items.removeAt(position)
//        notifyItemRemoved(position)
//        notifyDataSetChanged()
//    }


    interface RecyclerViewAdapterEventListener {
        fun onChangeCallback(view: View, items: ArrayList<AddPlaceItem>)
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
        if (items == null) {
            return 0
        }
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val mStartDragListener = startDragListener

        val data = items.get(position)

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

        holder.itemView.movebtn.setOnTouchListener { view, motionEvent ->
            if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN){
                mStartDragListener.onStartDarg(holder)
            }
            return@setOnTouchListener false
        }

        holder.itemView.deletebtn.setOnClickListener {
            items.removeAt(position)
            notifyItemRemoved(position)

            for(i in 0 until items.size){
                items[i].count = i+1
            }
            listener.onChangeCallback(it, items)
            notifyDataSetChanged()
//            Log.d("alert_item",items.toString())
        }

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var placeCount: TextView
        var placeNm: TextView
        var placeType: TextView
        var movebtn: ImageView
        var deletebtn: ImageView

        init {
            placeCount = itemView.findViewById(R.id.tv_placeCount)
            placeNm = itemView.findViewById(R.id.tv_placeNm)
            placeType = itemView.findViewById(R.id.tv_placeType)
            movebtn = itemView.findViewById(R.id.movebtn)
            deletebtn = itemView.findViewById(R.id.deletebtn)
        }
    }


}
