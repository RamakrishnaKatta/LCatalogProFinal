package com.immersionslabs.lcatalogpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VendorProfileActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = "VendorProfileActivity";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/vendors/specific/";
    private static final int REQUEST_UPDATE = 0;
    private static String VENDOR_URL = null;
    String vendor_id, vendor_name, vendor_address, vendor_image, vendor_no_of_articles;
    TextView profile_vendor_name, profile_vendor_location;
    AppCompatImageView profile_vendor_logo;
    Button profile_vendor_articles_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);

        Toolbar toolbar = findViewById(R.id.vendor_profile_toolbar);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        profile_vendor_name = findViewById(R.id.profile_vendor_name);
        profile_vendor_location = findViewById(R.id.profile_vendor_address);
        profile_vendor_logo = findViewById(R.id.profile_vendor_image);
        profile_vendor_articles_list = findViewById(R.id.profile_vendor_articles_list);

        profile_vendor_articles_list.setOnClickListener(v -> {
            Bundle vendor_data = new Bundle();
            vendor_data.putString("vendor_id", vendor_id);

            Intent intent = new Intent(VendorProfileActivity.this, VendorCatalogActivity.class).putExtras(vendor_data);
            startActivity(intent);
        });

        final Bundle vendor_data = getIntent().getExtras();
        assert vendor_data != null;
        vendor_id = vendor_data.getString("vendor_id");

        VENDOR_URL = REGISTER_URL + vendor_id;
        Log.e(TAG, "VENDOR_URL--" + VENDOR_URL);

        getVendorData();

        if (NetworkConnectivity.checkInternetConnection(VendorProfileActivity.this)) {

        } else {
            InternetMessage();
        }
    }

    private void getVendorData() {

        final JSONObject baseclass = new JSONObject();
        ApiService.getInstance(this).getData(this, false, "VENDOR PROFILE ACTIVITY", VENDOR_URL, "VENDOR_PROFILE");
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("VENDOR_PROFILE")) {
            Log.e(TAG, "response" + response);

            try {
                String response_type = response.getString("success");
                Log.e(TAG, "Vendor Response Type--" + response_type);
                String response_message = response.getString("message");
                Log.e(TAG, "Vendor Response Message--" + response_message);

                JSONArray array = response.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);
                    vendor_id = object.getString("id");
                    vendor_name = object.getString("name");
                    vendor_address = object.getString("location");
                    vendor_image = object.getString("logo");
                    vendor_no_of_articles = object.getString("total_objects");
                }

                Log.e(TAG, "Article Vendor ID--" + vendor_id);
                Log.e(TAG, "Article Vendor Name--" + vendor_name);
                Log.e(TAG, "Article Vendor Address--" + vendor_address);
                Log.e(TAG, "Article Vendor Image--" + vendor_image);

                profile_vendor_name.setText(vendor_name);
                profile_vendor_location.setText(vendor_address);
                profile_vendor_articles_list.setText("VENDOR ARTICLES (" + vendor_no_of_articles + ")");

                Glide.with(getApplicationContext())
                        .load(EnvConstants.APP_BASE_URL + "/upload/vendorLogos/" + vendor_image)
                        .placeholder(R.drawable.dummy_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profile_vendor_logo);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(VendorProfileActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPDATE) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(this,R.color.red));
        snackbar.setAction("RETRY", v -> {
            snackbar.dismiss();
            if (NetworkConnectivity.checkInternetConnection(VendorProfileActivity.this)) {
            } else {
                InternetMessage();
            }
        });
        snackbar.show();
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