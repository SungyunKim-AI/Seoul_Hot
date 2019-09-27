package com.inseoul.api_manager

import com.inseoul.data_model.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

// ContentType
// 관광지 12

// 문화시설 14
// 행사/공연/축제 15
// 레포츠 28

// 숙박 32

// 음식점 39

// MoblieOS AND
// MoblieApp InSeoul
// areaCode

interface RetrofitService {

    // 검색
    @GET("searchKeyword?ServiceKey=V3TPLc8KikVyK235xNyOorabnl1eDnekQJSTWtpl4eQXyE3MWxAUjlZXJo6PIxrmLZGlixdOVWTSs8PmCfb4nQ%3D%3D&listYN=Y")
    fun searchKeyWord(
//        @Query("ServiceKey") key:String,
        @Query("keyword") keyword:String,
//        @Query("contentType") contentType:Int,
        @Query("areaCode") areaCode:Int,
        @Query("MobileOS") OS:String,
        @Query("MobileApp") App:String,
        @Query("_type") type:String

    ): Observable<SearchKeyWordModel>

    // 이미지
    @GET("detailImage?ServiceKey=V3TPLc8KikVyK235xNyOorabnl1eDnekQJSTWtpl4eQXyE3MWxAUjlZXJo6PIxrmLZGlixdOVWTSs8PmCfb4nQ%3D%3D&imageYN=Y")
    fun searchDetailImage(
        @Query("contentId") contentId:Int,
        @Query("MobileOS") OS:String,
        @Query("MobileApp") App:String,
        @Query("_type") type:String
    ): Observable<DetailImageModel>

    // 오늘 날씨
    @GET("VilageFrcstDspthDocInfoService/WidOverlandForecast?ServiceKey=OuWYx%2FiEKH%2F4M%2FfNA1UkjXhgmbyzoZ%2Br6qd2PO9Xl5YrwSsRh9aJy28X0LbJz4ztErDoP5p1vEXgekEyLJS57g%3D%3D")
    fun weather_default(
        @Query("regId") regId:String,
//        @Query("ServiceKey") ServiceKey:String,
        @Query("_type") _type:String

    ): Observable<ForecastModel_default>


    @GET("SecndSrtpdFrcstInfoService2/ForecastTimeData?ServiceKey=OuWYx%2FiEKH%2F4M%2FfNA1UkjXhgmbyzoZ%2Br6qd2PO9Xl5YrwSsRh9aJy28X0LbJz4ztErDoP5p1vEXgekEyLJS57g%3D%3D")
    fun ShortTermWeather(
        @Query("base_date") base_date:Int,
        @Query("base_time") base_time:String,
        @Query("nx") nx:Int,
        @Query("ny") ny:Int,
        @Query("numOfRows") numOfRows:Int,
        @Query("pageNo") pageNo:Int,

        @Query("_type") _type:String

    ): Observable<ForecastModel_ShortTerm>

    // 중기 예보 (날씨)
    @GET("MiddleFrcstInfoService/getMiddleLandWeather?ServiceKey=OuWYx%2FiEKH%2F4M%2FfNA1UkjXhgmbyzoZ%2Br6qd2PO9Xl5YrwSsRh9aJy28X0LbJz4ztErDoP5p1vEXgekEyLJS57g%3D%3D")
    fun MiddleWeather(
        @Query("regId") regId:String,
        @Query("tmFc") tmFc:Long,
        @Query("numOfRows") numOfRows:Int,
        @Query("pageNo") pageNo:Int,

        @Query("_type") _type:String

    ): Observable<ForecastModel_MiddleWeather>
    // 중기 예보 (온도)
    @GET("MiddleFrcstInfoService/getMiddleTemperature?ServiceKey=OuWYx%2FiEKH%2F4M%2FfNA1UkjXhgmbyzoZ%2Br6qd2PO9Xl5YrwSsRh9aJy28X0LbJz4ztErDoP5p1vEXgekEyLJS57g%3D%3D")
    fun MiddleTemperature(
        @Query("regId") regId:String,
        @Query("tmFc") tmFc:Long,
        @Query("numOfRows") numOfRows:Int,
        @Query("pageNo") pageNo:Int,

        @Query("_type") _type:String

    ): Observable<ForecastModel_MiddleTemperature>
    // 리뷰 받아오기

    @GET("ShowPlan.php")
    fun getTimeLine(): Observable<TimeLineModel>

    @GET("ShowReview.php")
    fun getReview(
        @Query("IDNUM") idnum:String
    ): Observable<ReviewDataModel>
}
