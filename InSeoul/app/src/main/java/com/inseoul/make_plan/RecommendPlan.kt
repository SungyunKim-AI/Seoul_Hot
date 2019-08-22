package com.inseoul.make_plan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.inseoul.R
import com.inseoul.Server_mapdata.Spot
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.text.Charsets.UTF_8

class RecommendPlan: AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener{


    var selectedMarker: Marker?= null
    val markerList = ArrayList<MarkerItem>()

    private val POLYLINE_STROKE_WIDTH_PX = 5f

    lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend_plan)

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

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
        jsonParsing(getJSONString())
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
        var center: CameraUpdate = CameraUpdateFactory.newLatLng(marker?.position)
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



    ///////////////서버 파싱////////////////////////
    private fun jsonParsing(json: String) {
        try {
            val jsonObject = JSONObject(json)

            val movieArray = jsonObject.getJSONArray("data")

            for (i in 0 until movieArray.length()) {
                val movieObject = movieArray.getJSONObject(i)

                val spot = Spot()

                spot.setIDNUM(movieObject.getInt("IDNUM")) //movieObject.getInt("IDNUM") 업소 아이디
                spot.setY(movieObject.getDouble("Yd")) //movieObject.getDouble("Yd") =위도
                spot.setX(movieObject.getDouble("Xd")) /// movieObject.getDouble("Xd") =경도
                ///////////////////////////////////////////////////////
                markerList.add(MarkerItem(movieObject.getDouble("Yd"), movieObject.getDouble("Xd"), i))
                ////////////////////////////////////////////////////

            }
            for (markerItem in markerList) {
                addMarker(markerItem, false)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun getJSONString(): String {
        var json = ""
        try {
            val `is` = assets.open("is_map.json")
            val fileSize = `is`.available()

            val buffer = ByteArray(fileSize)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return json
    }



}
