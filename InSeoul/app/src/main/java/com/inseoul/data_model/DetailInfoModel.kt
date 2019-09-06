package com.inseoul.data_model

class DetailInfoModel(
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
        val items:ArrayList<item>,
        val numOfRows:Int,
        val pageNo:Int,
        val totalCount:Int
    )
    data class item(
        val contentid:Int,
        val contenttypeid:Int

    )

}