package com.inseoul.forecast

data class Forecast_shortTermItem(
    val month:Int,
    val day:Int,
    var date:Int?,
    var time:Int?,
    var T1H: Double?,
    var SKY:Int?,
    var PTY:Int?

) {
}