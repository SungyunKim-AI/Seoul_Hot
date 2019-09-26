package com.inseoul.add_place

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import kotlinx.android.synthetic.main.item_add_place.view.*
import org.w3c.dom.Text


class AddPlace_ViewPagerAdapter(
    val c: Context,
    val mCount: Int,
    val addListener: ViewPagerAdapterEventListener,
    val itemlist:ArrayList<ArrayList<AddPlaceItem>>
)
    : RecyclerView.Adapter<AddPlace_ViewPagerAdapter.ViewHolder>(), AddPlace_RecyclerViewAdapter.OnStartDragListener {

    lateinit var mCallback: ItemTouchHelperCallback
    lateinit var mItemTouchHelper : ItemTouchHelper

    override fun onStartDarg(dragHolder: AddPlace_RecyclerViewAdapter.ViewHolder) {
        mItemTouchHelper.startDrag(dragHolder)
    }

    val tabList: List<Int> = List(mCount,{i->i})

    interface ViewPagerAdapterEventListener {
        fun onClick(view: View, position: Int)
        fun on_friendBtn_Click(view: View, position: Int)
        fun onChangeCallback2(view: View, itemlist: ArrayList<ArrayList<AddPlaceItem>>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_add_place_page, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = tabList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var layoutManager: RecyclerView.LayoutManager
        var adapter: AddPlace_RecyclerViewAdapter
        layoutManager = LinearLayoutManager(c, RecyclerView.VERTICAL, false)
        holder.recyclerView.layoutManager = layoutManager

        val listener = object: AddPlace_RecyclerViewAdapter.RecyclerViewAdapterEventListener{
            override fun onChangeCallback(view: View, items: ArrayList<AddPlaceItem>) {
                addListener.onChangeCallback2(view, itemlist)
            }


        }

        adapter = AddPlace_RecyclerViewAdapter(c, listener,itemlist[position], this)


        mCallback = ItemTouchHelperCallback(adapter)
        mItemTouchHelper = ItemTouchHelper(mCallback)
        mItemTouchHelper.attachToRecyclerView(holder.recyclerView)

        holder.recyclerView.adapter = adapter


        holder.addPlaceBtn.setOnClickListener{
            addListener.onClick(it, position)
        }
        holder.addFriendBtn.setOnClickListener{
            addListener.on_friendBtn_Click(it,position)
        }
 

    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var recyclerView: RecyclerView
        var addPlaceBtn: TextView
        var addFriendBtn:TextView
        init{
            recyclerView = itemView.findViewById(R.id.recyclerView_addPlace)
            addPlaceBtn = itemView.findViewById(R.id.addBtn_recyclerview)
            addFriendBtn = itemView.findViewById(R.id.shareBtn)
        }
    }
}