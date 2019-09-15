package com.inseoul

data class testData2(
    val response:Response
) {
    data class Response(
        val header:Header,
        val body: Body
    )
    data class Header(
        val resultCode:String,
        val resultMsg:String
    )
    data class Body(
        val items:items,
        val numOfRows:Int,
        val pageNo:Int,
        val totalCount:Int
    )
    data class items(
        val item:ArrayList<item>
    )
    data class item(
        val baseDate:Int,
        val baseTime:String,
        val category: String,
        val fcstDate:Int,
        val fcstTime:String,
        val fcstValue:Double,
        val nx:Int,
        val ny:Int
    )
}