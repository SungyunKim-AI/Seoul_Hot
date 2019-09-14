package com.inseoul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.inseoul.api_manager.RetrofitService
import com.inseoul.data_model.ForecastModel_ShortTerm
import com.inseoul.forecast.Forecast_shortTermItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register_review_page.*
import kotlinx.android.synthetic.main.activity_search_detail.*
import kotlinx.android.synthetic.main.activity_test.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
//        ForecastAPI_default()
//        ForecastAPI_weather()
//        ForecastAPI_temperature()

        ForecastAPI_ShortTerm()
//        ForecastAPI4()

    }
    lateinit var model_shortTerm: ArrayList<Forecast_shortTermItem>
    fun ForecastAPI_ShortTerm(){

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

        base_data = s_format
        base_time = s_time.toString() + s_minute.toString()

        model_shortTerm = ArrayList()
        model_shortTerm.add(Forecast_shortTermItem(s_month, s_day, null, null, null,null,null))
        model_shortTerm.add(Forecast_shortTermItem(s_month, s_day, null, null, null,null,null))
        model_shortTerm.add(Forecast_shortTermItem(s_month, s_day, null, null, null,null,null))
        model_shortTerm.add(Forecast_shortTermItem(s_month, s_day, null, null, null,null,null))

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .baseUrl("http://newsky2.kma.go.kr/service/")
            .build()
            .create(RetrofitService::class.java)
            .ShortTermWeather(base_data, base_time, nx, ny, numOfRows, pageNo, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("shortTerm", it.toString())
                var str = ""
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


            },{
                Log.v("Fail","")
            })
    }

    fun ForecastAPI_default(){
        val regId = "11B10101"
        val _type = "json"
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .baseUrl("http://newsky2.kma.go.kr/service/")
            .build()
            .create(RetrofitService::class.java)
            .weather_default(regId, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                var str = ""

                for(i in 0..it.response.body.items.item.size - 1){
                    val data = it.response.body.items.item[i]
                    str += data.toString()
                    str += "\n"
//                    Log.v("tlqkf",data.firstimage2.toString())
                }

                test.text = str
            },{
                Log.v("Fail","")
            })
    }
/*
    fun ForecastAPI2(){
        val base_date = 20190911
        val base_time = "0500"
        val nx = 55
        val ny = 127
        val numOfRows = 10
        val pageNo = 1
        val type = "json"
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .baseUrl("http://newsky2.kma.go.kr/service/")
            .build()
            .create(RetrofitService::class.java)
            .Test2(base_date, base_time, nx, ny, numOfRows, pageNo, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                var str = ""
                Log.v("hsoh0aa306", it.response.body.items.item.size.toString())
                for(i in 0..it.response.body.items.item.size - 1){
                    val data = it.response.body.items.item[i]
                    str += data.fcstDate.toString()
                    str += " "
                    str += data.fcstTime
                    str += " "


                    str += data.category
                    str += " "
                    str += data.fcstValue.toString()
                    str += "\n"
//                    Log.v("tlqkf",data.firstimage2.toString())
                }

                test.text = str
            },{
                Log.v("Fail","")
            })
    }
    */
    fun ForecastAPI_weather(){
        val regId = "11B00000"
        val tmFc = 201909091800
        val _type = "json"
        val numOfRows = 1
        val pageNo = 1

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .baseUrl("http://newsky2.kma.go.kr/service/")
            .build()
            .create(RetrofitService::class.java)
            .MiddleWeather(regId, tmFc, numOfRows, pageNo, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                var str = ""

                val data = it.response.body.items.item
                str += data.toString()

                test.text = str
            },{
                Log.v("Fail","")
            })
    }
    fun ForecastAPI_temperature(){
        val regId = "11B10101"
        val tmFc = 201909100600
        val _type = "json"
        val numOfRows = 1
        val pageNo = 1

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .baseUrl("http://newsky2.kma.go.kr/service/")
            .build()
            .create(RetrofitService::class.java)
            .MiddleTemperature(regId, tmFc, numOfRows, pageNo, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                var str = ""
                val data = it.response.body.items.item
                str += data.toString()
                test.text = str
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
}
