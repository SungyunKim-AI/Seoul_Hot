package com.inseoul.register_review;

import android.graphics.Color;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.inseoul.R;

import java.util.ArrayList;

public class register_review extends AppCompatActivity {

    private ArrayList<register_review_recyclerview> mArrayList;
    private register_review_adapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_review);

        //리뷰 작성시 인텐트로 데이터 전발 받을 함수 선언
        String review_title;
        String review_date;

        Bundle extras = getIntent().getExtras();

        review_title = extras.getString("textview_title_past");
        review_date = extras.getString("textview_date_past");

        TextView textView1 = (TextView)findViewById(R.id.text_review_date);

        String str_title = review_title;
        String str_date = review_date;

        textView1.setText(str_date);



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
}
