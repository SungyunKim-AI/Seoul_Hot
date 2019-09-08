package com.inseoul.my_page

import android.app.ProgressDialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.inseoul.R
import com.inseoul.Server.idnumRequest
import com.inseoul.manage_member.SaveSharedPreference
import com.inseoul.manage_schedules.adapter_schedule
import com.inseoul.manage_schedules.adapter_schedule_past
import com.inseoul.manage_schedules.recyclerview_schedule
import com.inseoul.manage_schedules.recyclerview_schedule_past
import com.inseoul.search.SearchDetail
import com.inseoul.timeline.TimeLineItem
import com.inseoul.timeline.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_my_page.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MyPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        initToolbar()
        initViewPager()
    }

    lateinit var test:ArrayList<ArrayList<MyPage_Item>>
    fun initTest(){
        test = ArrayList()
        for(j in 0..2){
            var t = ArrayList<MyPage_Item>()

            for(i in 0..10){
                t.add(MyPage_Item("This is Title" + i.toString(), "This is Content" + i.toString(), null))
            }
            test.add(t)

        }

    }

    lateinit var adapter:MyPage_ViewPagerAdapter
    fun initViewPager(){

        initTest()

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


        ///////////////////////////////////////////////////////

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
