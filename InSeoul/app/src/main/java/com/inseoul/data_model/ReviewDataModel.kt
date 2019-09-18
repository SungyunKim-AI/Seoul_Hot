package com.inseoul.data_model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter.writeString
import com.google.android.material.internal.ParcelableSparseArray
import com.inseoul.forecast.Forecast_shortTermItem

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
        val REVIEW:String
    ):Parcelable{
        constructor(source: Parcel) : this(
            source.readString()!!,
            source.readString()!!,
            source.readString()!!,
            source.readString()!!,
            source.readString()!!,
            source.readString()!!,
            source.readString()!!
        )
        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(hash)
            writeString(IDNUM)
            writeString(SUBNUM)
            writeString(PLACEID)
            writeString(SCORE)
            writeString(IMGNAME)
            writeString(REVIEW)
        }
        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Review> = object : Parcelable.Creator<Review> {
                override fun createFromParcel(source: Parcel): Review = Review(source)
                override fun newArray(size: Int): Array<Review?> = arrayOfNulls(size)
            }
        }
        override fun describeContents() = 0
    }

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