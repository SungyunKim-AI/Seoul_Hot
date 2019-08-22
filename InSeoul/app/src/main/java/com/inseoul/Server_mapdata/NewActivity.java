package com.inseoul.Server_mapdata;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.inseoul.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class NewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
    }
    private void jsonParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray movieArray = jsonObject.getJSONArray("Movies");

            for(int i=0; i<movieArray.length(); i++)
            {
                JSONObject movieObject = movieArray.getJSONObject(i);

                Spot spot = new Spot();

                spot.setIDNUM(movieObject.getInt("IDNUM")); //movieObject.getInt("IDNUM") 업소 아이디
                spot.setY(movieObject.getDouble("Yd")); //movieObject.getDouble("Yd") =위도
                spot.setX(movieObject.getDouble("Xd")); /// movieObject.getDouble("Xd") =경도
                ///////////////////////////////////////////////////////

                
                ////////////////////////////////////////////////////

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getJSONString(){
        String json = "";
        try {
            InputStream is = getAssets().open("IS_MAP.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }catch(IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

}
