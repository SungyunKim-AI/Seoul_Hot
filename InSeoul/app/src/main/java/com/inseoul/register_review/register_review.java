package com.inseoul.register_review;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

        TextView textView = (TextView)findViewById(R.id.text_review_title);
        TextView textView1 = (TextView)findViewById(R.id.text_review_date);

        String str_title = review_title;
        String str_date = review_date;

        textView.setText(str_title);
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


    }
}
