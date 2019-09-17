package com.inseoul.Server;

import androidx.annotation.Nullable;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddPlaceRegister extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/MakePlan.php";
    private Map<String, String> parameters;

    public AddPlaceRegister(String userID, String userPW, String userEMAIL, String userName, String Plan, String mem, Response.Listener<String> listener){

        super(Request.Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("PLANNAME", userID);
        parameters.put("DPDATE", userPW);
        parameters.put("ADDATE", userEMAIL);
        parameters.put("THEME", userName);
        parameters.put("PLAN", Plan);
        parameters.put("MEM", mem);




    }


    @Override
    public Map<String, String>getParams(){


        return parameters;
    }
}
