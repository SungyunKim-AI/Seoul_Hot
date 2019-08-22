package com.inseoul.make_plan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.inseoul.R
import com.inseoul.Server_mapdata.Spot
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import kotlin.text.Charsets.UTF_8
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.PolylineOptions


class RecommendPlan : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {
    var listIDNUM = ArrayList<Int>()
    var listUPSO = ArrayList<String>()

    var selectedMarker: Marker? = null
    val markerList = ArrayList<MarkerItem>()
    val lineList = ArrayList<LatLng>()

    private val POLYLINE_STROKE_WIDTH_PX = 5f
    private val COLOR_BLACK_ARGB = -0x1000000

    lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend_plan)
        fetchJson()
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)

        getMarkerItems()
    }

    //데이터 객체
    data class MarkerItem(
        val lat: Double,
        val lng: Double,
        var order: Int
    ) {
        var latLng: LatLng? = null
    }

    data class lineItem(
        var latLng: LatLng
    )

    fun getMarkerItems() {
        jsonParsing(getJSONString())

        //폴리 라인 만들기
        val polyline = mMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .addAll(lineList)
        )

        polyline.width = POLYLINE_STROKE_WIDTH_PX
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lineList[0], 12f))

    }


    private fun addMarker(
        markerItem: MarkerItem,
        isSelectedMarker: Boolean
    ): Marker {

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
        isSelectedMarker: Boolean
    ): Marker {
        var lat = marker.position.latitude
        var lng = marker.position.longitude
        var order = marker.title.toInt()
        var temp: MarkerItem = MarkerItem(lat, lng, order)

        return addMarker(temp, isSelectedMarker)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        var center: CameraUpdate = CameraUpdateFactory.newLatLng(marker?.position)
        mMap.animateCamera(center)

        changeSelectedMarker(marker)

        return true
    }

    fun changeSelectedMarker(marker: Marker?) {
        //선택했던 마커 되돌리기
        if (selectedMarker != null) {
            addMarker(selectedMarker!!, false)
            selectedMarker!!.remove()
        }

        //선택한 마커 표시
        if (marker != null) {
            selectedMarker = addMarker(marker, true)
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
            var count = 0
            val movieArray = jsonObject.getJSONArray("data")
            for (j in 0 until listIDNUM.size) {
                for (i in 0..movieArray.length()) {
                    val movieObject = movieArray.getJSONObject(i)

                    if (listIDNUM[j] == movieObject.getInt("IDNUM")) {
                        val spot = Spot()

                        spot.setIDNUM(movieObject.getInt("IDNUM")) //movieObject.getInt("IDNUM") 업소 아이디
                        spot.setY(movieObject.getDouble("Yd")) //movieObject.getDouble("Yd") =위도
                        spot.setX(movieObject.getDouble("Xd")) /// movieObject.getDouble("Xd") =경도
                        ///////////////////////////////////////////////////////
                        markerList.add(MarkerItem(movieObject.getDouble("Yd"), movieObject.getDouble("Xd"), j))
                        Log.d("alert", j.toString())

                        markerList[count].latLng = LatLng(markerList[count].lat, markerList[count].lng)
                        lineList.add(markerList[count].latLng!!)
                        count++
                        continue
                        ////////////////////////////////////////////////////
                    }

                    if (count == listIDNUM.size) break
                }
                for (i in 0 until markerList.size) {

                    addMarker(markerList[i], false)
                }
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

    fun fetchJson() {

        val url = URL("http://ksun1234.cafe24.com/PlanList.php")
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                //println("Success to execute request! : $body")
                val jsonObject = JSONObject(body)


                val movieArray = jsonObject.getJSONArray("response")
                for (i in 0 until movieArray.length()) {
                    val movieObject = movieArray.getJSONObject(i)
                    val cache = movieObject.getString("PLAN").split(",")
                    for (j in 0 until cache.size) {
                        listIDNUM.add(j, cache[j].toInt())
                    }

                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request!")
            }
        })

    }


}
