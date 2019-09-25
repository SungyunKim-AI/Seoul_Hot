package com.inseoul.data_model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter.writeString
import com.google.android.material.internal.ParcelableSparseArray
import com.inseoul.forecast.Forecast_shortTermItem

data class ReviewDataModel(
    val response:ArrayList<plan>
) {
    data class plan(
        val H:String,
        val IDNUM:String,
        val PLACEID:String,
        val SCORE:String,
        val IMGNAME:String,
        val REVIEW:String,
        val UPSO_NM: String,
        val Lat:String,
        val Lng:String,
        val Spot_new:String
    )


}