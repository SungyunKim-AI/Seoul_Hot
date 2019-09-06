package com.inseoul.search

import android.os.Parcel
import android.os.Parcelable

data class Search_Item(
    val id: Int,
    val title: String,
    val url: String?,
    val type: Int,
    val posX: Double?,
    val posY: Double?,
    val addr1: String?,
    val addr2: String?,
    val tel: String?
) : Parcelable{

    constructor(source: Parcel) : this(
    source.readInt(),
    source.readString()!!,
    source.readString(),
    source.readInt(),
    source.readValue(Double::class.java.classLoader) as Double?,
    source.readValue(Double::class.java.classLoader) as Double?,
    source.readString(),
    source.readString(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(title)
        writeString(url)
        writeInt(type)
        writeValue(posX)
        writeValue(posY)
        writeString(addr1)
        writeString(addr2)
        writeString(tel)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Search_Item> = object : Parcelable.Creator<Search_Item> {
            override fun createFromParcel(source: Parcel): Search_Item = Search_Item(source)
            override fun newArray(size: Int): Array<Search_Item?> = arrayOfNulls(size)
        }
    }

}