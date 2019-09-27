package com.inseoul.add_place

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.PopupMenu
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


    interface OnStartDragListener {
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

        for (i in 0 until items.size) {
            items[i].count = i + 1
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
            if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN) {
                mStartDragListener.onStartDarg(holder)
            }
            return@setOnTouchListener false
        }

        holder.itemView.syncbtn.setOnClickListener {
            holder.itemView.movebtn.visibility = GONE
            holder.itemView.syncbtn.visibility = GONE

            holder.itemView.editbtn.visibility = VISIBLE
        }

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var placeCount: TextView
        var placeNm: TextView
        var placeType: TextView
        var movebtn: ImageView
        var syncbtn : ImageView
        var editbtn : ImageView

        init {
            placeCount = itemView.findViewById(R.id.tv_placeCount)
            placeNm = itemView.findViewById(R.id.tv_placeNm)
            placeType = itemView.findViewById(R.id.tv_placeType)
            movebtn = itemView.findViewById(R.id.movebtn)
            syncbtn = itemView.findViewById(R.id.syncbtn)
            editbtn = itemView.findViewById(R.id.editbtn)


            itemView.editbtn.setOnClickListener {
                val popupMenu = PopupMenu(context, it)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.edit_order ->{

                            itemView.movebtn.visibility = VISIBLE
                            itemView.syncbtn.visibility = VISIBLE

                            true
                        }
                        R.id.deletePlace ->{

                            items.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)

                            for (i in 0 until items.size) {
                                items[i].count = i + 1
                            }
                            listener.onChangeCallback(itemView, items)
                            notifyDataSetChanged()
//            Log.d("alert_item",items.toString())

                            true
                        }
                        else -> false
                    }
                }
                popupMenu.inflate(R.menu.edit_add_place)

                try {
                    val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                    fieldMPopup.isAccessible = true
                    val mPopup = fieldMPopup.get(popupMenu)
                    mPopup.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(mPopup, true)
                } catch (e: Exception){
                    Log.e("Main", "Error showing menu icons.", e)
                } finally {
                    popupMenu.show()
                }
            }

        }




    }


}
