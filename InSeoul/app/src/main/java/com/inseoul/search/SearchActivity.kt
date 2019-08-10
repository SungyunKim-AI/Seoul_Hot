package com.inseoul.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initTest()
        initBtn()
    }

    fun initBtn(){
        search.setOnClickListener {
            initRecyclerView()
        }
    }

    ////////////////// Recycler View //////////////////
    private val test = ArrayList<SearchItem>()
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: SearchAdapter? = null

    fun initTest(){

        for(i in 0..10){
            test.add(SearchItem("This is Title" + i.toString(), "This is Content" + i.toString()))
        }
    }




    fun initRecyclerView(){
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val listener = object: SearchAdapter.RecyclerViewAdapterEventListener{
            override fun onClick(view: View) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        adapter = SearchAdapter(this, listener, test)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, 1))
    }
}
