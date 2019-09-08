package com.inseoul.timeline

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R

class ViewPagerAdapter(val c: Context, val itemlist:ArrayList<TimeLineItem>)
    : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    val list = listOf("Tab1", "Tab2", "Tab3")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.timeline_page, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        var layoutManager: RecyclerView.LayoutManager? = null
        var adapter: TimeLineAdapter? = null
        layoutManager = LinearLayoutManager(c, RecyclerView.VERTICAL, false)
        holder.recyclerView.layoutManager = layoutManager
        val listener = object: TimeLineAdapter.RecyclerViewAdapterEventListener{
            override fun onClick(view: View) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        adapter = TimeLineAdapter(c, listener, itemlist)
        holder.recyclerView.adapter = adapter
        holder.recyclerView.addItemDecoration(DividerItemDecoration(c, 1))
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var recyclerView: RecyclerView
        init{
            recyclerView = itemView.findViewById(R.id.recyclerView_addPlace)
        }
    }
}