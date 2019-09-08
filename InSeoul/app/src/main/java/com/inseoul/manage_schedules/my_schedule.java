package com.inseoul.manage_schedules;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.inseoul.R;
import com.inseoul.Server.idnumRequest;
import com.inseoul.add_place.AddPlaceActivity;
import com.inseoul.make_plan.MakePlanActivity;
import com.inseoul.register_review.RegisterReviewActivity;
import com.inseoul.manage_member.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class my_schedule extends AppCompatActivity {

    //현재 일정 변수
    private ArrayList<recyclerview_schedule> mArrayList;
    private adapter_schedule mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    //지난 일정 변수
    private ArrayList<recyclerview_schedule_past> mArrayList_past;
    private adapter_schedule_past mAdapter_past;
    private RecyclerView mRecyclerView_past;
    private LinearLayoutManager mLinearLayoutManager_past;


    public ArrayList<Integer> planidarray = new ArrayList<Integer>();
    private  ArrayList<String> planlist = new ArrayList<>();
    private  ArrayList<String> planlist_past = new ArrayList<>();

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

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        mLinearLayoutManager = new LinearLayoutManager(my_schedule.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        // MainActivity에서 RecyclerView의 데이터에 접근
        mArrayList_past = new ArrayList<>();
        mArrayList = new ArrayList<>();

        String idNUM = SaveSharedPreference.getUserID(this);

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try
                                {
//                    Log.d("dd", response);
                                    JSONObject jsonResponse = new JSONObject(response);
                                    JSONArray success= jsonResponse.getJSONArray("response");
                                    int count=0;
                                    while (count<success.length()){
                                        JSONObject object = success.getJSONObject(count);
                                        planidarray.add(object.getInt("PLANID"));
//                        Log.d(this.getClass().getName(), planidarray.toString());

                                        count++;
                                    }
                                    if(success.length()==0)
                                    {
                                        LinearLayout layout = (LinearLayout)findViewById(R.id.first_layout);
                                        layout.setVisibility(View.VISIBLE);
                    }
                    else{

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        idnumRequest idnumrequest = new idnumRequest(idNUM, responseListener);
        RequestQueue queue = Volley.newRequestQueue(my_schedule.this);
        queue.add(idnumrequest);

        ShowPlanTask showPlanTask = new ShowPlanTask();
        showPlanTask.execute();


        //////////////지나간 일정///////////////
        mRecyclerView_past = (RecyclerView) findViewById(R.id.recyclerview_past_list);
        mLinearLayoutManager_past = new LinearLayoutManager(this);
        mRecyclerView_past.setLayoutManager(mLinearLayoutManager_past);


    }




    //버튼 세팅
    public void initBtn() {
        // 일정 추가하기 클릭 이벤트
        TextView add_schedule = (TextView) findViewById(R.id.add_schedule);
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
                Intent intent2 = new Intent(getBaseContext(), RegisterReviewActivity.class);
                intent2.putExtra("textview_title_past", pastSchedules.getSchedule_title_past());
                intent2.putExtra("textview_date_past", pastSchedules.getSchedule_date_past());
                intent2.putExtra("PLANLIST",planlist_past.get(position));
                intent2.putExtra("PLANID",planidarray.get(position));
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

    public class ShowPlanTask extends AsyncTask<Void,Void,String> {

        String target;
        int PlanID;
        ProgressDialog asyncDialog = new ProgressDialog(my_schedule.this);




        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            asyncDialog.setMessage("콘텐츠 확인 중 입니다...");



            // show dialog

            asyncDialog.show();



            super.onPreExecute();

            target="http://ksun1234.cafe24.com/ShowPlan.php"; // 웹 호스팅 정보를 받기위한  php 파일 주소


        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Log.d("asyncTask","Start");
                URL url = new URL(target);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                Log.d("asyncTask","Connect");
                InputStream inputStream = con.getInputStream();
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    stringBuilder.append(json + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                con.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(String result){


            try{
                Log.d("asyncTask","onPostExecute");
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                int count=0;
                while (count<jsonArray.length()){
                    Log.d("asyncTask","Reading"+jsonArray);
                    JSONObject object = jsonArray.getJSONObject(count);
                    int planID = object.getInt("H");
                    Log.d("as",planidarray.toString());
                    if(planidarray.contains(planID)){
                        Log.d("async","ss"+ Integer.toString(planID));
                        mArrayList.add(new recyclerview_schedule(object.getString("TripName")  , object.getString("DPDATE" )));
                        mAdapter = new adapter_schedule(mArrayList);
                        mRecyclerView.setAdapter(mAdapter);
                        planlist.add(object.getString("PLAN"));
                        planlist_past.add(object.getString("PLAN"));
                        mArrayList_past.add(new recyclerview_schedule_past(object.getString("TripName"), object.getString("DPDATE" )));
                        mAdapter_past = new adapter_schedule_past(mArrayList_past);
                        mRecyclerView_past.setAdapter(mAdapter_past);

                    }






                    count++;

                }


            }catch (Exception e){
                e.printStackTrace();
            }

            asyncDialog.dismiss();
        }
        protected void onCancelled()
        {

            super.onCancelled();
        }
    }
}