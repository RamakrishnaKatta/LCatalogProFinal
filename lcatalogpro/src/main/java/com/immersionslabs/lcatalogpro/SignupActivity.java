package com.immersionslabs.lcatalogpro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.CustomMessage;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = "SignupActivity";

    private static final int REQUEST_SIGNUP = 0;
    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/users";

    public static final String KEY_USERNAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_NO = "mobile_no";
    public static final String KEY_ADDRESS = "adress";
    public static final String KEY_TYPE = "type";
    public static final String KEY_VENDOR_ID = "vendor_id";

    TextView app_name;
    String name, email, address, mobile, password, reEnterPassword;
    String resp, code, message;

    String type = "CUSTOMER";

    int vendor_id = 100000; // This Value should be changed when a user is registered under specific customer
    private EditText _nameText, _addressText, _emailText, _mobileText, _passwordText, _reEnterPasswordText;
    private Button _signupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar_signup);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        _signupButton = findViewById(R.id.btn_signup);
        _signupButton.setOnClickListener(v -> {
            if (NetworkConnectivity.checkInternetConnection(SignupActivity.this)) {
                try {
                    signup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                InternetMessage();
            }
        });
//        if (NetworkConnectivity.checkInternetConnection(SignupActivity.this)) {
//        } else {
//            InternetMessage();
//        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.red));
        snackbar.setAction("RETRY", v -> {
            snackbar.dismiss();
            if (NetworkConnectivity.checkInternetConnection(SignupActivity.this)) {
            } else {
                InternetMessage();
            }
        });
        snackbar.show();
    }

    private void signup() throws JSONException {

        Log.e(TAG, "Sign Up");
        Log.e(TAG, "----------------------------");

        Log.e(TAG, "KEY_USERNAME--  " + KEY_USERNAME);
        Log.e(TAG, "KEY_PASSWORD--  " + KEY_PASSWORD);
        Log.e(TAG, "KEY_EMAIL--     " + KEY_EMAIL);
        Log.e(TAG, "KEY_MOBILE_NO-- " + KEY_MOBILE_NO);
        Log.e(TAG, "KEY_ADDRESS--   " + KEY_ADDRESS);
        Log.e(TAG, "KEY_TYPE--      " + KEY_TYPE);
        Log.e(TAG, "KEY_VENDOR_ID-- " + KEY_VENDOR_ID);

        Log.e(TAG, "----------------------------");

        _nameText = findViewById(R.id.input_name);
        name = _nameText.getText().toString().trim();
        Log.e(TAG, KEY_USERNAME + "--      " + name);

        _addressText = findViewById(R.id.input_address);
        address = _addressText.getText().toString().trim();
        Log.e(TAG, KEY_ADDRESS + "--    " + address);

        _emailText = findViewById(R.id.input_email);
        email = _emailText.getText().toString().trim();
        Log.e(TAG, KEY_EMAIL + "--     " + email);

        _mobileText = findViewById(R.id.input_mobile);
        mobile = _mobileText.getText().toString().trim();
        Log.e(TAG, KEY_MOBILE_NO + "-- " + mobile);

        _passwordText = findViewById(R.id.input_password);
        password = _passwordText.getText().toString().trim();
        Log.e(TAG, KEY_PASSWORD + "--  " + password);

        _reEnterPasswordText = findViewById(R.id.input_reEnterPassword);
        reEnterPassword = _reEnterPasswordText.getText().toString().trim();

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        // SIGN_UP LOGIC !!
        JSONObject signup_parameters = new JSONObject();
        signup_parameters.put(KEY_USERNAME, name);
        signup_parameters.put(KEY_ADDRESS, address);
        signup_parameters.put(KEY_EMAIL, email);
        signup_parameters.put(KEY_MOBILE_NO, mobile);
        signup_parameters.put(KEY_PASSWORD, password);
        signup_parameters.put(KEY_TYPE, type);
        signup_parameters.put(KEY_VENDOR_ID, vendor_id);

        Log.e(TAG, "----------------------------");

        Log.e(TAG, "KEY_USERNAME --  " + name);
        Log.e(TAG, "KEY_ADDRESS --   " + address);
        Log.e(TAG, "KEY_EMAIL --     " + email);
        Log.e(TAG, "KEY_MOBILE_NO -- " + mobile);
        Log.e(TAG, "KEY_PASSWORD --  " + password);
        Log.e(TAG, "KEY_TYPE --      " + type);
        Log.e(TAG, "KEY_VENDOR_ID -- " + vendor_id);

        Log.e(TAG, "request--" + signup_parameters);

//        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();

        ApiService.getInstance(this).postData(this, REGISTER_URL, signup_parameters, "SIGNUP", "USER_SIGNUP");
        new android.os.Handler().postDelayed(
                () -> {
                    if (Objects.equals(message, "SUCCESS") || Objects.equals(code, "200")) {
                        onSignupSuccess();
                    } else {
                        onSignupFailed();
                    }
//                        progressDialog.dismiss();
                }, 3000);
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("USER_SIGNUP")) {
            try {
                resp = response.getString("success");
                code = response.getString("status_code");
                message = response.getString("message");
                onSignupSuccess();
                Log.e(TAG, "Response--" + resp + " Status Code--" + code + " Message--" + message);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(SignupActivity.this, "Internal Error", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    public void onSignupSuccess() {

        CustomMessage.getInstance().CustomMessage(SignupActivity.this, "SignUp Success, Please Welcome");

        _signupButton = findViewById(R.id.btn_signup);
        _signupButton.setEnabled(false);
        setResult(RESULT_OK, null);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {

        CustomMessage.getInstance().CustomMessage(SignupActivity.this, "SignUp failed, Please Try Again");

        if (name != null) {
            _nameText.setText(name);
        }
        if (email != null) {
            _emailText.setText(email);
        }
        if (address != null) {
            _addressText.setText(address);
        }
        if (mobile != null) {
            _mobileText.setText(mobile);
        }

        _signupButton = findViewById(R.id.btn_signup);
        _signupButton.setEnabled(true);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure you don't want to SignUp ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(SignupActivity.this, UserTypeActivity.class);
                startActivity(intent);
                // SignupActivity.super.onDestroy();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CustomMessage.getInstance().CustomMessage(SignupActivity.this, "Thanks for thinking again.");
            }
        });
        builder.show();
    }

    public boolean validate() {
        boolean valid = true;

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
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