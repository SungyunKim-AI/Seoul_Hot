package com.inseoul.make_plan

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_make_plan.*
import java.text.SimpleDateFormat
import java.util.*
import android.view.MotionEvent
import android.widget.*


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_plan)
        initBtn()

        //날짜 입력 포커스 되면 키보드 비활성화
        date_text_start!!.showSoftInputOnFocus = false
        date_text_end!!.showSoftInputOnFocus = false

        //날짜 입력 레퍼런스 파일
        textview_date_start = this.date_text_start
        textview_time_start = this.time_text_start
        //button_date_start = this.DatePickBtn_start

        textview_date_end = this.date_text_end
        textview_time_end = this.time_text_end
        //button_date_end = this.DatePickBtn_end

        textview_plan_title = this.PlanTitle

        dateSetBtn_start()
        dateSetBtn_end()

        timeSetBtn_start()
        timeSetBtn_end()

        initTheme()

        //toolbar 커스텀 코드
        val mtoolbar = findViewById(R.id.toolbar_make_plan) as Toolbar
        setSupportActionBar(mtoolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        mtoolbar.title = "NEW PLAN"
        mtoolbar.setTitleTextColor(Color.WHITE)
    }


    //////////DatePicker Dialog, TimePicker Dialog
    fun dateSetBtn_start() {
        pickDate(1)
    }

    fun dateSetBtn_end() {
        pickDate(2)
    }

    fun timeSetBtn_start() {
        TimeTouchListener(1)
    }

    fun timeSetBtn_end() {
        TimeTouchListener(2)
    }

    fun pickDate(flag: Int) {
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(flag)
            }
        }
        if (flag == 1) {
            textview_date_start!!.setOnTouchListener(View.OnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        DatePickerDialog(
                            this@MakePlanActivity, dateSetListener,
                            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                }
                false
            })
        } else if (flag == 2) {
            textview_date_end!!.setOnTouchListener(View.OnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        DatePickerDialog(
                            this@MakePlanActivity, dateSetListener,
                            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                }
                false
            })
        }

    }

    private fun updateDateInView(flag: Int) {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.KOREA)
        if (flag == 1) {
            textview_date_start!!.setText(sdf.format(cal.time))
            str_start = textview_date_start!!.text.toString()
            textview_time_start!!.requestFocus()
            focus_event()

        } else if (flag == 2) {
            textview_date_end!!.setText(sdf.format(cal.time))
            str_end = textview_date_end!!.text.toString()
            textview_time_end!!.requestFocus()
            focus_event()
        }
    }

    fun focus_event() {
        if (currentFocus == textview_time_start) {
            pickTime(1)
        } else if (currentFocus == textview_time_end) {
            pickTime(2)
        }
    }

    fun TimeTouchListener(flag: Int) {
        if (flag == 1) {
            textview_time_start!!.setOnTouchListener(View.OnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pickTime(flag)
                    }
                }
                false
            })
        } else if (flag == 2) {
            textview_time_end!!.setOnTouchListener(View.OnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pickTime(flag)
                    }
                }
                false
            })
        }

    }

    fun pickTime(flag: Int) {

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            if (flag == 1) {
                textview_time_start!!.setText(SimpleDateFormat("HH:mm").format(cal.time))
                str_start += " :: "
                str_start += textview_time_start!!.text
            } else if (flag == 2) {
                textview_time_end!!.setText(SimpleDateFormat("HH:mm").format(cal.time))
                str_end += " :: "
                str_end += textview_time_end!!.text
            }

        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
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
            if (str_start == null || str_end == null || textview_plan_title == null || theme == null) {
                Toast.makeText(this, "미입력", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this, AddPlaceActivity::class.java)
                intent.putExtra("PlanTitle", textview_plan_title!!.text.toString())
                intent.putExtra("PlanDate", str_start + " ~ " + str_end)
                intent.putExtra("PlanTheme", theme.toString())
                Log.d("alert", theme.toString())

                startActivityForResult(intent, REQ_CODE)
            }
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
}