package com.immersionslabs.lcatalogpro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectCatalogActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = "ProjectCatalogActivity";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/getprojects";

    RecyclerView recyclerView;
    LinearLayoutManager Project_Manager;
    ProjectAdapter adapter;

    private ArrayList<String> project_ids;
    private ArrayList<String> project_name;
    private ArrayList<String> project_description;
    private ArrayList<String> project_subDescription;
    private ArrayList<String> project_images;
    private ArrayList<String> project_3ds;
    private ArrayList<String> project_pattern;
    private ArrayList<String> project_vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_catalog);

        recyclerView = findViewById(R.id.project_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Toolbar toolbar = findViewById(R.id.toolbar_project);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        project_ids = new ArrayList<>();
        project_name = new ArrayList<>();
        project_description = new ArrayList<>();
        project_subDescription = new ArrayList<>();
        project_images = new ArrayList<>();
        project_3ds = new ArrayList<>();
        project_pattern = new ArrayList<>();
        project_vendor = new ArrayList<>();

        CommonGetData();
    }

    private void CommonGetData() {
        Log.e(TAG, " Project URL" + REGISTER_URL);
        ApiService.getInstance(this).getData(this, false, "CAMPAIGN", REGISTER_URL, "PROJECT_CAMPAIGN");
    }

    private void GetData(JSONArray resp) {
        for (int i = 0; i < resp.length(); i++) {
            JSONObject object;

            try {
                object = resp.getJSONObject(i);
                JSONObject jsonObject = object.getJSONObject("vendor_id");
                String vendorid = jsonObject.getString("id");

                project_ids.add(object.getString("_id"));
                project_name.add(object.getString("projectName"));
                project_description.add(object.getString("projectDescription"));
                project_images.add(object.getString("images"));
                project_subDescription.add(object.getString("projectSubDescription"));
                project_3ds.add(object.getString("projectView_3d"));
                project_pattern.add(object.getString("patternImg"));
                project_vendor.add(vendorid);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG, "project ids" + project_ids);
        Log.e(TAG, "project Name" + project_name);
        Log.e(TAG, "project Description" + project_description);
        Log.e(TAG, "project Sub Description" + project_subDescription);
        Log.e(TAG, "project Images" + project_images);
        Log.e(TAG, "Project 3ds" + project_3ds);
        Log.e(TAG, "Project Pattern" + project_pattern);
        Log.e(TAG, "Project vendorid" + project_vendor);

        Project_Manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(Project_Manager);
        ProjectAdapter adapter = new ProjectAdapter(this, project_ids, project_name, project_description, project_subDescription, project_images, project_3ds, project_pattern, project_vendor);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("PROJECT_CAMPAIGN")) {
            Log.e(TAG, " response" + response);
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
        Toast.makeText(ProjectCatalogActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
