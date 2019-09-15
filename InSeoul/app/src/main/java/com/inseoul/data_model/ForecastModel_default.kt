package com.inseoul.data_model

class ForecastModel_default(
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
        val announceTime:Long,
        val numEf:Int,
        val regId: String,
        val rnSt:Int,
        val rnYn:Int,
        val ta:Int,
        val wd1:String,
        val wd2:String,
        val wdTnd:Int,
        val wf:String,
        val wfCd:String
    )
}