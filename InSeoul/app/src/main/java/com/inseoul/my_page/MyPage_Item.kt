package com.inseoul.my_page

import android.graphics.drawable.Drawable

data class MyPage_Item(
    val title:String,
    val date:String,
    val thumbnail: Drawable?,
    val review:Boolean
) {
}