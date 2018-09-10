package com.immersionslabs.lcatalogpro.network;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ApiCommunication {

    void onResponseCallback(JSONObject response, String flag);

    void onErrorCallback(VolleyError error, String flag);
}