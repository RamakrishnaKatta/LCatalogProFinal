package com.immersionslabs.lcatalogpro;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.immersionslabs.lcatalog.Utils.CustomMessage;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.NetworkConnectivity;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.CustomMessage;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class VendorRegistrationActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = "VendorRegistration";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/vendor_requests";

    public static final String KEY_V_COMPANYNAME = "company_name";
    public static final String KEY_V_CONTACTPERSONNAME = "contact_person_name";
    public static final String KEY_V_TOTALMODELS = "tot_models";
    public static final String KEY_V_EMAIL = "email";
    public static final String KEY_V_ADDRESS = "address";
    public static final String KEY_V_LOCATION = "loc";
    public static final String KEY_V_STATE = "state";
    public static final String KEY_V_PIN = "pin";
    public static final String KEY_V_MOBILENO = "mobile";

    String companyName, companyContactName, companyAddress, companyLocation, companyState, companyPin, companyEmail, companyMobileNo, companyModelCount;
    String resp, message;
    private EditText v_companyName, v_companyContactPerson, v_companyAddress, v_companyLocation, v_companyState, v_companyPin, v_companyEmail, v_companyMobileNo, v_totalModels;
    private Button v_registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_registration);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar_vendor_reg);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        v_registerButton = findViewById(R.id.btn_vendor_submit);
        v_registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectivity.checkInternetConnection(VendorRegistrationActivity.this)) {
                    try {
                        vendorRegister();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    InternetMessage();
                }
            }
        });

//        if (NetworkConnectivity.checkInternetConnection(VendorRegistrationActivity.this)) {
//
//        } else {
//            InternetMessage();
//        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                if (NetworkConnectivity.checkInternetConnection(VendorRegistrationActivity.this)) {
                } else {
                    InternetMessage();
                }
            }
        });
        snackbar.show();
    }

    private void vendorRegister() throws JSONException {
        Log.e(TAG, "Signup");

        Log.e(TAG, "KEY_V_COMPANYNAME--" + KEY_V_COMPANYNAME);
        Log.e(TAG, "KEY_V_CONTACTPERSONNAME--" + KEY_V_CONTACTPERSONNAME);
        Log.e(TAG, "KEY_V_TOTALMODELS--" + KEY_V_TOTALMODELS);
        Log.e(TAG, "KEY_V_EMAIL--" + KEY_V_EMAIL);
        Log.e(TAG, "KEY_V_ADDRESS--" + KEY_V_ADDRESS);
        Log.e(TAG, "KEY_V_LOCATION--" + KEY_V_LOCATION);
        Log.e(TAG, "KEY_V_STATE--" + KEY_V_STATE);
        Log.e(TAG, "KEY_V_PIN--" + KEY_V_PIN);
        Log.e(TAG, "KEY_V_MOBILENO--" + KEY_V_MOBILENO);

        v_companyName = findViewById(R.id.vendor_name);
        companyName = v_companyName.getText().toString().trim();
        Log.e(TAG, "Company Name--" + companyName);

        v_companyContactPerson = findViewById(R.id.vendor_contact_name);
        companyContactName = v_companyContactPerson.getText().toString().trim();
        Log.e(TAG, "Company ContactName--" + companyContactName);

        v_companyAddress = findViewById(R.id.vendor_address);
        companyAddress = v_companyAddress.getText().toString().trim();
        Log.e(TAG, "Company Address--" + companyAddress);

        v_companyLocation = findViewById(R.id.vendor_location);
        companyLocation = v_companyLocation.getText().toString().trim();
        Log.e(TAG, "Company Location--" + companyLocation);

        v_companyState = findViewById(R.id.vendor_state);
        companyState = v_companyState.getText().toString().trim();
        Log.e(TAG, "Company State--" + companyState);

        v_companyPin = findViewById(R.id.vendor_pincode);
        companyPin = v_companyPin.getText().toString().trim();
        Log.e(TAG, "company PinCode--" + companyPin);

        v_companyEmail = findViewById(R.id.vendor_email);
        companyEmail = v_companyEmail.getText().toString().trim();
        Log.e(TAG, "Company Email--" + companyEmail);

        v_companyMobileNo = findViewById(R.id.vendor_mobile);
        companyMobileNo = v_companyMobileNo.getText().toString().trim();
        Log.e(TAG, "company Mobile No--" + companyMobileNo);

        v_totalModels = findViewById(R.id.vendor_modelcount);
        companyModelCount = v_totalModels.getText().toString().trim();
        Log.e(TAG, "Company Model Count--" + companyModelCount);

        if (!validate()) {
            onVendorRegistrationFailed();
            return;
        }
        v_registerButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(VendorRegistrationActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting Vendor Registration Form to L Catalog......");
        progressDialog.show();

        JSONObject vendor_request = new JSONObject();

        vendor_request.put(KEY_V_COMPANYNAME, companyName);
        vendor_request.put(KEY_V_CONTACTPERSONNAME, companyContactName);
        vendor_request.put(KEY_V_ADDRESS, companyAddress);
        vendor_request.put(KEY_V_LOCATION, companyLocation);
        vendor_request.put(KEY_V_STATE, companyState);
        vendor_request.put(KEY_V_PIN, companyPin);
        vendor_request.put(KEY_V_EMAIL, companyEmail);
        vendor_request.put(KEY_V_MOBILENO, companyMobileNo);
        vendor_request.put(KEY_V_TOTALMODELS, companyModelCount);

        Log.e(TAG, "vendor_request--" + vendor_request);

        final JSONObject request = new JSONObject();
        request.put("request", vendor_request);
        Log.e(TAG, "Request--" + request);

        ApiService.getInstance(this).postData(this, REGISTER_URL, vendor_request, "VENDOR_REQUEST", "VENDOR_REQUEST");

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed depending on success
                        if (Objects.equals(resp, "success")) {
                            onVendorRegistrationSuccess();
                        } else {
                            onVendorRegistrationFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("VENDOR_REQUEST")) {
            Log.e(TAG, "Response--" + response);

            try {
                resp = response.getString("success");
//                    code = requestResponse.getString("code");
//                    message = requestResponse.getString("message");
                Log.e(TAG, "response--" + resp);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(VendorRegistrationActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {
        boolean valid = true;

        v_companyName = findViewById(R.id.vendor_name);
        String companyName = v_companyName.getText().toString().trim();
        if (companyName.isEmpty() || companyName.length() < 3) {
            v_companyName.setError("Enter Valid Name, at least 3 characters");
            valid = false;
        } else {
            v_companyName.setError(null);
        }

        v_companyContactPerson = findViewById(R.id.vendor_contact_name);
        String companyContactName = v_companyContactPerson.getText().toString().trim();
        if (companyContactName.isEmpty() || companyContactName.length() < 3) {
            v_companyContactPerson.setError("Enter Valid Name, at least 3 characters");
            valid = false;
        } else {
            v_companyContactPerson.setError(null);
        }

        v_companyAddress = findViewById(R.id.vendor_address);
        String companyAddress = v_companyAddress.getText().toString().trim();
        if (companyAddress.isEmpty()) {
            v_companyAddress.setError("Enter Valid Address");
            valid = false;
        } else {
            v_companyAddress.setError(null);
        }

        v_companyLocation = findViewById(R.id.vendor_location);
        final String companyLocation = v_companyLocation.getText().toString().trim();
        if (companyLocation.isEmpty()) {
            v_companyLocation.setError("Enter Valid Location");
            valid = false;
        } else {
            v_companyLocation.setError(null);
        }

        v_companyState = findViewById(R.id.vendor_state);
        String companyState = v_companyState.getText().toString().trim();
        if (companyState.isEmpty()) {
            v_companyState.setError("Enter Valid State");
            valid = false;
        } else {
            v_companyState.setError(null);
        }

        v_companyPin = findViewById(R.id.vendor_pincode);
        String companyPin = v_companyPin.getText().toString().trim();
        if (companyPin.isEmpty()) {
            v_companyPin.setError("Enter Valid Pin");
            valid = false;
        } else {
            v_companyPin.setError(null);
        }

        v_companyEmail = findViewById(R.id.vendor_email);
        String companyEmail = v_companyEmail.getText().toString().trim();
        if (companyEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(companyEmail).matches()) {
            v_companyEmail.setError("enter a valid email address");
            valid = false;
        } else {
            v_companyEmail.setError(null);
        }

        v_companyMobileNo = findViewById(R.id.vendor_mobile);
        String companyMobileNo = v_companyMobileNo.getText().toString().trim();
        if (companyMobileNo.isEmpty() || companyMobileNo.length() != 10) {
            v_companyMobileNo.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            v_companyMobileNo.setError(null);
        }

        v_totalModels = findViewById(R.id.vendor_modelcount);
        String companyModelCount = v_totalModels.getText().toString().trim();
        if (companyModelCount.isEmpty()) {
            v_totalModels.setError("Enter a valid no of models you require");
            valid = false;
        } else {
            v_totalModels.setError(null);
        }

        return valid;
    }

    public void onVendorRegistrationSuccess() {

        Toast.makeText(this, "Successfully registered your request, We will respond very soon! ", Toast.LENGTH_LONG).show();
        v_registerButton = findViewById(R.id.btn_vendor_submit);
        v_registerButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onVendorRegistrationFailed() {

        CustomMessage.getInstance().CustomMessage(VendorRegistrationActivity.this, "Vendor Registration Failed");

        if (companyName != null) {
            v_companyName.setText(companyName);
        }
        if (companyContactName != null) {
            v_companyContactPerson.setText(companyContactName);
        }
        if (companyAddress != null) {
            v_companyAddress.setText(companyAddress);
        }
        if (companyLocation != null) {
            v_companyLocation.setText(companyLocation);
        }
        if (companyState != null) {
            v_companyState.setText(companyState);
        }
        if (companyPin != null) {
            v_companyPin.setText(companyPin);
        }
        if (companyEmail != null) {
            v_companyEmail.setText(companyEmail);
        }
        if (companyMobileNo != null) {
            v_companyMobileNo.setText(companyMobileNo);
        }
        if (companyModelCount != null) {
            v_totalModels.setText(companyModelCount);
        }

        v_registerButton = findViewById(R.id.btn_vendor_submit);
        v_registerButton.setEnabled(false);
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
