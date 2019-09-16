package com.inseoul.forecast

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import com.inseoul.api_manager.RetrofitService
import com.inseoul.data_model.ForecastModel_MiddleTemperature
import com.inseoul.data_model.ForecastModel_MiddleWeather
import com.inseoul.data_model.ForecastModel_ShortTerm
import com.inseoul.data_model.ForecastModel_default
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_forecast.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ForecastActivity : AppCompatActivity() {

    lateinit var model_default:ForecastModel_default.items
    lateinit var model_weather:ForecastModel_MiddleWeather.item
    lateinit var model_temp:ForecastModel_MiddleTemperature.item

    var now = System.currentTimeMillis()
    lateinit var date:Date
    lateinit var format:String
    lateinit var year:String
    lateinit var month:String
    lateinit var day:String
    lateinit var time:String
    lateinit var yDate:Date
    lateinit var yFormat:String

    lateinit var r_date:String
    lateinit var tDate:String
    lateinit var ttDate:String
    lateinit var retrofit:RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        init()
        initToolbar()
        ForecastAPI_default()
        var tmFc:Long = 0
        if(time.toInt() >= 18) {
            tmFc = (format + "1800").toLong()
        } else if(time.toInt() in 6..17){
            tmFc = (format + "0600").toLong()
        } else {
            tmFc = (yFormat + "1800").toLong()
        }
        ForecastAPI_weather(tmFc)
        ForecastAPI_temperature(tmFc)

        Log.e("itemList", itemList.toString())
    }

    fun init(){
        retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .baseUrl("http://newsky2.kma.go.kr/service/")
            .build()
            .create(RetrofitService::class.java)
        itemList = ArrayList()

        now = System.currentTimeMillis()
        date = Date(now)
        format = SimpleDateFormat("yyyyMMdd").format(date)
        year = SimpleDateFormat("MM").format(date)
        month = SimpleDateFormat("MM").format(date)
        day = SimpleDateFormat("dd").format(date)
        time = SimpleDateFormat("HH").format(date)

        // 오늘 날짜
        val r_calendar = GregorianCalendar()
        r_calendar.add(Calendar.DATE, 0)
        r_date = SimpleDateFormat("MM/dd").format(r_calendar.getTime())

        // 어제 날짜
        val calendar = GregorianCalendar()
        calendar.add(Calendar.DATE, - 1)
        yFormat = SimpleDateFormat("yyyyMMdd").format(calendar.getTime())

        // 내일 날짜
        val t_calendar = GregorianCalendar()
        t_calendar.add(Calendar.DATE, + 1)
        tDate = SimpleDateFormat("MM/dd").format(t_calendar.getTime())
        // 모레 날짜
        val tt_calendar = GregorianCalendar()
        tt_calendar.add(Calendar.DATE, + 2)
        ttDate = SimpleDateFormat("MM/dd").format(tt_calendar.getTime())


        // 날씨 정보 재활용
        val data = getIntent().getParcelableExtra<Forecast_shortTermItem>("today_weather")

        Log.e("data_weather", data.toString())
        when(data.SKY){
            1->{
                now_weather_icon.setImageResource(R.drawable.w_sun)
            }
            3, 4->{
                when(data.PTY){
                    0->{
                        now_weather_icon.setImageResource(R.drawable.w_cloud)
                    }
                    1,2,4->{
                        now_weather_icon.setImageResource(R.drawable.w_rain)
                    }
                    3->{
                        now_weather_icon.setImageResource(R.drawable.w_snow)
                    }
                }
            }
        }
        now_temp.text = data.T1H.toString() + "º"
        now_date.text = month + "월" + day + "일"
//        Log.d("hsoh0306", format + "\n" + yFormat)
    }
    lateinit var itemList:ArrayList<Forecast_item>
    fun initData(){

        var t3 = day.toInt() + 3
        itemList.add(Forecast_item("$month/$t3", model_temp.taMax3, model_temp.taMin3, model_weather.wf3Am, model_weather.wf3Pm))
        var t4 = day.toInt() + 4
        itemList.add(Forecast_item("$month/$t4", model_temp.taMax4, model_temp.taMin4, model_weather.wf4Am, model_weather.wf4Pm))
        var t5 = day.toInt() + 5
        itemList.add(Forecast_item("$month/$t5", model_temp.taMax5, model_temp.taMin5, model_weather.wf5Am, model_weather.wf5Pm))
        var t6 = day.toInt() + 6
        itemList.add(Forecast_item("$month/$t6", model_temp.taMax6, model_temp.taMin6, model_weather.wf6Am, model_weather.wf6Pm))
        var t7 = day.toInt() + 7
        itemList.add(Forecast_item("$month/$t7", model_temp.taMax7, model_temp.taMin7, model_weather.wf7Am, model_weather.wf7Pm))


    }

    lateinit var adapter:Forecast_adapter
    fun initRecyclerView(){
        var layoutManager: RecyclerView.LayoutManager? = null
        layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapter = Forecast_adapter(this, itemList)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = adapter
    }

    fun ForecastAPI_default(){
        val regId = "11B10101"      // 서울 지역 코드
        val _type = "json"
        retrofit
            .weather_default(regId, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                model_default = it.response.body.items

                tt_date.text = tDate
                ttt_date.text = ttDate

//        val t_time = SimpleDateFormat("HH").format(model_default.item[0].announceTime)
                val t_time = time
                Log.v("hsoh0306_time", model_default.item[0].toString())
                if(t_time.toInt() >= 17 || t_time.toInt() < 5){
                    tt_temp.text = model_default.item[1].ta.toString() + "º" + "/" + model_default.item[2].ta.toString() + "º"

                    ttt_temp.text = model_default.item[3].ta.toString() + "º" + "/" + model_default.item[4].ta.toString() + "º"

                    when(model_default.item[4].wfCd){
                        "DB01"->{
                            ttt_weather_icon.setImageResource(R.drawable.w_sun)
                        }
                        "DB03"->{
                            ttt_weather_icon.setImageResource(R.drawable.w_cloud)
                        }
                        "DB04"->{
                            ttt_weather_icon.setImageResource(R.drawable.w_cloud)
                        }
                    }
                    when(model_default.item[4].rnYn){
                        1,4->{
                            ttt_weather_icon.setImageResource(R.drawable.w_rain)
                        }
                        2,3->{
                            ttt_weather_icon.setImageResource(R.drawable.w_snow)
                        }
                    }
                    when(model_default.item[2].wfCd){
                        "DB01"->{
                            tt_weather_icon.setImageResource(R.drawable.w_sun)
                        }
                        "DB03"->{
                            tt_weather_icon.setImageResource(R.drawable.w_cloud)
                        }
                        "DB04"->{
                            tt_weather_icon.setImageResource(R.drawable.w_cloud)
                        }
                    }
                    when(model_default.item[2].rnYn){
                        1,4->{
                            tt_weather_icon.setImageResource(R.drawable.w_rain)
                        }
                        2,3->{
                            tt_weather_icon.setImageResource(R.drawable.w_snow)
                        }
                    }


                } else if(t_time.toInt() in 11..16){
                    tt_temp.text = model_default.item[1].ta.toString() + "º" + "/" + model_default.item[2].ta.toString() + "º"

                    ttt_temp.text = model_default.item[3].ta.toString() + "º" + "/" + model_default.item[4].ta.toString() + "º"

                    when(model_default.item[5].wfCd){
                        "DB01"->{
                            ttt_weather_icon.setImageResource(R.drawable.w_sun)
                        }
                        "DB03"->{
                            ttt_weather_icon.setImageResource(R.drawable.w_cloud)
                        }
                        "DB04"->{
                            ttt_weather_icon.setImageResource(R.drawable.w_cloud)
                        }
                    }
                    when(model_default.item[5].rnYn){
                        1,4->{
                            ttt_weather_icon.setImageResource(R.drawable.w_rain)
                        }
                        2,3->{
                            ttt_weather_icon.setImageResource(R.drawable.w_snow)
                        }
                    }
                    when(model_default.item[3].wfCd){
                        "DB01"->{
                            tt_weather_icon.setImageResource(R.drawable.w_sun)
                        }
                        "DB03"->{
                            tt_weather_icon.setImageResource(R.drawable.w_sun_cloud)
                        }
                        "DB04"->{
                            tt_weather_icon.setImageResource(R.drawable.w_cloud)
                        }
                    }
                    when(model_default.item[3].rnYn){
                        1,4->{
                            tt_weather_icon.setImageResource(R.drawable.w_rain)
                        }
                        2,3->{
                            tt_weather_icon.setImageResource(R.drawable.w_snow)
                        }
                    }

                } else if(t_time.toInt() in 5..10){
                    tt_temp.text = model_default.item[2].ta.toString() + "º" + "/" + model_default.item[3].ta.toString() + "º"

                    ttt_temp.text = model_default.item[4].ta.toString() + "º" + "/" + model_default.item[5].ta.toString() + "º"

                    when(model_default.item[4].wfCd){
                        "DB01"->{
                            ttt_weather_icon.setImageResource(R.drawable.w_sun)
                        }
                        "DB03"->{
                            ttt_weather_icon.setImageResource(R.drawable.w_sun_cloud)
                        }
                        "DB04"->{
                            ttt_weather_icon.setImageResource(R.drawable.w_cloud)
                        }
                    }
                    when(model_default.item[4].rnYn){
                        1,4->{
                            ttt_weather_icon.setImageResource(R.drawable.w_rain)
                        }
                        2,3->{
                            ttt_weather_icon.setImageResource(R.drawable.w_snow)
                        }
                    }
                    when(model_default.item[2].wfCd){
                        "DB01"->{
                            tt_weather_icon.setImageResource(R.drawable.w_sun)
                        }
                        "DB03"->{
                            tt_weather_icon.setImageResource(R.drawable.w_sun_cloud)
                        }
                        "DB04"->{
                            tt_weather_icon.setImageResource(R.drawable.w_cloud)
                        }
                    }
                    when(model_default.item[2].rnYn){
                        1,4->{
                            tt_weather_icon.setImageResource(R.drawable.w_rain)
                        }
                        2,3->{
                            tt_weather_icon.setImageResource(R.drawable.w_snow)
                        }
                    }
                }
            },{
                Log.v("Fail","")
            })
    }

    fun ForecastAPI_weather(tmFc:Long){
        val regId = "11B00000"  // 서울 지역 코드
        val _type = "json"
        val numOfRows = 1
        val pageNo = 1

        retrofit
            .MiddleWeather(regId, tmFc, numOfRows, pageNo, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                model_weather = it.response.body.items.item
            },{
                Log.v("Fail","")
            })
    }

    lateinit var model_shortTerm: ArrayList<ForecastModel_ShortTerm.item>
    fun ForecastAPI_ShortTerm(){

        model_shortTerm = ArrayList()
        val regId = "11B00000"  // 서울 지역 코드
        val _type = "json"
        val numOfRows = 10
        val pageNo = 1
        var base_data = 1
        var base_time ="a0"
        val nx = 1
        val ny = 1


        val s_now = System.currentTimeMillis()
        val s_date = Date(s_now)
        val s_format = SimpleDateFormat("yyyyMMdd").format(s_date).toInt()
        val s_time = SimpleDateFormat("HH").format(s_date).toInt()
        val s_minute = SimpleDateFormat("mm").format(s_date).toInt()

        if(s_time >= 6) {
            if(s_minute < 45){
                base_time = (s_time - 1).toString() + "30"
            }
        }


        retrofit
            .ShortTermWeather(base_data, base_time, nx, ny, numOfRows, pageNo, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("shortTerm", it.toString())
                for(i in 0 until it.response.body.items.item.size){
                    model_shortTerm.add(it.response.body.items.item[i])
                }
            },{
                Log.v("Fail","")
            })
    }
    fun ForecastAPI_temperature(tmFc:Long){
        val regId = "11B10101"      // 서울 지역 코드
        val _type = "json"
        val numOfRows = 1
        val pageNo = 1

        retrofit
            .MiddleTemperature(regId, tmFc, numOfRows, pageNo, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                model_temp = it.response.body.items.item

                initData()
                initRecyclerView()

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

    fun initToolbar(){
        //toolbar 커스텀 코드
        val mtoolbar = findViewById(R.id.toolbar_forecast) as Toolbar
        setSupportActionBar(mtoolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


        ///////////////////////////////////////////////////////

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

    class ProgressDialog {
        companion object {
            fun progressDialog(context: Context): Dialog {
                val dialog = Dialog(context)
                val inflate = LayoutInflater.from(context).inflate(R.layout.progress, null)
                dialog.setContentView(inflate)
                dialog.setCancelable(false)
                dialog.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                )
                return dialog
            }
        }
    }
}
