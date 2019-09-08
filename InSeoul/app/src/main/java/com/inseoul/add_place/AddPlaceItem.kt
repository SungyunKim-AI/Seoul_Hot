package com.inseoul.add_place

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

data class AddPlaceItem(
    var placeID:Int ? = null,
    var PlaceNm:String ?= null,
    var PlaceType:Int ?= null,
    var latLng: LatLng ?= null,
    var count: Int ?= null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(LatLng::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(placeID)
        parcel.writeString(PlaceNm)
        parcel.writeValue(PlaceType)
        parcel.writeParcelable(latLng, flags)
        parcel.writeValue(count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddPlaceItem> {
        override fun createFromParcel(parcel: Parcel): AddPlaceItem {
            return AddPlaceItem(parcel)
        }

        override fun newArray(size: Int): Array<AddPlaceItem?> {
            return arrayOfNulls(size)
        }
    }
}

