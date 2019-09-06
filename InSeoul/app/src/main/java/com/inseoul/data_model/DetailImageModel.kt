package com.inseoul.data_model

data class DetailImageModel(
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
        val contentid:Int,
        val originimgurl:String,
        val serialnum:String,
        val smallimageurl:String
    )
}