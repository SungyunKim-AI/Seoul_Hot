package com.inseoul.search

import com.google.android.gms.maps.model.LatLng
import com.inseoul.R

data class SearchItem(
    var placeNm:String,
    var placeIcon:Int

){
    //placeFlag => 명소:0, 맛집:1, 쇼핑:2
    var placeID :Int ?= null
    var placeLatLng:LatLng ?= null
}