package com.immersionslabs.lcatalogpro;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import static com.immersionslabs.lcatalog.Utils.EnvConstants.APP_BASE_URL;

public class UserAccountActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = "UserAccountActivity";

    private String LOGIN_URL = APP_BASE_URL + "/users";
    private static final int REQUEST_UPDATE = 0;

    SessionManager sessionmanager;
    CryptionRijndeal rijndeal_obj;

    private EditText name, email, address, mobile;
    private KeyListener listener;
    private Button edit_user, update_user;
    private String user_name, user_address, user_phone, user_email, user_id, user_password, user_global_id;
    String resp, message, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        Toolbar toolbar = findViewById(R.id.toolbar_user_account);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        sessionmanager = new SessionManager(getApplicationContext());
        HashMap hashmap = new HashMap();

        hashmap = sessionmanager.getUserDetails();

        user_name = (String) hashmap.get(SessionManager.KEY_NAME);
        user_address = (String) hashmap.get(SessionManager.KEY_ADDRESS);
        user_email = (String) hashmap.get(SessionManager.KEY_EMAIL);
        user_phone = (String) hashmap.get(SessionManager.KEY_MOBILE_NO);
        user_id = (String) hashmap.get(SessionManager.KEY_USER_ID);
        user_password = (String) hashmap.get(SessionManager.KEY_PASSWORD);
        user_global_id = (String) hashmap.get(SessionManager.KEY_GLOBAL_USER_ID);

        rijndeal_obj = new CryptionRijndeal();

        name = findViewById(R.id.user_input_name);
        disableEditText(name);
        name.setText(user_name);

        address = findViewById(R.id.user_input_address);
        disableEditText(address);
        address.setText(user_address);

        email = findViewById(R.id.user_input_email);
        disableEditText(email);
        email.setText(user_email);

        mobile = findViewById(R.id.user_input_mobile);
        disableEditText(mobile);
        mobile.setText(user_phone);

        edit_user = findViewById(R.id.btn_edit_account);
        edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {

                enableEditText(name);
                enableEditText(email);
                enableEditText(address);
                enableEditText(mobile);

                edit_user.setVisibility(View.GONE);
                update_user = findViewById(R.id.btn_update_account);
                update_user.setVisibility(View.VISIBLE);
                update_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update();
                    }
                });
            }
        });

        if (NetworkConnectivity.checkInternetConnection(UserAccountActivity.this)) {

        } else {
            InternetMessage();
        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                if (NetworkConnectivity.checkInternetConnection(UserAccountActivity.this)) {
                } else {
                    InternetMessage();
                }
            }
        });
        snackbar.show();
    }

    public void update() {

        if (!validate()) {
            UpdateFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(UserAccountActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Account...");
        progressDialog.show();

        user_name = name.getText().toString();
        user_email = email.getText().toString();
        user_phone = mobile.getText().toString();
        user_address = address.getText().toString();

        try {
            final JSONObject user_update_parameters = new JSONObject();
            user_update_parameters.put("name", user_name);
            user_update_parameters.put("email", user_email);
            user_update_parameters.put("mobile_no", user_phone);
            user_update_parameters.put("adress", user_address);

            Log.e(TAG, "Request--" + user_update_parameters);

            final JSONObject request = new JSONObject();
            request.put("request", user_update_parameters);
            Log.e(TAG, "Request--" + request);
            LOGIN_URL += "/" + user_global_id;
            Log.e(TAG, "Global user Id--" + user_global_id);

            ApiService.getInstance(this).putData(this, LOGIN_URL, user_update_parameters, "UPDATE", "USER_UPDATE");

        } catch (Exception e) {
            e.printStackTrace();
        }
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (Objects.equals(message, "FAILURE")) {
                            UpdateFailed();
                        } else {
                            updateSuccess();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("USER_UPDATE")) {
            Log.e(TAG, "response--" + response);

            try {
                resp = response.getString("success");
                code = response.getString("status_code");
                message = response.getString("message");
                Log.e(TAG, "resp " + resp + " code--" + code + " message--" + message);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(UserAccountActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
    }

    public void updateSuccess() {

        CustomMessage.getInstance().CustomMessage(UserAccountActivity.this, "Update Success");

        Toast.makeText(getBaseContext(), "Update Success", Toast.LENGTH_LONG).show();

        sessionmanager.updatedetails(user_name, user_email, user_phone, user_address);
        try {
            String enc_email_text = rijndeal_obj.encrypt(user_email);
            String enc_password_text = rijndeal_obj.encrypt(user_password);

            final String Credentials = enc_email_text + "  ###  " + enc_password_text;
            UserCheckUtil.writeToFile(Credentials, "customer");

            String text_file_data = UserCheckUtil.readFromFile("customer");
            Log.e(TAG, "User Details-- " + text_file_data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, UserAccountActivity.class);
        startActivity(intent);
        finish();
    }

    public void UpdateFailed() {
        CustomMessage.getInstance().CustomMessage(UserAccountActivity.this, "Update failed");

        // Toast.makeText(getBaseContext(), "Update failed", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, UserAccountActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPDATE) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
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

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setClickable(false);
        listener = editText.getKeyListener();
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setEnabled(true);
        editText.setClickable(true);
        editText.setCursorVisible(true);
        editText.setKeyListener(listener);
    }

    public boolean validate() {
        boolean valid = true;

        name = findViewById(R.id.user_input_name);
        address = findViewById(R.id.user_input_address);
        email = findViewById(R.id.user_input_email);
        mobile = findViewById(R.id.user_input_mobile);

        String user_name = name.getText().toString().trim();
        String user_address = address.getText().toString().trim();
        String user_email = email.getText().toString().trim();
        String user_mobile = mobile.getText().toString().trim();

        if (user_name.isEmpty() || user_name.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (user_address.isEmpty()) {
            address.setError("Enter Valid Address");
            valid = false;
        } else {
            address.setError(null);
        }

        if (user_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (user_mobile.isEmpty() || user_mobile.length() != 10) {
            mobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobile.setError(null);
        }

        return valid;
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
