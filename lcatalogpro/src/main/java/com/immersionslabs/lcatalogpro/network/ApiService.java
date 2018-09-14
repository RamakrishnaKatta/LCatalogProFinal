package com.immersionslabs.lcatalogpro.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ApiService {

    private static ApiService instance;
    private static Context mCtx;
    private static Context context;
    private static int intClearCache = 0;
    private static ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private Map headers;

    private ApiService(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiService getInstance(Context context) {
        if (instance == null)
            instance = new ApiService(context);
        return instance;
    }

    public static synchronized ApiService getInstance(Context context, int intClearCache) {
        ApiService.context = context;
        ApiService.intClearCache = intClearCache;
        if (instance == null)
            instance = new ApiService(context);
        return instance;
    }

    public static synchronized ApiService getInstance(Context context, int intClearCache, ProgressDialog dialog) {
        ApiService.context = context;
        ApiService.intClearCache = intClearCache;
        ApiService.progressDialog = dialog;
        if (instance == null)
            instance = new ApiService(context);
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            if (intClearCache == 1) {
                requestQueue.getCache().clear();
            }
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public void getData(final ApiCommunication listener, boolean iscached, final String SCREEN_NAME, final String url, final String flag) {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> listener.onResponseCallback(response, flag), error -> {
                    if (error.networkResponse != null) {
                        try {
                            Toast.makeText(mCtx, "Internal Error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    listener.onErrorCallback(error, flag);
                    getRequestQueue().getCache().remove(url);
                });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                4000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(jsObjRequest);
        Log.e(SCREEN_NAME + " URL_HIT ", jsObjRequest.getUrl() + " ");
    }

    public void postData(final ApiCommunication listener, final String url, JSONObject params, final String SCREEN_NAME, final String flag) {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, params, response -> listener.onResponseCallback(response, flag), error -> {
                    if (error.networkResponse != null) {
                        try {
                            Log.e(SCREEN_NAME + "-" + flag, error.networkResponse.data.toString() + "");
                            Toast.makeText(mCtx, "internal error", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    listener.onErrorCallback(error, flag);
                    getRequestQueue().getCache().remove(url);
                });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                4000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(jsObjRequest);
        Log.e(SCREEN_NAME + " URL_HIT ", jsObjRequest.getUrl() + " ");
    }

    public void putData(final ApiCommunication listener, final String url, JSONObject params, final String SCREEN_NAME, final String flag) {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, url, params, response -> {
                    ////Log.e(SCREEN_NAME, response + "");
                    listener.onResponseCallback(response, flag);
                }, error -> {
                    if (error.networkResponse != null) {
                        try {
                            Log.e(SCREEN_NAME + "-" + flag, error.networkResponse.data.toString() + "");
                            Toast.makeText(mCtx, "Out Data", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    listener.onErrorCallback(error, flag);
                    getRequestQueue().getCache().remove(url);
                });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                4000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(jsObjRequest);
        Log.e(SCREEN_NAME, jsObjRequest.getUrl() + " ");
    }

    public void deleteData(final ApiCommunication listener, boolean isCached, final String SCREEN_NAME, final String url, final String flag) {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, null, response -> {
                    ////System.out.println("___" + response + "___");
                    listener.onResponseCallback(response, flag);
                }, error -> {
                    if (error.networkResponse != null) {
                        try {
                            Log.e(SCREEN_NAME + "-" + flag, error.networkResponse.data.toString() + "");
                            Toast.makeText(mCtx, "Out Data", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    listener.onErrorCallback(error, flag);
                    getRequestQueue().getCache().remove(url);
                });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                4000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(jsObjRequest);
        Log.e(SCREEN_NAME, jsObjRequest.getUrl() + " ");
    }
}