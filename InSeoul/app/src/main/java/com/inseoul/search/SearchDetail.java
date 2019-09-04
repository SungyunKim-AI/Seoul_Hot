package com.inseoul.search;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.inseoul.R;
import com.inseoul.add_place.AddPlaceActivity;
import com.inseoul.manage_member.SaveSharedPreference;
import com.inseoul.manage_schedules.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchDetail extends AppCompatActivity implements View.OnClickListener {

    String m_name = null;
    private int plan_count;
    private Button btnAlert;


    /////////////TEST CODE///////////////
    ArrayList<planlist> testlist = new ArrayList<>();
    private int upsoID;
    private ArrayList<Integer> planidarray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);

        init();
        initToolbar();
    }

    public void init(){
        String idNUM = SaveSharedPreference.getUserID(this);

        SearchItem searchItem = getIntent().getParcelableExtra("placeData");
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    Log.d("dd", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray success= jsonResponse.getJSONArray("response");
                    int count=0;
                    plan_count = success.length();
                    while (count<success.length()){
                        JSONObject object = success.getJSONObject(count);
                        planidarray.add(object.getInt("PLANID"));
                        Log.d(this.getClass().getName(), planidarray.toString());


                        count++;

                    }
                    ShowPlanTask showPlanTask = new ShowPlanTask();
                    showPlanTask.execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        idnumRequest idnumrequest = new idnumRequest(idNUM, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SearchDetail.this);
        queue.add(idnumrequest);


        /////////////TEST CODE///////////////



        btnAlert = (Button)findViewById(R.id.add_my_list);
        btnAlert.setOnClickListener(this);
    }


    //툴바 세팅
    public void initToolbar() {
        //toolbar 커스텀 코드
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar_search_details);
        setSupportActionBar(mtoolbar);
        // Get the ActionBar here to configure the way it behaves.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        mtoolbar.setTitle(m_name);
        mtoolbar.setTitleTextColor(Color.WHITE);
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
        ProgressDialog asyncDialog = new ProgressDialog(SearchDetail.this);




        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            target="http://ksun1234.cafe24.com/ShowPlan.php"; // 웹 호스팅 정보를 받기위한  php 파일 주소


        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Log.d("asyncTask","Strart");
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

                        testlist.add(new planlist(object.getString("TripName"), object.getString("DPDATE"), object.getInt("H")));
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
    //다이얼 로그 세팅 onClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_my_list:

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                //alertBuilder.setIcon(R.drawable.ic_launcher);
                alertBuilder.setTitle("이곳을 추가할 일정을 선택하세요");

                // List Adapter 생성
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchDetail.this,
                        android.R.layout.select_dialog_singlechoice);
                for(int i=0;i<plan_count;i++){
                    adapter.add(testlist.get(i).getTitle());
                }

                // 버튼 생성
                alertBuilder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });

                // Adapter 셋팅
                alertBuilder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // AlertDialog 안에 있는 AlertDialog
                                final String strName = adapter.getItem(id);
                                AlertDialog.Builder innBuilder = new AlertDialog.Builder(SearchDetail.this);
                                innBuilder.setMessage(strName);
                                innBuilder.setTitle("일정을 수정 하시겠습니까?");
                                innBuilder
                                        .setPositiveButton(
                                                "확인",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        dialog.dismiss();
                                                        Intent intent_add = new Intent(SearchDetail.this, AddPlaceActivity.class);
                                                        intent_add.putExtra("PlanTitle_add",strName);
                                                        intent_add.putExtra("flag_key",1);
                                                        Log.d("alert",strName);
                                                        startActivity(intent_add);
                                                    }
                                                });
                                innBuilder.setNegativeButton(
                                        "취소",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                dialog.dismiss();
                                            }
                                        }
                                );
                                innBuilder.show();
                            }
                        });
                alertBuilder.show();
                break;

            default:
                break;
        }
    }

    ////////////////TEST CODE//////////////
    class planlist {
        String title;
        String date;

        public int getPlanid() {
            return planid;
        }

        public void setPlanid(int planid) {
            this.planid = planid;
        }

        int planid;
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public planlist(String title, String date, int planid){
            this.title = title;
            this.date = date;
            this.planid = planid;
        }
    }
}
