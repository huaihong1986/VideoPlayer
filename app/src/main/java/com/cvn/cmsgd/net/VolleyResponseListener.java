package com.cvn.cmsgd.net;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public interface VolleyResponseListener {

    void onDataStartGetRequest(JsonObjectRequest request);
    void onDataStartPostRequest(VolleyPostRequest request);
    void onDataSuccessResponse(JSONObject response, int requestCode);
    void onDataErrorResponse(VolleyError error, int requestCode);

}
