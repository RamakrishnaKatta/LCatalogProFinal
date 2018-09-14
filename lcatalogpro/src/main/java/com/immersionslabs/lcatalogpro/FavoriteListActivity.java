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
import com.immersionslabs.lcatalogpro.adapters.MyFavoriteAdapter;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FavoriteListActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = FavoriteListActivity.class.getSimpleName();

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/users/favouriteArticles/";
    private static String FAVOURITE_URL = null;
    private static String GUEST_FAVOURITE_URL = null;

    String user_id, USER_LOG_TYPE;
    RecyclerView recycler;
    GridLayoutManager favoritemanager;
    SessionManager sessionmanager;

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
        setContentView(R.layout.activity_favorite);

        USER_LOG_TYPE = EnvConstants.user_type;

        sessionmanager = new SessionManager(getApplicationContext());
        HashMap hashmap = new HashMap();

        hashmap = sessionmanager.getUserDetails();
        user_id = (String) hashmap.get(SessionManager.KEY_USER_ID);

        FAVOURITE_URL = REGISTER_URL + user_id;
        GUEST_FAVOURITE_URL = EnvConstants.APP_BASE_URL + "/vendorArticles";

        recycler = findViewById(R.id.favorite_recycler);
        recycler.setHasFixedSize(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Toolbar toolbar = findViewById(R.id.toolbar_favorite);
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
    }

    private void CommonGetData() {

        Log.e(TAG, "CommonGetData: " + REGISTER_URL);
        if (USER_LOG_TYPE.equals("CUSTOMER")) {
            ApiService.getInstance(this).getData(this, false, "FAVORITE ACTIVITY", FAVOURITE_URL, "FAVORITE_LIST");
        } else if (USER_LOG_TYPE.equals("GUEST")) {
            Iterator iterator = EnvConstants.user_Favourite_list.iterator();
            while (iterator.hasNext()) {
                String TEMP_GUEST_FAVOURITE_URL = GUEST_FAVOURITE_URL + "/" + iterator.next().toString();
                ApiService.getInstance(this).getData(this, false, "FAVORITE ACTIVITY", TEMP_GUEST_FAVOURITE_URL, "GUEST");
            }
        }
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("FAVORITE_LIST")) {
            Log.e(TAG, " response" + response);
            try {
                JSONArray resp = response.getJSONArray("data");
                GetArrayData(resp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (flag.equals("GUEST")) {
            try {
                JSONObject RESP = response.getJSONObject("data");
                GetObjectData(RESP);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(FavoriteListActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
    }

    private void GetArrayData(JSONArray resp) {
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

        Log.e(TAG, "Ids" + item_ids);
        Log.e(TAG, "Names" + item_names);
        Log.e(TAG, "Descriptions" + item_descriptions);
        Log.e(TAG, "Prices" + item_prices);
        Log.e(TAG, "Images" + item_images);
        Log.e(TAG, "Dimensions" + item_dimensions);
        Log.e(TAG, "Discounts" + item_discounts);
        Log.e(TAG, "3ds" + item_3ds);
        Log.e(TAG, "Vendors" + item_vendors);

        favoritemanager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(favoritemanager);
        MyFavoriteAdapter adapter = new MyFavoriteAdapter(this, item_ids, item_names, item_descriptions,
                item_prices, item_discounts, item_dimensions, item_images, item_3ds, item_vendors);
        recycler.setAdapter(adapter);
    }

    private void GetObjectData(JSONObject obj) {
        try {
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

        Log.e(TAG, "Ids" + item_ids);
        Log.e(TAG, "Names" + item_names);
        Log.e(TAG, "Descriptions" + item_descriptions);
        Log.e(TAG, "Prices" + item_prices);
        Log.e(TAG, "Images" + item_images);
        Log.e(TAG, "Dimensions" + item_dimensions);
        Log.e(TAG, "Discounts" + item_discounts);
        Log.e(TAG, "3ds" + item_3ds);
        Log.e(TAG, "Vendors" + item_vendors);

        favoritemanager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(favoritemanager);
        MyFavoriteAdapter adapter = new MyFavoriteAdapter(this, item_ids, item_names, item_descriptions,
                item_prices, item_discounts, item_dimensions, item_images, item_3ds, item_vendors);
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
    public void onResume() {
        super.onResume();
        item_ids.clear();
        item_descriptions.clear();
        item_names.clear();
        item_images.clear();
        item_vendors.clear();
        item_prices.clear();
        item_discounts.clear();
        item_dimensions.clear();
        item_3ds.clear();

        CommonGetData();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}