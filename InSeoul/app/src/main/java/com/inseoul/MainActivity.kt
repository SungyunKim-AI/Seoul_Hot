package com.inseoul

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.inseoul.api_manager.RetrofitService
import com.inseoul.forecast.ForecastActivity
import com.inseoul.forecast.Forecast_shortTermItem
import com.inseoul.home.HomeAdapter
import com.inseoul.home.HomeItem
import com.inseoul.make_plan.MakePlanActivity
import com.inseoul.manage_member.SaveSharedPreference
import com.inseoul.manage_member.SignInActivity
import com.inseoul.manage_member.SignUpActivity
import com.inseoul.my_page.MyPageActivity
import com.inseoul.review.ReviewActivity
import com.inseoul.search.SearchActivity
import com.inseoul.timeline.TimeLineActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    val key = "4d4956476768736f3131397547724879" // 서울시 데이터 API Key

    lateinit var backPressCloseHandler: BackPressCloseHandler

    var isFabOpen: Boolean = false
    lateinit var fab_open1: Animation
    lateinit var fab_open2: Animation
    lateinit var fab_open3: Animation
    lateinit var fab_close1: Animation
    lateinit var fab_close2: Animation
    lateinit var fab_close3: Animation
    lateinit var fab_rotate: Animation
    lateinit var fab_rotate_close: Animation

    lateinit var model_shortTerm: ArrayList<Forecast_shortTermItem>

    fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    lateinit var w_intent:Intent
    fun ForecastAPI_ShortTerm(){
        w_intent = Intent(this, ForecastActivity::class.java)

        model_shortTerm = ArrayList()

        val _type = "json"
        val numOfRows = 40
        val pageNo = 1
        var base_data = 1
        var base_time =""
        val nx = 60
        val ny = 127

        val s_now = System.currentTimeMillis()
        val s_date = Date(s_now)
        val s_format = SimpleDateFormat("yyyyMMdd").format(s_date).toInt()
        val s_month = SimpleDateFormat("MM").format(s_date).toInt()
        val s_day = SimpleDateFormat("dd").format(s_date).toInt()

        val s_time = SimpleDateFormat("HH").format(s_date).toInt()
        val s_minute = SimpleDateFormat("mm").format(s_date).toInt()

        model_shortTerm.add(Forecast_shortTermItem(s_month, s_day, null, null, null,null,null))
        model_shortTerm.add(Forecast_shortTermItem(s_month, s_day, null, null, null,null,null))
        model_shortTerm.add(Forecast_shortTermItem(s_month, s_day, null, null, null,null,null))
        model_shortTerm.add(Forecast_shortTermItem(s_month, s_day, null, null, null,null,null))


        base_data = s_format
        if(s_minute < 30){
            base_time = (s_time - 1).toString() + "30"
        } else{
            base_time = (s_time).toString() + "30"
        }
//        base_time = s_time.toString() + s_minute.toString()

        Log.e("time_d", s_format.toString())
        Log.e("time_t", s_time.toString())
        Log.e("time_m", s_minute.toString())
        Log.e("time_base",base_time)


        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://newsky2.kma.go.kr/service/")
            .client(createOkHttpClient())
            .build()
            .create(RetrofitService::class.java)
            .ShortTermWeather(base_data, base_time, nx, ny, numOfRows, pageNo, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("shortTerm", it.toString())
                for(i in 0 until it.response.body.items.item.size) {
                    when(it.response.body.items.item[i].category){
                        "T1H"->{
                            model_shortTerm[i % 4].T1H = it.response.body.items.item[i].fcstValue

                        }
                        "PTY"->{
                            model_shortTerm[i % 4].PTY = it.response.body.items.item[i].fcstValue.toInt()
                        }
                        "SKY"->{
                            model_shortTerm[i % 4].SKY = it.response.body.items.item[i].fcstValue.toInt()
                        }
                    }
                }
                model_shortTerm[0].date = it.response.body.items.item[0].fcstDate
                model_shortTerm[0].time = it.response.body.items.item[0].fcstTime

                model_shortTerm[1].date = it.response.body.items.item[1].fcstDate
                model_shortTerm[1].time = it.response.body.items.item[1].fcstTime

                model_shortTerm[2].date = it.response.body.items.item[2].fcstDate
                model_shortTerm[2].time = it.response.body.items.item[2].fcstTime

                model_shortTerm[3].date = it.response.body.items.item[3].fcstDate
                model_shortTerm[3].time = it.response.body.items.item[3].fcstTime


                for(i in 0..3){
                    if(model_shortTerm[i].SKY != null && model_shortTerm[i].PTY != null){
                        w_intent.putExtra("today_weather", model_shortTerm[i])

                        when(model_shortTerm[i].SKY){
                            1->{
                                home_weather_icon.setImageResource(R.drawable.w_sun)
                                home_status.text = "맑음"
                            }
                            3, 4->{
                                when(model_shortTerm[i].PTY){
                                    0->{
                                        home_weather_icon.setImageResource(R.drawable.w_cloud)
                                        home_status.text = "흐림"
                                    }
                                    1,2,4->{
                                        home_weather_icon.setImageResource(R.drawable.w_rain)
                                        home_status.text = "비"
                                    }
                                    3->{
                                        home_weather_icon.setImageResource(R.drawable.w_snow)
                                        home_status.text = "눈"
                                    }
                                }
                            }
                        }
                        home_date.text = model_shortTerm[i].month.toString() + "/" + model_shortTerm[i].day.toString()
                        home_temp.text = model_shortTerm[i].T1H.toString() + "º"
                        break;
                    }
                }


                var str = "\n"
                str += model_shortTerm[0].toString()
                str += "\n"
                str += model_shortTerm[1].toString()
                str += "\n"
                str += model_shortTerm[2].toString()
                str += "\n"
                str += model_shortTerm[3].toString()
                Log.e("hsoh0306", str)
//                test.text = str

            },{
                Log.v("Fail","")
            })
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ForecastAPI_ShortTerm()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setSupportActionBar(toolbar)

        initNav()

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)


        /////////////////////////////////////////////////////
        initPermission()
        initBtn()
        initTest()
        initRecyclerView()
        initFloating()

    }

    ////////////////FloatingButton Setting////////////////////
    private fun initFloating() {

        fab_rotate = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_anim)
        fab_rotate_close = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate_anim_close)
        fab_open1 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fab_open2 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fab_open2.duration = 500
        fab_open3 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        fab_open3.duration = 750

        fab_close1 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
        fab_close2 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
        fab_close2.duration = 550
        fab_close3 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
        fab_close3.duration = 800



        fab.setOnClickListener {
            anim()
        }
        fab1.setOnClickListener {
            anim()
            val intent = Intent(this, MakePlanActivity::class.java)
            startActivity(intent)
        }
        fab2.setOnClickListener {
            anim()
            if (!loginCheck())
                loginDialog()
            else {
                val intent = Intent(this, MyPageActivity::class.java)
                startActivity(intent)
            }
        }
        fab3.setOnClickListener {
            anim()
            val intent = Intent(this, TimeLineActivity::class.java)
            startActivity(intent)
        }


        appbarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (verticalOffset < -linearlayout_size.height) {
                    fab.show()
                }
                else {
                    fab.hide()
                    if(isFabOpen == true){
                        anim()
                    }
                }

            }
        })
    }

    fun anim() {

        if (isFabOpen) {
            fab.startAnimation(fab_rotate_close)
            fab1.startAnimation(fab_close3)
            fab2.startAnimation(fab_close2)
            fab3.startAnimation(fab_close1)
            fab1.isClickable = false
            fab2.isClickable = false
            fab3.isClickable = false
            isFabOpen = false
        } else {
            fab.startAnimation(fab_rotate)
            fab1.startAnimation(fab_open1)
            fab2.startAnimation(fab_open2)
            fab3.startAnimation(fab_open3)
            fab1.isClickable = true
            fab2.isClickable = true
            fab3.isClickable = true
            isFabOpen = true
        }
    }


    ///////////////Navigation Drawer Setting/////////////////
    fun initNav() {

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        license_btn.setOnClickListener {
            licenseDialog()
        }



        if (SaveSharedPreference.getUserID(this) != "") {
            nav_view.removeHeaderView(nav_view.getHeaderView(0))
            nav_view.inflateHeaderView(R.layout.nav_header_main_login)
        } else {
            nav_view.removeHeaderView(nav_view.getHeaderView(0))
            nav_view.inflateHeaderView(R.layout.nav_header_main)
        }
        val navView: NavigationView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val header = navigationView.getHeaderView(0)

        //nav_header_main_login
        if (SaveSharedPreference.getUserID(this) != "") {
            //회원 정보 출력
            var nameText = header.findViewById<TextView>(R.id.tvId)
            nameText.text = SaveSharedPreference.getUserName(this)

            //로그 아웃
            var logoutBtn = header.findViewById<Button>(R.id.login_status_on)
            logoutBtn.setOnClickListener {

                SaveSharedPreference.clearUserID(this)

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                //initNav()
                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()

            }
            //마이 페이지
            var myPageBtn = header.findViewById<Button>(R.id.myPage_on)
            myPageBtn.setOnClickListener {
                val intent = Intent(this, MyPageActivity::class.java)
                startActivity(intent)
            }

            //FAQ

            //일정 관리 tv_plan_count

        } else {
            //nav_header_main
            //로그인  login_status
            var loginBtn = header.findViewById<Button>(R.id.login_status)
            loginBtn.setOnClickListener {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }

            //FAQ faq

            //마이 페이지 myPage
            var myPageBtn = header.findViewById<Button>(R.id.myPage)
            myPageBtn.setOnClickListener {
                if (!loginCheck())
                    loginDialog()
            }
            //회원가입 텍스트  tv_signUp
            var signUpBtn = header.findViewById<TextView>(R.id.tv_signUp)
            signUpBtn.setOnClickListener {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }

        }


    }

    fun licenseDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("라이센스 정보")
        builder.setMessage("내용")

        builder.setNeutralButton("닫기") { _, _ ->

        }
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }

    fun loginCheck(): Boolean {
        return (SaveSharedPreference.getUserID(this) != "")
    }

    fun loginDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("로그인 하시겠습니까?")
            .setCancelable(false)
            .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, id ->
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            })
            .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = builder.create()
        alert.setTitle("로그인이 필요한 서비스 입니다.")
        alert.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }


    ///////////////// Toolbar Searching////////////////////
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }


    ////////////////// Button //////////////////
    fun initBtn() {
        toolbar.navigationIcon = getDrawable(R.drawable.hamburger)


        // 날씨
        weather.setOnClickListener {
            startActivity(w_intent)
        }

        /////////backpress//////////
        backPressCloseHandler = BackPressCloseHandler(this)




        MkPlanBtn.setOnClickListener {
            val intent = Intent(this, MakePlanActivity::class.java)
            startActivity(intent)

        }

        HistoryBtn.setOnClickListener {
            if (SaveSharedPreference.getUserID(this) != "") {
                val intent = Intent(this, MyPageActivity::class.java)
                startActivity(intent)
            } else {
                if (!loginCheck())
                    loginDialog()
            }
        }
        TimeLineBtn.setOnClickListener {
            val intent = Intent(this, TimeLineActivity::class.java)
            startActivity(intent)
        }

    }

    ////////////////// Recycler View //////////////////
    private val test = ArrayList<HomeItem>()
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: HomeAdapter? = null

    fun initTest() {

        for (i in 0..10) {
            test.add(HomeItem("This is Title" + i.toString(), "This is Content" + i.toString()))
        }
    }

    fun initRecyclerView() {
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView_addPlace.layoutManager = layoutManager
        val listener = object : HomeAdapter.RecyclerViewAdapterEventListener {
            override fun onClick(view: View) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                val intent = Intent(applicationContext, ReviewActivity::class.java)
                startActivity(intent)
            }
        }

        adapter = HomeAdapter(this, listener, test)
        recyclerView_addPlace.adapter = adapter
        recyclerView_addPlace.addItemDecoration(DividerItemDecoration(this, 1))
    }


    /////////////// Permission Check ///////////////

    val REQ_CAMERA = 1000
    val REQ_READ_GALLERY = 1001
    val REQ_WRITE_GALLERY = 1002

    fun initPermission() {
        if (!checkAppPermission(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        ) {
        } else {
//            Toast . makeText ( getApplicationContext (),
//                "권한이 승인되었습니다." , Toast . LENGTH_SHORT ). show ();
        }
        if (!checkAppPermission(
                arrayOf(
                    Manifest.permission.CAMERA
                )
            )
        ) {
        } else {
//            Toast . makeText ( getApplicationContext (),
//                "권한이 승인되었습니다." , Toast . LENGTH_SHORT ). show ();
        }

        if (!checkAppPermission(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        ) {
        } else {
//            Toast . makeText ( getApplicationContext (),
//                "권한이 승인되었습니다." , Toast . LENGTH_SHORT ). show ();
        }
    }

    fun checkAppPermission(requestPermission: Array<String>): Boolean {
        val requestResult = BooleanArray(requestPermission.size)
        for (i in requestResult.indices) {
            requestResult[i] = ContextCompat.checkSelfPermission(
                this,
                requestPermission[i]
            ) == PackageManager.PERMISSION_GRANTED
            if (!requestResult[i]) {
                return false
            }
        }
        return true
    } // checkAppPermission

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            REQ_CAMERA -> if (checkAppPermission(permissions)) { //퍼미션 동의했을 때 할 일
                Toast.makeText(this, "권한이 승인됨", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한이 거절됨", Toast.LENGTH_SHORT).show()
                finish()
            }
            REQ_WRITE_GALLERY -> if (checkAppPermission(permissions)) { //퍼미션 동의했을 때 할 일
                Toast.makeText(this, "권한이 승인됨", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한이 거절됨", Toast.LENGTH_SHORT).show()
                finish()
            }

            REQ_READ_GALLERY -> if (checkAppPermission(permissions)) { //퍼미션 동의했을 때 할 일
                Toast.makeText(this, "권한이 승인됨", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한이 거절됨", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    } // onRequestPermissionsResult


    ///////////Back 버튼///////////////
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            //super.onBackPressed()
            backPressCloseHandler.onBackPressed()
        }
    }


    ///////////////Activity LifeCycle//////////////////
    override fun onDestroy() {
        super.onDestroy()
        if (!SaveSharedPreference.getAutoFlag(this)) {
            //자동 로그인 체크 안 되어있을 때
            if (SaveSharedPreference.getUserID(this) != "")
                SaveSharedPreference.clearUserID(this)
        }
    }


}
