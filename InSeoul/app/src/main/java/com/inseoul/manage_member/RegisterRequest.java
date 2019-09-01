package com.inseoul.manage_member;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/User_Register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPW, String userEMAIL,String userName,  Response.Listener<String> listener){

        super(Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPW", userPW);
        parameters.put("userEMAIL", userEMAIL);
        parameters.put("userNAME", userName);




    }
    @Override
    public Map<String, String>getParams(){


        return parameters;
    }


}