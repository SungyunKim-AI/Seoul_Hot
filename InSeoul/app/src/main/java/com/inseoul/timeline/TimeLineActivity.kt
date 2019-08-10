package com.inseoul.timeline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_time_line.*

class TimeLineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)

        initTest()

        initViewPager()
    }

    private val test = ArrayList<TimeLineItem>()
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: TimeLineAdapter? = null

    fun initTest(){

        for(i in 0..10){
            test.add(TimeLineItem("This is Title" + i.toString(), "This is Content" + i.toString()))
        }
    }


    private var ViewPagerAdapter: ViewPagerAdapter? = null

    fun initViewPager(){
        view_pager2.adapter = ViewPagerAdapter(this, test)
        TabLayoutMediator(tabLayout, view_pager2, object : TabLayoutMediator.OnConfigureTabCallback {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                // Styling each tab here
                tab.setText("Tab $position")
            }
        }).attach()
    }
}
