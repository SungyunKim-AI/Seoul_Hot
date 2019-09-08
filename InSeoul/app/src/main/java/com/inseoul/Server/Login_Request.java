package com.inseoul.Server;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DGCOM on 2018-02-01.
 */

public class Login_Request extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/User_Login.php";
    private Map<String, String> parameters;

    public Login_Request(String userID, String userPW, Response.Listener<String> listener){

        super(Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPW", userPW);





    }
    @Override
    public Map<String, String>getParams(){


        return parameters;
    }


}