package com.inseoul.register_review

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.inseoul.BackPressCloseHandler
import com.inseoul.R
import com.inseoul.Server.*
import com.inseoul.manage_member.SaveSharedPreference
import com.inseoul.my_page.MyPage_Item
import com.inseoul.review.ReviewItem
import kotlinx.android.synthetic.main.activity_add_place_main.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_register_review.*
import kotlinx.android.synthetic.main.activity_register_review_page.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Handler

class RegisterReviewActivity : AppCompatActivity()  {

    lateinit var reviewArray:ArrayList<ReviewItem>
    lateinit var backPressCloseHandler: BackPressCloseHandler

    lateinit var review_title:String
    lateinit var review_date:String
    var planID = -1
    lateinit var tripList:ArrayList<String>

    lateinit var imgArray:ArrayList<ArrayList<String>>  // 서버로 넘길 Image의 URI

    // 카메라
    val REQUEST_IMAGE_CAPTURE = 1111
    val REQUEST_IMAGE_STORAGE = 1112

    var photoUri: Uri? = null

    var index = -1

    // viewpager adapter
    lateinit var adapter: RegisterReviewViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_review)

        initBackHandler()
        initToolbar()
        initIntent()
//        initView()
        initBtn()
//        initRecyclerView()
//        readFile()
        initViewPager()
        ServerGETDATE()
    }

//    lateinit var commentArray:ArrayList<String>

    fun initBtn(){

        submit_review.setOnClickListener {//LANIDnum: String,IndexOFPLAN:String , PlaceID: String, IMGNAMEARR:String , Review:String
            for(i in 0 until reviewArray.size){
                Log.v("comment_test", reviewArray[i].review_content)
                var imgarr = ""
                for(j in imgArray[i]){
                    var img = j.split("/").lastIndex
                    imgarr = imgarr + j.split("/")[img] + ","
                    val up:HTTPUPLOad = HTTPUPLOad()
                    up.HTTpfileUpload(j,j.split("/")[img])
                    Log.d("file Upload", j.split("/")[img])
                }

                ReviewWriteRequ(planID.toString(),i.toString(),reviewArray[i].num.toString(),imgarr,reviewArray[i].review_content.toString())
            }
        }
    }

    //    lateinit var testArray:ArrayList<ReviewItem>
/*
    fun initTest(){
        var testhash = ArrayList<String>()
        testhash.add("존맛")
        testhash.add("JMT")

        testArray = ArrayList()
        for(i in 0..10){
            testArray.add(ReviewItem(null,null,1,0, null, null, i,"존맛탱$i", testhash, ArrayList<Drawable?>(), null, 123.33,123.22,"xxxx", "xxx", 0, 0))
        }
    }
    */
    fun ReviewWriteRequ(PLANIDnum: String,IndexOFPLAN:String , PlaceID: String, IMGNAMEARR:String , Review:String){
        val responseListener = Response.Listener<String> { response ->
            try {
                Log.d("d", response)
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")
                if (success) {
                    Toast.makeText(this@RegisterReviewActivity, "정상적 등록 완료", Toast.LENGTH_SHORT).show()
                    //finish()

                } else {
                    Toast.makeText(this@RegisterReviewActivity, "등록 실패", Toast.LENGTH_SHORT).show()
                    //finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val registerRequest = ReviewWriteRequest(PLANIDnum,IndexOFPLAN,PlaceID, IMGNAMEARR,Review,responseListener)

        val queue = Volley.newRequestQueue(this@RegisterReviewActivity)
        queue.add(registerRequest)
    }



    val REQ_CAMERA = 1000
    val REQ_READ_GALLERY = 1001
    val REQ_WRITE_GALLERY = 1002

    fun cameraPermission(){
        val cameraPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            var strArr = Array<String>(100, { Manifest.permission.CAMERA })
            ActivityCompat.requestPermissions(
                this,
                strArr,
                REQ_CAMERA
            )

//            if(ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    Manifest.permission.CAMERA
//                )
//            ) else{
//            }
        } else {
            cameraPermissionFlag = 0    // Permission ok
        }
    }
    fun readStoragePermission(){
        val readStoragePermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)

        if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
            var strArr = Array<String>(100, { Manifest.permission.READ_EXTERNAL_STORAGE })
            ActivityCompat.requestPermissions(
                this,
                strArr,
                REQ_READ_GALLERY
            )
//            if(ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//            ) else{
//            }
        } else {
            readStoragePermissionFlag = 0    // Permission ok
        }
    }
    fun writeStoragePermission(){

        val writeStoragePermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (writeStoragePermission != PackageManager.PERMISSION_GRANTED) {

            var strArr = Array<String>(100, { Manifest.permission.WRITE_EXTERNAL_STORAGE })
            ActivityCompat.requestPermissions(
                this,
                strArr,
                REQ_WRITE_GALLERY
            )
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//            ) else {
//            }

        } else {
            readStoragePermissionFlag = 0    // Permission ok
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQ_CAMERA-> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    cameraPermissionFlag = 0    // Permission ok
                } else{

                }
                return
            }
            REQ_READ_GALLERY-> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readStoragePermissionFlag = 0    // Permission ok
                } else{
                }
                return
            }
            REQ_WRITE_GALLERY-> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readStoragePermissionFlag = 0    // Permission ok
                } else{

                }
                return
            }
        }
    }

    var cameraPermissionFlag = -1
    var readStoragePermissionFlag = -1

    private fun initViewPager() {

  //      initTest()



        val listener = object: RegisterReviewViewPagerAdapter.EventListener{
            override fun addPhotoOnClick(view: View, position: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                cameraPermission()
                if(cameraPermissionFlag != -1){
                    camera(position)
                }
            }

            override fun addGalleryOnClick(view: View, positon: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                writeStoragePermission()
                readStoragePermission()
                if(readStoragePermissionFlag != -1){
                    storage(positon)
                }
            }

        }
        adapter = RegisterReviewViewPagerAdapter(this, listener, reviewArray)
        viewpager.adapter = adapter
        TabLayoutMediator(tabLayout, viewpager, object : TabLayoutMediator.OnConfigureTabCallback {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                // Styling each tab here
            }
        }).attach()

        // comment Array Init

    }

    fun initIntent(){
        reviewArray = ArrayList()
        tripList = ArrayList()
        imgArray = ArrayList()

        val item = intent.getParcelableExtra<MyPage_Item>("item")
        planID = item.Num
        review_title = item.title
        review_date = item.date
        val plan_LIST = item.plan
                               // planID 필요
//        val extras = intent.extras
//        review_title = extras!!.getString("textview_title_past", "null")
//        review_date = extras!!.getString("textview_date_past", "null")
//       val plan_LIST = extras.getString("PLANLIST", "null")
//        planID = extras.getInt("PLANID",  -1)

        val planlist = plan_LIST!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val o = planlist.size
//        Log.d("json", Integer.toString(o))
//        Log.d("json", plan_LIST.toString())
//        commentArray = ArrayList()

        for (p in 0 until o) {
//            Log.d("json", plan_LIST.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[p])
            tripList!!.add(plan_LIST.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[p])
//            commentArray.add("")
            val iArray = ArrayList<String>()
            imgArray.add(iArray)
            reviewArray.add(ReviewItem(
                null,
                null,
                1,
                0,
                null,
                null,
                0,
                "",
                null,
                ArrayList<String?>(),
                null,
                123.33,
                123.22,
                "",
                "",
                0,
                0
            ))
        }


    }
    /////////////////////////////////////////SUNJAE SERVER??????????/////////////////////////////////////////////////////////////////////////////////////////////////
    fun ServerGETDATE(){
        for(i in 0 until reviewArray.size){
            val responseListener = Response.Listener<String> { response ->
                try {
                    Log.d("hsoh0306", response)
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getJSONObject("response")
                    Log.d("alert_search", "")
                    reviewArray[i] = ReviewItem(
                        null,
                        null,
                        1,
                        0,
                        null,
                        null, success.getInt("#"),
                        success.getString("UPSONM"),         // 서버 연결 후 업소 정보 받아서 리뷰 어레이 초기화 하기
                        null,
                        ArrayList<String?>(),
                        "",
                        123.33,
                        123.22,
                        "xxxx",
                        "xxx",
                        0,
                        0
                    )
                    adapter.notifyDataSetChanged()
                    Log.d("alert_search", "")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val idnumrequest = PlaceRequest(Integer.parseInt(tripList[i]), responseListener)
            val queue = Volley.newRequestQueue(this@RegisterReviewActivity)
            queue.add(idnumrequest)

        }
        Log.d("hsoh0306_2", reviewArray.toString())

    }

    /////////////////// Photo ///////////////////
    fun camera(position:Int){

        index = position
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, packageName, photoFile)

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    fun storage(position:Int){
        index = position

        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
        startActivityForResult(Intent.createChooser(intent, "사진 선택하기"), REQUEST_IMAGE_STORAGE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(imageFilePath)

            // Add Image URI
            imgArray[viewpager.currentItem].add(imageFilePath)

            reviewArray[index].imageList!!.add(imageFilePath)
            adapter.notifyDataSetChanged()

            Log.v("img", file.toString())
//            HTTpfileUpload()
        }
        if (requestCode == REQUEST_IMAGE_STORAGE && resultCode == Activity.RESULT_OK) {
            if(data!!.clipData == null){
//                list.add(data.toString())
            } else{
                var clipData = data.clipData
                if(clipData!!.itemCount > 10){
                    Toast.makeText(this, "사진은 10개까지 선택가능합니다", Toast.LENGTH_SHORT).show()
                    return
                } else if(clipData.itemCount == 1){
                    var dataStr = clipData.getItemAt(0).uri
                    val inputStream = contentResolver.openInputStream(clipData.getItemAt(0).uri)

                    // Add Image URI
                    val imgName = absolutelyPath(clipData.getItemAt(0).uri!!)
                    imgArray[viewpager.currentItem].add(imgName)

                    reviewArray[index].imageList!!.add(clipData.getItemAt(0).uri.path)
                } else if(clipData.itemCount > 1 && clipData.itemCount < 10){
                    for(i in 0 until clipData.itemCount){
                        val inputStream = contentResolver.openInputStream(clipData.getItemAt(i).uri)

                        // Add Image URI
                        val imgName = absolutelyPath(clipData.getItemAt(i).uri!!)
                        imgArray[viewpager.currentItem].add(imgName)

                        reviewArray[index].imageList!!.add(clipData.getItemAt(i).uri.path)
                    }
                }
            }

            Log.v("index", index.toString())
            adapter.notifyDataSetChanged()


        }

    }

    // 절대경로 변환
    fun absolutelyPath(path: Uri): String {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor = contentResolver.query(path, proj, null, null, null)!!
        var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()

        var result = c.getString(index)
        return result
    }

//    lateinit var imageFilePath: String
//    lateinit var imageFileName: String
    var imageFilePath = ""
    fun createImageFile(): File{
        var imageFileName = ""
        val timeStamp = SimpleDateFormat("yyMMddHHmm").format(Date())
        imageFileName = "TEST_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        imageFilePath = image.absolutePath
        imageFileName += ".jpg"
        return image
    }
    /////////////////////////////////////////////////////////
    fun initView(){
//        text_review_date.text = review_date
    }


    ////////////////Toolbar//////////////
    fun initToolbar() {
        //toolbar 커스텀 코드
        setSupportActionBar(toolbar_register_review)
        // Get the ActionBar here to configure the way it behaves.
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)

        actionBar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow) //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                backPressCloseHandler.onBackPressed_addPlace()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Back버튼 두번 눌러 종료하기
    fun initBackHandler() {
        backPressCloseHandler = BackPressCloseHandler(this)
    }

    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed_addPlace()
    }

}