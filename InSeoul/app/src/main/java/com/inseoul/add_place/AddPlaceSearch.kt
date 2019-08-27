package com.inseoul.add_place

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_search.*

class AddPlaceSearch : AppCompatActivity() {

    val RESULT_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar_search)
        initTest()

        //toolbar 커스텀 코드
        val mtoolbar = findViewById(R.id.toolbar_search) as Toolbar
        setSupportActionBar(mtoolbar)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
//        mtoolbar.title = "검색"
//        mtoolbar.setTitleTextColor("#000000")
        searchView.performClick()
        searchView.requestFocus()
        searchView.isSubmitButtonEnabled = true;

        // Search View EventListener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            // Text Change
            override fun onQueryTextChange(p0: String?): Boolean {
                Log.d("Text Change",p0)

                //////////////////////// DB Connect & Query ////////////////////////











                /////////////////////////////////////////////////////////////////////

                return false
            }

            // Submit
            override fun onQueryTextSubmit(p0: String?): Boolean {
//                textView.text = p0
//                Toast.makeText(applicationContext,"Submit Button!",Toast.LENGTH_SHORT).show()
                Log.d("Submit",p0)

                //////////////////////// DB Connect & Query ////////////////////////







                /////////////////////////////////////////////////////////////////////

                initRecyclerView()

                return false
            }
        })
    }

    ////////////////// Recycler View //////////////////
    private val test = ArrayList<AddPlaceItem>()
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: AddPlaceAdapter? = null

    fun initTest(){

        test.add(AddPlaceItem("This is Title0" , "This is Content0" , LatLng(37.567899,127.009506)))
        test.add(AddPlaceItem("This is Title1", "This is Content1", LatLng(37.520246,126.940682)))
        for(i in 2..10){
            test.add(AddPlaceItem("This is Title" + i.toString(), "This is Content" + i.toString(), LatLng(37.567899,127.009506)))
        }
    }




    fun initRecyclerView(){
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val listener = object: AddPlaceAdapter.RecyclerViewAdapterEventListener{
            override fun onClick(view: View, position: Int) {
                val intent = Intent()
                intent.putExtra("placeData",test[position])
                setResult(RESULT_CODE,intent)
                finish()
            }
            //리사이클러 뷰를 클릭했을때 SearchDetails 액티비티로 넘어가는 클릭 리스너
        }

        adapter = AddPlaceAdapter(this, listener, test)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, 1))
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

