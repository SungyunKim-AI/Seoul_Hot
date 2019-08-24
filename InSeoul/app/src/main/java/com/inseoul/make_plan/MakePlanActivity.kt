package com.inseoul.make_plan

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_make_plan.*
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL


class MakePlanActivity : AppCompatActivity() {

    //JSON LIST
    var planList = ArrayList<MakePlanItem>()

    // 날짜 저장
    lateinit var range:String

    lateinit var startYear:String
    lateinit var startMonth:String
    lateinit var startDay:String

    lateinit var endYear:String
    lateinit var endMonth:String
    lateinit var endDay:String

    lateinit var resultStr:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_plan)

        initToolbar()           //툴바 세팅
        fetchJson()             //서버 파일 다운로드
        initBtn()               //새로운 일정 버튼 클릭 리스너
        initRecyclerView()

    }

    fun initToolbar(){
        //toolbar 커스텀 코드
        val mtoolbar = findViewById(R.id.toolbar_make_plan) as Toolbar
        setSupportActionBar(mtoolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


        ///////////////////////////////////////////////////////

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



    //////////DatePicker Dialog
    inline fun Activity.showAlertDialog(func: RangePickerActivity.() -> Unit): AlertDialog =
        RangePickerActivity(this).apply {
            func()
        }.create()

    //////////////다음 액티비티로 넘어가는 부분
    val REQ_CODE: Int = 10000

    fun initBtn() {
        continueBtn.setOnClickListener {

            //미입력 부분이 있을시 toast 출력 및 버튼 비활성화
//            if (str_start == null || str_end == null || textview_plan_title == null || theme == null) {
//                Toast.makeText(this, "미입력", Toast.LENGTH_LONG).show()
//            } else {
//                val intent = Intent(this, AddPlaceActivity::class.java)
//                intent.putExtra("PlanTitle", textview_plan_title!!.text.toString())
//                intent.putExtra("PlanDate", str_start + " ~ " + str_end)
//                intent.putExtra("PlanTheme", theme.toString())
//                intent.putExtra("flag_key",2)
//
//                startActivityForResult(intent, REQ_CODE)
//            }

            val intent = Intent(this, AddPlaceActivity::class.java)

            intent.putExtra("PlanDate", resultStr)
            intent.putExtra("PlanRange", range)
            intent.putExtra("flag_key",2)

            startActivityForResult(intent, REQ_CODE)
        }

        select_date.setOnClickListener {

            val dialog:AlertDialog = showAlertDialog {
                cancelable = false
                selectBtnClickListener {
                    Log.v("Dialog", "submit")
                    var days = calendar_view.selectedDates
                    var r = days.size
                    range = r.toString() + "일"

                    if(r != 0){
                        val start_calendar = days.get(0)
                        startDay = start_calendar.get(Calendar.DAY_OF_MONTH).toString()
                        startMonth = (start_calendar.get(Calendar.MONTH) + 1).toString()
                        startYear = start_calendar.get(Calendar.YEAR).toString()

                        val end_calendar = days.get(r - 1)
                        endDay = end_calendar.get(Calendar.DAY_OF_MONTH).toString()
                        endMonth = (end_calendar.get(Calendar.MONTH) + 1).toString()
                        endYear = end_calendar.get(Calendar.YEAR).toString()

                        if(startYear == endYear){
                            resultStr = startYear + "." + startMonth + "." + startDay + " - " + endMonth + "." + endDay
                        }
                        if(startYear == endYear && startMonth == endMonth && startDay == endDay){
                            resultStr = startYear + "." + startMonth + "." + startDay
                        }
                        rangeDay.text = range
                        select_date.text = resultStr
//                        date_text_start.text = startDate
//                        date_text_end.text = endDate
                        Log.v("Dialog", startDay + endDay)
                        continueBtn.isEnabled = true
                    } else{
                        Log.v("Dialog", "null")
                    }
                }
                cancelBtnClickListener {
                    Log.v("Dialog", "cancel")
                }
            }
            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
    }



    ////////////////// Recycler View //////////////////
    //private val planData = ArrayList<MakePlanItem>()

    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: MakePlanAdapter? = null

    var img = ArrayList<ImgItem>()
    var reviewSize = -1
    fun initTest(){

        Log.v("size", reviewSize.toString())
        // ******************************************** //
        // Okhttp Callback -> notifyDataSetUpdate Call  //
        // ******************************************** //

        ///////////// TestImage Setting /////////////
        for(i in 0..2 - 1){
            var rnd = Random()
            var n = rnd.nextInt(5) + 1

            val temp = ArrayList<Drawable?>()
            for(j in 0..n){
                val index = rnd.nextInt(5) + 1
                val str = "sample$index"
                Log.v("TestImg", str)
                temp.add(baseContext.getDrawable(baseContext.resources.getIdentifier(str, "drawable",  baseContext.packageName)))
            }
            img.add(ImgItem(temp))
        }

        /////////////////////////////////////////////
    }
    fun initRecyclerView(){

        initTest()

        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val listener = object: MakePlanAdapter.RecyclerViewAdapterEventListener{
            override fun onClick(view: View, position: Int) {

                //intent로 장소 이름 전달
                val recommendIntent = Intent(this@MakePlanActivity, RecommendPlan::class.java)
                recommendIntent.putExtra("planData",planList[position])
                recommendIntent.putExtra("plan_array",planList[position].PLAN)
                Log.d("alert_sdf",planList[position].PLAN.toString())
                startActivity(recommendIntent)
            }
        }
        adapter = MakePlanAdapter(this, listener, planList, img)
        recyclerView.adapter = adapter
    }


    fun fetchJson() {

        val url = URL("http://ksun1234.cafe24.com/PlanList.php")
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                //println("Success to execute request! : $body")
                val jsonObject = JSONObject(body)


                val movieArray = jsonObject.getJSONArray("response")

                reviewSize = movieArray.length()
                for (i in 0 until movieArray.length()) {
                    val movieObject = movieArray.getJSONObject(i)
                    val cache_nm = movieObject.getString("TRIP_NAME")
                    planList.add(MakePlanItem(cache_nm))
                    val cache_theme = movieObject.getString("THEME")
                    planList[i].THEME = cache_theme
                    val cache_likes = movieObject.getString("LIKES")
                    planList[i].LIKES = cache_likes.toInt()
                    val cache_plan = movieObject.getString("PLAN").split(",")
                    for (j in 0 until cache_plan.size) {
                        planList[i].PLAN.add(j,cache_plan[j].toInt())
                    }
                    /////////////preview 작성 필요///////////////
                    planList[i].preview = "This is preview   $i"

                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request!")
            }
        })


    }


}




