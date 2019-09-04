package com.inseoul.add_place

import android.os.Parcel
import android.os.Parcelable

data class AddPlaceSearchItem(
    var placeNm:String,
    var placeIcon:Int,
    var placeID:Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(placeNm)
        parcel.writeInt(placeIcon)
        parcel.writeInt(placeID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddPlaceSearchItem> {
        override fun createFromParcel(parcel: Parcel): AddPlaceSearchItem {
            return AddPlaceSearchItem(parcel)
        }

        override fun newArray(size: Int): Array<AddPlaceSearchItem?> {
            return arrayOfNulls(size)
        }
    }
}