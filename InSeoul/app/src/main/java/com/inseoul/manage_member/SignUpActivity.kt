package com.inseoul.manage_member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        init()
    }

    fun init(){
        sign_up_submit.setOnClickListener {
            ////////// Connect DB //////////


            ////////////////////////////////
            finish();
        }
    }
}
