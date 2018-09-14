package com.immersionslabs.lcatalogpro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.immersionslabs.lcatalogpro.utils.CryptionRijndeal;
import com.immersionslabs.lcatalogpro.utils.CustomMessage;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;
import com.immersionslabs.lcatalogpro.utils.SessionManager;
import com.immersionslabs.lcatalogpro.utils.UserCheckUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();
    private static final int REQUEST_FORGOT = 0;

    private static final String PASSWORD_UPDATE_URL = EnvConstants.APP_BASE_URL + "/users/changePassword";

    CryptionRijndeal rijndeal_obj;
    SessionManager sessionManager;

    private Button _submitButton;
    private EditText _emailText, _passwordText, _reenterPasswordText;
    private String email, password, ReEnterPass;
    String code, message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        _submitButton = findViewById(R.id.btn_submit);
        rijndeal_obj = new CryptionRijndeal();
        sessionManager = new SessionManager(getApplicationContext());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar_login);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        _submitButton.setOnClickListener(v -> {
            try {
                submit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        if (NetworkConnectivity.checkInternetConnection(ForgotPasswordActivity.this)) {

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
            if (NetworkConnectivity.checkInternetConnection(ForgotPasswordActivity.this)) {
            } else {
                InternetMessage();
            }
        });
        snackbar.show();
    }

    private void submit() throws JSONException {
        Log.e(TAG, "Password Update Request");

        if (!validate()) {
            onSubmitFailed();
            return;
        }
        _submitButton = findViewById(R.id.btn_submit);
        _submitButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Password...");
        progressDialog.show();

        _emailText = findViewById(R.id.input_forgot_email);
        _passwordText = findViewById(R.id.input_New_password);
        _reenterPasswordText = findViewById(R.id.input_reEnter_New_Password);

        email = _emailText.getText().toString().trim();
        password = _passwordText.getText().toString().trim();
        ReEnterPass = _reenterPasswordText.getText().toString().trim();

        final JSONObject password_update_parameters = new JSONObject();
        password_update_parameters.put("email", email);
        password_update_parameters.put("password", password);
        Log.e(TAG, "Request--" + password_update_parameters);

        ApiService.getInstance(this).postData(this, PASSWORD_UPDATE_URL, password_update_parameters, "FORGOT PASSWORD ACTIVITY", "POST_PASSWORD");
        new android.os.Handler().postDelayed(() -> {
            // On complete call either onLoginSuccess or onLoginFailed
            if (Objects.equals(code, "200")) {
                onSubmitSuccess();
            } else {
                onSubmitFailed();
            }
            progressDialog.dismiss();
        }, 3000);
    }

    @Override
    public void onResponseCallback(JSONObject requestResponse, String flag) {
        if (flag.equals("POST_PASSWORD")) {
            Log.e(TAG, "response--" + requestResponse);
            try {
                code = requestResponse.getString("status_code");
                message = requestResponse.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(ForgotPasswordActivity.this, "Internal Error", Toast.LENGTH_LONG).show();
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

    /*Validations for the Forgot Password Activity*/
    private boolean validate() {
        boolean valid = true;
        _emailText = findViewById(R.id.input_forgot_email);
        _passwordText = findViewById(R.id.input_New_password);
        _reenterPasswordText = findViewById(R.id.input_reEnter_New_Password);

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        ReEnterPass = _reenterPasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (ReEnterPass.isEmpty() || ReEnterPass.length() < 4 || ReEnterPass.length() > 10 || !(ReEnterPass.equals(password))) {
            _reenterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reenterPasswordText.setError(null);
        }
        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FORGOT) {
            if (resultCode == RESULT_OK) {

                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    private void onSubmitSuccess() {
        _submitButton = findViewById(R.id.btn_submit);
        _submitButton.setEnabled(false);
        CustomMessage.getInstance().CustomMessage(this, "Password has been changed successfully, Try login with the new password");
        try {
            String emailtext = rijndeal_obj.encrypt(email);
            String passwordtext = rijndeal_obj.encrypt(password);
            final String Credentials = emailtext + "  ###  " + passwordtext;
            UserCheckUtil.writeToFile(Credentials, "customer");
            String text_file_data = UserCheckUtil.readFromFile("customer");
            Log.e(TAG, "User Details-- " + text_file_data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sessionManager.updatepassword(password);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void onSubmitFailed() {
        Button _submitbtn = findViewById(R.id.btn_submit);
        if (email != null) {
            _emailText.setText(email);
        }
        CustomMessage.getInstance().CustomMessage(this, "Your email is not registered, Please Enter a registered Email Id");
        _submitbtn.setEnabled(true);
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