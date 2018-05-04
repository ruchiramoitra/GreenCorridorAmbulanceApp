package com.example.android.redcross1;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruchira on 22-04-2018.
 */

public class SendRequest extends StringRequest{
    private static final String REGISTER_REQUEST_URL = "https://ruchira.000webhostapp.com/traffic.php";
    private Map<String, String> params;

    public SendRequest(String traffic_id,String source,String destination,String current, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("traffic_id",traffic_id);
        params.put("source",source);
        params.put("destination",destination);
        params.put("current",current);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
