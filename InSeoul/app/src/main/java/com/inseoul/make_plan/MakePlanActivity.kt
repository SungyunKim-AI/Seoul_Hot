package com.inseoul.make_plan

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_make_plan.*
import java.util.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class MakePlanActivity : AppCompatActivity() {

    //datepicker 선언
    //var button_date_start: Button? = null
    var textview_date_start: EditText? = null
    //var button_date_end: Button? = null
    var textview_date_end: EditText? = null
    var textview_time_start: EditText? = null
    var textview_time_end: EditText? = null
    var cal = Calendar.getInstance()

    var textview_plan_title: EditText? = null

    var str_start: String? = null
    var str_end: String? = null
    var theme: String? = null

    // 날짜 저장
    lateinit var range:String

    lateinit var startYear:String
    lateinit var startMonth:String
    lateinit var startDay:String

    lateinit var endYear:String
    lateinit var endMonth:String
    lateinit var endDay:String

    lateinit var startDate:String
    lateinit var endDate:String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_plan)
        initBtn()
        initTest()
        initRecyclerView()
        btnInit()

        //날짜 입력 레퍼런스 파일
        textview_plan_title = this.PlanTitle

        initTheme()

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


    //////////DatePicker Dialog

    inline fun Activity.showAlertDialog(func: RangePickerActivity.() -> Unit): AlertDialog =
        RangePickerActivity(this).apply {
            func()
        }.create()

    fun btnInit(){
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
//                        startDate= startYear + "년" + startMonth + "월" + startDay + "일"

                        val end_calendar = days.get(r - 1)
                        endDay = end_calendar.get(Calendar.DAY_OF_MONTH).toString()
                        endMonth = (end_calendar.get(Calendar.MONTH) + 1).toString()
                        endYear = end_calendar.get(Calendar.YEAR).toString()
//                        endDate = endYear + "년" + endMonth + "월" + endDay + "일"

                        var resultStr = ""
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
                    } else{
                        Log.v("Dialog", "null")
                    }
                }
                cancelBtnClickListener {
                    Log.v("Dialog", "cancel")
                }
            }
            dialog.show()
            /*
            CalendarDialog(this, OnDaysSelectionListener {
                var days = it
                var range = it.size
                Toast.makeText(applicationContext, it[0].toString() + "~" + it[range-1].toString(), Toast.LENGTH_SHORT).show()

            }
            */
//            ).setCalendarOrientation(HORIZONTAL)
            //        calendar_view.weekendDayTextColor = Color.parseColor("#FF0000")
            //
            //        calendar_view.calendarOrientation = HORIZONTAL
            //        calendar_view.selectionType = SelectionType.RANGE
            //
            //        calendar_view.currentDayIconRes = R.drawable.ic_down__arrow
            //
            //        calendar_view.selectedDayBackgroundStartColor = Color.parseColor("#B6E3E9")
            //        calendar_view.selectedDayBackgroundEndColor = Color.parseColor("#B6E3E9")
            //        calendar_view.selectedDayBackgroundColor = Color.parseColor("#D9F1F1")
        }
    }


    fun initTheme() {
        theme_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (theme_spinner.getItemAtPosition(position)) {
                    "역사" -> {
                        theme = "역사"
                    }
                    "맛집" -> {
                        theme = "맛집"
                    }
                    "힐링" -> {
                        theme = "힐링"
                    }
                    else -> {
                        theme = null
                    }
                }

            }
        }
    }


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
            intent.putExtra("PlanTitle", textview_plan_title!!.text.toString())
            intent.putExtra("PlanDate", str_start + " ~ " + str_end)
            intent.putExtra("PlanTheme", theme.toString())
            intent.putExtra("flag_key",2)

            startActivityForResult(intent, REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
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


    ////////////////// Recycler View //////////////////
    private val test = ArrayList<MakePlanItem>()
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: MakePlanAdapter? = null

    fun initTest(){

        for(i in 0..10){
            test.add(MakePlanItem("This is Title" + i.toString(), "This is Content" + i.toString()))
        }
    }

    fun initRecyclerView(){

        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val listener = object: MakePlanAdapter.RecyclerViewAdapterEventListener{
            override fun onClick(view: View, position: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates
                inform_layout.visibility = View.GONE
//                recyclerview.layoutParams.width = MATCH_PARENT
//                recyclerview_layout.layoutParams.weight =

                //intent로 장소 이름 전달
//                val detailsIntent = Intent(this, SearchDetail::class.java)
//                detailsIntent.putExtra("search_title",test[position].title)
//                startActivity(detailsIntent)
            }
            //리사이클러 뷰를 클릭했을때 SearchDetails 액티비티로 넘어가는 클릭 리스너
        }

        adapter = MakePlanAdapter(this, listener, test)
        recyclerView.adapter = adapter
    }


}





