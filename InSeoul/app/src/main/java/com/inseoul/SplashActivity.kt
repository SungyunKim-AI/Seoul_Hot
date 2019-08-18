package com.inseoul

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inseoul.manage_member.SignInActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }
    fun init(){
        var intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}
