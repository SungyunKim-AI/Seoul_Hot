package com.inseoul

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inseoul.forecast.ForecastFragment
import com.inseoul.forecast.Forecast_shortTermItem
import com.inseoul.home.HomeFragment
import com.inseoul.make_plan.MakePlanActivity
import com.inseoul.manage_member.SaveSharedPreference
import com.inseoul.manage_member.SignInActivity
import com.inseoul.my_page.MyPageFragment
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.collections.ArrayList

class MainActivity :
    AppCompatActivity()
{


    val key = "4d4956476768736f3131397547724879" // 서울시 데이터 API Key

    lateinit var backPressCloseHandler: BackPressCloseHandler

    lateinit var model_shortTerm: ArrayList<Forecast_shortTermItem>

    fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    lateinit var w_intent:Intent
    /*
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
        if(s_minute >= 45){
            base_time = (s_time).toString() + "30"
        } else if (s_minute in 30..45) {
            base_time = (s_time).toString() + "00"
        } else {
            base_time = (s_time - 1).toString() + "30"
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
//            .client(createOkHttpClient())
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
                        if(model_shortTerm[i].T1H == null){
                            home_temp.text = "점검중.."
                        } else {
                            home_temp.text = model_shortTerm[i].T1H.toString() + "º"
                        }
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
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        ForecastAPI_ShortTerm()

        /////////////////////////////////////////////////////
        initBackHandler()
        initPermission()
        initBtn()
        // Bottom Navigation
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        attachHome()
    }

    //Back버튼 두번 눌러 종료하기
    fun initBackHandler(){
        backPressCloseHandler = BackPressCloseHandler(this)
    }
    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed()
    }

    fun initBtn(){
        floating_button.setOnClickListener {
            if(loginCheck()){
                val intent = Intent(this, MakePlanActivity::class.java)
                startActivity(intent)
            }else{
                loginDialog()
            }

        }
    }


    ///////////////Navigation Drawer Setting/////////////////


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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }


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

    ///////////////Activity LifeCycle//////////////////
    override fun onDestroy() {
        super.onDestroy()
        if (!SaveSharedPreference.getAutoFlag(this)) {
            //자동 로그인 체크 안 되어있을 때
            if (SaveSharedPreference.getUserID(this) != "")
                SaveSharedPreference.clearUserID(this)
        }
    }


    ///////////////////////////////////////////////////////////////////////////////
    // Bottom Navigation
    ///////////////////////////////////////////////////////////////////////////////
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
//                textMessage.setText(R.string.title_home)
                attachHome()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
//                textMessage.setText(R.string.title_dashboard)
//                attachMakePlan()
                attachForecast()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
//                textMessage.setText(R.string.title_notifications)
                if(loginCheck()){
                    attachMyPage()
                }else{
                    loginDialog()
                }

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    ///////////////// Toolbar Searching////////////////////
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.search -> {
//                val intent = Intent(this, SearchActivity::class.java)
//                startActivity(intent)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//        return true
//    }
    ///////////////////////////////////////////////////////////////////////////////
    // Fragment
    ///////////////////////////////////////////////////////////////////////////////

    fun attachHome(){
        val frag = supportFragmentManager.findFragmentByTag("home")
        val tagStr = frag?.tag.toString()
        if(tagStr == "home"){

        } else{
            val homeTransaction = supportFragmentManager.beginTransaction()
            val homeFrag = HomeFragment()
            homeTransaction.replace(R.id.frame, homeFrag)
            val clear = supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            homeTransaction.commit()
        }
    }

    fun attachForecast(){
        val frag = supportFragmentManager.findFragmentByTag("forecast")
        val tagStr = frag?.tag.toString()
        if(tagStr == "forecast"){

        } else{
            val ForecastTransaction = supportFragmentManager.beginTransaction()
            val forecast = ForecastFragment()
            ForecastTransaction.replace(R.id.frame, forecast)
            val clear = supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            ForecastTransaction.commit()

        }

    }

    lateinit var MyPageFrag: Fragment
    fun attachMyPage(){
        val frag = supportFragmentManager.findFragmentByTag("mypage")
        val tagStr = frag?.tag.toString()
        if(tagStr == "mypage"){

        } else{
            val MyPageTransaction = supportFragmentManager.beginTransaction()
           MyPageFrag = MyPageFragment()
            MyPageTransaction.replace(R.id.frame, MyPageFrag)
            val clear = supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            MyPageTransaction.commit()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("tlqkf", "rotlqkf")
        attachMyPage()
        MyPageFrag.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
    }
}
