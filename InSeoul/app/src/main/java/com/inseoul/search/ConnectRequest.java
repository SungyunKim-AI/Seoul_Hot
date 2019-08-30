package com.inseoul.search;

import androidx.annotation.Nullable;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ConnectRequest extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/ConnectSearch.php";
    private Map<String, String> parameters;

    public ConnectRequest(String userID,  Response.Listener<String> listener){

        super(Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("userID", userID);





    }
    @Override
    public Map<String, String>getParams() {


        return parameters;
    }
}
