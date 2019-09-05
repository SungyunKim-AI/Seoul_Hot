package com.inseoul.api_manager

import com.inseoul.data_model.SearchKeyWordModel
import com.inseoul.testdata
import io.reactivex.Observable
import okio.Utf8
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
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
    @GET("searchKeyword?ServiceKey=V3TPLc8KikVyK235xNyOorabnl1eDnekQJSTWtpl4eQXyE3MWxAUjlZXJo6PIxrmLZGlixdOVWTSs8PmCfb4nQ%3D%3D&listYN=Y")
    fun test2(
//        @Query("ServiceKey") key:String,
        @Query("keyword") keyword:String,
        @Query("contentType") contentType:Int,
        @Query("areaCode") areaCode:Int,
        @Query("MobileOS") OS:String,
        @Query("MobileApp") App:String,
        @Query("_type") type:String

    ): Observable<testdata>

    @GET("searchKeyword?ServiceKey=V3TPLc8KikVyK235xNyOorabnl1eDnekQJSTWtpl4eQXyE3MWxAUjlZXJo6PIxrmLZGlixdOVWTSs8PmCfb4nQ%3D%3D&listYN=Y")
    fun searchKeyWord(
//        @Query("ServiceKey") key:String,
        @Query("keyword") keyword:String,
        @Query("contentType") contentType:Int,
        @Query("areaCode") areaCode:Int,
        @Query("MobileOS") OS:String,
        @Query("MobileApp") App:String,
        @Query("_type") type:String

    ): Observable<SearchKeyWordModel>
}