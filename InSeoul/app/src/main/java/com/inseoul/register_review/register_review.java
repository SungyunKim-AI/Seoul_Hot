package com.inseoul.register_review;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.inseoul.R;
import com.inseoul.manage_schedules.adapter_schedule;
import com.inseoul.manage_schedules.recyclerview_schedule;

import java.util.ArrayList;

public class register_review extends AppCompatActivity {

    private ArrayList<register_review_recyclerview> mArrayList;
    private register_review_adapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private int count = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_review);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        // MainActivity에서 RecyclerView의 데이터에 접근
        mArrayList = new ArrayList<>();

        mAdapter = new register_review_adapter(mArrayList);
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
                register_review_recyclerview dict = new register_review_recyclerview(count+"장소이름","유형" + count,"★★★★★");

                //mArrayList.add(0, dict); //RecyclerView의 첫 줄에 삽입
                mArrayList.add(dict); // RecyclerView의 마지막 줄에 삽입

                mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
            }
        });

    }
}
