package com.inseoul

data class testData3(
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
        val item:item
    )
    data class item(
        val regId:String,

        val wf3Am:String,
        val wf3Pm:String,
        val rnSt3Am:Int,
        val rnSt3Pm:Int,
        val wf4Am:String,
        val wf4Pm:String,
        val rnSt4Am:Int,
        val rnSt4Pm:Int,
        val wf5Am:String,
        val wf5Pm:String,
        val rnSt5Am:Int,
        val rnSt5Pm:Int,
        val wf6Am:String,
        val wf6Pm:String,
        val rnSt6Am:Int,
        val rnSt6Pm:Int,
        val wf7Am:String,
        val wf7Pm:String,
        val rnSt7Am:Int,
        val rnSt7Pm:Int,
        val wf8:String,
        val rnSt8:Int,
        val wf9:String,
        val rnSt9:Int,
        val wf10:String,
        val rnSt10:Int

    )
}