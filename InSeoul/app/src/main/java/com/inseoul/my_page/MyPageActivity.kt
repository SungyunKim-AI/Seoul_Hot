package com.inseoul.my_page

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.inseoul.R
import com.inseoul.Server.ShowPlanRegister
import com.inseoul.Server.idnumRequest
import com.inseoul.make_plan.MakePlanActivity
import com.inseoul.manage_member.SaveSharedPreference
import kotlinx.android.synthetic.main.activity_my_page.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MyPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        initToolbar()
        initData()
        initBtn()
    }

    fun initBtn(){
        add_schedule.setOnClickListener {
            val intent = Intent(this, MakePlanActivity::class.java)
            startActivity(intent)
        }
    }


    lateinit var test:ArrayList<ArrayList<MyPage_Item>>



    fun initData(){
        val id = SaveSharedPreference.getUserID(this)
        val responseListener = Response.Listener<String> { response ->
            try {
                test = ArrayList()
                 Log.d("dd", response);
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
                        "",
                        false
                    )
                    Log.d("d",`object`.toString())
                    val now = System.currentTimeMillis()
                    val date = Date(now)
                    val sdf = SimpleDateFormat("yyyy-MM-dd")

                    val getTime = sdf.parse(sdf.format(date))
                    val planTIME = sdf.parse(searchitm.date)
                    var d= 0
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
                e.printStackTrace()
            }
        }
        val idnumrequest = ShowPlanRegister(id,responseListener)
        val queue = Volley.newRequestQueue(this@MyPageActivity)
        queue.add(idnumrequest)
    }


    lateinit var adapter:MyPage_ViewPagerAdapter
    fun initViewPager(){


        adapter = MyPage_ViewPagerAdapter(this, test)
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

    fun initToolbar(){
        //toolbar 커스텀 코드
        val mtoolbar = findViewById(R.id.toolbar_my_page) as Toolbar
        setSupportActionBar(mtoolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요

    }


    ///////////////toolbar에서 back 버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
