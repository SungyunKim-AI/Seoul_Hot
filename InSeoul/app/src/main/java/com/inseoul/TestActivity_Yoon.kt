package com.inseoul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.Polyline
import java.util.Arrays.asList
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.Dot
import java.util.*
import kotlin.collections.ArrayList


class TestActivity_Yoon : AppCompatActivity(), OnMapReadyCallback {


    val apiKey = "AIzaSyArBMJ-s5uzGsRCNNyon9LeQsXDgCDmcTI"
    private lateinit var mMap: GoogleMap

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
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val marker = listOf(
            LatLng(-35.016, 143.321),
            LatLng(-34.747, 145.592),
            LatLng(-34.364, 147.891),
            LatLng(-33.501, 150.217),
            LatLng(-32.306, 149.248),
            LatLng(-32.491, 147.309)
        )

        //마크 찍기
        for(i in 0 .. marker.size-1)
        googleMap.addMarker(
            MarkerOptions().position(marker[i])
                .title(i.toString())
        )


        //폴리 라인 만들기
        val polyline1 = googleMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(-35.016, 143.321),
                    LatLng(-34.747, 145.592),
                    LatLng(-34.364, 147.891),
                    LatLng(-33.501, 150.217),
                    LatLng(-32.306, 149.248),
                    LatLng(-32.491, 147.309)
                )
        )

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.684, 133.903), 4f))

        // Set listeners for click events.
        //googleMap.setOnPolylineClickListener(this)
    }

//    val DOT = Dot()
//    val GAP = Gap(PATTERN_GAP_LENGTH_PX)
//
//    // Create a stroke pattern of a gap followed by a dot.
//    val PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT)
//
//    override fun onPolylineClick(polyline: Polyline) {
//        // Flip from solid stroke to dotted stroke pattern.
//        if ((polyline.getPattern() == null) || (polyline.pattern?.contains(DOT)!!)) {
//            polyline.setPattern(PATTERN_POLYLINE_DOTTED)
//        } else {
//            // The default pattern is a solid stroke.
//            polyline.setPattern(null)
//        }
//
//        Toast.makeText(this, "Route type " + polyline.getTag().toString(), Toast.LENGTH_SHORT).show()
//    }

}
