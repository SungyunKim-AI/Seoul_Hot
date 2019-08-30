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
import com.inseoul.search.SearchActivity
import kotlinx.android.synthetic.main.activity_add_place.*
import org.json.JSONObject
import java.util.*
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class AddPlaceActivity :
    AppCompatActivity(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    lateinit var mMap: GoogleMap
    var selectedMarker: Marker? = null
    val markerList = ArrayList<AddPlaceItem>()
    val lineList = ArrayList<LatLng>()

    private val POLYLINE_STROKE_WIDTH_PX = 7f
    private val PATTERN_GAP_LENGTH_PX = 10f
    private val COLOR_BLACK_ARGB = -0x1000000
    private val DOT = Dot()
    private val GAP = Gap(PATTERN_GAP_LENGTH_PX)
    private val PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)
        initToolbar()
        init()
        initMap()
//        initTheme()             //여행 테마 세팅

    }

    fun init() {

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
        } else if (flag == 4) {
            //flag가 4이라면 AddPlaceSearch에서 넘어옴
        }


        /////////////완료 버튼//////////
        finishBtn.setOnClickListener {

            val intent = Intent()
            intent.putExtra("result", 1)    // TEST CODE
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        addBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }


    /////////////////AddPlaceSearch에서 넘어왔을때 호출//////////////////////////////
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1) {
            val placeData = data?.getParcelableExtra<AddPlaceItem>("placeData")
            markerList.add(placeData!!)
            lineList.add(markerList[markerList.size-1].latLng!!)
        }
        for (i in 0 until markerList.size) {
            markerList[i].count = i
            //Log.d("alert_대입",markerList[i].count.toString())
            addMarker(markerList[i], false)

        }
        getMarkerItems()
        if(markerList.size != 0){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lineList[markerList.size-1], 12f))
        }else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.552122, 126.988270), 12f))
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


    fun initMap(){
        readFile()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }
    var positionArray = ArrayList<ArrayList<Double>>()

    fun readFile(){
        val scan = Scanner(resources.openRawResource(R.raw.is_map))
        var result = ""
        while(scan.hasNextLine()){
            val line = scan.nextLine()
            result += line
        }
        parsingGson(result)
    }

    fun parsingGson(result:String){
        val json = JSONObject(result)
        val array = json.getJSONArray("data")
        for(i in 0 until array.length()){
            val lat = array.getJSONObject(i).getString("Yd")
            val lng = array.getJSONObject(i).getString("Xd")
            val pos = ArrayList<Double>()
            pos.add(lat.toDouble())
            pos.add(lng.toDouble())
            positionArray.add(pos)
            Log.d("LatLng", "$lat, $lng")
        }
    }
    ////////////////// Compute Distance //////////////////
    fun distance(lat1:Double, lat2:Double, lng1:Double, lng2:Double):Double{
        val theta = lng1 - lng2;
        var dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1))*cos(deg2rad(lat2))*cos(deg2rad(theta))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1609.344      // Mile to Meter

        return dist
    }

    fun deg2rad(deg:Double):Double{     // Degree to Radian
        return (deg * PI/ 180.0)
    }
    fun rad2deg(rad:Double):Double{     // Radian to Degree
        return (rad * 180/ PI)
    }
    //////////////////////////////////////////////////////

    //    Camera Option Reference
//    1: World
//    5: Landmass/continent
//    10: City
//    15: Streets
//    20: Buildings
    ////////////////// Map //////////////////
    override fun onMapReady(googleMap: GoogleMap) {

        val Default = LatLng(37.552122, 126.988270)     //남산타워
        val DISTANCE = 1000

        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Default, 13f))
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)

        mMap.setOnMapClickListener {
            mMap.clear()                    // 수정 필요
            /////////////// 클릭한 지점의 위치 ///////////////
            val mk = MarkerOptions();
            mk.title("좌표")
            val lat = it.latitude
            val lng = it.longitude
            Log.e("position", "$lat, $lng")
            mk.snippet("$lat, $lng");
            mk.position(it)
            mk.icon(BitmapDescriptorFactory.fromResource(R.drawable.default_marker))

            mMap.addMarker(mk)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(it))

            //////////////////////////////////////////////////

            /////////////// 반경 1km(DISTANCE) 내의 데이터 마커로 표시 ///////////////
            for(i in 0 until positionArray.size){
                if(distance(it.latitude, positionArray[i][0], it.longitude, positionArray[i][1])< DISTANCE){
                    var marker = MarkerOptions()
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.default_marker))
                    marker.position(LatLng(positionArray[i][0], positionArray[i][1]))
                    mMap.addMarker(marker)

                }
            }
            /////////////////////////////////////////////////////////////////////////
        }

    }

    fun getMarkerItems() {

        //폴리 라인 만들기
        val polyline = mMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .addAll(lineList)
        )
        polyline.width = POLYLINE_STROKE_WIDTH_PX
        polyline.pattern = PATTERN_POLYLINE_DOTTED
    }


    private fun addMarker(
        markerItem: AddPlaceItem,
        isSelectedMarker: Boolean
    ): Marker {

        var position = markerItem.latLng
        var order = markerItem.count
       //Log.d("alert_출력",markerItem.count.toString())

        var markerOptions = MarkerOptions()
        markerOptions.position(position!!)
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
        var title = marker.title
        var preview = marker.title
        var latlng = marker.position
        var temp: AddPlaceItem = AddPlaceItem(title, preview, latlng)

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
