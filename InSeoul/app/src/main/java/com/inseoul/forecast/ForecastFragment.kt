package com.inseoul.forecast


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ForecastFragment : Fragment() {


    lateinit var model_default: ForecastModel_default.items
    lateinit var model_weather: ForecastModel_MiddleWeather.item
    lateinit var model_temp: ForecastModel_MiddleTemperature.item

    var now = System.currentTimeMillis()
    lateinit var date: Date
    lateinit var format:String
    lateinit var year:String
    lateinit var month:String
    lateinit var day:String
    lateinit var time:String
    lateinit var yDate: Date
    lateinit var yFormat:String

    lateinit var r_date:String
    lateinit var tDate:String
    lateinit var ttDate:String
    lateinit var retrofit: RetrofitService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        ForecastAPI_ShortTerm()
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
//        val data = getIntent().getParcelableExtra<Forecast_shortTermItem>("today_weather")


//        Log.d("hsoh0306", format + "\n" + yFormat)
    }
    lateinit var itemList:ArrayList<Forecast_item>
    fun initData(){

        val calendar = GregorianCalendar()
        calendar.add(Calendar.DATE, + 3)
        var t3 = SimpleDateFormat("MM/dd").format(calendar.getTime())

        itemList.add(Forecast_item(t3, model_temp.taMax3, model_temp.taMin3, model_weather.wf3Am, model_weather.wf3Pm))

        calendar.add(Calendar.DATE, + 1)
        var t4 = SimpleDateFormat("MM/dd").format(calendar.getTime())
        itemList.add(Forecast_item(t4, model_temp.taMax4, model_temp.taMin4, model_weather.wf4Am, model_weather.wf4Pm))

        calendar.add(Calendar.DATE, + 1)
        var t5 = SimpleDateFormat("MM/dd").format(calendar.getTime())
        itemList.add(Forecast_item(t5, model_temp.taMax5, model_temp.taMin5, model_weather.wf5Am, model_weather.wf5Pm))

        calendar.add(Calendar.DATE, + 1)
        var t6 = SimpleDateFormat("MM/dd").format(calendar.getTime())
        itemList.add(Forecast_item(t6, model_temp.taMax6, model_temp.taMin6, model_weather.wf6Am, model_weather.wf6Pm))

        calendar.add(Calendar.DATE, + 1)
        var t7 = SimpleDateFormat("MM/dd").format(calendar.getTime())
        itemList.add(Forecast_item(t7, model_temp.taMax7, model_temp.taMin7, model_weather.wf7Am, model_weather.wf7Pm))


    }

    lateinit var adapter:Forecast_adapter
    fun initRecyclerView(){
        var layoutManager: RecyclerView.LayoutManager? = null
        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        adapter = Forecast_adapter(context!!, itemList)
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


                var index = 0
                for(i in 0..3){
                    if(model_shortTerm[i].SKY != null && model_shortTerm[i].PTY != null){
//                        w_intent.putExtra("today_weather", model_shortTerm[i])
                        index = i
                        break;
                    }
                }
                when(model_shortTerm[index].SKY){
                    1->{
                        now_weather_icon.setImageResource(R.drawable.w_sun)
//                                home_weather_icon.setImageResource(R.drawable.w_sun)
//                                home_status.text = "맑음"
                    }
                    3, 4->{
                        when(model_shortTerm[index].PTY){
                            0->{
                                now_weather_icon.setImageResource(R.drawable.w_cloud)

//                                        home_weather_icon.setImageResource(R.drawable.w_cloud)
//                                        home_status.text = "흐림"
                            }
                            1,2,4->{
                                now_weather_icon.setImageResource(R.drawable.w_cloud)

//                                        home_weather_icon.setImageResource(R.drawable.w_rain)
//                                        home_status.text = "비"
                            }
                            3->{
                                now_weather_icon.setImageResource(R.drawable.w_snow)
//                                        home_weather_icon.setImageResource(R.drawable.w_snow)
//                                        home_status.text = "눈"
                            }
                        }
                    }
                }
//                        home_date.text = model_shortTerm[i].month.toString() + "/" + model_shortTerm[i].day.toString()
                if(model_shortTerm[index].T1H == null){
//                            home_temp.text = "점검중.."
                    now_temp.text = "점검중.."

                } else {
                    now_temp.text = model_shortTerm[index].T1H.toString() + "º"
                    now_date.text = month + "월" + day + "일"

//                            home_temp.text = model_shortTerm[i].T1H.toString() + "º"
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
//                when(data.SKY){
//                    1->{
//                        now_weather_icon.setImageResource(R.drawable.w_sun)
//                    }
//                    3, 4->{
//                        when(data.PTY){
//                            0->{
//                                now_weather_icon.setImageResource(R.drawable.w_cloud)
//                            }
//                            1,2,4->{
//                                now_weather_icon.setImageResource(R.drawable.w_rain)
//                            }
//                            3->{
//                                now_weather_icon.setImageResource(R.drawable.w_snow)
//                            }
//                        }
//                    }
//                }
//                now_temp.text = data.T1H.toString() + "º"
//                now_date.text = month + "월" + day + "일"

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
