package com.inseoul.register_review;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.inseoul.R;
import com.inseoul.make_plan.MakePlanActivity;
import com.inseoul.manage_schedules.my_schedule;

import java.util.ArrayList;

public class register_review extends AppCompatActivity {

    private ArrayList<register_review_recyclerview> mArrayList;
    private register_review_adapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private ImageButton imgBtn;
    private ImageView img_view;
    final private int REQUEST_IMAGE_CAPTURE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_review);

        // Init Variable
        imgBtn = findViewById(R.id.imageButton);
        img_view = findViewById(R.id.img_view);

        //리뷰 작성시 인텐트로 데이터 전발 받을 함수 선언
        String review_title;
        String review_date;

        Bundle extras = getIntent().getExtras();

        review_title = extras.getString("textview_title_past");
        review_date = extras.getString("textview_date_past");

        TextView textView = (TextView)findViewById(R.id.text_review_title);
        TextView textView1 = (TextView)findViewById(R.id.text_review_date);

        String str_title = review_title;
        String str_date = review_date;

        textView.setText(str_title);
        textView1.setText(str_date);

        init();

        //리사이클러뷰 생성
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_rating);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        // MainActivity에서 RecyclerView의 데이터에 접근
        mArrayList = new ArrayList<>();

        /////////Test Code///////////
        for(int i=0; i<3;i++){
            mArrayList.add(new register_review_recyclerview("장소명"+i, "장소유형"+i,"★★★★★"));
        }

        mAdapter = new register_review_adapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);


        // RecyclerView의 줄(row) 사이에 수평선을 넣기위해 사용됩니다.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

    }

    private void init(){

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imgBtn.setVisibility(View.GONE);
            img_view.setVisibility(View.VISIBLE);
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ((ImageView)findViewById(R.id.img_view))
                    .setImageBitmap(imageBitmap);
        }
    }




}
