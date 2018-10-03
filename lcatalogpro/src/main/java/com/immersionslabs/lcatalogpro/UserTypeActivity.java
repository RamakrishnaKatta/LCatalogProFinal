package com.immersionslabs.lcatalogpro;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.immersionslabs.lcatalogpro.utils.CustomMessage;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;
import com.immersionslabs.lcatalogpro.utils.PrefManager;
import com.immersionslabs.lcatalogpro.utils.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.immersionslabs.lcatalogpro.utils.UserCheckUtil.isExternalStorageAvailable;
import static com.immersionslabs.lcatalogpro.utils.UserCheckUtil.isExternalStorageReadOnly;

public class UserTypeActivity extends AppCompatActivity {

    private static final String TAG = UserTypeActivity.class.getSimpleName();

    private static final int MY_PERMISSIONS_REQUEST = 10;
    private static final int REQUEST_USERTYPE = 0;
    Toast toast;
    TextView app_name;
    AppCompatImageButton _customer, _newCustomer, _shopper;
    SessionManager sessionmanager;
    private PrefManager prefManager1;
    private boolean success = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        sessionmanager = new SessionManager(getApplicationContext());

        if (sessionmanager.isUserLoggedIn()) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
        RequestPermissions();

        _customer = findViewById(R.id.btn_customer);
        _customer.setOnClickListener(v -> {
            Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
            startActivity(intent);

            if (!isExternalStorageReadOnly() && isExternalStorageAvailable()) {
                Log.e(TAG, "Enter this loop of Folder Creation");
                CreateFolderStructure();
            }
        });

        _newCustomer = findViewById(R.id.btn_new_customer);
        _newCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(UserTypeActivity.this, SignupActivity.class);
            startActivity(intent);

            if (!isExternalStorageReadOnly() && isExternalStorageAvailable()) {
                Log.e(TAG, "Enter this loop of Folder Creation");
                CreateFolderStructure();
            }
        });

        _shopper = findViewById(R.id.btn_shopper);
        _shopper.setOnClickListener(v -> {
            Intent intent = new Intent(UserTypeActivity.this, GuestActivity.class);
            startActivity(intent);

            if (!isExternalStorageReadOnly() && isExternalStorageAvailable()) {
                Log.e(TAG, "Enter this loop of Folder Creation");
                CreateFolderStructure();
            }
        });

        prefManager1 = new PrefManager(this);
        Log.e(TAG, "" + prefManager1.UserTypeActivityScreenLaunch());
        if (prefManager1.UserTypeActivityScreenLaunch()) {
            ShowcaseView();
        }

        //Disables the keyboard to appear on the activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // checkInternetConnection();
        if (NetworkConnectivity.checkInternetConnection(UserTypeActivity.this)) {
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
            if (NetworkConnectivity.checkInternetConnection(UserTypeActivity.this)) {
            } else {
                InternetMessage();
            }
        });
        snackbar.show();
    }

    private void CreateFolderStructure() {
        String root_Path = Environment.getExternalStorageDirectory().toString() + "//L_CATALOG_PRO";
        String screenshots_Path = Environment.getExternalStorageDirectory().toString() + "//L_CATALOG_PRO/Screenshots";

        File Root_Folder, Screenshots_Folder;

        if (Environment.getExternalStorageState().contains(Environment.MEDIA_MOUNTED)) {
            Root_Folder = new File(root_Path);
            Screenshots_Folder = new File(screenshots_Path);
        } else {
            Root_Folder = new File(root_Path);
            Screenshots_Folder = new File(screenshots_Path);
        }

        if (Root_Folder.exists()) {
            CustomMessage.getInstance().CustomMessage(UserTypeActivity.this, "Welcome Back !!");
        } else {

            if (!Root_Folder.exists()) {
                success = Root_Folder.mkdirs();
            }
            if (!Screenshots_Folder.exists()) {
                success = Screenshots_Folder.mkdirs();
            }
            if (success) {
                CustomMessage.getInstance().CustomMessage(UserTypeActivity.this, "Get Set Go !!");
            }
        }
    }

    /*Showcaseview for the Signup, Login, Guest*/
    private void ShowcaseView() {
        prefManager1.setUserTypeActivityScreenLaunch();
        Log.e(TAG, "Show case views are Implemented here" + prefManager1.UserTypeActivityScreenLaunch());

        Typeface text_font = ResourcesCompat.getFont(Objects.requireNonNull(getApplicationContext()), R.font.assistant_semibold);

        assert text_font != null;
        final TapTargetSequence sequence = new TapTargetSequence(this).targets(
                TapTarget.forView(findViewById(R.id.btn_new_customer), "SIGN UP", "Click here if you want to sign up with us...")
                        .cancelable(true)
                        .transparentTarget(true)
                        .outerCircleColor(R.color.primary_dark)
                        .targetRadius(25)
                        .textTypeface(text_font)
                        .textColor(R.color.white)
                        .tintTarget(true)
                        .id(1),
                TapTarget.forView(findViewById(R.id.btn_customer), "LOG IN", "Click here if you visited us before")
                        .cancelable(true)
                        .transparentTarget(true)
                        .outerCircleColor(R.color.primary_dark)
                        .targetRadius(25)
                        .textTypeface(text_font)
                        .textColor(R.color.white)
                        .tintTarget(true)
                        .id(2),
                TapTarget.forView(findViewById(R.id.btn_shopper), "GUEST LOGIN", "Click here if you are a Onetime User")
                        .cancelable(true)
                        .transparentTarget(true)
                        .outerCircleColor(R.color.primary_dark)
                        .targetRadius(25)
                        .textTypeface(text_font)
                        .textColor(R.color.white)
                        .tintTarget(true)
                        .id(3)
        ).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
            }
        });
        sequence.start();
    }

    /*Permissions Required for the app and granted*/

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Alert");
        builder.setMessage("Press OK to leave the App");
        builder.setPositiveButton("OK", (dialog, which) -> {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            UserTypeActivity.super.onDestroy();
        });
        builder.setNegativeButton("CANCEL", null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_USERTYPE) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void RequestPermissions() {

        int PermissionCamera = ContextCompat.checkSelfPermission(UserTypeActivity.this, android.Manifest.permission.CAMERA);
        int PermissionReadStorage = ContextCompat.checkSelfPermission(UserTypeActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int PermissionWriteStorage = ContextCompat.checkSelfPermission(UserTypeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int PermissionContacts = ContextCompat.checkSelfPermission(UserTypeActivity.this, android.Manifest.permission.READ_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (PermissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (PermissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (PermissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (PermissionContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_CONTACTS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.e(TAG, "Permission callback called-------");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with all three permissions
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check all three permissions
                    if (perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "Camera and Storage(Read and Write) permission granted");
                        // Process the normal flow
                        // Else any one or both the permissions are not granted
                    } else {
                        Log.e(TAG, "Some permissions are not granted ask again ");

                        // Permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        // ShouldShowRequestPermissionRationale will return true
                        // Show the dialog or SnackBar saying its necessary and try again otherwise proceed with setup.

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {

                            showDialogOK((dialog, which) -> {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        RequestPermissions();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        // proceed with logic by disabling the related features or quit the app.
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(0);
                                        break;
                                }
                            });
                        }
                        // permission is denied (and never ask again is checked)
                        // shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                            // Proceed with logic by disabling the related features or quit the app.
                        }
                    } // other 'case' lines to check for other permissions this app might request
                }
            }
        }
    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                .setMessage("Storage and Camera Services are Mandatory for this Application")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("CANCEL", okListener)
                .create()
                .show();
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