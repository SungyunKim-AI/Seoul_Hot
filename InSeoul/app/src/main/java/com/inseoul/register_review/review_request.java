package com.inseoul.register_review;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class review_request extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/ReviewRegister.php";
    private Map<String, String> parameters;

    public review_request(int PLANID, int SCORE, String IMAGENAME, String USERREVIEW, Response.Listener<String> listener){

        super(Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("PLANID",Integer.toString( PLANID));
        parameters.put("SCORE", Integer.toString( SCORE));
        parameters.put("IMGNAME", IMAGENAME);
        parameters.put("REVIEW",USERREVIEW);




    }
    @Override
    public Map<String, String>getParams(){


        return parameters;
    }


}