package com.inseoul.Server;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdatePlanRequest extends StringRequest {
    final static private String URL= "http://ksun1234.cafe24.com/UpdatePlan.php";
    private Map<String, String> parameters;

    public UpdatePlanRequest(String userID,String DPDATE,String ADDATE, String userPW,  String Plan,String Day, String mem, Response.Listener<String> listener){

        super(Request.Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("PLANNAME", userID);
        parameters.put("DPDATE", DPDATE);
        parameters.put("ADDATE", ADDATE);
        parameters.put("PLAN", Plan);
        parameters.put("MEM", mem);
        parameters.put("Day", Day);
        parameters.put("planID", userPW);




    }


    @Override
    public Map<String, String>getParams(){


        return parameters;
    }
}
