package com.inseoul.home


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import com.inseoul.add_place.AddPlaceActivity
import com.inseoul.api_manager.RetrofitService
import com.inseoul.data_model.TimeLineModel
import com.inseoul.manage_member.SaveSharedPreference
import com.inseoul.review.ReviewActivity
import com.inseoul.search.SearchActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.recyclerView_addPlace
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        setSupportActionBar(toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayShowTitleEnabled(false)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.setHomeButtonEnabled(true)

//        initToolbar()
        getReviewFromServer()
        initRecyclerView()
        initBtn()
//        initFloating()

    }
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    fun initBtn() {
        search_btn.setOnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)

        }
    }

    ////////////////// Recycler View //////////////////
    private val itemList = ArrayList<HomeItem>()
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: HomeAdapter

    lateinit var rawData: ArrayList<TimeLineModel.timeline>

    // Review 받아오기

    fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    fun getReviewFromServer() {
        rawData = ArrayList()
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://ksun1234.cafe24.com/")
//            .client(createOkHttpClient())
            .build()
            .create(RetrofitService::class.java)
            .getTimeLine()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                Log.e("http_ok", it.response.toString())
                for (i in 0 until it.response.size) {
                    val d = it.response[i]
                    if (d.ReviewBool == 1) {
                        rawData.add(d)

                        Log.d("main_review", d.toString())
                        val writer = d.MEM.split("&") as ArrayList<String>?
                        var u = ""
                        if (writer!!.size == 2) {
                            u = writer!![0]
                        } else {
                            u = (writer!![0] + " 외 " + (writer!!.size - 1).toString()) + "명"
                        }
                        val thumbnail = d.Review!!

                        var temp = d.ADDATE.split("-")
                        var date = temp[0] + "." + temp[1] + "." + temp[2]
                        itemList.add(HomeItem(thumbnail, d.TripName, date, u, d.LIKES, d.H))
                    }
                }
                adapter.notifyDataSetChanged()
            }, {
                Log.v("Fail", "")
            })

    }

    fun scrapDialog(context: Context, position:Int){

        val builder = AlertDialog.Builder(context)

        builder.setTitle("일정을 스크랩 합니다.")
            .setMessage("이 일정을 스크랩 하여 새로운 일정을 생성 합니다.")
            .setPositiveButton("확인") { dialog, id ->
                scrapThis(context,position)
            }
            .setNegativeButton("취소") { dialog, id ->

            }.show()
    }

    fun scrapThis(context: Context,position:Int){
        val intent = Intent(context, AddPlaceActivity::class.java)
        //planID 받아와서 planData 불러오기
        //planData받아와서 intent로 전달
        //불러 와야 하는것 : DPDATE, ADDATE, PlANDATE
//        DPDATE = extras.getString("startDate", "2222-22-22")
//        ADDATE = extras.getString("endDate", "2222-22-22")
        intent.putExtra("flag_key", 4)
        intent.putExtra("scrapPlan",itemList[position].)

        Log.d("alert_plan",)

        startActivity(intent)
    }


    fun initRecyclerView() {
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView_addPlace.layoutManager = layoutManager
        val listener = object : HomeAdapter.RecyclerViewAdapterEventListener {
            override fun onClick_scrap(view: View, position: Int) {
                scrapDialog(context!!)
            }

            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, ReviewActivity::class.java)

                // Review전달
                intent.putExtra("review", rawData[position].H)

                // 기본 정보
//                intent.putExtra("Plan", rawData[position].Plan)
                intent.putExtra("TripName", rawData[position].TripName)
                intent.putExtra("DPDATE", rawData[position].DPDATE)
                intent.putExtra("ADDATE", rawData[position].ADDATE)
                intent.putExtra("Writers", rawData[position].MEM)

//                intent.putExtra("plan_info", rawData[position].Review!![0].PlaceInfo)
                Log.e("review_intent", rawData[position].toString())

                startActivity(intent)
            }
        }

        adapter = HomeAdapter(context!!, listener, itemList)
        recyclerView_addPlace.adapter = adapter

    }

    ////////////////FloatingButton Setting////////////////////
//    private fun initFloating() {
//
//        fab_rotate = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_anim)
//        fab_rotate_close = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_anim_close)
//        fab_open1 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
//        fab_open2 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
//        fab_open2.duration = 500
//        fab_open3 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
//        fab_open3.duration = 750
//
//        fab_close1 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
//        fab_close2 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
//        fab_close2.duration = 550
//        fab_close3 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
//        fab_close3.duration = 800
//
//
//
//        fab.setOnClickListener {
//            anim()
//        }
//        fab1.setOnClickListener {
//            anim()
//            val intent = Intent(this, MakePlanActivity::class.java)
//            startActivity(intent)
//        }
//        fab2.setOnClickListener {
//            anim()
//            if (!loginCheck())
//                loginDialog()
//            else {
//                val intent = Intent(this, MyPageActivity::class.java)
//                startActivity(intent)
//            }
//        }
//        fab3.setOnClickListener {
//            anim()
//            val intent = Intent(this, TimeLineActivity::class.java)
//            startActivity(intent)
//        }
//
//
//        appbarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
//            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
//                if (verticalOffset < -linearlayout_size.height) {
//                    fab.show()
//                }
//                else {
//                    fab.hide()
//                    if(isFabOpen == true){
//                        anim()
//                    }
//                }
//
//            }
//        })
//    }
//
//    fun anim() {
//
//        if (isFabOpen) {
//            fab.startAnimation(fab_rotate_close)
//            fab1.startAnimation(fab_close3)
//            fab2.startAnimation(fab_close2)
//            fab3.startAnimation(fab_close1)
//            fab1.isClickable = false
//            fab2.isClickable = false
//            fab3.isClickable = false
//            isFabOpen = false
//        } else {
//            fab.startAnimation(fab_rotate)
//            fab1.startAnimation(fab_open1)
//            fab2.startAnimation(fab_open2)
//            fab3.startAnimation(fab_open3)
//            fab1.isClickable = true
//            fab2.isClickable = true
//            fab3.isClickable = true
//            isFabOpen = true
//        }
//    }

}
