package com.inseoul.Server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DisLikeRequest extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/Dislik.php";
    private Map<String, String> parameters;

    public DisLikeRequest(String userID,   Response.Listener<String> listener){

        super(Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("ReviewID", userID);





    }
    @Override
    public Map<String, String>getParams(){


        return parameters;
    }

}
