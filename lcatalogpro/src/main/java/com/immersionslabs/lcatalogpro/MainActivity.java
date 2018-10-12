package com.immersionslabs.lcatalogpro;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.immersionslabs.lcatalogpro.adapters.MainPageAdapter;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.ConnectionReceiver;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.Manager_BudgetList;
import com.immersionslabs.lcatalogpro.utils.Manager_CheckList;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;
import com.immersionslabs.lcatalogpro.utils.PrefManager;
import com.immersionslabs.lcatalogpro.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.immersionslabs.lcatalogpro.utils.EnvConstants.user_Favourite_list;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ApiCommunication {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String VENDOR_URL = EnvConstants.APP_BASE_URL + "/vendors";
    private static final String VENDOR_SPECIFIC_URL = EnvConstants.APP_BASE_URL + "/vendors/specific/";
    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/vendorArticles";
    boolean doubleBackToExitPressedOnce = false;
    String name, email, phone, address, user_log_type;
    String guest_name, guest_phone;
    TextView user_type, user_email, user_name;
    HashMap hashMap;
    SessionManager sessionmanager;
    ArrayList<String> object_ids;

    int doubleClick = 1;
    NavigationView navigationView;
    private PrefManager prefManager3;
    Manager_BudgetList manager_budgetList;
    Manager_CheckList manager_checkList;

    private ArrayList<String> vendor_ids;
    HashMap<String, String> hash_vendor;
    private String vendor_logo_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hash_vendor = new HashMap<>();
        vendor_ids = new ArrayList<>();
object_ids=new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        sessionmanager = new SessionManager(getApplicationContext());
        manager_budgetList = new Manager_BudgetList();
        manager_checkList = new Manager_CheckList();

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("ILLUSTRATION"));
        tabLayout.addTab(tabLayout.newTab().setText("OVERVIEW"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        final ViewPager viewPager = findViewById(R.id.pager);
        final MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        user_name = header.findViewById(R.id.user_name);
        user_type = header.findViewById(R.id.user_type_text);
        user_email = header.findViewById(R.id.user_email);
        ApiService.getInstance(this).getData(this,false,"MAIN_ACTIVITY",REGISTER_URL,"item_ids");
        if (sessionmanager.isUserLoggedIn()) {

            hashMap = new HashMap();
            hashMap = sessionmanager.getUserDetails();
            name = (String) hashMap.get(SessionManager.KEY_NAME);
            Log.e(TAG, "name:  " + name);

            address = (String) hashMap.get(SessionManager.KEY_ADDRESS);
            Log.e(TAG, "address:  " + address);

            email = (String) hashMap.get(SessionManager.KEY_EMAIL);
            Log.e(TAG, "email:  " + email);

            phone = (String) hashMap.get(SessionManager.KEY_MOBILE_NO);
            Log.e(TAG, "phone:  " + phone);

            user_log_type = (String) hashMap.get(SessionManager.KEY_USER_TYPE);
            Log.e(TAG, "User Log Type:  " + user_log_type);
            EnvConstants.user_type = user_log_type;

            user_name.setText(name);
            user_email.setText(email);
            user_type.setText(R.string.customer);

        } else {

            final Bundle guest_data = getIntent().getExtras();
            Log.d(TAG, "Dummy -- " + guest_data);

            assert guest_data != null;
            guest_name = guest_data.getString("guest_name");
            Log.e(TAG, "guest name:  " + guest_name);

            guest_phone = guest_data.getString("guest_phone");
            Log.e(TAG, "guest phone:  " + guest_phone);
            user_log_type = "GUEST";
            EnvConstants.user_type = user_log_type;
            user_name.setText(" " + guest_name);
            user_email.setText("Mobile # " + guest_phone);
            user_type.setText(R.string.guest);
        }

        prefManager3 = new PrefManager(this);
        Log.e(TAG, "" + prefManager3.MainActivityScreenLaunch());
        if (prefManager3.MainActivityScreenLaunch()) {
            ShowcaseView();
        }

        CustomMenu();
    }

    /*showcaseview for the MainActivity(Notifications and Welcome screen*/
    private void ShowcaseView() {
        prefManager3.SetMainActivityScreenLaunch();
        Log.e(TAG, "" + prefManager3.MainActivityScreenLaunch());
        Typeface text_font = ResourcesCompat.getFont(Objects.requireNonNull(getApplicationContext()), R.font.assistant_semibold);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        assert text_font != null;
        final TapTargetSequence sequence = new TapTargetSequence(this).targets(
                TapTarget.forToolbarMenuItem(toolbar, R.id.action_notifications, "NOTIFICATIONS", "All the notifications can be displayed Here")
                        .cancelable(true)
                        .transparentTarget(true)
                        .outerCircleColor(R.color.primary_dark)
                        .targetRadius(25)
                        .textTypeface(text_font)
                        .textColor(R.color.white)
                        .tintTarget(true)
                        .id(1),
                TapTarget.forToolbarMenuItem(toolbar, R.id.action_replay_info, "WELCOME", "If you miss the welcome screen you can see here ")
                        .cancelable(true)
                        .transparentTarget(true)
                        .outerCircleColor(R.color.primary_dark)
                        .targetRadius(25)
                        .textTypeface(text_font)
                        .textColor(R.color.white)
                        .tintTarget(true)
                        .id(2))
                .listener(new TapTargetSequence.Listener() {
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

    @Override
    public void onBackPressed() {

        if (doubleClick == 1) {
            Toast.makeText(this, "Double Click will take you to EXIT", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }

        doubleClick = doubleClick + 1;
//        if (doubleBackToExitPressedOnce) {
//            DrawerLayout drawer = findViewById(R.id.drawer_layout);
//            if (drawer.isDrawerOpen(GravityCompat.START)) {
//                drawer.closeDrawer(GravityCompat.START);
//            } else {
//                super.onBackPressed();
//            }
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Double Click will take you to EXIT", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                System.exit(0);
//            }
//        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    private void CustomMenu() {
        Menu nav_menu = navigationView.getMenu();

        if (Objects.equals(user_log_type, "CUSTOMER")) {
            nav_menu.findItem(R.id.nav_sign_up).setVisible(false);
        }

        if (Objects.equals(user_log_type, "GUEST")) {
            nav_menu.findItem(R.id.nav_user_account).setVisible(false);
            nav_menu.findItem(R.id.nav_logout).setTitle("EXIT");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and its LayerDrawable (layer-list)

        // MenuItem item = menu.findItem(R.id.action_notifications);
        // NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCount);

        // force the ActionBar to relayout its MenuItems. onCreateOptionsMenu(Menu) will be called again.

        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will automatically handle clicks on the
        // Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_notifications) {
            startActivity(new Intent(MainActivity.this, NotifyActivity.class));
            return true;

        } else if (id == R.id.action_replay_info) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Watch the welcome Slider, If you missed it");
            builder.setMessage("To see the welcome slider again, either you can go to Settings -> apps -> welcome slider -> clear data or Press OK ");
            builder.setPositiveButton("OK", (dialog, which) -> {

                // We normally won't show the welcome slider again in real app but this is for testing
                PrefManager prefManager = new PrefManager(getApplicationContext());

                // make first time launch TRUE
                prefManager.SetWelcomeActivityScreenLaunch(true);

                startActivity(new Intent(MainActivity.this, OnBoarding.class));
                finish();
            });
            builder.setNegativeButton("CANCEL", null);
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem Nav_item) {

        // Handle navigation view item clicks here.
        int id = Nav_item.getItemId();

        if (id == R.id.nav_catalog) {
            if (NetworkConnectivity.checkInternetConnection(MainActivity.this)) {
                Intent intent = new Intent(this, ProductCatalogActivity.class);
                startActivity(intent);
            } else {
                InternetMessage();
            }

        } else if (id == R.id.nav_augment) {
            Intent intent = new Intent(MainActivity.this, MultiAugmentActivity.class);
            EnvConstants.AugmentFlag=false;
            startActivity(intent);
        } else if (id == R.id.nav_project_campaign) {
            if (NetworkConnectivity.checkInternetConnection(MainActivity.this)) {
                Intent intent = new Intent(this, ProjectCatalogActivity.class);
                startActivity(intent);
            } else {
                InternetMessage();
            }

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, GalleryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_user_account) {
            if (Objects.equals(user_log_type, "CUSTOMER")) {

                Toast.makeText(this, "This is Your Profile !!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, UserAccountActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "You are a Guest, You don't possess an Account !! Thanks and try Signing up ", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_ven) {
            if (NetworkConnectivity.checkInternetConnection(MainActivity.this)) {
                Toast.makeText(this, "We will not disappoint you, Lets get in Touch !!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, VendorListActivity.class);
                startActivity(intent);
            } else {
                InternetMessage();
            }

        } else if (id == R.id.nav_ven_reg) {
            Toast.makeText(this, "We will not disappoint you, Lets get in Touch !!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, VendorRegistrationActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_user_favourites) {
            if (Objects.equals(user_log_type, "CUSTOMER")) {

                Intent intent = new Intent(this, FavoriteListActivity.class);
                // intent.putExtra("userlogtype", "CUSTOMER");
                startActivity(intent);
                Toast.makeText(this, "You can see all your favourites here !!", Toast.LENGTH_SHORT).show();
            } else {
                if (!user_Favourite_list.isEmpty()) {
                    Intent intent = new Intent(this, FavoriteListActivity.class);
                    //   intent.putExtra("userlogtype", "GUEST");
                    startActivity(intent);
                    Toast.makeText(this, "You can see your Temporary favourites here !!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (id == R.id.nav_user_budget_bar) {
            Toast.makeText(this, "Here is your Budget Bar, Check out !!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, BudgetListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_user_check_list) {
            Toast.makeText(this, "Here is your CheckList!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CheckListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_user_notify) {
            Toast.makeText(this, "Here are all your notifications, Check out !!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, NotifyActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_sign_up) {
            Toast.makeText(this, "Thanks for your thought on Creating an Account, Appreciated !!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Sign Out");
            builder.setMessage("Your BudgetList and CheckList will be Erased. \n Are you sure want to SignOut ?");
            builder.setPositiveButton("OK", (dialog, which) -> {

                Toast.makeText(MainActivity.this, "Successfully Signed Out", Toast.LENGTH_SHORT).show();
                user_Favourite_list.clear();
                sessionmanager.logoutUser();
                manager_budgetList.BUDGET_CLEAR_ARRAY_ARTICLES();
                manager_checkList.CHECKLIST_CLEAR_ARRAY_ARTICLES();
                Intent intent = new Intent(MainActivity.this, UserTypeActivity.class);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("CANCEL", null);
            builder.show();

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_faq) {
            Intent intent = new Intent(this, FAQActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.red));
        snackbar.setAction("RETRY", v -> {
            snackbar.dismiss();
            if (ConnectionReceiver.isConnected()) {
                if (ConnectionReceiver.isConnected()) {
                    snackbar.dismiss();
                    onStart();
                    Log.e(TAG, "onClick: brrrrrruuuuuuuuuuh");
                }
            } else {
                InternetMessage();
            }
        });
        snackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionmanager.isUserLoggedIn()) {

            hashMap = new HashMap();
            hashMap = sessionmanager.getUserDetails();
            name = (String) hashMap.get(SessionManager.KEY_NAME);
            Log.e(TAG, "name:  " + name);

            address = (String) hashMap.get(SessionManager.KEY_ADDRESS);
            Log.e(TAG, "address:  " + address);

            email = (String) hashMap.get(SessionManager.KEY_EMAIL);
            Log.e(TAG, "email:  " + email);

            phone = (String) hashMap.get(SessionManager.KEY_MOBILE_NO);
            Log.e(TAG, "phone:  " + phone);

            user_log_type = (String) hashMap.get(SessionManager.KEY_USER_TYPE);
            Log.e(TAG, "User Log Type:  " + user_log_type);
            EnvConstants.user_type = user_log_type;

            user_name.setText(name);
            user_email.setText(email);
            user_type.setText(R.string.customer);
        }
        vendorapicall();
    }

    private void vendorapicall() {
        ApiService.getInstance(this).getData(this, false, "VENDOR_LIST", VENDOR_URL, "VENDOR_LIST_ALL");
    }

    private void vendorSpecificApicall() {
        Log.e(TAG, "Im in vspispecific" + vendor_ids);
        for (Object vendor_id : vendor_ids) {
            String unique_vendor_specific_url = VENDOR_SPECIFIC_URL + vendor_id;

            ApiService.getInstance(this).getData(this, false, "VENDOR_SPECIFIC", unique_vendor_specific_url, "UNIQUE");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("VENDOR_LIST_ALL")) {
            Log.e(TAG, "response " + response);
            try {
                JSONArray resp = response.getJSONArray("data");
                GetData(resp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (flag.equals("UNIQUE")) {
            try {
                JSONObject jsonObject = response.getJSONObject("other_details");
                JSONArray jsonArray = response.getJSONArray("data");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                vendor_logo_img = jsonObject1.getString("logo");
                GetFullDetails(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(flag.equals("item_ids"))
        {
            try
            {
                JSONArray jsonArray=response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject;
                    jsonObject = jsonArray.getJSONObject(i);
                    String id=jsonObject.getString("_id");
                    object_ids.add(id);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

EnvConstants.objectids=object_ids;
        Log.e(TAG, "envobjectids " + EnvConstants.objectids);
    }

    private void GetFullDetails(JSONObject jsonObject) {
        try {
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String type = jsonObject.getString("type");
            String email = jsonObject.getString("email");
            String adress = jsonObject.getString("adress");
            String mobile_no = jsonObject.getString("mobile_no");
            String other_details = jsonObject.getString("other_details");
            String vendor_id = jsonObject.getString("vendor_id");

            Log.e(TAG, "vendorSpecific id:  " + id);
            Log.e(TAG, "vendorSpecific name:  " + name);
            Log.e(TAG, "vendorSpecific type:  " + type);
            Log.e(TAG, "vendorSpecific email:  " + email);
            Log.e(TAG, "vendorSpecific mobile_no:  " + mobile_no);
            Log.e(TAG, "vendorSpecific other_details:  " + other_details);
            Log.e(TAG, "vendorSpecific vendor_id:  " + vendor_id);

            hash_vendor.put(id, id);
            hash_vendor.put(id + SessionManager.KEY_VENDOR_NAME, name);
            hash_vendor.put(id + SessionManager.KEY_VENDOR_TYPE, type);
            hash_vendor.put(id + SessionManager.KEY_VENDOR_EMAIL, email);
            hash_vendor.put(id + SessionManager.KEY_VENDOR_MOBILE, mobile_no);
            hash_vendor.put(id + SessionManager.KEY_VENDOR_OTHERDETAILS, other_details);
            hash_vendor.put(id + SessionManager.KEY_VENDOR_ID, vendor_id);
            hash_vendor.put(id + SessionManager.KEY_VENDOR_ADDRESS, adress);
            hash_vendor.put(id + SessionManager.KEY_VENDOR_LOGO, vendor_logo_img
            );
            sessionmanager.SetVendorDetails(id, hash_vendor);
            Log.e(TAG, "GetFullDetails: session" + hash_vendor);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(MainActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
    }

    private void GetData(JSONArray resp) {
        for (int i = 0; i < resp.length(); i++) {
            JSONObject object;
            try {
                object = resp.getJSONObject(i);

                vendor_ids.add(object.getString("id"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, " vendorspecificids" + vendor_ids);
        vendorSpecificApicall();
    }
}