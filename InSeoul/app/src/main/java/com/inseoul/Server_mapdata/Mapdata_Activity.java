package com.inseoul.Server_mapdata;

import android.os.AsyncTask;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.inseoul.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Mapdata_Activity extends AppCompatActivity {

    private List<Spot> spotList; // 리스트뷰에 들어갈 내용 ( 장소 IDNUM, 도로명 주소)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapdata_);

        new BackgroundTask().execute();// 어싱크태스크
    }


    public class BackgroundTask extends AsyncTask<Void,Void,String> {

        String target;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            target="http://ksun1234.cafe24.com/SpotList.php"; // 웹 호스팅 정보를 받기위한  php 파일 주소

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

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


            spotList = new ArrayList<Spot>();
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                //int count=0;
                String Spot_new;
                int IDNUM;
                int count=0;
                while (count<jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    IDNUM = object.getInt("IDNUM"); //업소 id
                    Log.d(this.getClass().getName(), Integer.toString(IDNUM));
                    Spot_new = object.getString("Spot_new");  // 업소 도로명주소
                    if(Spot_new.contains("(")){
                        Spot_new = Spot_new.split("\\(")[0];
                    }
                    Log.d(this.getClass().getName(), Spot_new);

                    //////////////////////////////////////////////////////








                    //////////////////////////////////////////////////////

                    //Spot notice = new Spot(IDNUM, Spot_new); // List에 넣기위함.
                    //spotList.add(notice);


                    count++;
                    //adapter = new NoticeListAdapter(getApplicationContext(),noticeList);

                    //noticeListview.setAdapter(adapter);
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
