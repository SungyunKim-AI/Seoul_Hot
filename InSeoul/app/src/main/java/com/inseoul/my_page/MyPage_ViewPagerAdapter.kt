package com.inseoul.my_page

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import com.inseoul.add_place.AddPlaceActivity
import com.inseoul.register_review.RegisterReviewActivity
import com.inseoul.review.ReviewActivity


class MyPage_ViewPagerAdapter(
    val c: Context,
    val itemlist:ArrayList<ArrayList<MyPage_Item>>
)
    : RecyclerView.Adapter<MyPage_ViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_my_page_page, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var layoutManager: RecyclerView.LayoutManager
        var adapter: MyPage_RecyclerViewAdapter
        layoutManager = LinearLayoutManager(c, RecyclerView.VERTICAL, false)
        holder.recyclerView.layoutManager = layoutManager

        val listener = object: MyPage_RecyclerViewAdapter.RecyclerViewAdapterEventListener{
            override fun onClick(view: View, p:Int) {

                when(position){
                    0->{
                        //AddPlaceActivity로
                        val intent = Intent(c,AddPlaceActivity::class.java)
                        intent.putExtra("flag_key",2)
                        intent.putExtra("PlanID",itemlist[0][p].Num)
                        c.startActivity(intent)
                        (c as Activity).finish()
                    }
                    1->{
                        // 리뷰 작성으로
                        val intent = Intent(c,RegisterReviewActivity::class.java)
                        intent.putExtra("item",itemlist[1][p])
                        c.startActivity(intent)
                        (c as Activity).finish()
                    }
                    2->{
                        // 내가 쓴 리뷰로
                        val intent = Intent(c,ReviewActivity::class.java)
                        c.startActivity(intent)
                        (c as Activity).finish()
                    }
                }

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