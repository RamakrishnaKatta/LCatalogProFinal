package com.immersionslabs.lcatalogpro;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.immersionslabs.lcatalog.adapters.ProjectFragmentAdapter;

public class ProjectPageActivity extends AppCompatActivity {

    private static final String TAG = "ProjectPageActivity";

    String project_images;
    String project_position, project_name, project_id, project_description, project_sub_desc, project_vendor_id;
    String project_3ds, project_pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = findViewById(R.id.toolbar_projectdetails);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Bundle b = getIntent().getExtras();
        assert b != null;

        project_name = (String) b.getCharSequence("Project_Name");
        project_description = (String) b.getCharSequence("Project_Description");
        project_sub_desc = (String) b.getCharSequence("Project_SubDescription");
        project_position = (String) b.getCharSequence("Project_Position");
        project_id = (String) b.getCharSequence("Project_id");
        project_3ds = (String) b.getCharSequence("Project_View3d");
        project_pattern = (String) b.getCharSequence("Project_PatternImg");
        project_vendor_id = b.getString("Project_VendorId");
        project_images = (String) b.getCharSequence("Project_Image");

        Log.e(TAG, "Project Name----" + project_name);
        Log.e(TAG, "\n Project Description----" + project_description);
        Log.e(TAG, "\n Project Sub Description----" + project_sub_desc);
        Log.e(TAG, "\n Project project_position----" + project_position);
        Log.e(TAG, "\n Project Id----" + project_id);
        Log.e(TAG, "\n Project Pattern----" + project_pattern);
        Log.e(TAG, "\n Project Images----" + project_images);
        Log.e(TAG, "\n Project 3DS File----" + project_3ds);
        Log.e(TAG, "\n Project Vendor Id----" + project_vendor_id);

        TabLayout tabLayout = findViewById(R.id.project_details_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Design"));
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Parts"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.project_details_pager);
        final ProjectFragmentAdapter adapter = new ProjectFragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                project_name, project_id, project_description, project_sub_desc, project_images, project_3ds, project_pattern, project_vendor_id);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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
}