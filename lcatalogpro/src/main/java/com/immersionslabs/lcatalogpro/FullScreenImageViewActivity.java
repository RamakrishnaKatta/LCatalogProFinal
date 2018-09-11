package com.immersionslabs.lcatalogpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.immersionslabs.lcatalogpro.adapters.FullScreenImageAdapter;
import com.immersionslabs.lcatalogpro.utils.ImageUtils;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;

public class FullScreenImageViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image_view);

        ViewPager viewPager = findViewById(R.id.pager);

        ImageUtils imageUtils = new ImageUtils(getApplicationContext());

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        FullScreenImageAdapter adapter = new FullScreenImageAdapter(FullScreenImageViewActivity.this, imageUtils.getFilePaths());

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);

        if (NetworkConnectivity.checkInternetConnection(FullScreenImageViewActivity.this)) {

        } else {
            InternetMessage();
        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(this,R.color.red));
        snackbar.setAction("RETRY", v -> {
            snackbar.dismiss();
            if (NetworkConnectivity.checkInternetConnection(FullScreenImageViewActivity.this)) {

            } else {
                InternetMessage();
            }
        });
        snackbar.show();
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