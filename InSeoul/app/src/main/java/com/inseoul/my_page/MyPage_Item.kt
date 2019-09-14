package com.inseoul.my_page

import android.graphics.drawable.Drawable

data class MyPage_Item(
    val Num:Int,

    val title:String,
    val date:String,
    val theme:String,
    val likes:Int,
    val plan:String,
    val mem:String,
    val thumbnail: Drawable?,
    val review:Boolean
) {
}