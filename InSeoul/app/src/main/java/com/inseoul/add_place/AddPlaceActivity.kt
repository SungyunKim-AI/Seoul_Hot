package com.inseoul.add_place

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.inseoul.R
import com.inseoul.make_plan.MarkerItem
import kotlinx.android.synthetic.main.activity_add_place.*

class AddPlaceActivity :
    AppCompatActivity(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    lateinit var mMap: GoogleMap
    var selectedMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)
        init()
//        initTheme()             //여행 테마 세팅

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    fun init(){

        initToolbar()


        val extras = intent.extras
        var flag: Int
        flag = extras!!.getInt("flag_key", -1)
        Log.d("alert", flag.toString())
        if (flag == 2) {
            //flag가 2이라면 MakePlanActivity에서 넘어옴
            var date: String
            var theme: String

            date = extras!!.getString("PlanDate", "NULL")
            theme = extras!!.getString("PlanTheme", "NULL")

//            textview_theme = this.textview_plantheme

            PlanTitle.hint = date + " 여정"
            textview_plandate.text = date
//            textview_theme!!.setText(theme)

        } else if (flag == 1) {
            //flag가 1이라면 SearchDetail에서 넘어옴

            var title_add: String
            var date_add: String
            var theme_add: String

            title_add = extras!!.getString("PlanTitle_add", "NULL")

//            textview_theme = this.textview_plantheme
            textview_plandate.text = ""

//            textview_theme!!.setText(null)
        } else if (flag == 3) {
            //flag가 3이라면 my_schedule에서 넘어옴
            var title_edit: String

            title_edit = extras!!.getString("textview_title", "NULL")

//            textview_theme = this.textview_plantheme

            textview_plandate.text = ""
        }


        /////////////완료 버튼//////////
        finishBtn.setOnClickListener {

            val intent = Intent()
            intent.putExtra("result", 1)    // TEST CODE
            setResult(Activity.RESULT_OK, intent)
            finish()

        }
        addBtn.setOnClickListener {
            val intent = Intent(this, AddPlaceSearch::class.java)
            startActivity(intent)
        }
    }

    fun initTheme() {
        theme_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (theme_spinner.getItemAtPosition(position)) {
                    "역사" -> {
//                        theme = "역사"
                    }
                    "맛집" -> {
//                        theme = "맛집"
                    }
                    "힐링" -> {
//                        theme = "힐링"
                    }
                    else -> {
//                        theme = null
                    }
                }

            }
        }
    }


    ////////////////// Map //////////////////
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.566502, 126.977918), 12f))          //map 시작 위치 : 서울 시청

        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)

        getMarkerItems()
    }

    fun getMarkerItems() {

//        //폴리 라인 만들기
//        val polyline = mMap.addPolyline(
//            PolylineOptions()
//                .clickable(true)
//                .addAll(lineList)
//        )
//        polyline.width = POLYLINE_STROKE_WIDTH_PX
//        polyline.pattern = PATTERN_POLYLINE_DOTTED
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lineList[0], 12f))
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



    ////////////////Toolbar//////////////
    fun initToolbar() {
        //toolbar 커스텀 코드
        val mtoolbar = findViewById(R.id.toolbar_add_place) as Toolbar
        setSupportActionBar(mtoolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
    }
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

