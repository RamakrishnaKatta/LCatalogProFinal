package com.immersionslabs.lcatalogpro.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class NotifyIDService extends FirebaseInstanceIdService {

    private static final String TAG = "NotifyIDService";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/userTokens";
    private static String TOKEN_REG_URL = null;

    String refreshedToken;
    String resp, code, message;
    Context context;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "onTokenRefresh: Refreshed Token " + refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {

        TOKEN_REG_URL = REGISTER_URL + refreshedToken;
        Log.e(TAG, "sendRegistrationToServer: TOKEN_REG_URL" + TOKEN_REG_URL);
        try {
            NotificationData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void NotificationData() throws JSONException {

        final JSONObject baseclass = new JSONObject();
        baseclass.put("vendor_id", 100000);
        baseclass.put("token_id", refreshedToken);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, baseclass, response -> {
            try {
                resp = response.getString("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(NotifyIDService.this, "Internal Error", Toast.LENGTH_LONG).show();
            // As of f605da3 the following should work
            NetworkResponse response = error.networkResponse;
            if (error instanceof ServerError && response != null) {
                try {
                    String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    // Now you can use any deserializer to make sense of data
                    JSONObject request = new JSONObject(res);
                } catch (UnsupportedEncodingException | JSONException e1) {
                    // Couldn't properly decode data to string
                    e1.printStackTrace();
                }
            }
        }) {
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("vendor_id", "100000");
                params.put("token_id", refreshedToken);
                return params;
            }

            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}