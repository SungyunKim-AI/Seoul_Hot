package com.inseoul.review

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.model.LatLng
import com.inseoul.MainActivity
import com.inseoul.R
import com.inseoul.make_plan.RecommendPlan
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.activity_review_cover.view.*
import java.util.*
import kotlin.collections.ArrayList

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        initToolbar()
        initViewPager()

    }

    /////////// Init View Pager ///////////
    var Y = -1f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Y = event!!.getY()
        Log.e("px", Y.toString())
        Y = px2dp(Y)
        Log.e("dp", Y.toString())
        return super.onTouchEvent(event)
    }

    fun px2dp(px:Float):Float{
        return px / (resources.displayMetrics.densityDpi / 160f)
    }

    // Intercept Touch by activity with Y Position
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Y = ev!!.getY()
        Log.e("px", Y.toString())
        Y = px2dp(Y)
        view_pager2.isUserInputEnabled = (view_pager2.currentItem == 0 || (view_pager2.currentItem != 0 && Y > 400) || (view_pager2.currentItem == adapter.itemCount - 1 && Y > 650))
        return super.dispatchTouchEvent(ev)
    }

    lateinit var adapter:Review_ViewPagerAdapter

    fun initViewPager(){
        initTest()
        adapter = Review_ViewPagerAdapter(supportFragmentManager,this, testList)
        view_pager2.adapter = adapter
//        view_pager2.isUserInputEnabled = true
//        view_pager2.onInterceptTouchEvent()
//        view_pager2.onInterceptTouchEvent(false)
        val PageChangeCallback = object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                toolbar_review.isVisible = (position != 0)
                super.onPageSelected(position)
            }
            override fun onPageScrollStateChanged(state: Int) {
//                view_pager2.isUserInputEnabled = ((view_pager2.currentItem == 0) || Y > 350)
                when(state){
                    ViewPager2.SCROLL_STATE_IDLE->{

                    }
                    ViewPager2.SCROLL_STATE_SETTLING->{

                    }
                    ViewPager2.SCROLL_STATE_DRAGGING->{

                    }
                }
                super.onPageScrollStateChanged(state)
            }
        }
        view_pager2.registerOnPageChangeCallback(PageChangeCallback)
//        view_pager2.layout
    }

    /////////// TEST /////////////

    lateinit var testList:ArrayList<ReviewItem>

    fun initTest(){
        testList = ArrayList()
        var num = 1
        var upso_name = "피나치공"
        var hashTag = ArrayList<String>()
        hashTag.add("JMT")
        hashTag.add("존맛탱")
        var imageList = ArrayList<Drawable?>()

        var rnd = Random()
        var n = rnd.nextInt(5) + 1
        for(j in 0..n){
            val index = rnd.nextInt(5) + 1
            val filename = "sample$index"
            imageList.add(baseContext.getDrawable(baseContext.resources.getIdentifier(filename, "drawable",  baseContext.packageName)))
        }
        // LatLng(37.552122, 126.988270)
        var review_content = "좋아요~!"
        var posX = 37.543492
        var posY = 127.077388
        var location = "03036 서울 종로구 자하문로15길 18"
        var call = "02-722-0911"
        var like = 3433
        var dislike = 342


        //val testCover = ReviewItem(0, coverImg, "JMT 서울 여행", "2019.8.31-9.2","hsoh0306")
        val coverImg = baseContext.getDrawable(baseContext.resources.getIdentifier("sample1", "drawable",  baseContext.packageName))
        val reviewInfo = reviewInfo("Let's go, 지리여행", "2019.03.01-09.02", "hsoh0306", coverImg)

//        37.569406443, 126.979100633
//        37.551116258, 126.987871433
//        37.567482824, 127.008075554
//        37.562516281, 126.985138659
        var testLatLng = ArrayList<LatLng>()
        testLatLng.add(LatLng(37.569406443, 126.979100633))
        testLatLng.add(LatLng(37.551116258, 126.987871433))
        testLatLng.add(LatLng(37.567482824, 127.008075554))
        testLatLng.add(LatLng(37.562516281, 126.985138659))
        val testS = reviewSummary(testLatLng)

        val testItem_cover = ReviewItem(reviewInfo, testS, 0,  num, upso_name, hashTag, imageList, review_content, posX, posY, location, call, like, dislike)
        val testItem = ReviewItem(reviewInfo, testS, 1,  num, upso_name, hashTag, imageList, review_content, posX, posY, location, call, like, dislike)
        val testItem_Summary = ReviewItem(reviewInfo, testS, 2,  num, upso_name, hashTag, imageList, review_content, posX, posY, location, call, like, dislike)

        testList.add(testItem_cover)
        testList.add(testItem)
        testList.add(testItem)
        testList.add(testItem)
        testList.add(testItem)
        testList.add(testItem_Summary)

    }


    ////////////////Toolbar//////////////
    fun initToolbar() {
        //toolbar 커스텀 코드
        setSupportActionBar(toolbar_review)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_white) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        toolbar_review.bringToFront()
    }

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
