package com.inseoul.register_review

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

import java.util.HashMap

class review_request(
    PLANID: Int,
    SCORE: Int,
    IMAGENAME: String,
    USERREVIEW: String,
    listener: Response.Listener<String>
) : StringRequest(Request.Method.POST, URL, listener, null) {
    private val parameters: MutableMap<String, String>

    init {
        parameters = HashMap()
        parameters["PLANID"] = Integer.toString(PLANID)
        parameters["SCORE"] = Integer.toString(SCORE)
        parameters["IMGNAME"] = IMAGENAME
        parameters["REVIEW"] = USERREVIEW


    }

    public override fun getParams(): Map<String, String> {


        return parameters
    }

    companion object {
        private val URL = "http://ksun1234.cafe24.com/ReviewRegister.php"
    }


}