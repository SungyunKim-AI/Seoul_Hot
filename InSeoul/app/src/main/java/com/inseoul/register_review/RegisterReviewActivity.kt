package com.inseoul.register_review

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.core.content.FileProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.inseoul.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_review)

        initToolbar()
          initIntent()
//        initView()
//        initBtn()
//        initRecyclerView()
//        readFile()
        initViewPager()
    }

    lateinit var testArray:ArrayList<ReviewItem>
    fun initTest(){
        var testhash = ArrayList<String>()
        testhash.add("존맛")
        testhash.add("JMT")

        testArray = ArrayList()
        for(i in 0..10){
            testArray.add(ReviewItem(null,null,1,0, null, null, i,"존맛탱$i", testhash, ArrayList<Drawable?>(), null, 123.33,123.22,"xxxx", "xxx", 0, 0))
        }
    }
    fun initServer(){
        testArray = ArrayList()
        val count =0
        for( i in tripList){

        }
    }


    lateinit var adpater: RegisterReviewViewPagerAdapter
    private fun initViewPager() {

        initTest()

        val listener = object: RegisterReviewViewPagerAdapter.EventListener{
            override fun addPhotoOnClick(view: View, position: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                camera(position)
            }

            override fun addGalleryOnClick(view: View, positon: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                storage(positon)
            }
        }
        adpater = RegisterReviewViewPagerAdapter(this, listener, testArray)
        viewpager.adapter = adpater
        TabLayoutMediator(tabLayout, viewpager, object : TabLayoutMediator.OnConfigureTabCallback {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                // Styling each tab here
            }
        }).attach()
    }

    lateinit var review_title:String
    lateinit var review_date:String
    var planID = -1
    lateinit var tripList:ArrayList<String>

    fun initIntent(){
        tripList = ArrayList()

        val extras = intent.extras
        review_title = extras!!.getString("textview_title_past", "null")
        review_date = extras!!.getString("textview_date_past", "null")
        val plan_LIST = extras.getString("PLANLIST", "null")
        planID = extras.getInt("PLANID",  -1)

        val planist = plan_LIST!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val o = planist.size
        Log.d("json", Integer.toString(o))
        Log.d("json", plan_LIST.toString())
        for (p in 0 until o) {
            Log.d("json", plan_LIST.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[p])
            tripList!!.add(plan_LIST.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[p])
        }
    }

    /////////////////// Photo ///////////////////
    val REQUEST_IMAGE_CAPTURE = 1111
    val REQUEST_IMAGE_STORAGE = 1112

    var photoUri: Uri? = null

    var index = -1
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
            testArray[index].imageList!!.add(Drawable.createFromPath(imageFilePath))
            adpater.notifyItemChanged(index)

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

                    testArray[index].imageList!!.add(Drawable.createFromStream(inputStream, clipData.getItemAt(0).uri.path))
                } else if(clipData.itemCount > 1 && clipData.itemCount < 10){
                    for(i in 0 until clipData.itemCount){
                        val inputStream = contentResolver.openInputStream(clipData.getItemAt(i).uri)

                        testArray[index].imageList!!.add(Drawable.createFromStream(inputStream, clipData.getItemAt(i).uri.path))
                    }
                }
            }

            Log.v("index", index.toString())
            adpater.notifyDataSetChanged()


        }

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
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}