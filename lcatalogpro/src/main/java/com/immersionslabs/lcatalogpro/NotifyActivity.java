package com.immersionslabs.lcatalogpro;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalogpro.adapters.NotificationAdapter;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotifyActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = "NotifyActivity";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/notification";

    ArrayList<String> notification_titles;
    ArrayList<String> notification_messages;
    ArrayList<String> notification_images;
    ArrayList<String> notification_ids;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        Toolbar toolbar = findViewById(R.id.notify_toolbar);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Firebase is Getting Activated", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        notification_titles = new ArrayList<>();
        notification_messages = new ArrayList<>();
        notification_images = new ArrayList<>();
        notification_ids = new ArrayList<>();

        GetNotificationData();

    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.red));
        snackbar.setAction("RETRY", v -> {
            snackbar.dismiss();
            if (NetworkConnectivity.checkInternetConnection(NotifyActivity.this)) {

            } else {
                InternetMessage();
            }
        });
        snackbar.show();
    }

    private void GetNotificationData() {
        loading = ProgressDialog.show(this, "Please wait...", "Fetching data...", false, false);

        final JSONObject baseclass = new JSONObject();

        ApiService.getInstance(this).getData(this, false, "NOTIFICATIONS ACTIVITY", REGISTER_URL, "NOTIFY");
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        Log.e(TAG, "response--" + response);

        try {
            JSONArray resp = response.getJSONArray("data");
            loading.dismiss();
            NotificationView(resp);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onStart() {
        super.onStart();
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(NotifyActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
    }

    private void NotificationView(JSONArray resp) {
        RecyclerView recyclerView = findViewById(R.id.Notification_recycler);
        recyclerView.setHasFixedSize(true);

        for (int i = 0; i < resp.length(); i++) {
            JSONObject obj;
            try {
                obj = resp.getJSONObject(i);

                notification_ids.add(obj.getString("_id"));
                notification_messages.add(obj.getString("body"));
                notification_titles.add(obj.getString("title"));
                notification_images.add(obj.getString("img"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "ids******" + notification_ids);
        Log.e(TAG, "messages******" + notification_messages);
        Log.e(TAG, "images******" + notification_images);
        Log.e(TAG, "titles******" + notification_titles);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        NotificationAdapter adapter = new NotificationAdapter(this, notification_ids, notification_messages, notification_images, notification_titles);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}