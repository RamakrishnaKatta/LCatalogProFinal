package com.immersionslabs.lcatalogpro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalogpro.adapters.ProjectPartDetailsAdapter;
import com.immersionslabs.lcatalogpro.adapters.ProjectPartImageSliderAdapter;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ProjectPartsActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = ProjectPartsActivity.class.getSimpleName();

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/getProjectDetails/";
    private static String PROJECT_PART_ARTICLE_URL = null;
    private static final String ARTICLE_SPECIFIC_URL = EnvConstants.APP_BASE_URL + "/vendorArticles/";
    private static String UNIQUE_ARTICLE_URL = null;

    TextView part_name, part_Desc;
    AppCompatImageView part_image;
    AppCompatImageButton part_augment, part_3dview;

    RecyclerView recyclerView;
    ProjectPartDetailsAdapter adapter;
    GridLayoutManager layoutManager;
    Context mcontext;

    String p_id, p_name, p_3ds, p_images, p_desc;
    String p_image1, p_image2, p_image3, p_image4, p_image5;

    private ArrayList<String> part_articles_id;
    private ArrayList<String> part_article_images;
    private ArrayList<String> part_article_name;
    private ArrayList<String> project_ids;

    ArrayList<String> slider_images = new ArrayList<>();
    private LinearLayout slider_dots;
    private ViewPager viewPager;
    ProjectPartImageSliderAdapter imageSliderAdapter;
    TextView[] dots;
    int page_position = 0;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_part_details);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = findViewById(R.id.toolbar_project_part);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.project_part_item_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        part_name = findViewById(R.id.part_title_text);
        part_Desc = findViewById(R.id.part_description_text);
        part_image = findViewById(R.id.part_image_view);
        part_3dview = findViewById(R.id.part_3dview_icon);
        part_augment = findViewById(R.id.part_augment_icon);

        part_articles_id = new ArrayList<>();
        part_article_name = new ArrayList<>();
        part_article_images = new ArrayList<>();
        project_ids = new ArrayList<>();

        mcontext = getApplicationContext();

        final Bundle b = getIntent().getExtras();
        assert b != null;

        p_id = (String) b.getCharSequence("_id");
        p_3ds = (String) b.getCharSequence("partview_3d");
        p_name = (String) b.getCharSequence("partName");
        p_images = (String) b.getCharSequence("partimages");
        p_desc = (String) b.getCharSequence("partDesc");

        Log.e(TAG, "project_id ---- " + p_id);
        Log.e(TAG, "part_3ds-----" + p_3ds);
        Log.e(TAG, "part_name" + p_name);
        Log.e(TAG, "part_name " + part_name);
        Log.e(TAG, "part_Desc" + part_Desc);
        Log.e(TAG, "part images" + p_images);

        part_name.setText(p_name);
        part_Desc.setText(p_desc);

        try {
            JSONArray image_json = new JSONArray(p_images);
            for (int i = 0; i < image_json.length(); i++) {
                p_image1 = image_json.getString(0);
                p_image2 = image_json.getString(1);
                p_image3 = image_json.getString(2);
                p_image4 = image_json.getString(3);
                p_image5 = image_json.getString(4);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "ProjectPartImage 1----" + p_image1);
        Log.e(TAG, "ProjectPartImage 2----" + p_image2);
        Log.e(TAG, "ProjectPartImage 3----" + p_image3);
        Log.e(TAG, "ProjectPartImage 4----" + p_image4);
        Log.e(TAG, "ProjectPartImage 5----" + p_image5);

        final String[] Images = {p_image1, p_image2, p_image3, p_image4, p_image5};
        Collections.addAll(slider_images, Images);

        viewPager = findViewById(R.id.project_part_view_pager);
        imageSliderAdapter = new ProjectPartImageSliderAdapter(ProjectPartsActivity.this, slider_images, p_id);
        viewPager.setAdapter(imageSliderAdapter);

        slider_dots = findViewById(R.id.project_part_slide_dots);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        part_augment.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.AppCompatAlertDialogStyle);
            builder.setTitle("You are about to enter Augment Enabled Camera");
            builder.setMessage("This requires 2min of your patience, Do you wish to enter ?");
            builder.setPositiveButton("OK", (dialog, which) -> {

                Intent intent = new Intent(ProjectPartsActivity.this, AugmentActivity.class);
                startActivity(intent);
            });
            builder.setNegativeButton("CANCEL", null);
            builder.show();
        });

        part_3dview.setOnClickListener(v -> {
            Bundle b5 = new Bundle();
            b5.putString("part3dsName", p_3ds);
            b5.putString("name_project", p_id);
            Intent _3d_intent = new Intent(ProjectPartsActivity.this, Article3dViewActivity.class).putExtras(b5);
            startActivity(_3d_intent);
        });

        PROJECT_PART_ARTICLE_URL = REGISTER_URL + p_id;
        Log.e(TAG, "PROJECT_PART_URL------" + PROJECT_PART_ARTICLE_URL);

        getpartData();
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_images.size()];

        slider_dots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.WHITE);
            slider_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#004D40"));
    }

    final Runnable update = new Runnable() {
        @Override
        public void run() {
            if (page_position == slider_images.size()) {
                page_position = 0;
            } else {
                page_position = page_position + 1;
            }
            viewPager.setCurrentItem(page_position, true);
        }
    };

    private void getpartData() {
        ApiService.getInstance(this).getData(this, false, "PROJECT_PART_DATA", PROJECT_PART_ARTICLE_URL, "PART_ARTICLE");
    }

    public void getarticledata() {
        Iterator iterator = EnvConstants.part_articles_id.iterator();
        while (iterator.hasNext()) {
            UNIQUE_ARTICLE_URL = ARTICLE_SPECIFIC_URL + iterator.next();
            ApiService.getInstance(this).getData(this, false, "ARTICLE_PROJECT_DATA", UNIQUE_ARTICLE_URL, "ARTICLE_DATA");
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("PART_ARTICLE")) {

            try {
                JSONObject resp = response.getJSONObject("data");
                project_ids.add(resp.getString("_id"));
                Log.e(TAG, "responseproject " + response);

                JSONArray parts = resp.getJSONArray("parts");
                Log.e(TAG, "partsjson: " + parts);
                getData(parts);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (flag.equals("ARTICLE_DATA")) {
            try {
                JSONObject resp = response.getJSONObject("data");
                Log.e(TAG, "project_articles " + response);
                String id = resp.getString("_id");
                EnvConstants.part_article_name.put(id, resp.getString("name"));
                EnvConstants.part_articles_vendor_id.put(id, resp.getString("vendor_id"));
                EnvConstants.part_articles_description.put(id, resp.getString("description"));
                EnvConstants.part_articles_price.put(id, resp.getString("price"));
                EnvConstants.part_article__discounts.put(id, resp.getString("discount"));
                EnvConstants.part_article_dimensions.put(id, resp.getString("dimensions"));
                EnvConstants.part_articles_3ds.put(id, resp.getString("view_3d"));
                EnvConstants.part_articles_pattern.put(id, resp.getString("pattern"));
                EnvConstants.part_article_images.put(id, resp.getString("img"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void getData(JSONArray parts) {
        for (int i = 0; i < parts.length(); i++) {
            JSONObject object;
            try {
                object = parts.getJSONObject(i);
                JSONArray array = object.getJSONArray("articlesData");
                Log.e(TAG, "getData: articlesdata" + array);
                for (int j = 0; j < object.length(); j++) {

                    JSONObject object1 = array.getJSONObject(j);
                    part_articles_id.add(object1.getString("_id"));
                    part_article_name.add(object1.getString("name"));
                    part_article_images.add(object1.getString("img"));
                    EnvConstants.part_articles_id = part_articles_id;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, " Article Id" + part_articles_id);
        Log.e(TAG, " Article Name" + part_article_name);
        Log.e(TAG, " Article Images" + part_article_images);

        getarticledata();
        layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProjectPartDetailsAdapter(this, part_articles_id, part_article_name, part_article_images, mcontext);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(this, "Internal Error", Toast.LENGTH_SHORT).show();
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