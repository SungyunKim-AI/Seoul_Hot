package com.inseoul.add_place;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PlaceRequest extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/Upso_Profile.php";
    private Map<String, String> parameters;

    public PlaceRequest(int planID,  Response.Listener<String> listener){

        super(Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("planid", Integer.toString(planID));





    }
    @Override
    public Map<String, String>getParams() {


        return parameters;
    }
}
