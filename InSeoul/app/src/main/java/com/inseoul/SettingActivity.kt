package com.inseoul

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import com.inseoul.manage_member.SaveSharedPreference
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initToolbar()
        init()
        initBtn()
    }
    fun licenseDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("라이센스 정보")
        builder.setMessage(R.string.licence)

        builder.setNeutralButton("닫기") { _, _ ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun init(){
        if(loginCheck()){
            user_name.text = SaveSharedPreference.getUserID(this)
            sign_in.visibility = GONE
            sign_out.visibility = VISIBLE
        } else {
            user_name.text = "로그인을 해주세요"
            sign_in.visibility = VISIBLE
            sign_out.visibility = GONE

        }
    }

    fun loginCheck(): Boolean {
        return (SaveSharedPreference.getUserID(this) != "")
    }
    fun initBtn(){
        licence.setOnClickListener {
            licenseDialog()
        }
        faq.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/sjEdtqFb"))
            startActivity(intent)
        }
        sign_in.setOnClickListener {

        }
        sign_out.setOnClickListener {

        }

    }
    ////////////////Toolbar//////////////
    fun initToolbar() {
        //toolbar 커스텀 코드
        setSupportActionBar(toolbar_setting)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        toolbar_setting.bringToFront()
    }

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
