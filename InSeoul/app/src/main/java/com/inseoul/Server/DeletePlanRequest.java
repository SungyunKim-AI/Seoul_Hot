package com.inseoul.Server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeletePlanRequest extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/UserReview.php";
    private Map<String, String> parameters;

    public  DeletePlanRequest(String userID,  Response.Listener<String> listener){

        super(Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("PlanID", userID);





    }
    @Override
    public Map<String, String>getParams() {


        return parameters;
    }
}
