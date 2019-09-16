package com.inseoul.my_page


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_my_page.*

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
