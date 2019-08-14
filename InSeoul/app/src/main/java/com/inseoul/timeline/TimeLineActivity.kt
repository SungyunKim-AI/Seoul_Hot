package com.inseoul.timeline

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
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


        //toolbar 커스텀 코드
        val mtoolbar = findViewById(R.id.toolbar_timeline) as Toolbar
        setSupportActionBar(mtoolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        mtoolbar.title = "Time Line"
        mtoolbar.setTitleTextColor(Color.WHITE)
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

    //toolbar에서 back 버튼
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
