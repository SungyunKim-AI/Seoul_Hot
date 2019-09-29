package com.inseoul.review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.inseoul.R
import com.inseoul.api_manager.RetrofitService
import com.inseoul.data_model.ReviewDataModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.item_home.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        // 앱 첫 실행시에만 실행하도록 수정하기
        val toast = Toast.makeText(this, "좌우로 드래그 하세요",Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 50)
        toast.show()

        requestServer()

        initToolbar()
        init()
    }
    fun init(){
        Handler().postDelayed(
            {
                initViewPager()
            },
            1500
        )
    }

    /////////// Init View Pager ///////////
    var Y = -1f
    // Intercept Touch by activity with Y Position
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Y = ev!!.getY()
        Log.e("px", Y.toString())
        // px2dp
        Y /= (resources.displayMetrics.densityDpi / 160f)
        view_pager2.isUserInputEnabled = (view_pager2.currentItem == 0 || (view_pager2.currentItem != 0 && Y > 400) || (view_pager2.currentItem == adapter.itemCount - 1 && Y > 650))
        return super.dispatchTouchEvent(ev)
    }

    lateinit var adapter:Review_ViewPagerAdapter

    fun initViewPager(){
//        initTest()
        Log.v("http_ok2", itemList.toString())
        adapter = Review_ViewPagerAdapter(supportFragmentManager,this, itemList)
        view_pager2.adapter = adapter
//        view_pager2.isUserInputEnabled = true
//        view_pager2.onInterceptTouchEvent()
//        view_pager2.onInterceptTouchEvent(false)


        val PageChangeCallback = object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
//                toolbar_review.isVisible = (position != 0)
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

    lateinit var itemList:ArrayList<ReviewItem>

    lateinit var rawData:ArrayList<ReviewDataModel.plan>
    fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(interceptor)
        return builder.build()
    }
    fun requestServer(){
        itemList = ArrayList()
        rawData = ArrayList()
        var reviewNum = intent.extras!!.getString("review", "")
        Log.v("ReviewID", reviewNum)
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://ksun1234.cafe24.com/")
            .client(createOkHttpClient())
            .build()
            .create(RetrofitService::class.java)
            .getReview(reviewNum)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                Log.e("http_ok_2", it.response.toString())
                for(i in 0 until it.response.size) {
                    val d = it.response[i]
                    rawData.add(d)

                }
                initData()
            },{
                Log.v("Fail","")
            })

    }

    fun initData(){
        Log.v("server complete", rawData.toString())
        var TripName = intent.extras!!.getString("TripName", "")

        var DPDATE = intent.extras!!.getString("DPDATE", "").split("-")
        var ADDATE = intent.extras!!.getString("ADDATE", "").split("-")

        var mem = intent.extras!!.getString("Writers")
        var u = ""
        var firstWriter = ""
        if(mem!!.contains("&")){
            val writer = mem.split("&") as ArrayList<String>?
            if(writer!!.size == 2){
                u = writer!![0]
            } else {
                u = (writer!![0] + " 외 " + (writer!!.size - 1).toString()) + "명"
            }
            firstWriter = writer[0]
        } else {
            u = mem
            firstWriter = mem
        }

        var range = ""

        if(DPDATE[0] == ADDATE[0]){
            range = DPDATE[0] + "." + DPDATE[1] + "." + DPDATE[2] + "-" + ADDATE[1] + "." + ADDATE[2]

        } else{
            range = DPDATE[0] + "." + DPDATE[1] + "." + DPDATE[2] + "-" + ADDATE[0] + "." + ADDATE[1] + "." + ADDATE[2]
        }

        var img = rawData[0].IMGNAME.split(",") as java.util.ArrayList<String?>?

        var coverImg = img!![0]

        var reviewInfo = reviewInfo(TripName, range, u, coverImg!!)

        var count = rawData.size
        var cover = ReviewItem(reviewInfo, null, 0, 0, null, null, -1, "", null, img, rawData[0].REVIEW, 37.543492, 127.077388, "인생", "인생넘버", 0, 0)
//        var summary = ReviewItem(reviewInfo, null, 2, 0, null, null, -1, "", null, img, rawData[0].REVIEW, 37.543492, 127.077388, "인생", "인생넘버", 0, 0)

        itemList.add(cover)
        for(i in 0 until count){
            var temp = ReviewItem(
                reviewInfo,
                null,
                1,
                0,
                null,
                null,
                (i + 1),
                rawData[i].UPSO_NM,
                null,
                rawData[i].IMGNAME.split(",") as java.util.ArrayList<String?>?,
                rawData[i].REVIEW,
                rawData[i].Lng.toDouble(),
                rawData[i].Lat.toDouble(),
                rawData[i].Spot_new,
                "인생넘버",
                0,
                0
            )
            itemList.add(temp)
        }
        Log.v("aaaaaa", itemList.toString())
//        itemList.add(summary)

        itemList[0].type = 0
        itemList[count + 1].type = 2

        adapter.notifyDataSetChanged()

    }
    /////////// TEST /////////////

//    lateinit var testList:ArrayList<ReviewItem>

//    fun initTest(){
//        testList = ArrayList()
//        var num = 1
//        var upso_name = "피나치공"
//        var hashTag = ArrayList<String>()
//        hashTag.add("JMT")
//        hashTag.add("존맛탱")
//        var imageList = ArrayList<Drawable?>()
//
//        var rnd = Random()
//        var n = rnd.nextInt(5) + 1
//        for(j in 0..n){
//            val index = rnd.nextInt(5) + 1
//            val filename = "sample$index"
//            imageList.add(baseContext.getDrawable(baseContext.resources.getIdentifier(filename, "drawable",  baseContext.packageName)))
//        }
//        // LatLng(37.552122, 126.988270)
//        var review_content = "좋아요~!"
//        var posX = 37.543492
//        var posY = 127.077388
//        var location = "03036 서울 종로구 자하문로15길 18"
//        var call = "02-722-0911"
//        var like = 3433
//        var dislike = 342
//
//
//        //val testCover = ReviewItem(0, coverImg, "JMT 서울 여행", "2019.8.31-9.2","hsoh0306")
//        val coverImg = baseContext.getDrawable(baseContext.resources.getIdentifier("sample1", "drawable",  baseContext.packageName))
//        val reviewInfo = reviewInfo("Let's go, 지리여행", "2019.03.01-09.02", "hsoh0306", coverImg)
//
////        37.569406443, 126.979100633
////        37.551116258, 126.987871433
////        37.567482824, 127.008075554
////        37.562516281, 126.985138659
//        var testLatLng = ArrayList<LatLng>()
//        testLatLng.add(LatLng(37.569406443, 126.979100633))
//        testLatLng.add(LatLng(37.551116258, 126.987871433))
//        testLatLng.add(LatLng(37.567482824, 127.008075554))
//        testLatLng.add(LatLng(37.562516281, 126.985138659))
//        val testS = reviewSummary(testLatLng)
//
//        val testItem_cover = ReviewItem(reviewInfo, testS, 0, 0, null, null,  num, upso_name, hashTag, imageList, review_content, posX, posY, location, call, like, dislike)
//        val testItem = ReviewItem(reviewInfo, testS, 1, 0, null, null,  num, upso_name, hashTag, imageList, review_content, posX, posY, location, call, like, dislike)
//        val testItem_Summary = ReviewItem(reviewInfo, testS, 2, 0, null, null,  num, upso_name, hashTag, imageList, review_content, posX, posY, location, call, like, dislike)
//
//        testList.add(testItem_cover)
//        testList.add(testItem)
//        testList.add(testItem)
//        testList.add(testItem)
//        testList.add(testItem)
//        testList.add(testItem_Summary)
//
//    }
//

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
