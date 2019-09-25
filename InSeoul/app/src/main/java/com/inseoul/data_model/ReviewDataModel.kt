package com.inseoul.data_model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter.writeString
import com.google.android.material.internal.ParcelableSparseArray
import com.inseoul.forecast.Forecast_shortTermItem

data class placeInfo(
    val UPSO_NM:String,
    val Lat:String,
    val Lng:String,
    val Spot_new:String
):Parcelable{
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!
    )
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(UPSO_NM)
        writeString(Lat)
        writeString(Lng)
        writeString(Spot_new)
    }
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<placeInfo> = object : Parcelable.Creator<placeInfo> {
            override fun createFromParcel(source: Parcel): placeInfo = placeInfo(source)
            override fun newArray(size: Int): Array<placeInfo?> = arrayOfNulls(size)
        }
    }
    override fun describeContents() = 0
}

data class ReviewDataModel(
    val response:ArrayList<plan>
) {

    data class Review(
        val hash:String,
        val IDNUM:String,
        val SUBNUM:String,
        val PLACEID:String,
        val SCORE:String,
        val IMGNAME:String,
        val REVIEW:String,
        val TourApi:String,
        val PlaceInfo:ArrayList<placeInfo>
    )

    data class plan(
        val Plan:String,
        val H:String,
        val TripName:String,
        val DPDATE:String,
        val ADDATE:String,
        val THEME:String,
        val LIKES:String,
        val ReviewBool:Int,
        val Review:ArrayList<Review>?
    )


}