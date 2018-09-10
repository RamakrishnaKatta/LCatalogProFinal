package com.immersionslabs.lcatalogpro;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.immersionslabs.lcatalogpro.utils.EnvConstants;


public class Experimental3DViewActivity extends AppCompatActivity {

    private static final String TAG = "Experimental3DViewActivity";

    String ARTICLE_URL_3DS = EnvConstants.PORTAL_BASE_URL + "/#/3d_view/";
    String PROJECT_URL_3DS = EnvConstants.PORTAL_BASE_URL + "/#/3d_view_project/";
    String project_3ds_file_data, article_3ds_file_data, flag;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimental_3dview);

        Toolbar toolbar = findViewById(R.id.toolbar_exp_3dView);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);
        WebView webView_3ds = findViewById(R.id.webView_3ds);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Bundle article_3ds_data = getIntent().getExtras();
        assert article_3ds_data != null;
        article_3ds_file_data = (String) article_3ds_data.getCharSequence("article_3ds_file");
        flag = article_3ds_data.getString("flag");
        Log.e(TAG, "FLAG--" + flag);

        final Bundle project_3ds_data = getIntent().getExtras();
        assert project_3ds_data != null;
        project_3ds_file_data = (String) article_3ds_data.getCharSequence("project_3ds_file");
        flag = article_3ds_data.getString("flag");
        Log.e(TAG, "FLAG--" + flag);

        assert flag != null;
        if (flag.equals("article")) {
            ARTICLE_URL_3DS += article_3ds_file_data;
            Log.e(TAG, "VENDOR_URL--" + ARTICLE_URL_3DS);
            webView_3ds.loadUrl(ARTICLE_URL_3DS);

        } else if (flag.equals("project")) {
            PROJECT_URL_3DS += project_3ds_file_data;
            Log.e(TAG, "PROJECT_3D_URL--" + PROJECT_URL_3DS);
            webView_3ds.loadUrl(PROJECT_URL_3DS);
        }
        webView_3ds.clearCache(true);

        WebSettings webSettings = webView_3ds.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
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