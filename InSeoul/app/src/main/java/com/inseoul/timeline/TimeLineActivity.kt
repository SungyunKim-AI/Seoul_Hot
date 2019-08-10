package com.inseoul.timeline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_time_line.*

class TimeLineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        initViewPager()
    }

    private var ViewPagerAdapter: ViewPagerAdapter? = null

    fun initViewPager(){
        view_pager2.adapter = ViewPagerAdapter()

    }
}
