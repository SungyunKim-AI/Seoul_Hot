package com.inseoul.make_plan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.inseoul.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import kotlinx.android.synthetic.main.activity_add_place.*
import kotlinx.android.synthetic.main.activity_search.*

class AddPlaceActivity :
    AppCompatActivity(),
    OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)
        initMap()
        initBtn()
    }


    fun initBtn(){
        finishBtn.setOnClickListener {
//            val intent = Intent(this, His::class.java)
//            startActivity(intent)
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
}

