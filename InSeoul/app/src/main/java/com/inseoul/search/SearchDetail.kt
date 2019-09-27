package com.inseoul.search

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.inseoul.R
import com.inseoul.Server.ShowPlanRegister
import com.inseoul.add_place.AddPlaceActivity
import com.inseoul.api_manager.RetrofitService
import com.inseoul.make_plan.MakePlanActivity
import com.inseoul.manage_member.SaveSharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_detail.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchDetail :
    AppCompatActivity(),
    View.OnClickListener,
    OnMapReadyCallback {

    var planList = ArrayList<planItem>()

    lateinit var data: Search_Item
    lateinit var imgList: ArrayList<String>
    lateinit var adapter: SearchDetailViewpagerAdapter

    var flag = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)
        initToolbar()
        initData()
        initViewPager()
        initView()
        initMap()
        initPlanList()


        Handler().postDelayed(
            {
                decideColor()
                adapter.notifyDataSetChanged()

            },
            500
        )
    }

    fun decideColor(){
        if (imgList.size == 0) {
            detail_title.setTextColor(Color.BLACK)
            add_my_list.setImageDrawable(getDrawable(R.drawable.ic_bookmark_black))
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_arrow) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
            no_image.visibility = VISIBLE
        } else {
            detail_title.setTextColor(Color.WHITE)
        }
    }
    fun loadDetailImg() {
        val MobileOS = "AND"
        val MobileApp = "InSeuol"
        val contentId = data.id
//        val contentTypeId = data.type
        val _type = "json"
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .baseUrl("http://api.visitkorea.or.kr/openapi/service/rest/KorService/")
            .build()
            .create(RetrofitService::class.java)
            .searchDetailImage(contentId, MobileOS, MobileApp, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                var d =
                    Log.v("detail image", it.toString())
                var str = ""

                for (i in 0..it.response.body.items.item.size - 1) {
                    val data = it.response.body.items.item[i]
//                    Log.v("tlqkf",data.firstimage2.toString())
                    imgList.add(data.originimgurl)
                }
                val size = adapter.itemCount
                val PageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        img_num.text = (position + 1).toString() + "/$size"
                        super.onPageSelected(position)
                    }
                }
                detail_image_view_pager.registerOnPageChangeCallback(PageChangeCallback)
                detail_indicator.setViewPager(detail_image_view_pager);

            }, {
                Log.v("Fail", "")
            })
    }

    fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    fun initData() {
        imgList = ArrayList()
        data = intent.getParcelableExtra<Search_Item>("data")
        flag = intent.getBooleanExtra("flag",false)
        if(flag)  add_my_list.visibility = GONE

        loadDetailImg()
    }


    fun initView() {
        detail_title.text = data.title
        detail_address.text = "주소: " + data.addr1

        if(data.tel == "02-120" || data.tel == "전환번호 미등록" || data.tel == "none"){
            detail_tel.text = "전화 번호 미등록..."
        } else {
            detail_tel.text = "전화 번호: " + data.tel
        }

        when(data.type){
            39 -> {
                detail_type.text = "#맛집"
            }
            12-> {
                detail_type.text = "#명소"
            }
            14, 15, 28, 38 -> {
                detail_type.text = "#문화"
            }
            32 -> {
                detail_type.text = "#숙소"
            }

        }

    }


    fun initViewPager() {

        adapter = SearchDetailViewpagerAdapter(this, imgList)
        detail_image_view_pager.adapter = adapter
        detail_indicator.setViewPager(detail_image_view_pager);
        previous.setOnClickListener {
            detail_image_view_pager.setCurrentItem(detail_image_view_pager.currentItem - 1, true)
        }
        next.setOnClickListener {
            detail_image_view_pager.setCurrentItem(detail_image_view_pager.currentItem + 1, true)
        }
    }

    lateinit var mapFragment: SupportMapFragment

    fun initMap() {
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.detail_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        var LatLng = LatLng(data.posY!!, data.posX!!)
        var marker = MarkerOptions()
        marker.position(LatLng)
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.default_marker))
        p0!!.addMarker(marker)
        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 15f))

    }


    fun initPlanList() {
        val id = SaveSharedPreference.getUserID(this)

        val responseListener = Response.Listener<String> { response ->
            try {

                //Log.d("dd", response)

                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getJSONArray("response")
                var count = 0

                if(planList != null){
                    planList.clear()
                }

                while (count < success.length()) {

                    val `object` = success.getJSONObject(count)
                    val temp = planItem(
                        `object`.getInt("#"), // planID
                        `object`.getString("TripName"), // Plan 이름
                        `object`.getString("DPDATE"), // 출발 날짜
                        `object`.getString("ADDATE") //도착날짜
                    )

                    //Log.d("alert_planItem", `object`.toString())

                    val now = System.currentTimeMillis()
                    val date = Date(now)
                    val dateStr = SimpleDateFormat("yyyy-MM-dd")

                    val getTime = dateStr.parse(dateStr.format(date))
                    val planTIME = dateStr.parse(temp.endDate)


                    if (getTime.before(planTIME)) {
                        planList.add(temp)
                    }

                    count++
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            if(flag == false){
                add_my_list.setOnClickListener(this)
            }

        }
        val idnumrequest = ShowPlanRegister(id, responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(idnumrequest)
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


    //다이얼 로그 세팅 onClick
    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_my_list -> {

                initPlanList()              //여행 일정 받아오기

                if (planList.size == 0) {

                   val alertBuilder = AlertDialog.Builder(this)
                    alertBuilder.setTitle("아직 일정이 없어요~")
                        .setMessage("일정을 생성하시겠습니까?")
                        .setPositiveButton("확인"){
                            dialogInterface, i ->
                            var intent = Intent(this, MakePlanActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .setNegativeButton("취소"){
                            dialogInterface, i ->
                        }
                        .show()


                } else {

                    val alertBuilder = AlertDialog.Builder(this)
                    //alertBuilder.setIcon(R.drawable.ic_launcher);
                    alertBuilder.setTitle("이곳을 추가할 일정을 선택하세요")

                    // List Adapter 생성
                    val adapter = ArrayAdapter<String>(
                        this@SearchDetail,
                        android.R.layout.select_dialog_singlechoice
                    )

                    for (i in 0 until planList.size) {
                        adapter.add(planList[i].planNm)
                    }

                    // 버튼 생성
                    alertBuilder.setNegativeButton(
                        "취소"
                    ) { dialog, which -> dialog.dismiss() }

                    // Adapter 셋팅
                    alertBuilder.setAdapter(
                        adapter
                    ) { dialog, id ->
                        // AlertDialog 안에 있는 AlertDialog
                        val strName = adapter.getItem(id)
                        val innBuilder = AlertDialog.Builder(this@SearchDetail)
                        innBuilder.setMessage(strName)
                        innBuilder.setTitle("일정을 수정 하시겠습니까?")
                        innBuilder
                            .setPositiveButton(
                                "확인"
                            ) { dialog, which ->
                                dialog.dismiss()
                                val intent_add = Intent(this@SearchDetail, AddPlaceActivity::class.java)
                                intent_add.putExtra("flag_key", 3)
                                intent_add.putExtra("PlanID",planList[adapter.getPosition(strName)].planID)
                                intent_add.putExtra("placeData", data)

                                startActivity(intent_add)
                                finish()
                            }
                        innBuilder.setNegativeButton(
                            "취소"
                        ) { dialog, which -> dialog.dismiss() }
                        innBuilder.show()
                    }
                    alertBuilder.show()
                }
            }

            else -> {
            }
        }
    }

    ////////////////Toolbar//////////////
    fun initToolbar() {
        //toolbar 커스텀 코드
        setSupportActionBar(toolbar_search_details)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_white) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        toolbar_search_details.bringToFront()
    }


    //플랜 아이템 객체
    data class planItem(var planID: Int, var planNm: String, var startDate: String, var endDate: String)
}
