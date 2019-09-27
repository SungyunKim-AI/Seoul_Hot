package com.inseoul.make_plan

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_make_plan.*
import java.util.*
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import com.inseoul.add_place.AddPlaceActivity


class MakePlanActivity : AppCompatActivity() {


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
//      fetchJson()             //서버 파일 다운로드
        initBtn()               //새로운 일정 버튼 클릭 리스너
//      initRecyclerView()
        calendar()

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


    //////////////다음 액티비티로 넘어가는 부분
    val REQ_CODE: Int = 10000

    fun calendar(){
        // Setting
        calendar_view.weekendDayTextColor = Color.parseColor("#FF0000")

        calendar_view.selectionType = SelectionType.RANGE

        calendar_view.currentDayIconRes = R.drawable.ic_down_arrow

        calendar_view.selectedDayBackgroundStartColor = Color.parseColor("#B6E3E9")
        calendar_view.selectedDayBackgroundEndColor = Color.parseColor("#B6E3E9")
        calendar_view.selectedDayBackgroundColor = Color.parseColor("#D9F1F1")

    }

    fun initBtn() {
        continueBtn.setOnClickListener {


            // Range Picker
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

                val intent = Intent(this, AddPlaceActivity::class.java)

                intent.putExtra("startDate",startYear+"-"+startMonth+"-"+startDay)
                intent.putExtra("endDate",endYear+"-"+endMonth+"-"+endDay)
                intent.putExtra("PlanDate", resultStr)
                intent.putExtra("PlanRange", range)
                intent.putExtra("flag_key",1)

                startActivityForResult(intent, REQ_CODE)

            } else{
                Toast.makeText(this,"날짜를 입력해주세요", Toast.LENGTH_SHORT).show()
            }


        }
        addFriendBtn.setOnClickListener{
            Toast.makeText(this,"친구일정 불러오기",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}





