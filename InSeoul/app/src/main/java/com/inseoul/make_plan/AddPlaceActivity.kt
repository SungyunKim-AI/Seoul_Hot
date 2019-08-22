package com.inseoul.make_plan

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.inseoul.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_add_place.*

class AddPlaceActivity :
    AppCompatActivity(),
    OnMapReadyCallback {

    var textview_title: TextView? = null
    var textview_date: TextView? = null
    var textview_theme: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)
        initMap()
        initBtn()
        initTitle()     //intent 받기

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
    fun initMap(){
        val options = NaverMapOptions()
            .camera(CameraPosition(LatLng(37.566502, 126.977918), 10.0))
            .mapType(NaverMap.MapType.Basic)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance(options).also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(naverMap: NaverMap) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        ////////////////// Preconditioning //////////////////

        val marker = Marker()
        marker.position = LatLng(37.54345, 127.07747)
        marker.map = naverMap

        naverMap.setOnMapClickListener { point, coord ->
            Toast.makeText(this, "${coord.latitude}, ${coord.longitude}",
                Toast.LENGTH_SHORT).show()
        }


        /////////////////////////////////////////////////////
    }


    //////////Intent 받는 함수///////////
    fun initTitle(){
        val extras = intent.extras
        var flag:Int
        flag = extras!!.getInt("flag_key",-1)
        Log.d("alert",flag.toString())
        if(flag==2){
            //flag가 2이라면 MakePlanActivity에서 넘어옴
            var title:String
            var date:String
            var theme:String

            title = extras!!.getString("PlanTitle","NULL")
            date = extras!!.getString("PlanDate","NULL")
            theme = extras!!.getString("PlanTheme","NULL")

            textview_title = this.textview_plantitle
            textview_date = this.textview_plandate
            textview_theme = this.textview_plantheme

            textview_title!!.setText(title)
            textview_date!!.setText(date)
            textview_theme!!.setText(theme)

        }else if(flag==1){
            //flag가 1이라면 SearchDetail에서 넘어옴

            var title_add:String
            var date_add:String
            var theme_add:String

            title_add = extras!!.getString("PlanTitle_add","NULL")

            textview_title = this.textview_plantitle
            textview_date = this.textview_plandate
            textview_theme = this.textview_plantheme

            textview_title!!.setText(title_add)
            textview_date!!.setText(null)
            textview_theme!!.setText(null)
        }else if(flag==3){
            //flag가 3이라면 my_schedule에서 넘어옴
            var title_edit:String

            title_edit = extras!!.getString("textview_title","NULL")

            textview_title = this.textview_plantitle
            textview_date = this.textview_plandate
            textview_theme = this.textview_plantheme

            textview_title!!.setText(title_edit)
            textview_date!!.setText(null)
            textview_theme!!.setText(null)
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

