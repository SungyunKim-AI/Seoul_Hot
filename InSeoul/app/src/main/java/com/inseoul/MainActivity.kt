package com.inseoul

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.home.HomeAdapter
import com.inseoul.home.HomeItem
import com.inseoul.make_plan.MakePlanActivity
import com.inseoul.manage_schedules.my_schedule
import com.inseoul.search.SearchActivity
import com.inseoul.timeline.TimeLineActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback
{
    val key = "4d4956476768736f3131397547724879" // 서울시 데이터 API Key

    /////////////// Permission Check ///////////////

    val REQ_CAMERA = 1000
    val REQ_READ_GALLERY = 1001
    val REQ_WRITE_GALLERY = 1002
    fun initPermission(){
        if(!checkAppPermission (arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE))){
            val builder = AlertDialog.Builder(this)
            builder.setMessage("겔러리 읽기에 대한 권한이 허용되어야 합니다.")
                .setTitle("권한 허용")
                .setIcon(R.drawable.abc_ic_star_black_48dp)
            builder.setPositiveButton("OK") { _, _ ->
                askPermission (arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQ_READ_GALLERY);
            }
            val dialog = builder.create()
            dialog.show()
        }else{
            Toast . makeText ( getApplicationContext (),
                "권한이 승인되었습니다." , Toast . LENGTH_SHORT ). show ();
        }
        if(!checkAppPermission (arrayOf(
                Manifest.permission.CAMERA))){
            val builder = AlertDialog.Builder(this)
            builder.setMessage("카메라에 대한 권한이 허용되어야 합니다.")
                .setTitle("권한 허용")
                .setIcon(R.drawable.abc_ic_star_black_48dp)
            builder.setPositiveButton("OK") { _, _ ->
                askPermission (arrayOf(Manifest.permission.CAMERA), REQ_CAMERA);

            }
            val dialog = builder.create()
            dialog.show()
        }else{
            Toast . makeText ( getApplicationContext (),
                "권한이 승인되었습니다." , Toast . LENGTH_SHORT ). show ();
        }
        if(!checkAppPermission (arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE))){
            val builder = AlertDialog.Builder(this)
            builder.setMessage("겔러리 쓰기에 대한 권한이 허용되어야 합니다.")
                .setTitle("권한 허용")
                .setIcon(R.drawable.abc_ic_star_black_48dp)
            builder.setPositiveButton("OK") { _, _ ->
                askPermission (arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQ_WRITE_GALLERY);
            }
            val dialog = builder.create()
            dialog.show()
        }else{
            Toast . makeText ( getApplicationContext (),
                "권한이 승인되었습니다." , Toast . LENGTH_SHORT ). show ();
        }
    }

    fun checkAppPermission(requestPermission: Array<String>): Boolean {
        val requestResult = BooleanArray(requestPermission.size)
        for (i in requestResult.indices) {
            requestResult[i] = ContextCompat.checkSelfPermission(
                this,
                requestPermission[i]
            ) == PackageManager.PERMISSION_GRANTED
            if (!requestResult[i]) {
                return false
            }
        }
        return true
    } // checkAppPermission

    fun askPermission(requestPermission: Array<String>, REQ_PERMISSION: Int) {
        ActivityCompat.requestPermissions(
            this, requestPermission,
            REQ_PERMISSION
        )
    } // askPermission

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            REQ_CAMERA -> if (checkAppPermission(permissions)) { //퍼미션 동의했을 때 할 일
                Toast.makeText(this, "권한이 승인됨", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한이 거절됨", Toast.LENGTH_SHORT).show()
                finish()
            }
            REQ_WRITE_GALLERY -> if (checkAppPermission(permissions)) { //퍼미션 동의했을 때 할 일
                Toast.makeText(this, "권한이 승인됨", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한이 거절됨", Toast.LENGTH_SHORT).show()
                finish()
            }

            REQ_READ_GALLERY -> if (checkAppPermission(permissions)) { //퍼미션 동의했을 때 할 일
                Toast.makeText(this, "권한이 승인됨", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한이 거절됨", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    } // onRequestPermissionsResult


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.navigationIcon = getDrawable(R.drawable.hamburger)

        navView.setNavigationItemSelectedListener(this)

        /////////////////////////////////////////////////////
        initPermission()
        initBtn()
//        initMap()
        initTest()
        initRecyclerView()


    }
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        type_mini.setOpenAPIKey(key)
        cultural_type_mini.setOpenAPIKey(key);
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }
           else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



    ////////////////// Button //////////////////
    fun initBtn(){

        MkPlanBtn.setOnClickListener {
            val intent = Intent(this, MakePlanActivity::class.java)
            startActivity(intent)

        }

        HistoryBtn.setOnClickListener {
            val intent = Intent(this, my_schedule::class.java)
            startActivity(intent)
        }

        TimeLineBtn.setOnClickListener {
            val intent = Intent(this, TimeLineActivity::class.java)
            startActivity(intent)

        }
    }

    ////////////////// Map //////////////////
    fun initMap(){
        val options = NaverMapOptions()
            .camera(CameraPosition(LatLng(37.54345, 127.07747), 14.0))
            .mapType(NaverMap.MapType.Basic)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance(options).also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(p0: NaverMap) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        ////////////////// Preconditioning //////////////////





        /////////////////////////////////////////////////////
    }

    ////////////////// Recycler View //////////////////
    private val test = ArrayList<HomeItem>()
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: HomeAdapter? = null

    fun initTest(){

        for(i in 0..10){
            test.add(HomeItem("This is Title" + i.toString(), "This is Content" + i.toString()))
        }
    }




    fun initRecyclerView(){
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val listener = object: HomeAdapter.RecyclerViewAdapterEventListener{
            override fun onClick(view: View) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        adapter = HomeAdapter(this, listener, test)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, 1))
    }

}
