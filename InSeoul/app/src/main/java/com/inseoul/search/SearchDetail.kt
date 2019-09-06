package com.inseoul.search

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.inseoul.add_place.AddPlaceActivity
import com.inseoul.api_manager.RetrofitService
import com.inseoul.manage_member.SaveSharedPreference
import com.inseoul.manage_schedules.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_detail.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class SearchDetail :
    AppCompatActivity(),
    View.OnClickListener,
    OnMapReadyCallback
{

    internal var m_name: String? = null
    private var plan_count: Int = 0
    private val btnAlert: Button? = null


    /////////////TEST CODE///////////////
    internal var testlist = ArrayList<planlist>()
    private val upsoID: Int = 0
    private val planidarray = ArrayList<Int>()


    lateinit var data: Search_Item
    lateinit var imgList:ArrayList<String>
    lateinit var adapter:SearchDetailViewpagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)
        initToolbar()
        initData()
        initViewPager()
        initView()
        initMap()
        Log.v("Detail data", data.toString())

//        init()
    }

    fun loadDetailImg(){
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
                    Log.v("detail image",it.toString())
                var str = ""

                for(i in 0..it.response.body.items.item.size - 1){
                    val data = it.response.body.items.item[i]
//                    Log.v("tlqkf",data.firstimage2.toString())
                    imgList.add(data.originimgurl)
                }
                val size = adapter.itemCount
                val PageChangeCallback = object: ViewPager2.OnPageChangeCallback(){
                    override fun onPageSelected(position: Int) {
                        img_num.text = (position + 1).toString() + "/$size"
                        super.onPageSelected(position)
                    }
                }
                detail_image_view_pager.registerOnPageChangeCallback(PageChangeCallback)
                adapter.notifyDataSetChanged()
                detail_indicator.setViewPager(detail_image_view_pager);

            },{
                Log.v("Fail","")
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
        loadDetailImg()

    }


    fun initView(){
        detail_title.text = data.title
        detail_address.text = "주소: " + data.addr1

    }
    fun initViewPager(){

        adapter = SearchDetailViewpagerAdapter(this, imgList)
        detail_image_view_pager.adapter = adapter
        detail_indicator.setViewPager(detail_image_view_pager);
        previous.setOnClickListener {
            detail_image_view_pager.setCurrentItem(detail_image_view_pager.currentItem - 1,true)
        }
        next.setOnClickListener {
            detail_image_view_pager.setCurrentItem(detail_image_view_pager.currentItem + 1,true)
        }
    }

    lateinit var mapFragment:SupportMapFragment

    fun initMap(){
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.detail_map) as SupportMapFragment
//        bottom_sheet_map
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var LatLng = LatLng(data.posY!!, data.posX!!)
        var marker = MarkerOptions()
        marker.position(LatLng)
        marker.icon(BitmapDescriptorFactory.fromResource(com.inseoul.R.drawable.default_marker))
        p0!!.addMarker(marker)
        p0!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 15f))

    }

    fun init() {
        val idNUM = SaveSharedPreference.getUserID(this)

        //        SearchItem searchItem = getIntent().getParcelableExtra("placeData");
        val responseListener = object : Response.Listener<String> {
            override fun onResponse(response: String) {
                try {
                    Log.d("dd", response)
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getJSONArray("response")
                    var count = 0
                    plan_count = success.length()
                    while (count < success.length()) {
                        val `object` = success.getJSONObject(count)
                        planidarray.add(`object`.getInt("PLANID"))
                        Log.d(this.javaClass.name, planidarray.toString())


                        count++

                    }
                    val showPlanTask = ShowPlanTask()
                    showPlanTask.execute()

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        val idnumrequest = idnumRequest(idNUM, responseListener)
        val queue = Volley.newRequestQueue(this@SearchDetail)
        queue.add(idnumrequest)


        /////////////TEST CODE///////////////


        //        btnAlert = (Button)findViewById(R.id.add_my_list);
        //        btnAlert.setOnClickListener(this);
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    inner class ShowPlanTask : AsyncTask<Void, Void, String>() {

        var target = ""
        internal var PlanID: Int = 0
        internal var asyncDialog = ProgressDialog(this@SearchDetail)


        override fun onPreExecute() {

            super.onPreExecute()

            target = "http://ksun1234.cafe24.com/ShowPlan.php" // 웹 호스팅 정보를 받기위한  php 파일 주소


        }

        override fun doInBackground(vararg voids: Void): String? {
            try {
                Log.d("asyncTask", "Strart")
                val url = URL(target)
                val con = url.openConnection() as HttpURLConnection
                Log.d("asyncTask", "Connect")
                val inputStream = con.inputStream
                val stringBuilder = StringBuilder()
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var json: String
                while ((bufferedReader.readLine()) != null) {
                    json = bufferedReader.readLine()
                    stringBuilder.append(json + "\n")
                }
                bufferedReader.close()
                inputStream.close()
                con.disconnect()
                return stringBuilder.toString().trim { it <= ' ' }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        public override fun onPostExecute(result: String) {


            try {
                Log.d("asyncTask", "onPostExecute")
                val jsonObject = JSONObject(result)
                val jsonArray = jsonObject.getJSONArray("response")

                var count = 0
                while (count < jsonArray.length()) {
                    Log.d("asyncTask", "Reading$jsonArray")
                    val `object` = jsonArray.getJSONObject(count)
                    val planID = `object`.getInt("H")
                    Log.d("as", planidarray.toString())
                    if (planidarray.contains(planID)) {
                        Log.d("async", "ss" + Integer.toString(planID))

                        testlist.add(
                            planlist(
                                `object`.getString("TripName"),
                                `object`.getString("DPDATE"),
                                `object`.getInt("H")
                            )
                        )
                    }

                    count++

                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

            asyncDialog.dismiss()
        }

        override fun onCancelled() {

            super.onCancelled()
        }
    }

    //다이얼 로그 세팅 onClick
    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_my_list -> {

                val alertBuilder = AlertDialog.Builder(this)
                //alertBuilder.setIcon(R.drawable.ic_launcher);
                alertBuilder.setTitle("이곳을 추가할 일정을 선택하세요")

                // List Adapter 생성
                val adapter = ArrayAdapter<String>(
                    this@SearchDetail,
                    android.R.layout.select_dialog_singlechoice
                )
                for (i in 0 until plan_count) {
                    adapter.add(testlist[i].title)
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
                            intent_add.putExtra("PlanTitle_add", strName)
                            intent_add.putExtra("flag_key", 1)
                            Log.d("alert", strName!!)
                            startActivity(intent_add)
                        }
                    innBuilder.setNegativeButton(
                        "취소"
                    ) { dialog, which -> dialog.dismiss() }
                    innBuilder.show()
                }
                alertBuilder.show()
            }

            else -> {
            }
        }
    }

    ////////////////TEST CODE//////////////
    internal inner class planlist(var title: String, var date: String, var planid: Int)
}
