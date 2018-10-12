package com.immersionslabs.lcatalogpro;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalogpro.adapters.GridViewAdapter;
import com.immersionslabs.lcatalogpro.adapters.ListViewHorizontalAdapter;
import com.immersionslabs.lcatalogpro.adapters.ListViewVerticalAdapter;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductCatalogActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = ProductCatalogActivity.class.getSimpleName();

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/vendorArticles";

    GridViewAdapter grid_Adapter;
    ListViewVerticalAdapter Vertical_Adapter;
    ListViewHorizontalAdapter horizontal_Adapter;
    RecyclerView base_recycler;
    ProgressBar progressBar;
    GridLayoutManager Grid_Manager;
    LinearLayoutManager Horizontal_Manager, Vertical_Manager;
    SwipeRefreshLayout refreshLayout;
    FloatingActionButton fab_grid, fab_vertical, fab_horizontal;
    Boolean Load_more = false;

    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_vendors;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_ids;
    private ArrayList<String> item_3ds;
    private ArrayList<String> item_patterns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_catalog);

        fab_grid = findViewById(R.id.fab_grid_list);
        fab_vertical = findViewById(R.id.fab_vertical_list);
        fab_horizontal = findViewById(R.id.fab_horizontal_list);

        base_recycler = findViewById(R.id.recycler);
        base_recycler.setHasFixedSize(true);
        base_recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Horizontal_Manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        Vertical_Manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Grid_Manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        refreshLayout = findViewById(R.id.swipe_refresh);
        progressBar = findViewById(R.id.progress_grid);

        Toolbar toolbar = findViewById(R.id.toolbar_catalog);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        item_names = new ArrayList<>();
        item_descriptions = new ArrayList<>();
        item_prices = new ArrayList<>();
        item_discounts = new ArrayList<>();
        item_vendors = new ArrayList<>();
        item_images = new ArrayList<>();
        item_dimensions = new ArrayList<>();
        item_ids = new ArrayList<>();
        item_3ds = new ArrayList<>();
        item_patterns = new ArrayList<>();

        fab_vertical.setSize(1);
        fab_horizontal.setSize(1);
        fab_grid.setSize(0);

        commonGetdata();

        /*Floating Button for Grid View*/
        fab_grid.setOnClickListener(v -> {
            fab_vertical.setSize(1);
            fab_horizontal.setSize(1);
            fab_grid.setSize(0);
            base_recycler.setLayoutManager(Grid_Manager);
            base_recycler.setAdapter(grid_Adapter);
        });

        /*Floating Button for Vertical List View*/
        fab_vertical.setOnClickListener(v -> {
            fab_vertical.setSize(0);
            fab_horizontal.setSize(1);
            fab_grid.setSize(1);
            base_recycler.setLayoutManager(Vertical_Manager);
            base_recycler.setAdapter(Vertical_Adapter);
        });

        /*Floating Button for Horizontal List View*/
        fab_horizontal.setOnClickListener(v -> {
            fab_vertical.setSize(1);
            fab_horizontal.setSize(0);
            fab_grid.setSize(1);
            base_recycler.setLayoutManager(Horizontal_Manager);
            base_recycler.setAdapter(horizontal_Adapter);
        });

//        checkInternetConnection();
        if (NetworkConnectivity.checkInternetConnection(ProductCatalogActivity.this)) {
        } else {
            InternetMessage();
        }
        refreshLayout.setOnRefreshListener(() -> {
            if (NetworkConnectivity.checkInternetConnection(ProductCatalogActivity.this)) {
                refreshLayout.setRefreshing(true);

                item_ids.clear();
                item_names.clear();
                item_descriptions.clear();
                item_prices.clear();
                item_discounts.clear();
                item_vendors.clear();
                item_images.clear();
                item_dimensions.clear();
                item_patterns.clear();

                commonGetdata();
            }
        });
    }

    private void commonGetdata() {
        Log.e(TAG, "commonGetdata: " + REGISTER_URL);
        ApiService.getInstance(this).getData(this, false, "CATALOGUE ACTIVITY", REGISTER_URL, "GETDATA");
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.red));
        snackbar.setAction("RETRY", v -> {
            snackbar.dismiss();
            if (NetworkConnectivity.checkInternetConnection(ProductCatalogActivity.this)) {
            } else {
                InternetMessage();
            }
        });
        snackbar.show();
    }

    /*Adapter class for GridView/ListViewHorizontal/ListViewVertical Adapter*/
    public void CatalogView(JSONArray g_jsonArray) {

        for (int i = 0; i < g_jsonArray.length(); i++) {
            JSONObject obj;
            try {
                obj = g_jsonArray.getJSONObject(i);

                item_ids.add(obj.getString("_id"));
                item_names.add(obj.getString("name"));
                item_descriptions.add(obj.getString("description"));
                item_prices.add(obj.getString("price"));
                item_discounts.add(obj.getString("discount"));
                item_vendors.add(obj.getString("vendor_id"));
                item_images.add(obj.getString("img"));
                item_dimensions.add(obj.getString("dimensions"));
                item_3ds.add(obj.getString("view_3d"));
                item_patterns.add(obj.getString("pattern"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "ids******" + item_ids);
        Log.e(TAG, "names******" + item_names);
        Log.e(TAG, "descriptions******" + item_descriptions);
        Log.e(TAG, "prices******" + item_prices);
        Log.e(TAG, "discounts******" + item_discounts);
        Log.e(TAG, "vendors******" + item_vendors);
        Log.e(TAG, "images******" + item_images);
        Log.e(TAG, "dimensions******" + item_dimensions);
        Log.e(TAG, "3ds******" + item_3ds);
        Log.e(TAG, "patterns******" + item_patterns);
        grid_Adapter = new GridViewAdapter(this, item_ids, item_names, item_descriptions, item_prices,
                item_discounts, item_vendors, item_images, item_dimensions, item_3ds, item_patterns);
        horizontal_Adapter = new ListViewHorizontalAdapter(this, item_ids, item_names, item_descriptions, item_prices,
                item_discounts, item_vendors, item_images, item_dimensions, item_3ds, item_patterns);
        Vertical_Adapter = new ListViewVerticalAdapter(this, item_ids, item_names, item_descriptions, item_prices,
                item_discounts, item_vendors, item_images, item_dimensions, item_3ds, item_patterns);

        if (fab_vertical.getSize() == 1 && fab_horizontal.getSize() == 1 && fab_grid.getSize() == 0) {
            base_recycler.removeAllViews();
            base_recycler.setLayoutManager(Grid_Manager);
            base_recycler.setAdapter(grid_Adapter);

        } else if (fab_vertical.getSize() == 0 && fab_horizontal.getSize() == 1 && fab_grid.getSize() == 1) {
            base_recycler.removeAllViews();
            base_recycler.setLayoutManager(Vertical_Manager);
            base_recycler.setAdapter(Vertical_Adapter);

        } else if (fab_vertical.getSize() == 1 && fab_horizontal.getSize() == 0 && fab_grid.getSize() == 1) {
            base_recycler.removeAllViews();
            base_recycler.setLayoutManager(Horizontal_Manager);
            base_recycler.setAdapter(horizontal_Adapter);
        }
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

    //    Api response ======================================
    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("GETDATA")) {
            Log.e(TAG, "onResponseCallback: response" + response);
            Log.e(TAG, "RESP : " + response);
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            try {
                JSONArray resp = response.getJSONArray("data");
                if (!Load_more) {
                    CatalogView(resp);
                } else {
                    for (int i = 0; i < resp.length(); i++) {
                        JSONObject obj;
                        try {
                            obj = resp.getJSONObject(i);

                            item_ids.add(obj.getString("_id"));
                            item_names.add(obj.getString("name"));
                            item_descriptions.add(obj.getString("description"));
                            item_prices.add(obj.getString("price"));
                            item_discounts.add(obj.getString("discount"));
                            item_vendors.add(obj.getString("vendor_id"));
                            item_dimensions.add(obj.getString("dimensions"));
                            item_3ds.add(obj.getString("view_3d"));
                            item_images.add(obj.getString("img"));
                            item_patterns.add(obj.getString("pattern"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fab_vertical.getSize() == 1 && fab_horizontal.getSize() == 1 && fab_grid.getSize() == 0) {
                        grid_Adapter.notifyDataSetChanged();
                    } else if (fab_vertical.getSize() == 0 && fab_horizontal.getSize() == 1 && fab_grid.getSize() == 1) {
                        Vertical_Adapter.notifyDataSetChanged();
                    } else if (fab_vertical.getSize() == 1 && fab_horizontal.getSize() == 0 && fab_grid.getSize() == 1) {
                        horizontal_Adapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(ProductCatalogActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
    }
}