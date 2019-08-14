package com.inseoul.make_plan

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
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


    val REQ_CODE:Int = 10000
    fun initBtn(){
        continueBtn.setOnClickListener {
            val intent = Intent(this, AddPlaceActivity::class.java)
            startActivityForResult(intent, REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
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
