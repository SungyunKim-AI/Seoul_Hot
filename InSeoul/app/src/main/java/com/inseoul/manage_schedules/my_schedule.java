package com.inseoul.manage_schedules;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.inseoul.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);

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

        Button buttonInsert = (Button)findViewById(R.id.add_schedule);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                // Dictionary 생성자를 사용하여 ArrayList에 삽입할 데이터를 만듭니다.
                recyclerview_schedule dict = new recyclerview_schedule(count+"","apple" + count);

                //mArrayList.add(0, dict); //RecyclerView의 첫 줄에 삽입
                mArrayList.add(dict); // RecyclerView의 마지막 줄에 삽입

                mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
            }
        });

    }
}