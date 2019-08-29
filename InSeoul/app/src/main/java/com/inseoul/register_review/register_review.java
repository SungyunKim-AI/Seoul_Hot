package com.inseoul.register_review;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.graphics.Color;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.inseoul.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class register_review extends AppCompatActivity {

    private ArrayList<register_review_recyclerview> mArrayList;
    private register_review_adapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private  ArrayList<String> tripList;
    private ImageButton imgBtn;
    private ImageView img_view;
    final private int REQUEST_IMAGE_CAPTURE = 1111;
    private String IMGpath;
    private SharedPreferences appData;
    private ArrayList<String> planlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_review);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

         appData = getSharedPreferences("appData", MODE_PRIVATE);
        Boolean saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        int idNUM = appData.getInt("ID", 0);





        // Init Variable
        imgBtn = findViewById(R.id.imageButton);
        img_view = findViewById(R.id.img_view);

        //리뷰 작성시 인텐트로 데이터 전발 받을 함수 선언
        String review_title;
        String review_date;

        Bundle extras = getIntent().getExtras();

        review_title = extras.getString("textview_title_past");
        review_date = extras.getString("textview_date_past");
        String plan_LIST = extras.getString("PLANLIST");
        String []planist = plan_LIST.split(",");
        int o = planist.length;
        Log.d("json",Integer.toString(o));
        Log.d("json",plan_LIST.toString());
//        while(o>0){
//            Log.d("json",Integer.toString(o));
//
//                tripList.add(planist[planist.length-o]);
//            o--;
//        }

        TextView textView1 = (TextView)findViewById(R.id.text_review_date);

        String str_title = review_title;
        String str_date = review_date;

        textView1.setText(str_date);

        init();

        //리사이클러뷰 생성
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_rating);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        // MainActivity에서 RecyclerView의 데이터에 접근
        mArrayList = new ArrayList<>();
        //jsonParsing(getJsonString());
        /////////Test Code///////////
        for(int i=0; i<3;i++){

        }

        mAdapter = new register_review_adapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);


        // RecyclerView의 줄(row) 사이에 수평선을 넣기위해 사용됩니다.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        //toolbar 커스텀 코드
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar_register_review);
        setSupportActionBar(mtoolbar);
        // Get the ActionBar here to configure the way it behaves.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        mtoolbar.setTitle(str_title);
        mtoolbar.setTitleTextColor(Color.WHITE);
    }

    private void init(){

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTakePhotoIntent();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {


            imgBtn.setVisibility(View.GONE);
            img_view.setVisibility(View.VISIBLE);

            ((ImageView)findViewById(R.id.img_view))
                    .setImageURI(photoUri);
            HTTpfileUpload();
        }




    }

    private Uri photoUri;


    private String getJsonString()
    {
        String json = "";

        try {
            InputStream is = getAssets().open("inseoul_upso_data.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }
    private void jsonParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray movieArray = jsonObject.getJSONArray("data");

            for(int j=0; j<tripList.size();j++){
                for(int i=0; i<movieArray.length(); i++)
                {
                    JSONObject movieObject = movieArray.getJSONObject(i);
                    if(movieObject.getInt("Id_Num")==Integer.parseInt(tripList.get(j))){
                        mArrayList.add(new register_review_recyclerview(movieObject.getString("Upso_nm"), movieObject.getString("class"),"★★★★★"));
                    }


                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    public String imageFilePath;
    public String imageFileName;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        imageFileName += ".jpg";
        return image;
    }


    //toolbar에서 back 버튼
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void HTTpfileUpload(){
        String pathToOurFile = imageFilePath;
        String urlServer = "http://ksun1234.cafe24.com/UploadIMG.php";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            FileInputStream mFileInputStream = new FileInputStream(imageFilePath);
            URL connectUrl = new URL(urlServer);
            Log.d("Test", "mFileInputStream  is " + imageFileName);
            Log.d("Test", "mFileInputStream  is " + imageFilePath);
            // open connection
            HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + imageFileName +"\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            Log.d("Test", "image byte is " + bytesRead);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Test" , "File is written");
            mFileInputStream.close();
            dos.flush(); // finish upload...

            // get response
            int ch;
            InputStream is = conn.getInputStream();
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }
            String s=b.toString();
            Log.e("Test", "result = " + s);

            dos.close();
            conn.disconnect();


        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            // TODO: handle exception
        }
    }




}
