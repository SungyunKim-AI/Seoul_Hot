package com.inseoul.my_page

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import com.inseoul.register_review.RegisterReviewActivity
import com.inseoul.review.ReviewActivity


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
            override fun onClick(view: View, p:Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                if(position == 1){
                    // 리뷰 작성으로
                    val intent = Intent(c,RegisterReviewActivity::class.java)
                    intent.putExtra("item",itemlist[1][p])
                    Log.e("hsoh0306_regist_intent", itemlist[1][p].toString())
                    c.startActivity(intent)
                }
                if(position == 2){
                    // 내가 쓴 리뷰로
                    val intent = Intent(c,ReviewActivity::class.java)
                    c.startActivity(intent)
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