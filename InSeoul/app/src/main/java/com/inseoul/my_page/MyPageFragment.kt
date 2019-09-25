package com.inseoul.my_page


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.inseoul.R
import com.inseoul.Server.ShowPlanRegister
import com.inseoul.SettingActivity
import com.inseoul.manage_member.SaveSharedPreference
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.activity_my_page.my_page_viewpager
import kotlinx.android.synthetic.main.activity_my_page.tabLayout
import kotlinx.android.synthetic.main.fragment_my_page.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MyPageFragment : Fragment() {

    lateinit var test:ArrayList<ArrayList<MyPage_Item>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        test = ArrayList()
        initBtn()
        initData()
    }
    fun initBtn(){
//        add_schedule.setOnClickListener {
//            val intent = Intent(context, MakePlanActivity::class.java)
//            startActivity(intent)
//        }
        setting.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    fun initData(){
        val id = SaveSharedPreference.getUserID(context)
        val responseListener = Response.Listener<String> { response ->
            try {
                Log.d("mypage", response)
                var Will:ArrayList<MyPage_Item>
                var Past:ArrayList<MyPage_Item>
                var Review:ArrayList<MyPage_Item>
                Will = ArrayList<MyPage_Item>()
                Past = ArrayList<MyPage_Item>()
                Review = ArrayList<MyPage_Item>()
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getJSONArray("response")
                var count = 0
                while (count < success.length()) {

                    val `object` = success.getJSONObject(count)
                    var searchitm = MyPage_Item(
                        `object`.getInt("#"),
                        `object`.getString("TripName"),
                        `object`.getString("DPDATE"),

                        `object`.getString("THEME"),
                        `object`.getInt("LIKES"),
                        `object`.getString("Plan"),
                        `object`.getString("MEM"),
                        `object`.getString("ADDATE"),
                        false
                    )
                    Log.d("d",`object`.toString())
                    val now = System.currentTimeMillis()
                    val date = Date(now)
                    val dateString = SimpleDateFormat("yyyy-MM-dd")

                    val getTime = dateString.parse(dateString.format(date))
                    val planTIME = dateString.parse(searchitm.thumbnail)

                    if(`object`.getInt("Rebool")==1){
                        Review.add(searchitm)
                    }
                    else{
                        if(getTime.before(planTIME)){
                            Will.add(searchitm)
                        }
                        else{
                            Past.add(searchitm)
                        }
                    }

                    ///////////////////////////////////////////////////////////////////////

                    count++
                }
                test.add(Will)
                test.add(Past)
                test.add(Review)
                initViewPager()

            } catch (e: Exception) {
                Log.v("Error", e.toString())
                e.printStackTrace()
            }
        }
        val idnumrequest = ShowPlanRegister(id,responseListener)
        val queue = Volley.newRequestQueue(context)
        queue.add(idnumrequest)

    }


    lateinit var adapter:MyPage_ViewPagerAdapter
    fun initViewPager(){
        Log.v("mypage_response", test.toString())
        adapter = MyPage_ViewPagerAdapter(activity!!.applicationContext, test)
        my_page_viewpager.adapter = adapter

        TabLayoutMediator(tabLayout, my_page_viewpager, object : TabLayoutMediator.OnConfigureTabCallback {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                // Styling each tab here
                when(position){
                    0->{
                        tab.setText("일정")
                    }
                    1->{
                        tab.setText("지난 일정")
                    }
                    2->{
                        tab.setText("리뷰")
                    }
                }
            }
        }).attach()

    }

}
