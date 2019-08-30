package com.inseoul.search;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/SearchHashtag.php";
    private Map<String, String> parameters;

    public SearchRequest(String userID,  Response.Listener<String> listener){

        super(Request.Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("userNum", userID);





    }
    @Override
    public Map<String, String>getParams() {


        return parameters;
    }
}
