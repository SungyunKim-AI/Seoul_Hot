package com.inseoul

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.inseoul.api_manager.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class TestActivity :
    AppCompatActivity()
{

    val apiKey = "V3TPLc8KikVyK235xNyOorabnl1eDnekQJSTWtpl4eQXyE3MWxAUjlZXJo6PIxrmLZGlixdOVWTSs8PmCfb4nQ%3D%3D"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        test3()
    }
    fun test3(){
        val KEY = "48734b694668736f36356d6a676775"
        val MobileOS = "AND"
        val MobileApp = "InSeuol"
        val contentType = 12
        val areaCode = 1
        val _type = "json"
        val keyword = "잠실"
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
            .client(createOkHttpClient())
            .baseUrl("http://api.visitkorea.or.kr/openapi/service/rest/KorService/")
            .build()
            .create(RetrofitService::class.java)
            .test2(keyword, contentType, areaCode, MobileOS, MobileApp, _type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.v("tlqkf",it.toString())
                var str = ""
                for(i in 0..it.response.body.items.item.size - 1){

                    str += it.response.body.items.item[i].title
                    str += "\n"

                }
                test_text.text = str

            },{
                Log.v("Fail","")
            })
    }
    fun createOkHttpClient(): OkHttpClient{
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(interceptor)
        return builder.build()
    }

}
