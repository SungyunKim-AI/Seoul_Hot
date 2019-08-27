package com.inseoul.make_plan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.inseoul.R
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import kotlinx.android.synthetic.main.activity_add_place.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class AddPlaceActivity :
    AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)
        initMap()
        initBtn()
        initTitle()     //intent 받기
        initToolbar()
//        initTheme()             //여행 테마 세팅

    }

    fun initToolbar(){
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

    fun initBtn(){
        finishBtn.setOnClickListener {
//            val intent = Intent(this, His::class.java)
//            startActivity(intent)
            val intent = Intent()
            intent.putExtra("result", 1)    // TEST CODE
            setResult(Activity.RESULT_OK, intent)
            finish()

        }
    }

    ////////////////// Map //////////////////

    lateinit var mMap:GoogleMap
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
    fun initMap(){
        readFile()
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment!!.getMapAsync(this)

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
    override fun onMapReady(p0: GoogleMap?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mMap = p0!!

        val Default = LatLng(37.543578, 127.077363)     // 새천년관
        val DISTANCE = 1000

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Default, 15f))

        mMap.setOnMapClickListener {
            mMap.clear()
            /////////////// 클릭한 지점의 위치 ///////////////
            val mk = MarkerOptions();
            mk.title("좌표")
            val lat = it.latitude
            val lng = it.longitude
            Log.e("position", "$lat, $lng")
            mk.snippet("$lat, $lng");
            mk.position(it)
            mMap.addMarker(mk)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
            //////////////////////////////////////////////////

            /////////////// 반경 1km(DISTANCE) 내의 데이터 마커로 표시 ///////////////
            for(i in 0 until positionArray.size){
                if(distance(it.latitude, positionArray[i][0], it.longitude, positionArray[i][1])< DISTANCE){
                    var marker = MarkerOptions()

                    marker.position(LatLng(positionArray[i][0], positionArray[i][1]))
                    mMap.addMarker(marker)

                }
            }
            /////////////////////////////////////////////////////////////////////////
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return true
    }

    override fun onMapClick(p0: LatLng?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    //////////Intent 받는 함수///////////
    fun initTitle(){
        val extras = intent.extras
        var flag:Int
        flag = extras!!.getInt("flag_key",-1)
        Log.d("alert",flag.toString())
        if(flag==2){
            //flag가 2이라면 MakePlanActivity에서 넘어옴
            var date:String
            var theme:String
            
            date = extras!!.getString("PlanDate","NULL")
            theme = extras!!.getString("PlanTheme","NULL")

//            textview_theme = this.textview_plantheme

            PlanTitle.hint = date + " 여정"
            textview_plandate.text = date
//            textview_theme!!.setText(theme)

        }else if(flag==1){
            //flag가 1이라면 SearchDetail에서 넘어옴

            var title_add:String
            var date_add:String
            var theme_add:String

            title_add = extras!!.getString("PlanTitle_add","NULL")

//            textview_theme = this.textview_plantheme
            textview_plandate.text = ""

//            textview_theme!!.setText(null)
        }else if(flag==3){
            //flag가 3이라면 my_schedule에서 넘어옴
            var title_edit:String

            title_edit = extras!!.getString("textview_title","NULL")

//            textview_theme = this.textview_plantheme

            textview_plandate.text = ""
        }

    }


    //toolbar에서 back 버튼
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

