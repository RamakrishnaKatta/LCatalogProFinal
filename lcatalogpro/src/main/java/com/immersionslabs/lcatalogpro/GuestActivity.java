package com.immersionslabs.lcatalogpro;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.immersionslabs.lcatalogpro.utils.CustomMessage;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;
import com.immersionslabs.lcatalogpro.utils.PrefManager;
import com.immersionslabs.lcatalogpro.utils.SessionManager;
import com.immersionslabs.lcatalogpro.utils.UserCheckUtil;

import java.io.File;
import java.util.Calendar;
import java.util.Objects;

public class GuestActivity extends AppCompatActivity {

    private static final String TAG = "GuestActivity";
    private static final int REQUEST_GUEST_LOGIN = 0;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    EditText _guestNameText, _GuestPhoneText;
    Button _guestLoginButton;
    EditText _nameText, _mobileText;
    AppCompatImageButton get_details;
    String guest_name, guest_phone;
    File file_guest;
    String[] text_from_guest_file;
    SessionManager sessionmanager;
    private PrefManager prefManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        sessionmanager = new SessionManager(getApplicationContext());

        _guestLoginButton = findViewById(R.id.btn_guest);
        get_details = findViewById(R.id.btn_get_data);

        Toolbar toolbar = findViewById(R.id.toolbar_guest);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String guest_text_file_location = Environment.getExternalStorageDirectory() + "/L_CATALOG/guest.txt";
        file_guest = new File(guest_text_file_location);

        //Disables the keyboard to appear on the activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        get_details.setOnClickListener(v -> {
            setData();
            _guestLoginButton.setEnabled(true);
        });

        _guestLoginButton.setOnClickListener(v -> guest_login());

        prefManager2 = new PrefManager(this);
        Log.e(TAG, "" + prefManager2.GuestActivityScreenLaunch());
        if (prefManager2.GuestActivityScreenLaunch()) {
            ShowcaseView();
        }

        if (NetworkConnectivity.checkInternetConnection(GuestActivity.this)) {
        } else {
            InternetMessage();
        }
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
        snackbar.setAction("RETRY", v -> {
            snackbar.dismiss();
            if (NetworkConnectivity.checkInternetConnection(GuestActivity.this)) {
            } else {
                InternetMessage();
            }
        });
        snackbar.show();
    }

    private void ShowcaseView() {
        prefManager2.setGuestActivityScreenLaunch();
        Log.e(TAG, "" + prefManager2.GuestActivityScreenLaunch());
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
        if (!file_guest.exists()) {
            CustomMessage.getInstance().CustomMessage(GuestActivity.this, "No Saved Details Yet");

        } else {
            text_from_guest_file = UserCheckUtil.readFromFile("guest").split(" ### ");

            _guestNameText = findViewById(R.id.input_name);
            _guestNameText.setText(text_from_guest_file[0].trim());

            _GuestPhoneText = findViewById(R.id.input_mobile);
            _GuestPhoneText.setText(text_from_guest_file[1].trim());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    public void guest_login() {
        Log.e(TAG, "Guest Customer Login");

        if (!validateGuest()) {
            onLoginFailed();
            return;
        }

        _guestLoginButton = findViewById(R.id.btn_guest);
        _guestLoginButton.setEnabled(false);

        _guestNameText = findViewById(R.id.input_name);
        guest_name = _guestNameText.getText().toString().trim();

        _GuestPhoneText = findViewById(R.id.input_mobile);
        guest_phone = _GuestPhoneText.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Providing Access...");
        progressDialog.show();

        // Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void scheduleAlarm() {
        Intent myIntent = new Intent(getBaseContext(), MyScheduledReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putInt("val", 8);
        myIntent.putExtras(bundle);
        pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0,
                myIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 13);
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);
        Log.v("GuestAcivity", "session valid for 15 minutes");
    }

    public void onLoginFailed() {
        Button _guestLoginButton = findViewById(R.id.btn_guest);

        CustomMessage.getInstance().CustomMessage(GuestActivity.this, "Login failed Please Signup or Try Logging Again");
        _guestLoginButton.setEnabled(true);
    }

    public void onLoginSuccess() {
        _guestLoginButton = findViewById(R.id.btn_guest);
        CustomMessage.getInstance().CustomMessage(GuestActivity.this, "Login Success");
//        CustomMessage.getInstance().CustomMessage(this, "You are the Guest User Your session is Valid for 15 minutes,Thank you");
        Toast.makeText(this, "You are the Guest User Your Session is valid for 15 minutes only,Thank you", Toast.LENGTH_LONG).show();

        Bundle user_data = new Bundle();
        user_data.putString("guest_name", guest_name);
        user_data.putString("guest_phone", guest_phone);
        user_data.putString("user_type", "GUEST");
        Log.e(TAG, "User -- " + user_data);

        final String Credentials = guest_name + "  ###  " + guest_phone;
        UserCheckUtil.writeToFile(Credentials, "guest");
        String text_file_date = UserCheckUtil.readFromFile("guest");
        Log.e(TAG, "User Details-- " + text_file_date);

        Log.d(TAG, "onLoginSuccess: ");

        sessionmanager.logoutUser();

        Intent intent = new Intent(this, MainActivity.class).putExtras(user_data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        //if (background.isAppIsInBackground(this)){
        scheduleAlarm();
        // }

        finish();
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

    public boolean validateGuest() {
        boolean validGuest = true;

        _nameText = findViewById(R.id.input_name);
        _mobileText = findViewById(R.id.input_mobile);

        String name = _nameText.getText().toString();
        String mobile = _mobileText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            validGuest = false;
        } else {
            _nameText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            validGuest = false;
        } else {
            _mobileText.setError(null);
        }

        return validGuest;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d(TAG, "onPause: Scheduled Alarm Cancelled ");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}