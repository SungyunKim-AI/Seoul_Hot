package com.inseoul.my_page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R


class MyPage_ViewPagerAdapter(
    val c: Context,
    val itemlist:ArrayList<ArrayList<MyPage_Item>>
)
    : RecyclerView.Adapter<MyPage_ViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_my_page_page, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return itemlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        var layoutManager: RecyclerView.LayoutManager? = null
        var adapter: MyPage_RecyclerViewAdapter? = null
        layoutManager = LinearLayoutManager(c, RecyclerView.VERTICAL, false)
        holder.recyclerView.layoutManager = layoutManager
        val listener = object: MyPage_RecyclerViewAdapter.RecyclerViewAdapterEventListener{
            override fun onClick(view: View) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        when(position){
            0, 1-> {
                adapter = MyPage_RecyclerViewAdapter(c, listener, itemlist[position], 0)
            }
            2->{
                adapter = MyPage_RecyclerViewAdapter(c, listener, itemlist[position], 1)
            }
            else -> {
                adapter = MyPage_RecyclerViewAdapter(c, listener, itemlist[position], 0)
            }
        }
        holder.recyclerView.adapter = adapter

    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var recyclerView: RecyclerView
        init{
            recyclerView = itemView.findViewById(R.id.my_page_recyclerview)
        }
    }
}