package com.inseoul.search


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.inseoul.R
import com.inseoul.Server.SearchRequest
import com.inseoul.add_place.AddPlaceActivity
import com.inseoul.api_manager.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {

    lateinit var category: ArrayList<ArrayList<Search_Item>>
    var tour = ArrayList<Search_Item>()
    var culture = ArrayList<Search_Item>()
    var food = ArrayList<Search_Item>()
    var hotel = ArrayList<Search_Item>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar_search)

        init()
        initViewPager()

        // Search View EventListener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // Text Change
            override fun onQueryTextChange(p0: String?): Boolean {
                //Log.d("Text Change",p0)
                //////////////////////// DB Connect & Query ////////////////////////

                /////////////////////////////////////////////////////////////////////
                return false
            }

            // Submit
            override fun onQueryTextSubmit(p0: String?): Boolean {
                //Log.d("Submit",p0)
                //////////////////////// DB Connect & Query ////////////////////////
                tour.clear()
                hotel.clear()
                food.clear()
                culture.clear()
//                searchKeyword(p0!!)
                initData(p0)
                /////////////////////////////////////////////////////////////////////
                return false
            }
        })
    }

    ////////////////////// Tour API //////////////////////
    fun searchKeyword(keyword: String) {
        val MobileOS = "AND"
        val MobileApp = "InSeuol"
//        val contentType = 12
        val areaCode = 1
        val _type = "json"
        // ContentType
        // 관광지 12

        // 문화시설 14
        // 행사/공연/축제 15
        // 레포츠 28

        // 숙박 32

        // 음식점 39
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
//            .client(createOkHttpClient())
            .baseUrl("http://api.visitkorea.or.kr/openapi/service/rest/KorService/")
            .build()
            .create(RetrofitService::class.java)
            .searchKeyWord(keyword, areaCode, MobileOS, MobileApp, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
//
//                for (i in 0..it.response.body.items.item.size - 1) {
//                    val data = it.response.body.items.item[i]
////                    Log.v("tlqkf",data.firstimage2.toString())
//
//                    var searchitem = Search_Item(
//                        data.contentid,
//                        data.title,
//                        data.firstimage2.toString(),
//                        data.contenttypeid,
//                        data.mapx,
//                        data.mapy,
//                        data.addr1,
//                        data.addr2,
//                        data.tel,
//                        0
//
//                    )
//                    when (data.contenttypeid) {
//                        12 -> {
//                            tour.add(searchitem)
//                        }
//                        14, 15, 28, 38 -> {
//                            culture.add(searchitem)
//                        }
//                        39 -> {
//                            food.add(searchitem)
//                        }
//                        32 -> {
//                            hotel.add(searchitem)
//                        }
//                    }
//
//                }
//                adapter.itemlist[0] = tour
//                adapter.itemlist[1] = culture
//                adapter.itemlist[2] = food
//                adapter.itemlist[3] = hotel
//
//                adapter.notifyDataSetChanged()
////                test_text.text = str

            }, {
                //                Log.v("Fail","")
            })
    }

    fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    //////////////////////////////////////////////////////
    fun init() {
        //toolbar 커스텀 코드
        val mtoolbar = findViewById(R.id.toolbar_search) as Toolbar
        setSupportActionBar(mtoolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요

        searchView.performClick()
        searchView.requestFocus()
        searchView.isSubmitButtonEnabled = true

    }

    //////////////////서버 통신////////////////////
    fun initData(p0: String?) {
        val responseListener = Response.Listener<String> { response ->
            try {
                //                    Log.d("dd", response);
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getJSONArray("response")
                var count = 0
                while (count < success.length()) {
                    val `object` = success.getJSONObject(count)
                    Log.d("d",`object`.toString())
                    //////////////////////////////////////////////////////////////////////
                    //var searchItm = Search_Item(`object`.getInt("IDNUM"),`object`.getString("NAME"), )
                    // "class"=>분류,"IDNUM"=>$번호,"NAME"=>업소명 ,"PH"=>전화번호,"Lat"=>lat,"Lng"=>lng,"Spot_new"=>도로명 주소,"INFO"=>세부정보,"HashTag"=>해시태그."IMGURL" => 이미지 주소
                    //`object`.getInt("IDNUM") 와 같이 정보 긁어오면됨.
                    var d= 0
                    when (`object`.getString("class")) {
                        "맛집" -> {
                            d=39
                        }
                        "쇼핑"-> {
                            d=12
                        }
                        "명소" -> {
                            d=14
                        }
                        else -> {
                            d = `object`.getInt("class")
                        }

                    }
                    var searchitm = Search_Item(
                        `object`.getInt("IDNUM"),
                        `object`.getString("NAME"),
                        d,
                        `object`.getDouble("Lat"),
                        `object`.getDouble("Lng"),
                        `object`.getString("Spot_new"),
                        `object`.getString("HashTag"),
                        `object`.getString("PH"),
                        1,
                        `object`.getString("IMGURL")
                    )
                    when (`object`.getString("class")) {
                        "맛집", "39" -> {
                            food.add(searchitm)
                        }
                        "쇼핑", "12"-> {
                            tour.add(searchitm)
                        }
                        "명소", "14", "15", "28", "38" -> {
                            culture.add(searchitm)
                        }
                        "32" -> {
                            hotel.add(searchitm)
                        }

                    }
                    Log.v("superTlqkf", searchitm.toString())

                    ///////////////////////////////////////////////////////////////////////

                    count++
                }

//                Log.v("superTlqkf", tour.toString())
                adapter.itemlist[0] = tour
                adapter.itemlist[1] = culture
                adapter.itemlist[2] = food
                adapter.itemlist[3] = hotel

                adapter.notifyDataSetChanged()
                TabLayoutMediator(search_tabLayout, search_viewpager, object : TabLayoutMediator.OnConfigureTabCallback {
                    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                        // Styling each tab here
                        when (position) {
                            0 -> {
                                tab.setText("관광" + " " + tour.size.toString())
                            }
                            1 -> {
                                tab.setText("문화/쇼핑" + " " + culture.size.toString())
                            }
                            2 -> {
                                tab.setText("맛집" + " " + food.size.toString())
                            }
                            3 -> {
                                tab.setText("숙박" + " " + hotel.size.toString())
                            }
                        }
                    }
                }).attach()
                if (success.length() == 0) {

                } else {

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val idnumrequest = SearchRequest(p0,responseListener)
        val queue = Volley.newRequestQueue(this@SearchActivity)
        queue.add(idnumrequest)
    }


    lateinit var adapter: SearchViewPagerAdpater
    fun initViewPager() {
        category = ArrayList()
        for (i in 0..3) {
            category.add(ArrayList<Search_Item>())
        }
        val flag = intent.hasExtra("flag")


        val listener = object : SearchAdapter.RecyclerViewAdapterEventListener {
            override fun onClick(view: View, position: Int, categoryIndex: Int) {
                val intent = Intent(this@SearchActivity, AddPlaceActivity::class.java)
                intent.putExtra("placeData", category[categoryIndex][position])
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        adapter = SearchViewPagerAdpater(this, category, listener, flag)
        search_viewpager.adapter = adapter
        TabLayoutMediator(search_tabLayout, search_viewpager, object : TabLayoutMediator.OnConfigureTabCallback {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                // Styling each tab here
                when (position) {
                    0 -> {
                        tab.setText("관광")
                    }
                    1 -> {
                        tab.setText("문화/쇼핑")
                    }
                    2 -> {
                        tab.setText("맛집")
                    }
                    3 -> {
                        tab.setText("숙박")
                    }
                }
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
