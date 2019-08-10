package com.inseoul.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.inseoul.R
import kotlinx.android.synthetic.main.timeline_page.view.*

class ViewPagerAdapter: RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

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
        holder.test.text = "Page" + list.get(position)

        if (position % 2 == 0)
            holder.test.setBackgroundResource(R.color.blue)
        else
            holder.test.setBackgroundResource(R.color.orange)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var test: TextView
        init{
            test = itemView.findViewById(R.id.test)
        }
    }
}