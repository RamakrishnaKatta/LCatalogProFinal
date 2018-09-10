package com.immersionslabs.lcatalogpro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.adapters.VendorListAdapter;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VendorListActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = "VendorListActivity";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/vendors";

    RecyclerView recycler;
    GridLayoutManager VendorListManager;
    private ArrayList<String> vendor_ids;
    private ArrayList<String> vendor_names;
    private ArrayList<String> vendor_logos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list);

        recycler = findViewById(R.id.vendor_list_recycler);
        recycler.setHasFixedSize(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Toolbar toolbar = findViewById(R.id.toolbar_vendor_list);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        vendor_ids = new ArrayList<>();
        vendor_names = new ArrayList<>();
        vendor_logos = new ArrayList<>();

        CommonGetData();
    }

    private void CommonGetData() {
        Log.e(TAG, "CommonGetData: " + REGISTER_URL);
        final JSONObject object = new JSONObject();
        ApiService.getInstance(this).getData(this, false, "VENDOR LIST ACTIVITY", REGISTER_URL, "VENDOR_LIST");
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("VENDOR_LIST")) {

            Log.e(TAG, "response " + response);

            try {
                JSONArray resp = response.getJSONArray("data");
                GetData(resp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(VendorListActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
    }

    private void GetData(JSONArray resp) {

        for (int i = 0; i < resp.length(); i++) {
            JSONObject obj = null;

            try {
                obj = resp.getJSONObject(i);

                vendor_ids.add(obj.getString("id"));
                vendor_names.add(obj.getString("name"));
                vendor_logos.add(obj.getString("logo"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG, " ids" + vendor_ids);
        Log.e(TAG, " names" + vendor_names);
        Log.e(TAG, " logos" + vendor_logos);

        VendorListManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(VendorListManager);
        VendorListAdapter adapter = new VendorListAdapter(this, vendor_ids, vendor_names, vendor_logos);
        recycler.setAdapter(adapter);
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
