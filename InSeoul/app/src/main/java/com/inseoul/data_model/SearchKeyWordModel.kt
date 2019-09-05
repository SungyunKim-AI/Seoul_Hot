package com.inseoul.data_model

import com.inseoul.testdata

class SearchKeyWordModel(
    val response:Response
) {
    data class Response(
        val header: testdata.header,
        val body: testdata.body
    )
    data class header(
        val resultCode:String,
        val resultMsg:String
    )
    data class body(
        val items: testdata.items,
        val numOfRows:Int,
        val pageNo:Int,
        val totalCount:Int
    )
    data class items(
        val item:ArrayList<testdata.item>
    )
    data class item(
        val addr1:String?,
        val addr2:String?,
        val areacode:Int?,
        val booktour:Int?,
        val cat1:String?,
        val cat2:String?,
        val cat3:String?,
        val contentid:Int,
        val contenttypeid:Int,
        val createdtime:Long,
        val firstImage:String?,
        val firstImage2:String?,
        val mapx:Double?,
        val mapy:Double?,
        val mlevel:Int?,
        val modifiedtime:Long,
        val readcount:Int?,
        val sigungucode:Int?,
        val tel:String?,
        val title:String
    )
}