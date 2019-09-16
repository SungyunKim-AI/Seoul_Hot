package com.inseoul.forecast

import android.os.Parcel
import android.os.Parcelable

data class Forecast_shortTermItem(
    val month:Int,
    val day:Int,
    var date:Int?,
    var time:Int?,
    var T1H: Double?,
    var SKY:Int?,
    var PTY:Int?

): Parcelable {
    constructor(source: Parcel) : this(
    source.readInt(),
    source.readInt(),
    source.readInt(),
    source.readInt(),
    source.readValue(Double::class.java.classLoader) as Double?,
    source.readInt(),
    source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(month)
        writeInt(day)
        writeInt(date!!)
        writeInt(time!!)
        writeValue(T1H)
        writeInt(SKY!!)
        writeInt(PTY!!)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Forecast_shortTermItem> = object : Parcelable.Creator<Forecast_shortTermItem> {
            override fun createFromParcel(source: Parcel): Forecast_shortTermItem = Forecast_shortTermItem(source)
            override fun newArray(size: Int): Array<Forecast_shortTermItem?> = arrayOfNulls(size)
        }
    }

}