package com.inseoul.make_plan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_make_plan.*

class MakePlanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_plan)
        initBtn()
    }

    fun initBtn(){
        continueBtn.setOnClickListener {
            val intent = Intent(this, AddPlaceActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}
