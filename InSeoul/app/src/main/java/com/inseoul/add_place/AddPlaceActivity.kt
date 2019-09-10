package com.inseoul.add_place

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.inseoul.R
import com.inseoul.Server.AddPlaceRegister
import com.inseoul.Server.PlaceRequest
import com.inseoul.search.SearchActivity
import com.inseoul.search.Search_Item
import kotlinx.android.synthetic.main.activity_add_place.*
import kotlinx.android.synthetic.main.activity_add_place_main.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.view.View.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AddPlaceActivity :
    AppCompatActivity(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    lateinit var mMap: GoogleMap
    var selectedMarker: Marker? = null
    var markerList = ArrayList<AddPlaceItem>()
    var lineList = ArrayList<LatLng>()
    var mCount = 0

    private val POLYLINE_STROKE_WIDTH_PX = 7f
    private val PATTERN_GAP_LENGTH_PX = 10f
    private val COLOR_BLACK_ARGB = -0x1000000
    private val DOT = Dot()
    private val GAP = Gap(PATTERN_GAP_LENGTH_PX)
    private val PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT)
    private var PlanName = ""
    private var PLAN = ""
    private var DPDATE = ""
    private var ADDATE = ""
    private var THEME = ""


    lateinit var fade_out: Animation
    lateinit var fade_in: Animation
    lateinit var rotate_open: Animation
    lateinit var rotate_close: Animation
    var isBtnOpen: Boolean = false

    lateinit var adapter: AddPlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place_main)

        initToolbar()
        initBottomSheet()
        init()
        initMap()

    }

    ////////////////////// Bottom Sheet //////////////////////
    lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    var STATEFLAG = 0     // STATE_HALF_EXPANDED = 0
    // STATE_EXPANDED = 1
    // STATE_COLLAPSED = 2
    fun initBottomSheet() {
        app_bar.isActivated = true
        sheetBehavior = BottomSheetBehavior.from(add_place_bottom_sheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheet_btn.setImageDrawable(getDrawable(R.drawable.ic_down_arrow_white))
                } else {
                    bottomSheet_btn.setImageDrawable(getDrawable(R.drawable.ic_up_arrow))

                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING && STATEFLAG == 0) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
                }
            }

        }
        )

        bottomSheet_btn.setOnClickListener {
            if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                STATEFLAG = 0
                sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            } else if (sheetBehavior.state == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                STATEFLAG = 1
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                STATEFLAG = 0
                sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                app_bar.setExpanded(true, true)
                add_place_title.text = ""
            }

        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    fun init() {

        val listener = object : AddPlaceAdapter.RecyclerViewAdapterEventListener {
            override fun onClick(view: View, position: Int) {

            }
        }
        adapter = AddPlaceAdapter(this, listener, markerList)
        recyclerView_addPlace.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView_addPlace.adapter = adapter

        val extras = intent.extras
        var flag = extras!!.getInt("flag_key", -1)

        when (flag) {
            // from MakePlanActivity
            1 -> {
                val date = extras!!.getString("PlanDate", "NULL")
                PlanTitle.hint = date + " 여정"
                textview_plandate.text = date

            }
            //from MySchedulesActivity
            2 -> {
                val planID = extras!!.getInt("PlanID")
                //Log.d("alert_planID",planID.toString())
                /////////////////////////////////////////////////

                //PlanID를 서버에 보내서 플랜 받아오게
                //Plan에서
                // 1. 일정 제목
                // 2. 일정 날짜
                // 3. 일정에 포함되어 있는 장소들의 ID 값들

                //////////////////////////////////////////////////
                val date = extras!!.getString("textview_date")
                PlanTitle.setText(extras!!.getString("textview_title", date))
                textview_plandate.text = date

            }
            //from SearchDetail
            3 -> {
                val planID = extras!!.getInt("PlanID")
                //Log.d("alert_planID",planID.toString())
                /////////////////////////////////////////////////

                //PlanID를 서버에 보내서 플랜 받아오게
                //Plan에서
                // 1. 일정 제목
                // 2. 일정 날짜
                // 3. 일정에 포함되어 있는 장소들의 ID 값들

                //////////////////////////////////////////////////

                //SearchDetail에서 받아온 PlaceID 값
                val placeID = extras!!.getInt("PlaceID")
            }
        }


        /////////////완료 버튼//////////
        finishBtn.setOnClickListener {
            val responseListener = Response.Listener<String> { response ->
                try {
                    Log.d("d", response)
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        Toast.makeText(this@AddPlaceActivity, "정상적 등록 완료", Toast.LENGTH_SHORT).show()
                        //finish()

                    } else {
                        Toast.makeText(this@AddPlaceActivity, "등록 실패", Toast.LENGTH_SHORT).show()
                        //finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            val registerRequest =
                AddPlaceRegister(PlanName, DPDATE, ADDATE, THEME, PLAN, responseListener)

            val queue = Volley.newRequestQueue(this@AddPlaceActivity)
            queue.add(registerRequest)
            val intent = Intent()
            intent.putExtra("result", 1)    // TEST CODE
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        addBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("flag", true)
            startActivityForResult(intent, 3000)
        }

        fade_out = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out_morebtn_anim)
        rotate_open = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_anim)
        rotate_close = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_anim_close)
        fade_in = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in_btn_anim)

        edit_more.setOnClickListener {
            anim()
        }

        editPlanDateBtn.setOnClickListener {
            anim()
            val intent = Intent(this@AddPlaceActivity, EditPlanDate::class.java)
            startActivityForResult(intent, 2000)
        }


        deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this@AddPlaceActivity)
            builder.setMessage("등록 된 장소들이 삭제됩니다.\n일정을 삭제하시겠습니까?")

            builder.setPositiveButton("확인") { dialog, id ->
                anim()
                val intent = Intent()
                intent.putExtra("result", 1)    // TEST CODE
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            builder.setNegativeButton("취소") { dialog, id ->
                anim()
                dialog.cancel()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        PlanTitle.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                PlanTitle.isFocusableInTouchMode = false
                PlanTitle.isFocusable = false
                editPlanTitleComplete.visibility = GONE
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(PlanTitle.windowToken, 0)
                true
            } else {
                false
            }
        }

        editPlanTitleBtn.setOnClickListener {
            PlanTitle.isFocusableInTouchMode = true
            PlanTitle.requestFocus()
            editPlanTitleComplete.visibility = VISIBLE
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(PlanTitle, 0)
        }

        editPlanTitleComplete.setOnClickListener {
            PlanTitle.width = WRAP_CONTENT
            PlanTitle.isFocusableInTouchMode = false
            PlanTitle.isFocusable = false
            editPlanTitleComplete.visibility = GONE
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(PlanTitle.windowToken, 0)
        }
    }

    fun anim() {

        if (isBtnOpen) {
            editPlanDateBtn.visibility = INVISIBLE
            deleteBtn.visibility = INVISIBLE
            edit_more.startAnimation(rotate_close)
            editPlanDateBtn.startAnimation(fade_out)
            deleteBtn.startAnimation(fade_out)
            editPlanDateBtn.isClickable = false
            deleteBtn.isClickable = false
            isBtnOpen = false
        } else {
            edit_more.startAnimation(rotate_open)
            editPlanDateBtn.startAnimation(fade_in)
            deleteBtn.startAnimation(fade_in)
            editPlanDateBtn.isClickable = true
            deleteBtn.isClickable = true
            isBtnOpen = true
        }
    }


    ////////////////////////////Search에서 넘어왔을때 호출//////////////////////////////
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                2000 -> {
                    //EditPlanDate에서 넘어왔을때
                    var edit_resultStr = data?.getStringExtra("edit_resultStr")

                    PlanTitle.hint = edit_resultStr.toString() + " 여정"
                    textview_plandate.text = edit_resultStr.toString()

                    Toast.makeText(this, "일정 변경 완료", Toast.LENGTH_SHORT).show()
                }
                3000 -> {
                    //Search에서 넘어왔을 때

                    val item = data!!.getParcelableExtra<Search_Item>("placeData")
                    Log.d("alert_back", item.toString())

                    lineList.add(LatLng(item.posY!!, item.posX!!))
                    markerList.add(
                        AddPlaceItem(
                            item.id,
                            item.title,
                            item.type,
                            lineList[lineList.size - 1],
                            lineList.size
                        )
                    )
                    //Log.d("alert_marekrList",markerList[markerList.size-1].toString())
                    adapter.notifyDataSetChanged()

                }
            }

            //마커 새로 찍기
            for (i in 0 until markerList.size) {
                addMarker(markerList[i], false)
            }
            getMarkerItems()
            if (markerList.size != 0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lineList[markerList.size - 1], 12f))
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.552122, 126.988270), 12f))
            }


        }
    }

    fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }


    ////////////////// Compute Distance //////////////////
    fun distance(lat1: Double, lat2: Double, lng1: Double, lng2: Double): Double {
        val theta = lng1 - lng2;
        var dist =
            sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1609.344      // Mile to Meter

        return dist
    }

    fun deg2rad(deg: Double): Double {     // Degree to Radian
        return (deg * PI / 180.0)
    }

    fun rad2deg(rad: Double): Double {     // Radian to Degree
        return (rad * 180 / PI)
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

            if (sheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
                STATEFLAG = 2
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                app_bar.setExpanded(false, true)
                add_place_title.text = textview_plandate.text.toString() + "여정"
            } else {
                mMap.clear()                    // 수정 필요
                /////////////// 클릭한 지점의 위치 ///////////////
                val mk = MarkerOptions()
                mk.title("좌표")
                val lat = it.latitude
                val lng = it.longitude
                //Log.e("position", "$lat, $lng")
                mk.snippet("$lat, $lng")
                mk.position(it)
                mk.icon(BitmapDescriptorFactory.fromResource(R.drawable.default_marker))

                mMap.addMarker(mk)
                mMap.animateCamera(CameraUpdateFactory.newLatLng(it))

                //////////////////////////////////////////////////

                /////////////// 반경 1km(DISTANCE) 내의 데이터 마커로 표시 ///////////////
//                for (i in 0 until positionArray.size) {
//                    if (distance(it.latitude, positionArray[i][0], it.longitude, positionArray[i][1]) < DISTANCE) {
//                        var marker = MarkerOptions()
//                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.default_marker))
//                        marker.position(LatLng(positionArray[i][0], positionArray[i][1]))
//                        mMap.addMarker(marker)
//
//                    }
//                }
                /////////////////////////////////////////////////////////////////////////
            }
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

        var placeNm = markerItem.PlaceNm
        var placePosition = markerItem.latLng
        var mCount = markerItem.count
        //Log.d("alert_출력",markerItem.count.toString())

        var markerOptions = MarkerOptions()
        markerOptions.position(placePosition!!)
        markerOptions.title(placeNm)
        markerOptions.snippet(mCount.toString())

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
        var placeID = null
        var placeNm = marker.title
        var placeType = null
        var latlng = marker.position
        var count = marker.snippet.toInt()
        var temp = AddPlaceItem(placeID, placeNm, placeType, latlng, count)

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
        setSupportActionBar(toolbar_add_place)
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
