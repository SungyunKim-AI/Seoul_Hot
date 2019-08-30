package com.inseoul.search;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.inseoul.R;
import com.inseoul.Server_mapdata.Mapdata_Activity;
import com.inseoul.manage_schedules.idnumRequest;
import com.inseoul.manage_schedules.my_schedule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search2Activity extends AppCompatActivity {
    private Button searchBTN;
    private SearchView mSearchView;
    private SearchView searchView;
    private MenuItem mSearch;
    private AlertDialog dialog2;
    private ArrayList<Integer> hNUM = new ArrayList<>();
    private ArrayList<Integer> upNUM = new ArrayList<>();
    private boolean[] succ = {false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //search_menu.xml 등록
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        mSearch=menu.findItem(R.id.search);

        //메뉴 아이콘 클릭했을 시 확장, 취소했을 시 축소



        //menuItem을 이용해서 SearchView 변수 생성
        SearchView sv=(SearchView)mSearch.getActionView();
        //확인버튼 활성화
        sv.setSubmitButtonEnabled(true);

        //SearchView의 검색 이벤트
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            Log.d("dd", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray success= jsonResponse.getJSONArray("response");
                            int count=0;
                            while (count<success.length()){
                                JSONObject object = success.getJSONObject(count);
                                ////////////////////////
                                object.getInt("H"); //Hash값
                                hNUM.add(object.getInt("H"));

                                count++;

                            }
                            if(success.length()>=1)
                            {
                                succ[0] = true;


                            }
                            else{

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                SearchRequest idnumrequest = new SearchRequest(query, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Search2Activity.this);
                queue.add(idnumrequest);

                Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (succ[0]) {
                            try {
                                Log.d("dd", response);
                                JSONObject jsonResponse = new JSONObject(response);
                                JSONArray success = jsonResponse.getJSONArray("response");
                                int count = 0;
                                while (count < success.length()) {
                                    JSONObject object = success.getJSONObject(count);
                                    ////////////////////////
                                    object.getInt("H"); //Hash값
                                    upNUM.add(object.getInt("H"));

                                    count++;

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
               if(succ[0]){
                   for(int j =0; j<hNUM.size();j++){
                       ConnectRequest idnumrequest2 = new ConnectRequest(hNUM.get(j).toString(), responseListener2);
                       queue = Volley.newRequestQueue(Search2Activity.this);
                       queue.add(idnumrequest2);
                   }
               }

                return true;
            }

            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("ds",newText);
                return true;
            }
        });
        return true;
    }


}
