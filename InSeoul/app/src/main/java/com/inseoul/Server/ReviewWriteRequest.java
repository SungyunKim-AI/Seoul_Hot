package com.inseoul.Server;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReviewWriteRequest extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/RegisterReview.php";
    private Map<String, String> parameters;
    public ReviewWriteRequest(String userID, String userPW, String userEMAIL, String userName, String Plan,  Response.Listener<String> listener){

        super(Request.Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("IDNUM", userID);
        parameters.put("SUBNUM", userPW);
        parameters.put("PLACEID", userEMAIL);
        parameters.put("IMGNAME", userName);
        parameters.put("REVIEW", Plan);





    }


    @Override
    public Map<String, String> getParams(){


        return parameters;
    }
}
