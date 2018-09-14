package com.immersionslabs.lcatalogpro;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.immersionslabs.lcatalogpro.adapters.GridViewImageAdapter;
import com.immersionslabs.lcatalogpro.utils.ImageUtils;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    RecyclerView recycler;
    GridLayoutManager manager;
    ImageUtils imageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_view);

        Toolbar toolbar = findViewById(R.id.toolbar_gallery);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        recycler = findViewById(R.id.image_grid_view);

        imageUtils = new ImageUtils(this);
        // loading all image paths from SD card
        ArrayList<String> imagePaths = imageUtils.getFilePaths();

        // Gridview adapter
        manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        GridViewImageAdapter adapter = new GridViewImageAdapter(GalleryActivity.this, imagePaths);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        if (NetworkConnectivity.checkInternetConnection(GalleryActivity.this)) {
            onStart();
        } else {
            InternetMessage();
        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.red));
        snackbar.setAction("RETRY", v -> {
            snackbar.dismiss();
            if (NetworkConnectivity.checkInternetConnection(GalleryActivity.this)) {
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