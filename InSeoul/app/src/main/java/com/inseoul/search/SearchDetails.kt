package com.inseoul.search

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_search_details.*

class SearchDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_details)

        var m_name:String
        val extras = intent.extras
        m_name = extras!!.getString("search_title", "NULL")

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
}
