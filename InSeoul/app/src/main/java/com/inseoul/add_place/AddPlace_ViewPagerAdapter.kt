package com.inseoul.add_place

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R


class AddPlace_ViewPagerAdapter(
    val c: Context,
    val mCount: Int,
    val addListener: ViewPagerAdapterEventListener,
    val itemlist:ArrayList<ArrayList<AddPlaceItem>>
)
    : RecyclerView.Adapter<AddPlace_ViewPagerAdapter.ViewHolder>() {

    val tabList: List<Int> = List(mCount,{i->i})

    interface ViewPagerAdapterEventListener {
        fun onClick(view: View, position: Int)
        fun on_friendBtn_Click(view: View, position: Int)
        fun on_editPlan_Click(view: View, position: Int)
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

            override fun onClick(view: View,position: Int) {
                Toast.makeText(c,"$position 을 클릭하였습니다.",Toast.LENGTH_SHORT).show()
            }
        }

        adapter = AddPlace_RecyclerViewAdapter(c, listener,itemlist[position])


        val callback = ItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(holder.recyclerView)

        holder.recyclerView.adapter = adapter


        holder.addPlaceBtn.setOnClickListener{
            addListener.onClick(it, position)
        }
        holder.addFriendBtn.setOnClickListener{
            addListener.on_friendBtn_Click(it,position)
        }
        holder.editPlanBtn.setOnClickListener {
            addListener.on_editPlan_Click(it,position)
        }

    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var recyclerView: RecyclerView
        var addPlaceBtn: TextView
        var addFriendBtn:TextView
        var editPlanBtn : TextView
        init{
            recyclerView = itemView.findViewById(R.id.recyclerView_addPlace)
            addPlaceBtn = itemView.findViewById(R.id.addBtn_recyclerview)
            addFriendBtn = itemView.findViewById(R.id.shareBtn)
            editPlanBtn = itemView.findViewById(R.id.editPlan)
        }
    }
}