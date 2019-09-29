package com.inseoul.add_place

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.inseoul.R
import com.inseoul.Server.AddPlaceRegister
import com.inseoul.search.SearchActivity
import com.inseoul.search.Search_Item
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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.inseoul.BackPressCloseHandler
import com.inseoul.MainActivity
import com.inseoul.Server.DeletePlanRequest
import com.inseoul.Server.ShowPlanRegister
import com.inseoul.Server.UpdatePlanRequest
import com.inseoul.home.HomeFragment
import com.inseoul.manage_member.SaveSharedPreference
import com.inseoul.manage_member.ValidateRequest
import com.inseoul.my_page.MyPageFragment
import com.inseoul.my_page.MyPage_Item
import kotlinx.android.synthetic.main.activity_add_place_2.*
import java.text.SimpleDateFormat


class AddPlaceActivity :
    AppCompatActivity(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    lateinit var backPressCloseHandler: BackPressCloseHandler

    lateinit var mMap: GoogleMap
    var selectedMarker: Marker? = null
    var dayList = ArrayList<ArrayList<AddPlaceItem>>()

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
    private var PLANID = 0
    private var MEM = ""

    lateinit var fade_out: Animation
    lateinit var fade_in: Animation
    lateinit var rotate_open: Animation
    lateinit var rotate_close: Animation
    var isBtnOpen: Boolean = false

    lateinit var searchitm: MyPage_Item
    lateinit var tempItem: Search_Item


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place_main)
        MEM = SaveSharedPreference.getUserID(this)
        initBackHandler()
        initToolbar()
        init()
        initMap()

    }


    ////////////////////////////////////////////////////////////////////////////////////////
    lateinit var adapter: AddPlace_ViewPagerAdapter
    var flag: Int = 1234

    fun init() {

        val extras = intent.extras
        flag = extras!!.getInt("flag_key", -1)
        DPDATE = extras!!.getString("startDate", "2222-22-22")
        ADDATE = extras!!.getString("endDate", "2222-22-22")

        // from MakePlanActivity
        if (flag == 1) {
            //날짜 차이 계산 하기
            val start = extras!!.getString("startDate")
            val end = extras!!.getString("endDate")
            initRecylcerview(start!!, end!!)

            val date = extras!!.getString("PlanDate", "NULL")
//            PlanTitle.hint = date + " 여정"

            textview_plandate.text = date
        } else {
            //from MyPageActivity
            PLANID = extras!!.getInt("PlanID")

            if (flag == 3) {
                tempItem = extras.getParcelable("placeData")!!
            }

            RequestPlanItem(PLANID, flag)

        }


        /////////////완료 버튼/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        finishBtn.setOnClickListener {
            savePlan()
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
            intent.putExtra("flag", flag)
            startActivityForResult(intent, 2000)
        }


        deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this@AddPlaceActivity)
            builder.setMessage("등록 된 장소들이 삭제됩니다.\n일정을 삭제하시겠습니까?")

            builder.setPositiveButton("확인") { dialog, id ->
                anim()

                val responseListener = Response.Listener<String> { response ->
                    Log.d("delkdet", PLANID.toString())
                }
                val idnumrequest = DeletePlanRequest(PLANID.toString(), responseListener)
                val queue = Volley.newRequestQueue(this@AddPlaceActivity)
                queue.add(idnumrequest)


                if (flag == 2 || flag == 3) {
                    val intent = Intent()
                    intent.putExtra("result", 1)    // TEST CODE
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    finish()
                }

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

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(PlanTitle.windowToken, 0)
                true
            } else {
                false
            }
        }


    }


    ////////////////////날짜 계산해서 개수 만큼 뷰페이저 생성///////////////////////
    fun initRecylcerview(start: String, end: String) {

        var formatter = SimpleDateFormat("yyyy-MM-dd")
        val beginDate = formatter.parse(start)
        val endDate = formatter.parse(end)

        val diff = endDate.getTime() - beginDate.getTime()
        val diffDays = ((diff / (24 * 60 * 60 * 1000)) + 1).toInt()

        for (i in 0..diffDays) {
            val t = ArrayList<AddPlaceItem>()
            dayList.add(t)
        }

        val listener = object : AddPlace_ViewPagerAdapter.ViewPagerAdapterEventListener {
            override fun onChangeCallback2(view: View, itemlist: ArrayList<ArrayList<AddPlaceItem>>) {
                dayList = itemlist
                initMarker()
            }

            //일정 추가 버튼
            override fun onClick(view: View, position: Int) {
                val intent = Intent(this@AddPlaceActivity, SearchActivity::class.java)
                intent.putExtra("flag", true)
                startActivityForResult(intent, 3000)
            }

            //친구 추가 버튼
            override fun on_friendBtn_Click(view: View, position: Int) {

                var friend_dialog = AlertDialog.Builder(this@AddPlaceActivity)
                val dialogView = layoutInflater.inflate(R.layout.dialog_edittext, null)
                val dialogText = dialogView.findViewById<EditText>(R.id.addboxdialog)

                friend_dialog.setTitle("친구와 함께 일정 만들기")
                    .setMessage("친구의 아이디를 입력하세요")
                    .setIcon(R.drawable.ic_group_add_black_24dp)
                    .setView(dialogView)
                    .setPositiveButton("추가") { dialogInterface, i ->


                        val responseListener = Response.Listener<String> { response ->
                            try {
                                val jsonResponse = JSONObject(response)
                                val success = jsonResponse.getBoolean("success")
                                if (success) {
                                    Log.d("sdfsdfs",dialogText.text.toString())
                                    Toast.makeText(this@AddPlaceActivity ,"이런 친구를 찾지 못했어요!",Toast.LENGTH_LONG).show()

                                } else {
                                    Toast.makeText(this@AddPlaceActivity ,"친구 등록 성공!",Toast.LENGTH_LONG).show()
                                    MEM += "&" + dialogText.text.toString()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        val validateRequest = ValidateRequest(dialogText.text.toString(), responseListener)
                        val queue = Volley.newRequestQueue(this@AddPlaceActivity)
                        queue.add(validateRequest)

                    }
                    .setNeutralButton("취소") { dialogInterface, i ->
                    }
                    .show()
            }
        }


        adapter = AddPlace_ViewPagerAdapter(this, diffDays, listener, dayList)
        add_place_viewpager.adapter = adapter


        TabLayoutMediator(tabLayout_addPlace, add_place_viewpager, object : TabLayoutMediator.OnConfigureTabCallback {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {

                var day = position + 1
                tab.setText("day-$day")

            }
        }).attach()

        if (tabLayout_addPlace.tabCount > 5) {
            tabLayout_addPlace.tabMode = TabLayout.MODE_SCROLLABLE
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
                    flag = data!!.getIntExtra("flag",1)
//                    Log.d("alert_back_flag",flag.toString())

                    PlanTitle.hint = edit_resultStr.toString() + " 여정"
                    textview_plandate.text = edit_resultStr.toString()
                    DPDATE = data.getStringExtra("DPDATE")
                    ADDATE = data.getStringExtra("ADDATE")
                    Toast.makeText(this, "일정 변경 완료", Toast.LENGTH_SHORT).show()
                }
                3000 -> {
                    //Search에서 넘어왔을 때

                    val item = data!!.getParcelableExtra<Search_Item>("placeData")

                    var selectDate = add_place_viewpager.currentItem

                    dayList[selectDate].add(
                        AddPlaceItem(
                            selectDate,
                            item.id,
                            item.title,
                            item.type,
                            LatLng(item.posY!!, item.posX!!),
                            dayList[selectDate].size + 1
                        )
                    )
                    adapter.notifyDataSetChanged()

                }
            }

            initMarker()

        }
    }

    //마커 새로 찍기
    fun initMarker() {

        mMap.clear()
        //var markerList = ArrayList<AddPlaceItem>()
        var markerList = dayList[add_place_viewpager.currentItem]

        for (i in 0 until markerList.size) {
            addMarker(markerList[i], false)
        }

        getMarkerItems()

        if (markerList.size != 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dayList[add_place_viewpager.currentItem][0].latLng, 12f))
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.552122, 126.988270), 12f))
        }
    }

    fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }


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

        }

        //View Page Change Call Back
        val PageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                //선택했던 마커 되돌리기
                if (selectedMarker != null) {
                    selectedMarker = null
                }

                initMarker()
            }
        }
        add_place_viewpager.registerOnPageChangeCallback(PageChangeCallback)

    }

    //폴리 라인 만들기
    fun getMarkerItems() {
        var lineList = ArrayList<LatLng>()
        for (i in 0 until dayList[add_place_viewpager.currentItem].size) {
            lineList.add(dayList[add_place_viewpager.currentItem][i].latLng!!)
        }

        val polyline = mMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .addAll(lineList)
        )
        polyline.width = POLYLINE_STROKE_WIDTH_PX
        polyline.pattern = PATTERN_POLYLINE_DOTTED
    }

    var placeOrder: Int = 0
    private fun addMarker(
        markerItem: AddPlaceItem,
        isSelectedMarker: Boolean
    ): Marker {

        var placeNm = markerItem.PlaceNm
        var placePosition = markerItem.latLng
        var mCount = markerItem.count

        var markerOptions = MarkerOptions()
        markerOptions.position(placePosition!!)
        markerOptions.title(placeNm)
        markerOptions.snippet(mCount.toString() + " 번째 일정")

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
        var count = marker.snippet.split(" ")[0].toInt()
        Log.d("alert_count",marker.snippet.split(" ")[0])
        var selectDate = dayList[add_place_viewpager.currentItem][count - 1].date
        var temp = AddPlaceItem(selectDate, placeID, placeNm, placeType, latlng, count)

        return addMarker(temp, isSelectedMarker)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        var center: CameraUpdate = CameraUpdateFactory.newLatLng(marker?.position)
        //mMap.animateCamera(center)

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

    /////////////////////////////////SERVER BY SUNJAE//////////////////////////////////
    fun RequestPlanItem(PlanID: Int, flag: Int) {

        val id = SaveSharedPreference.getUserID(this)
        var placearr: ArrayList<AddPlaceItem>
        placearr = ArrayList()
        val responseListener = Response.Listener<String> { response ->
            try {
                Log.d("dd", response)

                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getJSONArray("response")
                var count = 0
                while (count < success.length()) {
                    val `object` = success.getJSONObject(count)

                    if (`object`.getInt("#") == PlanID) {
                        DPDATE = `object`.getString("DPDATE")
                        ADDATE = `object`.getString("ADDATE")
                        MEM = `object`.getString("MEM")
                        searchitm = MyPage_Item(
                            `object`.getInt("#"), // planID
                            `object`.getString("TripName"), // Plan 이름
                            (DPDATE), // 출발 날짜 도착날짜는  "ADDATE"

                            `object`.getString("THEME"), // 여행 주제
                            `object`.getInt("LIKES"), // 좋아요수
                            `object`.getString("Plan"), // 플랜 리스트
                            `object`.getString("MEM"), // 멤버
                            ADDATE,
                            false,
                            `object`.getString("Day")
                        )
                        val arr = `object`.getJSONArray("extra")
                        for (i in 0..arr.length() - 1) {
                            val addp = arr.getJSONObject(i)
                            var d = 0
                            Log.d("error", addp.toString())
                            when (addp.getString("TYPE")) {
                                "맛집" -> {
                                    d = 39
                                }
                                "쇼핑" -> {
                                    d = 12
                                }
                                "명소" -> {
                                    d = 14
                                }
                                else -> {
                                    d = addp.getInt("TYPE")
                                }


                            }
                            placearr.add(
                                AddPlaceItem(
                                    0, addp.getInt("PLACEID"), addp.getString("Upso_nm"), d,
                                    LatLng(addp.getDouble("Lng"), addp.getDouble("Lat")), 0
                                )
                            )
                        }
                        break
                    }

                    count++
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


            initRecylcerview(searchitm.date, searchitm.thumbnail!!)

            val start_calendar = searchitm.date.split("-")
            var startDay = start_calendar[2]
            var startMonth = start_calendar[1]
            var startYear = start_calendar[0]

            val end_calendar = searchitm.thumbnail!!.split("-")
            var endDay = end_calendar[2]
            var endMonth = end_calendar[1]
            var endYear = end_calendar[0]

            var resultStr: String? = null

            if (startYear == endYear) {
                resultStr = startYear + "." + startMonth + "." + startDay + " - " + endMonth + "." + endDay
            }
            if (startYear == endYear && startMonth == endMonth && startDay == endDay) {
                resultStr = startYear + "." + startMonth + "." + startDay
            }

            val date = resultStr
            PlanTitle.setText(searchitm.title)
            textview_plandate.text = date

            //from my_page

            var count = 0
            //날짜 받아오기
            for (selectDate in 0..searchitm.day.split(",").lastIndex - 1) {
                for (i in 0..Integer.parseInt(searchitm.day.split(",")[selectDate]) - 1) {
                    dayList[selectDate].add(
                        AddPlaceItem(
                            selectDate,
                            placearr[count].placeID,
                            placearr[count].PlaceNm,
                            placearr[count].PlaceType,
                            placearr[count++].latLng,
                            dayList[selectDate].size + 1
                        )
                    )
                }
                initMarker()
            }

            if (flag == 3) {
                //from SearchDetail

                var selectDate = add_place_viewpager.currentItem

                dayList[selectDate].add(
                    AddPlaceItem(
                        selectDate,
                        tempItem.id,
                        tempItem.title,
                        tempItem.type,
                        LatLng(tempItem.posY!!, tempItem.posX!!),
                        dayList[selectDate].size + 1
                    )
                )

            }
            adapter.notifyDataSetChanged()
        }
        val idnumrequest = ShowPlanRegister(id, responseListener)
        val queue = Volley.newRequestQueue(this@AddPlaceActivity)
        queue.add(idnumrequest)

    }

    /////////////////////Save 함수//////////////////
    fun savePlan() {
        //저장 버튼 활성화
        val extras = intent.extras

        val responseListener = Response.Listener<String> { response ->
            try {
                Log.d("d", response)
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")
                if (success) {

                    Toast.makeText(this@AddPlaceActivity, "정상적으로 등록 완료 되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()

                } else {
                    Toast.makeText(this@AddPlaceActivity, "등록을 실패 하였습니다", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        PlanName = PlanTitle.text.toString()

        THEME = "NOTHEME"
        for (i in dayList)
            for (c in i)
                PLAN = PLAN + c.placeID + ","
        var day = ""
        for (i in 0..dayList.lastIndex - 1) {
            Log.d("dd", dayList[i].toString())
            day += (dayList[i].lastIndex + 1).toString() + ","
        }

        val registerRequest =
            AddPlaceRegister(
                PlanName,
                DPDATE,
                ADDATE,
                THEME,
                PLAN,
                day,
                MEM,
                responseListener
            )
        if (flag == 2 ) {
            val r2egisterRequest = UpdatePlanRequest(PlanName,DPDATE,
                ADDATE, PLANID.toString(), PLAN, day, MEM, responseListener)
            Log.d("dd",UpdatePlanRequest(PlanName,DPDATE,
                ADDATE, PLANID.toString(), PLAN, day, MEM, responseListener).toString())
            val queue = Volley.newRequestQueue(this@AddPlaceActivity)
            queue.add(r2egisterRequest)
            val intent = Intent()
            intent.putExtra("result", 1)    // TEST CODE
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            val queue = Volley.newRequestQueue(this@AddPlaceActivity)
            queue.add(registerRequest)
            val intent = Intent()
            intent.putExtra("result", 1)    // TEST CODE
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
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

        if (item.itemId == android.R.id.home) {
            //뒤로 가기 할때
            backPressCloseHandler.onBackPressed_addPlace()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /////////////////////Animation///////////////////////
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

    ////////////////// Compute Distance //////////////////
    fun distance(lat1: Double, lat2: Double, lng1: Double, lng2: Double): Double {
        val theta = lng1 - lng2
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

    //Back버튼 두번 눌러 종료하기
    fun initBackHandler() {
        backPressCloseHandler = BackPressCloseHandler(this)
    }

    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed_addPlace()
    }

}
