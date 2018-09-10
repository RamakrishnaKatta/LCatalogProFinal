package com.immersionslabs.lcatalogpro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.immersionslabs.lcatalogpro.utils.EnvConstants;

public class ExperimentalAugmentActivity extends AppCompatActivity {

    private static final String TAG = "ExperimentalAugmentActivity";

    String ARTICLE_URL_AUGMENT = EnvConstants.PORTAL_BASE_URL + "/#/articleAr/";
    String PROJECT_URL_AUGMENT = EnvConstants.PORTAL_BASE_URL + "/#/projectAr/";
    String article_augment_file_data, project_augment_file_data, flag;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimental_augment);

        Toolbar toolbar = findViewById(R.id.toolbar_exp_augment);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);
        WebView webView_augment = findViewById(R.id.webView_augment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Bundle article_augment_data = getIntent().getExtras();
        assert article_augment_data != null;
        article_augment_file_data = (String) article_augment_data.getCharSequence("article_augment_file");
        flag = article_augment_data.getString("flag");
        Log.e(TAG, "FLAG--" + flag);

        final Bundle project_augment_data = getIntent().getExtras();
        assert project_augment_data != null;
        project_augment_file_data = project_augment_data.getString("project_augment_file");
        flag = project_augment_data.getString("flag");
        Log.e(TAG, "FLAG--" + flag);

        assert flag != null;
        if (flag.equals("article")) {
            ARTICLE_URL_AUGMENT += article_augment_file_data;
            Log.e(TAG, "VENDOR_ARTICLE_URL--" + ARTICLE_URL_AUGMENT);
            webView_augment.loadUrl(ARTICLE_URL_AUGMENT);

        } else if (flag.equals("project")) {
            PROJECT_URL_AUGMENT += project_augment_file_data;
            Log.e(TAG, "VENDOR_PROJECT_URL--" + PROJECT_URL_AUGMENT);
            webView_augment.loadUrl(PROJECT_URL_AUGMENT);
        }
        webView_augment.clearCache(true);

        WebSettings webSettings = webView_augment.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }
}