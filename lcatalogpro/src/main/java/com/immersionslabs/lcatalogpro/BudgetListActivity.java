package com.immersionslabs.lcatalogpro;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.immersionslabs.lcatalogpro.adapters.BudgetListAdapter;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.Manager_BudgetList;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;
import com.immersionslabs.lcatalogpro.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BudgetListActivity extends AppCompatActivity {

    private static final String TAG = BudgetListActivity.class.getSimpleName();

    private static String BUDGETLIST_URL = EnvConstants.APP_BASE_URL + "/vendorArticles/";

    HashMap<String, Long> get_details;
    KeyListener listener;
    String USER_LOG_TYPE;

    SessionManager sessionManager;
    RecyclerView budgetlist_recycler;
    GridLayoutManager gridLayoutManager;
    Manager_BudgetList manager_budgetList;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_3ds;
    private ArrayList<String> item_vendors;

    Long current_value, total_budget_value, remaining_value;
    Set<String> set_list;

    String str_current_value, str_total_budget_value, str_remaining_value;
    String Guest_Total_budget, Guest_Current_value, Guest_Remaining_budget;

    EditText Total_budget, Current_value, Remaining_value;
    AppCompatButton Alter_Budget, Update_Budget, Clear_Budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_list);

        manager_budgetList = new Manager_BudgetList();
        USER_LOG_TYPE = EnvConstants.user_type;

        sessionManager = new SessionManager(getApplicationContext());

        budgetlist_recycler = findViewById(R.id.budget_recycler);
        budgetlist_recycler.setHasFixedSize(true);
        budgetlist_recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        set_list = new HashSet<>();

        Toolbar toolbar = findViewById(R.id.toolbar_budget_list);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Total_budget = findViewById(R.id.input_your_budget);
        disableEditText(Total_budget);

        Current_value = findViewById(R.id.input_your_current_value);
        disableEditText(Current_value);

        Remaining_value = findViewById(R.id.input_your_remaining_value);
        disableEditText(Remaining_value);

        Alter_Budget = findViewById(R.id.btn_alter_budget);
        Update_Budget = findViewById(R.id.btn_update_budget);
        Clear_Budget = findViewById(R.id.btn_clear_budget);

        item_ids = new ArrayList<>();
        item_descriptions = new ArrayList<>();
        item_names = new ArrayList<>();
        item_images = new ArrayList<>();
        item_vendors = new ArrayList<>();
        item_prices = new ArrayList<>();
        item_discounts = new ArrayList<>();
        item_dimensions = new ArrayList<>();
        item_3ds = new ArrayList<>();

        Alter_Budget.setOnClickListener(v -> {
            if (NetworkConnectivity.checkInternetConnection(BudgetListActivity.this)) {
                Alter_Budget.setVisibility(View.GONE);
                Update_Budget.setVisibility(View.VISIBLE);
                Total_budget.setFocusableInTouchMode(true);
                Total_budget.focusSearch(View.FOCUS_RIGHT);
                Total_budget.requestFocus();
                Total_budget.setSelection(Total_budget.getText().length());
                Total_budget.getShowSoftInputOnFocus();
                Total_budget.setTextColor(ContextCompat.getColor(this, R.color.red));
                enableEditText(Total_budget);
            } else {
                InternetMessage();
            }

        });

        Update_Budget.setOnClickListener(v -> {
            if (NetworkConnectivity.checkInternetConnection(BudgetListActivity.this)) {


                String Total_value_String = Total_budget.getText().toString();
                if (Total_value_String.isEmpty()) {
                    Toast.makeText(BudgetListActivity.this, "Invalid input,no value entered", Toast.LENGTH_LONG).show();
                } else {
                    Long Total_value = Long.parseLong(Total_budget.getText().toString());
                    if (EnvConstants.user_type.equals("CUSTOMER")) {
                        Long Current_value = sessionManager.BUDGET_GET_CURRENT_VALUE();
                        if (Total_value_String.isEmpty()) {
                            Toast.makeText(BudgetListActivity.this, "Invalid input,enter a higher value", Toast.LENGTH_LONG).show();
                        } else if (Total_value < Current_value) {
                            Toast.makeText(BudgetListActivity.this, "Invalid input,enter a higher value", Toast.LENGTH_LONG).show();
                        } else if (!(Total_value_String.isEmpty()) && !(Total_value < Current_value)) {
                            sessionManager.BUDGET_SET_TOTAL_VALUE(Total_value);
                        }
                    } else {
                        Long Current_value = manager_budgetList.BUDGET_GET_CURRENT();
                        if (Total_value_String.isEmpty()) {
                            Toast.makeText(BudgetListActivity.this, "Invalid input,enter a higher value", Toast.LENGTH_LONG).show();
                        } else if (Total_value < Current_value) {
                            Toast.makeText(BudgetListActivity.this, "Invalid input,enter a higher value", Toast.LENGTH_LONG).show();
                        } else if (!(Total_value_String.isEmpty()) && !(Total_value < Current_value)) {
                            manager_budgetList.BUDGET_SET_TOTAL(Total_value);
                        }
                    }
                }
                Update_Budget.setVisibility(View.GONE);
                Alter_Budget.setVisibility(View.VISIBLE);
                disableEditText(Total_budget);
                onResume();
            } else {
                InternetMessage();
            }
        });

        Clear_Budget.setOnClickListener(v -> {
            if (NetworkConnectivity.checkInternetConnection(BudgetListActivity.this)) {

                if (EnvConstants.user_type.equals("CUSTOMER")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BudgetListActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("ARE YOU SURE YOU WANT TO CLEAR THE BUDGET?");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sessionManager.BUDGET_CLEAR_ARTICLES();
                            onResume();
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BudgetListActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("ARE YOU SURE YOU WANT TO CLEAR THE BUDGET?");

                    builder.setPositiveButton("OK", (dialog, which) -> {
                        manager_budgetList.BUDGET_CLEAR_ARRAY_ARTICLES();
                        onResume();
                    });
                    builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());

                    builder.show();
                }
            } else {
                InternetMessage();
            }
        });
    }

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.red));
        snackbar.setAction("RETRY", v -> {
            snackbar.dismiss();
            if (NetworkConnectivity.checkInternetConnection(BudgetListActivity.this)) {
            } else {
                InternetMessage();
            }
        });
        snackbar.show();
    }

    private void CommongetData() {
        Log.e(TAG, "CommonGetData: " + BUDGETLIST_URL);

        if (USER_LOG_TYPE.equals("CUSTOMER")) {
            set_list = sessionManager.ReturnID();
            if (null == set_list || set_list.isEmpty()) {
                budgetlist_recycler.setVisibility(View.GONE);
            } else {
                Iterator iterator = set_list.iterator();
                while (iterator.hasNext()) {
                    String temp_budgetlist_url = BUDGETLIST_URL + iterator.next().toString();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, temp_budgetlist_url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject RESP = null;
                            try {
                                RESP = response.getJSONObject("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            assert RESP != null;
                            GetData(RESP);
                        }
                    }, error -> {
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(jsonObjectRequest);
                }
            }
        } else if (USER_LOG_TYPE.equals("GUEST")) {
            ArrayList<String> strings = manager_budgetList.BUDGET_GET_ARTICLE_IDS();
            if (null == strings || strings.isEmpty()) {
                budgetlist_recycler.setVisibility(View.GONE);
            } else {
                Iterator iterator = strings.iterator();
                while (iterator.hasNext()) {
                    String temp_budgtet_url = BUDGETLIST_URL + iterator.next().toString();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, temp_budgtet_url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject RESP = null;
                            try {
                                RESP = response.getJSONObject("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            GetData(RESP);

                        }
                    }, error -> {
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(jsonObjectRequest);
                }
            }
        }
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
        editText.requestFocus();
        editText.setFocusableInTouchMode(true);
        editText.setEnabled(true);
        editText.setClickable(true);
        editText.setCursorVisible(true);
        editText.setKeyListener(listener);
    }

    private void GetData(JSONObject obj) {

        try {
            item_ids.add(obj.getString("_id"));
            item_names.add(obj.getString("name"));
            item_descriptions.add(obj.getString("description"));
            item_prices.add(obj.getString("price"));
            item_images.add(obj.getString("img"));
            item_discounts.add(obj.getString("discount"));
            item_3ds.add(obj.getString("view_3d"));
            item_dimensions.add(obj.getString("dimensions"));
            item_vendors.add(obj.getString("vendor_id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "Ids" + item_ids);
        Log.e(TAG, "Names" + item_names);
        Log.e(TAG, "Descriptions" + item_descriptions);
        Log.e(TAG, "Prices" + item_prices);
        Log.e(TAG, "Images" + item_images);
        Log.e(TAG, "Dimensions" + item_dimensions);
        Log.e(TAG, "Discounts" + item_discounts);
        Log.e(TAG, "3ds" + item_3ds);
        Log.e(TAG, "Vendors" + item_vendors);

        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        budgetlist_recycler.setLayoutManager(gridLayoutManager);
        BudgetListAdapter adapter = new BudgetListAdapter(this, item_ids, item_names, item_descriptions, item_prices, item_discounts, item_dimensions, item_images, item_3ds, item_vendors);
        budgetlist_recycler.setAdapter(adapter);
    }

    public void onResume() {
        super.onResume();

        item_ids.clear();
        item_names.clear();
        item_descriptions.clear();
        item_prices.clear();
        item_images.clear();
        item_discounts.clear();
        item_3ds.clear();
        item_dimensions.clear();
        item_vendors.clear();

        CommongetData();

        if (EnvConstants.user_type.equals("CUSTOMER")) {
            sessionManager = new SessionManager(getApplicationContext());
            get_details = new HashMap<>();
            get_details = sessionManager.getBudgetDetails();

            current_value = get_details.get(SessionManager.KEY_CURRENT_VALUE);
            total_budget_value = get_details.get(SessionManager.KEY_TOTAL_BUDGET_VALUE);
            remaining_value = total_budget_value - current_value;

            str_total_budget_value = Long.toString(total_budget_value);
            str_current_value = Long.toString(current_value);
            str_remaining_value = Long.toString(remaining_value);
            Total_budget.setText(str_total_budget_value);
            Current_value.setText(str_current_value);
            Remaining_value.setText(str_remaining_value);

            if (sessionManager.BUDGET_RED_MARKER()) {
                Total_budget.setTextColor(ContextCompat.getColor(this, R.color.red));
                Current_value.setTextColor(ContextCompat.getColor(this, R.color.red));
                Remaining_value.setTextColor(ContextCompat.getColor(this, R.color.red));
            } else {
                Total_budget.setTextColor(ContextCompat.getColor(this, R.color.white));
                Current_value.setTextColor(ContextCompat.getColor(this, R.color.white));
                Remaining_value.setTextColor(ContextCompat.getColor(this, R.color.white));
            }

        } else {
            Guest_Total_budget = Long.toString(manager_budgetList.BUDGET_GET_TOTAL());
            Guest_Current_value = Long.toString(manager_budgetList.BUDGET_GET_CURRENT());
            Guest_Remaining_budget = Long.toString(manager_budgetList.BUDGET_GET_REMAINING());

            Total_budget.setText(Guest_Total_budget);
            Current_value.setText(Guest_Current_value);
            Remaining_value.setText(Guest_Remaining_budget);

            if (manager_budgetList.BUDGET_RED_MARKER()) {
                Total_budget.setTextColor(ContextCompat.getColor(this, R.color.red));
                Current_value.setTextColor(ContextCompat.getColor(this, R.color.red));
                Remaining_value.setTextColor(ContextCompat.getColor(this, R.color.red));
            } else {
                Total_budget.setTextColor(ContextCompat.getColor(this, R.color.white));
                Current_value.setTextColor(ContextCompat.getColor(this, R.color.white));
                Remaining_value.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
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
}