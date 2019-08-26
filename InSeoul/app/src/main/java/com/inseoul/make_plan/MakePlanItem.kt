package com.inseoul.make_plan

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class  MakePlanItem(
    var TRIP_NAME: String?
) : Parcelable{
    var preview: String ?= null
    var THEME: String? = null
    var LIKES: Int? = null
    var PLAN = ArrayList<Int>()
    var imgList = ArrayList<Drawable?>()

    constructor(parcel: Parcel) : this(parcel.readString()) {
        preview = parcel.readString()
        THEME = parcel.readString()
        LIKES = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(TRIP_NAME)
        parcel.writeString(preview)
        parcel.writeString(THEME)
        parcel.writeValue(LIKES)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MakePlanItem> {
        override fun createFromParcel(parcel: Parcel): MakePlanItem {
            return MakePlanItem(parcel)
        }

        override fun newArray(size: Int): Array<MakePlanItem?> {
            return arrayOfNulls(size)
        }
    }
}
