package com.inseoul.manage_schedules;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.inseoul.R;
import com.inseoul.make_plan.MakePlanActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class my_schedule extends AppCompatActivity {


    private static String TAG = "recyclerview_";

    private ArrayList<recyclerview_schedule> mArrayList;
    private adapter_schedule mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private int count = -1;

    private ArrayList<recyclerview_schedule_past> mArrayList_past;
    private adapter_schedule_past mAdapter_past;
    private RecyclerView mRecyclerView_past;
    private LinearLayoutManager mLinearLayoutManager_past;
    private int count_past = -1;

    // TEST
    private ArrayList<recyclerview_schedule_past> test = new ArrayList<recyclerview_schedule_past>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);


        //////////////////// Test Code ////////////////////

        for(int i=0;i<3;i++){
            test.add(new recyclerview_schedule_past("test1", "test2"));
        }


        //수정 중인 일정
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        // MainActivity에서 RecyclerView의 데이터에 접근
        mArrayList = new ArrayList<>();

        mAdapter = new adapter_schedule( mArrayList);
        mRecyclerView.setAdapter(mAdapter);


        // RecyclerView의 줄(row) 사이에 수평선을 넣기위해 사용됩니다.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        Button add_schedule = (Button)findViewById(R.id.add_schedule);
        add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(my_schedule.this, MakePlanActivity.class);
                startActivity(intent);
            }
        });

        //지나간 일정
        mRecyclerView_past = (RecyclerView) findViewById(R.id.recyclerview_past_list);
        mLinearLayoutManager_past = new LinearLayoutManager(this);
        mRecyclerView_past.setLayoutManager(mLinearLayoutManager_past);

        // MainActivity에서 RecyclerView의 데이터에 접근
        mArrayList_past = new ArrayList<>();

        mAdapter_past = new adapter_schedule_past( test);
        mRecyclerView_past.setAdapter(mAdapter_past);


        // RecyclerView의 줄(row) 사이에 수평선을 넣기위해 사용됩니다.
        DividerItemDecoration dividerItemDecoration_past = new DividerItemDecoration(mRecyclerView_past.getContext(),
                mLinearLayoutManager_past.getOrientation());
        mRecyclerView_past.addItemDecoration(dividerItemDecoration);

    }
}
