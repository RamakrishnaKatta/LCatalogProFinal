package com.immersionslabs.lcatalogpro;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.immersionslabs.lcatalogpro.loader3ds.MyGLRenderer;
import com.immersionslabs.lcatalogpro.loader3ds.MyGLSurfaceView;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;

public class Article3dViewActivity extends AppCompatActivity {

    private static final String TAG = "Article3dViewActivity";

    String a_name, a_3ds_file_name, p_3ds_file_name, p_name, part_3d_name, part_3ds_file_name;
    private MyGLSurfaceView mGLView;
    private MyGLRenderer mRenderer;
    private SeekBar scaleBar;
    String DOWNLOAD_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article3d_view);

        Toolbar toolbar = findViewById(R.id.toolbar_3dView);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle b3 = getIntent().getExtras();
        assert b3 != null;
        a_name = (String) b3.getCharSequence("article_name");
        a_3ds_file_name = (String) b3.getCharSequence("article_3ds_file_name");
        Log.e(TAG, "Name ---- " + a_name);
        Log.e(TAG, "3DS File Name ---- " + a_3ds_file_name);

        Bundle b4 = getIntent().getExtras();
        p_name = (String) b4.getCharSequence("projectName");
        p_3ds_file_name = (String) b4.getCharSequence("project_3ds_file_name");
        p_3ds_file_name = p_name + p_3ds_file_name;
        Log.e(TAG, "P_name ------" + p_name);

        Bundle b5 = getIntent().getExtras();
        String name_project = (String) b5.getCharSequence("name_project");
        part_3d_name = (String) b5.getCharSequence("part3dsName");
        part_3ds_file_name = name_project + part_3d_name;
        Log.e(TAG, "Part_name----" + part_3d_name);
        if (a_name != null)
            DOWNLOAD_URL = EnvConstants.APP_BASE_URL + "/upload/3dviewfiles/" + a_3ds_file_name;
        if (p_name != null)
            DOWNLOAD_URL = EnvConstants.APP_BASE_URL + "/upload/project_view3d/" + p_3ds_file_name;
        if (part_3d_name != null)
            DOWNLOAD_URL = EnvConstants.APP_BASE_URL + "/upload/partview_3d/" + part_3ds_file_name;

        FloatingActionButton fab = findViewById(R.id.fab_3dView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scaleBar.getVisibility() == View.INVISIBLE) {
                    scaleBar.setVisibility(View.VISIBLE);
                } else if (scaleBar.getVisibility() == View.VISIBLE) {
                    scaleBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        mGLView = findViewById(R.id.glView);
        mGLView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGLView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            mGLView.setEGLContextClientVersion(2);

            final DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            // Set the renderer for the GLSurfaceView
            mRenderer = new MyGLRenderer(this, DOWNLOAD_URL);

            mGLView.setRenderer(mRenderer, displayMetrics.density);

        } else {
            // Show error message, if the device is not OpenGL ES 2.0 compatible
            Toast.makeText(this, "OpenGL ES 2.0 is not supported on this device", Toast.LENGTH_LONG).show();

            return;
        }

        //Create a seek bar for scaling
        scaleBar = findViewById(R.id.seekbar1);

        if (scaleBar != null) {
            //Set the bar's event listener, which will update the scale factor everytime, the value is changed
            scaleBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChanged = 0;

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChanged = progress;
                    //Turn the integer value to a percentage value
                    mRenderer.changeScale(progressChanged / 100.0f);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
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
}


