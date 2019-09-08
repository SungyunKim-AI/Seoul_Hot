package com.inseoul.Server;

import androidx.annotation.Nullable;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class idnumRequest extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/UserReview.php";
    private Map<String, String> parameters;

    public idnumRequest(String userID,  Response.Listener<String> listener){

        super(Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("userNum", userID);





    }
    @Override
    public Map<String, String>getParams() {


        return parameters;
    }
}
