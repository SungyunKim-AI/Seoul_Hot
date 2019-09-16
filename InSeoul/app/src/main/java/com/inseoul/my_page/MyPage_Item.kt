package com.inseoul.my_page
import android.os.Parcel
import android.os.Parcelable


data class MyPage_Item(
    val Num:Int,
    val title:String,
    val date:String,
    val theme:String,
    val likes:Int,
    val plan:String,
    val mem:String,
    val thumbnail: String,
    val review:Boolean
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readValue(Boolean::class.java.classLoader) as Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(Num)
        parcel.writeString(title)
        parcel.writeString(date)
        parcel.writeString(theme)
        parcel.writeInt(likes)
        parcel.writeString(plan)
        parcel.writeString(mem)
        parcel.writeString(thumbnail)
        parcel.writeValue(review)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyPage_Item> {
        override fun createFromParcel(parcel: Parcel): MyPage_Item {
            return MyPage_Item(parcel)
        }

        override fun newArray(size: Int): Array<MyPage_Item?> {
            return arrayOfNulls(size)
        }
    }
}

