package com.inseoul.my_page

import android.app.Activity
import android.app.PendingIntent.getActivity
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
    val itemlist:ArrayList<ArrayList<MyPage_Item>>,
    val listener: MyPageEventListener
)
    : RecyclerView.Adapter<MyPage_ViewPagerAdapter.ViewHolder>() {

    interface MyPageEventListener{
        fun goAddPlace(view: View, position: Int, flag_key:Int, PlanID:Int)
        fun goRegisterReview(view: View, position: Int, item:MyPage_Item)
        fun goReview(view: View, position: Int, PlanID:Int, TripName:String, DPDATE:String, ADDATE:String, MEM:String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_my_page_page, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
    val RESULT_CODE = 2222

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var layoutManager: RecyclerView.LayoutManager
        var adapter: MyPage_RecyclerViewAdapter
        Log.v("hsoh0306_test", itemlist.toString())
        layoutManager = LinearLayoutManager(c, RecyclerView.VERTICAL, false)
        holder.recyclerView.layoutManager = layoutManager

        val listener = object: MyPage_RecyclerViewAdapter.RecyclerViewAdapterEventListener{
            override fun onClick(view: View, p:Int) {

                when(position){
                    0->{
                        //AddPlaceActivity로
                        listener.goAddPlace(view, position, 2, itemlist[0][p].Num)
                    }
                    1->{
                        // 리뷰 작성으로
                        listener.goRegisterReview(view, position, itemlist[1][p])
                    }
                    2->{
                        // 내가 쓴 리뷰로
//(view: View, position: Int, PlanID:Int, TripName:String, DPDATE:String, ADDATE:String, MEM:String)

                        val start_calendar = itemlist[2][p].date.split("-")
                        var startDay = start_calendar[2]
                        var startMonth = start_calendar[1]
                        var startYear = start_calendar[0]
                        var d = startYear+"-"+startMonth+"-"+startDay

                        val end_calendar = itemlist[2][p].thumbnail!!.split("-")
                        var endDay = end_calendar[2]
                        var endMonth = end_calendar[1]
                        var endYear = end_calendar[0]
                        var e = endYear+"-"+endMonth+"-"+endDay
                        Log.v("dateee", d + " " + e)
                        listener.goReview(view, position, itemlist[2][p].Num, itemlist[2][p].title, d,e, itemlist[2][p].mem)
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