package com.inseoul.data_model

data class TimeLineModel (
    val response:ArrayList<timeline>
){
    data class timeline(
        val PLAN:String,
        val H:String,
        val TripName:String,
        val DPDATE:String,
        val ADDATE:String,
        val THEME:String,
        val LIKES:String,
        val ReviewBool:Int,
        val Review:String?
    )
}