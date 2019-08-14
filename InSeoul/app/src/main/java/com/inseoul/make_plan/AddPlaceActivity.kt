package com.inseoul.make_plan

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.inseoul.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import kotlinx.android.synthetic.main.activity_add_place.*

class AddPlaceActivity :
    AppCompatActivity(),
    OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)
        initMap()
        initBtn()

        //toolbar 커스텀 코드
        val mtoolbar = findViewById(R.id.toolbar_add_place) as Toolbar
        setSupportActionBar(mtoolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        mtoolbar.title = "여행 장소 추가"
        mtoolbar.setTitleTextColor(Color.WHITE)
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
            .camera(CameraPosition(LatLng(37.54345, 127.07747), 14.0))
            .mapType(NaverMap.MapType.Basic)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance(options).also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(p0: NaverMap) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        ////////////////// Preconditioning //////////////////





        /////////////////////////////////////////////////////
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

