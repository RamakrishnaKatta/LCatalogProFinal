package com.immersionslabs.lcatalogpro;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.CustomMessage;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class FeedbackActivity extends AppCompatActivity implements ApiCommunication {

    public static final String TAG = "FeedbackActivity";

    EditText feed_name_text, feed_subject_text, feed_number_text;
    Button feed_Submit;
    String feed_name, feed_message, feed_number;
    String code, message;

    private static final String FEEDBACK_URL = EnvConstants.APP_BASE_URL + "/feedback";

    int vendor_id = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = findViewById(R.id.toolbar_feedback);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        feed_Submit = findViewById(R.id.btn_feed_submit);

        feed_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    feedback();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void feedback() throws JSONException {

        final ProgressDialog progressDialog = new ProgressDialog(FeedbackActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting...");
        progressDialog.show();

        feed_name_text = findViewById(R.id.input_feed_name);
        feed_number_text = findViewById(R.id.input_feed_number);
        feed_subject_text = findViewById(R.id.input_feed_subject);


        feed_name = feed_name_text.getText().toString().trim();
        feed_number = feed_number_text.getText().toString().trim();
        feed_message = feed_subject_text.getText().toString().trim();

        final JSONObject feedback = new JSONObject();
        feedback.put("name", feed_name);
        feedback.put("mobile", feed_number);
        feedback.put("message", feed_message);
        feedback.put("vendor_id", vendor_id);
        Log.e(TAG, "feedback: Request" + feedback);

        ApiService.getInstance(this).postData(this, FEEDBACK_URL, feedback, "FEED", "FEEDBACK_SUBMIT");
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Objects.equals(code, "200")) {
                    FeedSuccess();
                } else {
                    FeedFailed();
                }
                progressDialog.dismiss();
            }
        }, 3000);
    }

    private void FeedFailed() {
        feed_Submit = findViewById(R.id.btn_feed_submit);
        feed_Submit.setEnabled(true);
        CustomMessage.getInstance().CustomMessage(this, "Please try again");

    }

    private void FeedSuccess() {
        CustomMessage.getInstance().CustomMessage(this, "Thank you for your valuable Feedback");
        feed_Submit.setEnabled(true);
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
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("FEEDBACK_SUBMIT")) {
            Log.e(TAG, "onResponseCallback: response" + response);
            try {
                code = response.getString("status_code");
                message = response.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(FeedbackActivity.this, "Internal Error", Toast.LENGTH_LONG).show();
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

