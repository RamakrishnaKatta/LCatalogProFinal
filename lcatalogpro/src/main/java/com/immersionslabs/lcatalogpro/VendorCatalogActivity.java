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
import com.immersionslabs.lcatalogpro.adapters.VendorCatalogAdapter;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class VendorCatalogActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = "VendorCatalogActivity";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/userArticles/";
    private static String VENDOR_URL = null;

    RecyclerView vendor_recycler;
    GridLayoutManager VendorCatalogManager;
    String vendor_id;
    Integer vendor_id_no;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_3ds;
    private ArrayList<String> item_vendors;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_catalog);

        vendor_recycler = findViewById(R.id.vendor_catalog_recycler);
        vendor_recycler.setHasFixedSize(true);
        vendor_recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Toolbar toolbar = findViewById(R.id.toolbar_vendor_catalog);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        item_ids = new ArrayList<>();
        item_descriptions = new ArrayList<>();
        item_names = new ArrayList<>();
        item_images = new ArrayList<>();
        item_vendors = new ArrayList<>();
        item_prices = new ArrayList<>();
        item_discounts = new ArrayList<>();
        item_dimensions = new ArrayList<>();
        item_3ds = new ArrayList<>();

        final Bundle vendor_data = getIntent().getExtras();

        assert vendor_data != null;
        vendor_id = Objects.requireNonNull(vendor_data.getString("vendor_id")).trim();
        vendor_id_no = Integer.parseInt(vendor_id);
        vendor_id_no = vendor_id_no + 1;

        vendor_id = String.valueOf(vendor_id_no);
        Log.e(TAG, "Vendor Id" + vendor_id);

        VENDOR_URL = REGISTER_URL + vendor_id;
        Log.e(TAG, "VENDOR_URL" + VENDOR_URL);

        CommonGetData();
    }

    private void CommonGetData() {

        Log.e(TAG, "CommonGetData: " + REGISTER_URL);
        final JSONObject object = new JSONObject();

        ApiService.getInstance(this).getData(this, false, "VENDOR_CATALOG", VENDOR_URL, "VENDOR_CATALOG");
    }

    private void GetData(JSONArray resp) {

        for (int i = 0; i < resp.length(); i++) {
            JSONObject obj = null;

            try {
                obj = resp.getJSONObject(i);

                item_ids.add(obj.getString("_id"));
                item_names.add(obj.getString("name"));
                item_descriptions.add(obj.getString("description"));
                item_prices.add(obj.getString("price"));
                item_images.add(obj.getString("img"));
                item_discounts.add(obj.getString("discount"));
                item_3ds.add(obj.getString("view_3d"));
                item_dimensions.add(obj.getString("dimensions"));
                item_vendors.add(obj.getString("vendor_id"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG, " ids" + item_ids);
        Log.e(TAG, " name" + item_names);
        Log.e(TAG, " description" + item_descriptions);
        Log.e(TAG, " price" + item_prices);
        Log.e(TAG, " img" + item_images);
        Log.e(TAG, " dimensions" + item_dimensions);
        Log.e(TAG, " discount" + item_discounts);
        Log.e(TAG, " view_3d" + item_3ds);
        Log.e(TAG, " vendor_id" + item_vendors);

        VendorCatalogManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        vendor_recycler.setLayoutManager(VendorCatalogManager);
        VendorCatalogAdapter adapter = new VendorCatalogAdapter(this, item_ids, item_names, item_descriptions, item_prices, item_discounts, item_dimensions, item_images, item_3ds, item_vendors);
        vendor_recycler.setAdapter(adapter);
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

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("VENDOR_CATALOG")) {
            Log.e(TAG, "onResponse: response" + response);
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
        Toast.makeText(VendorCatalogActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
    }
}