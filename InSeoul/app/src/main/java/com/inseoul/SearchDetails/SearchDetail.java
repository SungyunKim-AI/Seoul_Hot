package com.inseoul.SearchDetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.inseoul.R;
import com.inseoul.make_plan.AddPlaceActivity;
import com.inseoul.manage_schedules.my_schedule;

import java.util.ArrayList;

public class SearchDetail extends AppCompatActivity implements View.OnClickListener {

    String m_name = null;
    private int plan_count=3;
    private Button btnAlert;

    /////////////TEST CODE///////////////
    ArrayList<planlist> testlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);

        init();
        initToolbar();
    }

    public void init(){
        Bundle extras = getIntent().getExtras();
        m_name = extras.getString("search_title");

        /////////////TEST CODE///////////////
        for (int i=0; i<plan_count;i++){
            testlist.add(new planlist("testTitle"+i, "testDate"+i));
        }


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
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
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

        public planlist(String title, String date){
            this.title = title;
            this.date = date;
        }
    }
}
