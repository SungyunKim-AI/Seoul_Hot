package com.inseoul

data class testdata(
//    val numOfRows:Int,
//    val pageNo:Int,
//    val totalCount:Int,
    val response:Response
) {
    data class Response(
        val header:header,
        val body:body
    )
    data class header(
        val resultCode:String,
        val resultMsg:String
        )
    data class body(
        val items:items,
        val numOfRows:Int,
        val pageNo:Int,
        val totalCount:Int
    )
    data class items(
        val item:ArrayList<item>
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