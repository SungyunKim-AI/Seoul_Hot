package com.inseoul.forecast

data class Forecast_item (
    val date:String,
    val t_high:Int,
    val t_low:Int,
    val weather_am:String,
    val weather_pm:String
) {
}