package com.inseoul.manage_schedules;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.inseoul.R;
import com.inseoul.add_place.AddPlaceActivity;
import com.inseoul.make_plan.MakePlanActivity;
import com.inseoul.register_review.register_review;

import java.util.ArrayList;

public class my_schedule extends AppCompatActivity {
    private ArrayList<recyclerview_schedule> mArrayList;
    private adapter_schedule mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private ArrayList<recyclerview_schedule_past> mArrayList_past;
    private adapter_schedule_past mAdapter_past;
    private RecyclerView mRecyclerView_past;
    private LinearLayoutManager mLinearLayoutManager_past;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);

        initToolbar();
        init();
        initBtn();
    }


    //리사이클러뷰 세팅
    public void init() {
        /////////////수정 중인 일정
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        // MainActivity에서 RecyclerView의 데이터에 접근
        mArrayList = new ArrayList<>();

        //////////////////// Test Code ////////////////////
        for (int i = 0; i < 3; i++) {
            mArrayList.add(new recyclerview_schedule("title" + i, "date" + i));
        }

        mAdapter = new adapter_schedule(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        // RecyclerView의 줄(row) 사이에 수평선을 넣기위해 사용됩니다.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);




        ////////////지나간 일정
        mRecyclerView_past = (RecyclerView) findViewById(R.id.recyclerview_past_list);
        mLinearLayoutManager_past = new LinearLayoutManager(this);
        mRecyclerView_past.setLayoutManager(mLinearLayoutManager_past);

        // 현재 액티비티에서 에서 RecyclerView의 데이터에 접근
        mArrayList_past = new ArrayList<>();
        mAdapter_past = new adapter_schedule_past(mArrayList_past);
        mRecyclerView_past.setAdapter(mAdapter_past);


        //////////////////// Test Code ////////////////////
        for (int i = 0; i < 3; i++) {
            mArrayList_past.add(new recyclerview_schedule_past("title" + i, "date" + i));
        }


        // RecyclerView의 줄(row) 사이에 수평선을 넣기위해 사용됩니다.
        DividerItemDecoration dividerItemDecoration_past = new DividerItemDecoration(mRecyclerView_past.getContext(),
                mLinearLayoutManager_past.getOrientation());
        mRecyclerView_past.addItemDecoration(dividerItemDecoration_past);
    }

    //버튼 세팅
    public void initBtn() {
        // 일정 추가하기 클릭 이벤트
        FloatingActionButton add_schedule = (FloatingActionButton) findViewById(R.id.add_schedule);
        add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(my_schedule.this, MakePlanActivity.class);
                startActivity(intent);
            }
        });


        //수정 중인 여정 리사이클러뷰 클릭시 현재 액티비티에서 AdDPlaceActivity로 데이터 전달
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView_past, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                recyclerview_schedule Schedules = mArrayList.get(position);

                //intent 전달
                Intent intent_addPlace = new Intent(getBaseContext(), AddPlaceActivity.class);
                intent_addPlace.putExtra("textview_title", Schedules.getSchedule_title());
                intent_addPlace.putExtra("textview_date", Schedules.getSchedule_date());
                intent_addPlace.putExtra("flag_key",3);
                startActivity(intent_addPlace);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));





        //지난 여정 리사이클러뷰 클릭시 현재 액티비티에서 register_review액티비티에 데이터 전달
        mRecyclerView_past.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView_past, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                recyclerview_schedule_past pastSchedules = mArrayList_past.get(position);

                //intent 전달
                Intent intent2 = new Intent(getBaseContext(), register_review.class);
                intent2.putExtra("textview_title_past", pastSchedules.getSchedule_title_past());
                intent2.putExtra("textview_date_past", pastSchedules.getSchedule_date_past());
                startActivity(intent2);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

    }

    //클릭리스너 인터페이스
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    //리사이클러뷰 터치 리스너
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private my_schedule.ClickListener clickListener;


        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final my_schedule.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    //툴바 세팅
    public void initToolbar() {
        //toolbar 커스텀 코드
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar_my_schedule);
        setSupportActionBar(mtoolbar);
        // Get the ActionBar here to configure the way it behaves.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
    }

    //toolbar에서 back 버튼
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}