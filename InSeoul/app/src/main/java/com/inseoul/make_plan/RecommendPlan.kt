package com.inseoul.make_plan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.inseoul.R
import com.inseoul.Server_mapdata.Spot
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.text.Charsets.UTF_8
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.Dot
import java.util.*


class RecommendPlan : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {

    //////////Activity Inform Setting/////////
    var planNm : String? = null
    var listIDNUM = ArrayList<Int>()
    var listUPSO = ArrayList<String>()

    var selectedMarker: Marker? = null
    val markerList = ArrayList<MarkerItem>()
    val lineList = ArrayList<LatLng>()

    private val POLYLINE_STROKE_WIDTH_PX = 7f
    private val PATTERN_GAP_LENGTH_PX = 10f
    private val COLOR_BLACK_ARGB = -0x1000000
    private val DOT = Dot()
    private val GAP = Gap(PATTERN_GAP_LENGTH_PX)
    private val PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT)

    lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend_plan)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        init()

        mMap = googleMap

        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)

        getMarkerItems()
    }

    fun init(){

        var planData = intent.getParcelableExtra<MakePlanItem>("planData")
        listIDNUM = intent.getIntegerArrayListExtra("plan_array")
        Log.d("alert",listIDNUM.toString())
        planNm = planData.TRIP_NAME

        initToolbar()
    }


    fun getMarkerItems() {
        jsonParsing(getJSONString())

        //폴리 라인 만들기
        val polyline = mMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .addAll(lineList)
        )
        polyline.width = POLYLINE_STROKE_WIDTH_PX
        polyline.pattern = PATTERN_POLYLINE_DOTTED

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lineList[0], 12f))
    }


    private fun addMarker(
        markerItem: MarkerItem,
        isSelectedMarker: Boolean
    ): Marker {

        var position = LatLng(markerItem.lat, markerItem.lng)
        var order = markerItem.order

        var markerOptions = MarkerOptions()
        markerOptions.position(position)
        markerOptions.title(order.toString())
        markerOptions.snippet(order.toString())

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
            selectedMarker?.showInfoWindow()                        //정보창 띄우기
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
                        //Log.d("alert", j.toString())

                        markerList[count].latLng = LatLng(markerList[count].lat, markerList[count].lng)
                        lineList.add(markerList[count].latLng!!)
                        count++
                        break
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

    fun initToolbar(){
        //toolbar 커스텀 코드
        val mtoolbar = findViewById(R.id.toolbar_recommend_plan) as Toolbar
        setSupportActionBar(mtoolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        mtoolbar.title = planNm
    }

    ///////////////toolbar에서 back 버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

//데이터 객체
data class MarkerItem(
    val lat: Double,
    val lng: Double,
    var order: Int
) {
    var latLng: LatLng? = null
}
