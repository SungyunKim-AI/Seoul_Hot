package com.inseoul.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R

class SearchViewPagerAdpater (
    val c: Context,
    val itemlist:ArrayList<SearchItem>
)
    : RecyclerView.Adapter<SearchViewPagerAdpater.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewPagerAdpater.ViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_search_page, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return itemlist.size

    }

    override fun onBindViewHolder(holder: SearchViewPagerAdpater.ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var layoutManager: RecyclerView.LayoutManager? = null
        var adapter: SearchAdapter? = null
        layoutManager = LinearLayoutManager(c, RecyclerView.VERTICAL, false)
        holder.recyclerView.layoutManager = layoutManager


        val listener = object : SearchAdapter.RecyclerViewAdapterEventListener {
                override fun onClick(view: View, position: Int) {
                    //intent로 SearchItem 전달
//                    val intent = Intent(c, SearchDetail::class.java)
//                    intent.putExtra("placeData", placeList[position])
//                    startActivity(intent)
                }
            }
        adapter = SearchAdapter(c, listener, itemlist)
        holder.recyclerView.adapter = adapter
        holder.recyclerView.addItemDecoration(DividerItemDecoration(c, 1))
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var recyclerView: RecyclerView
        init{
            recyclerView = itemView.findViewById(R.id.recyclerView)
        }
    }
}