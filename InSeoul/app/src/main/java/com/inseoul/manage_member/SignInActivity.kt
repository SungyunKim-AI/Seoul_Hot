package com.inseoul.manage_member

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.inseoul.MainActivity
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        init()
    }

    fun init() {
        signInBtn.setOnClickListener {

            ////////// Log in Permission Check //////////


            /////////////////////////////////////////////

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        signUpBtn.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
