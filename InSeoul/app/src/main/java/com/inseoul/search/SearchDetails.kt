package com.inseoul.search

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.inseoul.R


class SearchDetails : AppCompatActivity(), View.OnClickListener {

    var m_name: String? = null

    //라디오 버튼
    var textview_count: Int = 3
    var add_name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_details)

        init()
        initToolbar()
    }

    //초기 세팅
    fun init() {
        val extras = intent.extras
        m_name = extras!!.getString("search_title", "NULL")

        val v = findViewById<Button>(R.id.add_my_list)
        v.setOnClickListener(this)
    }

    //테스크 코드에서 여행 일정 불러와서 dialog 띄우기
    override fun onClick(v: View) {
        ///////////////TestCode/////////////
        val btn_list = ArrayList<viewItem>()
        for (i in 0 until textview_count) {
            btn_list.add(viewItem("test_title$i", "test_date$i"))
        }
        ////////////////////////////////////
        when (v.id) {
            R.id.add_my_list -> {

                val alertBuilder = AlertDialog.Builder(this)
                //alertBuilder.setIcon(R.drawable.ic_launcher)
                alertBuilder.setTitle("장소를 추가할 여행을 선택해주세요")

                // List Adapter 생성
                val adapter = ArrayAdapter<String>(this, R.id.list_add_place)

                ///////////////TestCode/////////////
                for (i in 0 until textview_count) {
                    adapter.add(btn_list[i].mtitle)
                }

                // 버튼 생성
                alertBuilder.setNegativeButton("취소",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            dialog.dismiss()
                        }
                    })

                // Adapter 셋팅
                alertBuilder.setAdapter(adapter,
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id: Int) {

                            // AlertDialog 안에 있는 AlertDialog
                            val strName = adapter.getItem(id)
                            val innBuilder = AlertDialog.Builder(this@SearchDetails)
                            innBuilder.setMessage(strName)
                            innBuilder.setTitle("당신이 선택한 것은")
                            innBuilder
                                .setPositiveButton("확인",
                                    object : DialogInterface.OnClickListener {
                                        override fun onClick(dialog: DialogInterface, which: Int) {
                                            dialog.dismiss()
                                        }
                                    })
                            innBuilder.show()
                        }
                    })
                alertBuilder.show()
            }
            else -> {
            }
        }
    }


    //툴바 세팅
    fun initToolbar() {
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
    internal class viewItem(var mtitle: String, var mdate: String)
}
