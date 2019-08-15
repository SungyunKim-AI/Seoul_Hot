package com.inseoul.search

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.inseoul.R
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_search_details.*
import kotlinx.android.synthetic.main.dialog_add_place.*
import java.util.ArrayList


class SearchDetails : AppCompatActivity() {

    var m_name:String? = null

    //라디오 버튼
    var radioBtn_count:Int = 3
    var add_name:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_details)

        init()
        initToolbar()
    }

    //초기 세팅
    fun init(){
        val extras = intent.extras
        m_name = extras!!.getString("search_title", "NULL")


        add_my_list.setOnClickListener {
            initDialog()
        }
    }

    //테스크 코드에서 여행 일정 불러와서 dialog 띄우기
    fun initDialog(){
        add_my_list.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_place, null)
            val dialogTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
            val dialogRadioGroup = dialogView.findViewById<RadioGroup>(R.id.dialog_radio_group)

            ///////////////TestCode/////////////
            val btn_list = ArrayList<radioItem>()
            for (i in 0 until radioBtn_count) {
                btn_list.add(radioItem("test_title$i", "test_date$i"))
            }


            for (i in 0 until radioBtn_count) {
                val radio_btn = RadioButton(this)
                radio_btn.id = i
                radio_btn.setText(btn_list[i].mtitle)
                radio_btn.height = WRAP_CONTENT
                radio_btn.width = WRAP_CONTENT
                //radio_btn.setTextColor(Color.rgb(0, 0, 0))
                //radio_btn.setLayoutParams(LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT))
                //radio_btn.setButtonDrawable(R.drawable.draw_radio_btn)
                //radio_btn.setTypeface(Bold_kor)
                //radio_btn.setTextSize(12)

                dialogRadioGroup.addView(radio_btn)
            }

            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    //확인 버튼 클릭시 클릭한 라디오 버튼 중에서 선택
                    Log.d("alert","확인버튼 클릭")

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                }
                .show()
        }
    }


    //툴바 세팅
    fun initToolbar(){
        //toolbar 커스텀 코드
        val mtoolbar = findViewById(R.id.toolbar_search_details) as Toolbar
        setSupportActionBar(mtoolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        mtoolbar.title = m_name
        mtoolbar.setTitleTextColor(Color.WHITE)
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

    ///////////////TestCode/////////////
    internal class radioItem(var mtitle: String, var mdate: String)
}
