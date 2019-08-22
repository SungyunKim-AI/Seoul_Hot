package com.inseoul

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.createBitmap
import androidx.core.view.drawToBitmap
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlin.collections.ArrayList


class TestActivity_Yoon : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {



    val apiKey = "AIzaSyArBMJ-s5uzGsRCNNyon9LeQsXDgCDmcTI"

    var selectedMarker: Marker ?= null
    private val POLYLINE_STROKE_WIDTH_PX = 5f
    lateinit var mMap:GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test__yoon)

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        if(mMap == null){
            Log.v("Map", "ERROR")
        }

//        //폴리 라인 만들기
//        val polyline1 = googleMap
//
//        polyline1.addPolyline(
//            PolylineOptions()
//                .clickable(true)
//                .addAll(marker)
//                .width(POLYLINE_STROKE_WIDTH_PX)
//        )


        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.684, 133.903), 4f))
        googleMap.setOnMarkerClickListener(this)
        googleMap.setOnMapClickListener(this)

        getMarkerItems()
    }

    //데이터 객체
    data class MarkerItem(
        val lat: Double,
        val lng: Double,
        var order: Int
    )

    fun getMarkerItems() {
        val markerList = ArrayList<MarkerItem>()
        //순재야 여기다가 Marker Item add 시켜주면 된다잉
//        for(i in 0 .. markerList.size-1) {
//            markerList.add(MarkerItem(-35.016, 143.321, i))
//        }

        ///////////////Test Code////////////////
        markerList.add(MarkerItem(-35.016, 143.321, 1))
        markerList.add(MarkerItem(-34.747, 145.592, 2))
        markerList.add(MarkerItem(-34.364, 147.891, 3))
        markerList.add(MarkerItem(-33.501, 150.217, 4))
        markerList.add(MarkerItem(-32.306, 149.248, 5))
        markerList.add(MarkerItem(-32.491, 147.309, 6))

        for (markerItem in markerList) {
            addMarker(markerItem, false)
        }
    }

    private fun addMarker(
        markerItem: MarkerItem,
        isSelectedMarker: Boolean): Marker {

        var position = LatLng(markerItem.lat, markerItem.lng)
        var order = markerItem.order
        //String formatted = NumberFormat . getCurrencyInstance ().format((price))


       var markerOptions = MarkerOptions()
        markerOptions.title(order.toString())
        markerOptions.position(position)

        if (isSelectedMarker) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.click_marker))
        } else {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.default_marker))
        }


        return mMap.addMarker(markerOptions!!)
    }

    private fun addMarker(
        marker: Marker,
        isSelectedMarker: Boolean): Marker{
        var lat = marker.position.latitude
        var lng = marker.position.longitude
        var order = marker.title.toInt()
        var temp: MarkerItem = MarkerItem(lat,lng, order)

        return addMarker(temp, isSelectedMarker)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        var center:CameraUpdate = CameraUpdateFactory.newLatLng(marker?.position)
        mMap.animateCamera(center)

        changeSelectedMarker(marker)

        return true
    }

    fun changeSelectedMarker(marker: Marker?){
        //선택했던 마커 되돌리기
        if(selectedMarker != null){
            addMarker(selectedMarker!!, false)
            selectedMarker!!.remove()
        }

        //선택한 마커 표시
        if(marker != null){
            selectedMarker = addMarker(marker,true)
            marker.remove()
        }
    }


    override fun onMapClick(p0: LatLng?) {
       changeSelectedMarker(null)
    }




}

