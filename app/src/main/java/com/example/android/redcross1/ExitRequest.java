package com.example.android.redcross1;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruchira on 24-04-2018.
 */

public class ExitRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://ruchira.000webhostapp.com/exitgeo.php";
    private Map<String, String> params;

    public ExitRequest(String traffic_id, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("traffic_id",traffic_id);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
