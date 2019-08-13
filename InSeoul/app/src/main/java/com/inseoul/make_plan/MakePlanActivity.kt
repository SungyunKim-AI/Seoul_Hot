package com.inseoul.make_plan

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.text.SimpleDateFormat
import java.util.*

class MakePlanActivity : AppCompatActivity() {

    var button_date: Button? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_plan)
        initBtn()

        //날짜 입력 레퍼런스 파일
        textview_date = this.date_text
        button_date = this.DatePickBtn

        textview_date!!.text="--/--/----"

        dateSetBtn()
    }

    fun initBtn(){
        continueBtn.setOnClickListener {
            val intent = Intent(this, AddPlaceActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun dateSetBtn(){
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        button_date!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@MakePlanActivity, dateSetListener,
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.KOREA)
        textview_date!!.text = sdf.format(cal.getTime())
    }



}
