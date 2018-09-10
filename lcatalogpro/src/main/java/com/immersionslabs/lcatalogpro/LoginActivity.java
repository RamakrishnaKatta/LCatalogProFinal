package com.immersionslabs.lcatalogpro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
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
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.CryptionRijndeal;
import com.immersionslabs.lcatalogpro.utils.CustomMessage;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;
import com.immersionslabs.lcatalogpro.utils.PrefManager;
import com.immersionslabs.lcatalogpro.utils.SessionManager;
import com.immersionslabs.lcatalogpro.utils.UserCheckUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LoginActivity extends AppCompatActivity implements ApiCommunication {

    private static final String TAG = "LoginActivity";

    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_FORGOT_PASSWORD = 0;

    private static final String LOGIN_URL = EnvConstants.APP_BASE_URL + "/customerLogin";
    private static final String Local_url = "http://192.168.0.10:4000/customerLogin";

    SessionManager sessionmanager;
    SharedPreferences preferences;
    CryptionRijndeal rijndeal_obj;
    private PrefManager prefManager5;

    TextView _forgot_password;
    EditText _emailText, _passwordText;
    Button _loginButton;
    AppCompatImageButton get_details;
    CoordinatorLayout LoginLayout;
    String userId, globalUserId;
    String resp, code, message;
    String userName, userEmail, userPhone, userAddress, userType;
    String email, password;
    File file_customer;
    String[] text_from_customer_file;
    ArrayList<String> temp = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userType = "CUSTOMER";

        sessionmanager = new SessionManager(getApplicationContext());

        _loginButton = findViewById(R.id.btn_login);
        _forgot_password = findViewById(R.id.link_forgot_password);
        get_details = findViewById(R.id.btn_get_data);
        LoginLayout = findViewById(R.id.LoginLayout);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        sessionmanager = new SessionManager(getApplicationContext());
        rijndeal_obj = new CryptionRijndeal();

        SharedPreferences settings = this.getSharedPreferences("LoginSession", Context.MODE_PRIVATE);
        String customer_text_file_location = Environment.getExternalStorageDirectory() + "/L_CATALOG/customer.txt";
        file_customer = new File(customer_text_file_location);

//        _forgot_password.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        //Disables the keyboard to appear on the activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar_login);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        get_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectivity.checkInternetConnection(LoginActivity.this)) {
                    try {
                        login();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    InternetMessage();
                }
            }
        });

        _forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivityForResult(intent, REQUEST_FORGOT_PASSWORD);
            }
        });

        prefManager5 = new PrefManager(this);
        Log.e(TAG, "" + prefManager5.LoginActivityScreenLaunch());
        if (prefManager5.LoginActivityScreenLaunch()) {
            showcaseview();
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
                if (NetworkConnectivity.checkInternetConnection(LoginActivity.this)) {

                } else {
                    InternetMessage();
                }
            }
        });
        snackbar.show();
    }

    private void showcaseview() {
        prefManager5.SetLoginActivityScreenLaunch();
        Log.e(TAG, "" + prefManager5.LoginActivityScreenLaunch());
        Typeface text_font = ResourcesCompat.getFont(Objects.requireNonNull(getApplicationContext()), R.font.assistant_semibold);
        assert text_font != null;
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.btn_get_data), "AUTO-FILL", "Click here to Auto fill your recent details ")
                        .cancelable(true)
                        .transparentTarget(true)
                        .outerCircleColor(R.color.primary_dark)
                        .targetRadius(25)
                        .textTypeface(text_font)
                        .textColor(R.color.white)
                        .tintTarget(true)
                , new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                    }
                });
    }

    private void setData() {

        if (!file_customer.exists()) {
            CustomMessage.getInstance().CustomMessage(LoginActivity.this, "No Saved Details Yet");

        } else {
            text_from_customer_file = UserCheckUtil.readFromFile("customer").split(" ### ");
            String dec_password_text = null;
            String dec_email_text = null;
            try {
                dec_email_text = rijndeal_obj.decrypt(text_from_customer_file[0].trim());
                dec_password_text = rijndeal_obj.decrypt(text_from_customer_file[1].trim());
            } catch (Exception e) {
                e.printStackTrace();
            }

            _emailText = findViewById(R.id.input_email);
            _emailText.setText(dec_email_text);

            _passwordText = findViewById(R.id.input_password);
            _passwordText.setText(dec_password_text);
        }

        _loginButton.setEnabled(true);
    }

    public void login() throws JSONException {
        Log.e(TAG, "Customer Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        _loginButton = findViewById(R.id.btn_login);
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        _emailText = findViewById(R.id.input_email);
        email = _emailText.getText().toString().trim();
        Log.e(TAG, "Entered email--" + email);

        _passwordText = findViewById(R.id.input_password);
        password = _passwordText.getText().toString().trim();
        Log.e(TAG, "Entered password--" + password);

        // Implement your own authentication logic here.
        try {
            String enc_email_text = rijndeal_obj.encrypt(email);
            String enc_password_text = rijndeal_obj.encrypt(password);
            final String Credentials = enc_email_text + "  ###  " + enc_password_text;
            UserCheckUtil.writeToFile(Credentials, "customer");
            String text_file_data = UserCheckUtil.readFromFile("customer");
            Log.e(TAG, "User Details-- " + text_file_data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JSONObject login_parameters = new JSONObject();
        login_parameters.put("email", email);
        login_parameters.put("password", password);
        Log.e(TAG, "Request--" + login_parameters);

        final JSONObject request = new JSONObject();
        request.put("request", login_parameters);
        Log.e(TAG, "Request--" + request);

        ApiService.getInstance(this).postData(this, LOGIN_URL, login_parameters, "LOGIN", "USER_LOGIN");

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                // On complete call either onLoginSuccess or onLoginFailed
                if (Objects.equals(message, "FAILURE") || Objects.equals(code, "500")) {
                    onLoginFailed();
                } else {
                    onLoginSuccess();
                }
                progressDialog.dismiss();
            }
        }, 3000);
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("USER_LOGIN")) {
            Log.e(TAG, "response: " + response);
            try {
                resp = response.getString("success");
                code = response.getString("status_code");
                message = response.getString("message");
                Log.e(TAG, "resp " + resp + " code--" + code + " message--" + message);

                JSONArray user_details_array = response.getJSONArray("data");
                for (int i = 0; i < user_details_array.length(); i++) {

                    JSONObject user_details = user_details_array.getJSONObject(i);
                    globalUserId = user_details.getString("_id");
                    userId = user_details.getString("id");
                    userName = user_details.getString("name");
                    userAddress = user_details.getString("address");
                    userEmail = user_details.getString("email");
                    userPhone = user_details.getString("mobile_no");

                    JSONArray fav_ids = user_details.getJSONArray("article_ids");
                    final int no_of_fav_ids = fav_ids.length() - 1;
                    for (int j = 0; j <= no_of_fav_ids; j++) {
                        temp.add(fav_ids.getString(j));
                    }

                    user_Favourite_list = temp;
                    Set<String> user_Favourite_list_set = new HashSet<String>(user_Favourite_list);
                    sessionmanager.setuserfavoirites(user_Favourite_list_set);
                    Log.e(TAG, "favourite JSON Ids " + fav_ids);
                    Log.e(TAG, "favourite Array List Ids " + user_Favourite_list_set);
                    Log.e(TAG, "User Name > " + userName + "\n User Address > " + userAddress + "\n User Email > " + userEmail + "\n User Phone > " + userPhone);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(LoginActivity.this, "Internal Error", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    public void onLoginSuccess() {

        Button _loginButton = findViewById(R.id.btn_login);

        //CustomMessage.getInstance().CustomMessage(LoginActivity.this, "Login Success");
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
        setResult(RESULT_OK, null);

        if (userName != null & userPhone != null & userAddress != null & userEmail != null) {

            sessionmanager.signupthings();
            sessionmanager.createUserLoginSession(globalUserId, userId, userType, userName, userEmail, userPhone, userAddress, password);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        } else {
            _loginButton = findViewById(R.id.btn_login);
//            CustomMessage.getInstance().CustomMessage(LoginActivity.this, "There is a issue with your Login, maybe a network/server issue! " +
//                    "\n Please try to login as guest for this time");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

            _loginButton.setEnabled(true);
        }
    }

    public void onLoginFailed() {
        Button _loginButton = findViewById(R.id.btn_login);
        // CustomMessage.getInstance().CustomMessage(LoginActivity.this, "Login failed Please Sign Up or Try Logging Again");
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
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

    public boolean validate() {

        EditText _emailText = findViewById(R.id.input_email);
        EditText _passwordText = findViewById(R.id.input_password);
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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
