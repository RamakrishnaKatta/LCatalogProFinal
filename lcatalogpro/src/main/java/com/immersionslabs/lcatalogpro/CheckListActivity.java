package com.immersionslabs.lcatalogpro;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.ListVerifiedEmailAddressesResult;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendCustomVerificationEmailRequest;
import com.amazonaws.services.simpleemail.model.SendCustomVerificationEmailResult;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.Manager_CheckList;
import com.immersionslabs.lcatalogpro.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class CheckListActivity extends AppCompatActivity {

    private static final String TAG = "CheckListActivity";

    private String CHECKLIST_URL = EnvConstants.APP_BASE_URL + "/vendorArticles/";

    private static String SENDER = "Immersion Software Labs Pvt. Ltd <enquiry@immersionslabs.com>";
    private static String RECIPIENT = null;
    private static String RECIPIENT_CC = null;
    private static String SUBJECT = null;
    private static String ATTACHMENT = null;
    private static String BODY_TEXT = null;
    private static String BODY_HTML = null;
    String USER_LOG_TYPE;
    StringBuffer stringBuffer;

    SessionManager sessionManager;
    RecyclerView recycler_checklist;
    Manager_CheckList manager_checkList;
    LinearLayoutManager linearlayoutmanager;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_3ds;
    private ArrayList<String> item_vendors;

    static Set<String> set_list, set_checklist_vendorids, set_checklist_articlevendorids;
    static HashMap<String, Set> map_vendorids, map_vendorarticleids;
    static HashMap<String, HashMap> map_vendordetails;
    boolean is_email_valid_boolean = false;

    TextView total_value;
    AppCompatButton Place_enquiry;

    private final static int SUCCESS = 0;
    private final static int ERROR = 1;
    private int result;
    private boolean returnval_emaivalidation;
    String _user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        manager_checkList = new Manager_CheckList();
        USER_LOG_TYPE = EnvConstants.user_type;

        sessionManager = new SessionManager(getApplicationContext());

        recycler_checklist = findViewById(R.id.checklist_recycler);
        recycler_checklist.setHasFixedSize(true);
        recycler_checklist.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        set_list = new HashSet<String>();
        set_checklist_vendorids = new HashSet<String>();
        set_checklist_articlevendorids = new HashSet<String>();

        map_vendorarticleids = new HashMap<>();
        map_vendorids = new HashMap<>();
        map_vendordetails = new HashMap<>();

        Toolbar toolbar = findViewById(R.id.toolbar_check_list);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Place_enquiry = findViewById(R.id.place_enquiry);
        total_value = findViewById(R.id.text_total_value);

        if (EnvConstants.user_type.equals("CUSTOMER")) {
            String Total_value_text = sessionManager.CHECKLIST_GET_CURRENT_VALUE().toString();
            Log.e(TAG, "Current Total Value " + Total_value_text);
            total_value.setText(Total_value_text);
        } else {
            String Total_value_text = manager_checkList.CHECKLIST_GET_CURRENT().toString();
            total_value.setText(Total_value_text);
        }

        item_ids = new ArrayList<>();
        item_descriptions = new ArrayList<>();
        item_names = new ArrayList<>();
        item_images = new ArrayList<>();
        item_vendors = new ArrayList<>();
        item_prices = new ArrayList<>();
        item_discounts = new ArrayList<>();
        item_dimensions = new ArrayList<>();
        item_3ds = new ArrayList<>();

        Place_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvConstants.user_type.equals("CUSTOMER")) {
                    if (set_checklist_vendorids != null && !set_checklist_vendorids.isEmpty()) {
                        HashMap userDetails = sessionManager.getUserDetails();
                        _user_email = userDetails.get(SessionManager.KEY_EMAIL).toString();
                        Log.e(TAG, "User Email" + _user_email);
                        try {
                            is_email_valid_boolean = is_email_valid(_user_email);
                            if (is_email_valid_boolean) {
                                Log.e(TAG, "ArticleId Set" + set_checklist_vendorids);
                                Iterator iterator = set_checklist_vendorids.iterator();

                                while (iterator.hasNext()) {
                                    String vendor_id = iterator.next().toString();
                                    String vendor_id_clone = vendor_id;
                                    Integer integer_vendor_id = Integer.parseInt(vendor_id) + 1;
                                    vendor_id = String.valueOf(integer_vendor_id);
                                    Log.e(TAG, "VendorId from sent email" + vendor_id);
                                    HashMap hashMap = map_vendordetails.get(vendor_id);
                                    String vendor_email = hashMap.get(vendor_id + SessionManager.KEY_VENDOR_EMAIL).toString();
                                    Set articleids = map_vendorarticleids.get(vendor_id_clone);
                                    if (articleids != null) {
                                        sendEmail(articleids, vendor_email, _user_email);
                                    }
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CheckListActivity.this, R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("Verification");
                                builder.setMessage("Your email is not a verified email, do you want to proceed with the verification?");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        VerifyEmail(_user_email);

                                    }
                                });
                                builder.setNegativeButton("CANCEL", null);
                                builder.show();
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(CheckListActivity.this, "Please Add Products in your checkList", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(CheckListActivity.this, "Please Add Products in your checkList", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckListActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Verification");
                    builder.setMessage("This feature is Not available for Guest User,Kindly Signup for more features");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(CheckListActivity.this, SignupActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("NO", null);
                    builder.show();
                }
            }
        });

        set_checklist_vendorids = sessionManager.GetCheckVendorId();
        Log.e(TAG, "Set Checklist Vendor Ids" + set_checklist_vendorids);

        if (null == set_checklist_vendorids) {

        } else {
            Iterator iterator1 = set_checklist_vendorids.iterator();
            while (iterator1.hasNext()) {
                String vendorid = iterator1.next().toString();
//                int vendorid_int=Integer.parseInt(vendorid)+1;
//                vendorid=Integer.toString(vendorid_int);
                set_checklist_articlevendorids = sessionManager.GetCheckArticleVendorId(vendorid);
                Log.e(TAG, "Article Vendor Id" + vendorid);
                if (set_checklist_articlevendorids != null) {
                    map_vendorarticleids.put(vendorid, set_checklist_articlevendorids);
                    Log.e(TAG, "Vendor Id from Map" + vendorid);
                }
            }
            Log.e(TAG, "Map Article Vendor" + map_vendorarticleids);

        }
        if (set_checklist_vendorids != null) {
            Iterator iterator = set_checklist_vendorids.iterator();
            while (iterator.hasNext()) {
                String vendorid = iterator.next().toString();
                int vendorid_int = Integer.parseInt(vendorid) + 1;
                vendorid = Integer.toString(vendorid_int);
                map_vendordetails.put(vendorid, sessionManager.GetVendorDetails(vendorid));

            }
            Log.e(TAG, "Vendor Details" + map_vendordetails);
        }
    }

    private void commongetData() {

        Log.e(TAG, "CommonGetData: " + CHECKLIST_URL);

        if (USER_LOG_TYPE.equals("CUSTOMER")) {
            set_list = sessionManager.ReturnCheckListID();
            if (null == set_list || set_list.isEmpty()) {
                recycler_checklist.setVisibility(View.GONE);
            } else {
                Iterator iterator = set_list.iterator();
                while (iterator.hasNext()) {
                    String temp_checklist_url = CHECKLIST_URL + iterator.next().toString();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, temp_checklist_url, null, new Response.Listener<JSONObject>() {
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
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(jsonObjectRequest);
                }
            }

        } else if (USER_LOG_TYPE.equals("GUEST")) {
            ArrayList<String> strings = manager_checkList.CHECKLIST_GET_ARTICLE_IDS();
            if (null == strings || strings.isEmpty()) {
                recycler_checklist.setVisibility(View.GONE);
            } else {
                Iterator iterator = strings.iterator();
                while (iterator.hasNext()) {
                    String temp_budgtet_url = CHECKLIST_URL + iterator.next().toString();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, temp_budgtet_url, null, new Response.Listener<JSONObject>() {
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
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(jsonObjectRequest);
                }
            }
        }
    }

    private boolean is_email_valid(String email_id) {

        final String email = email_id;
        Thread EmailValidationThread = new Thread(new Runnable() {
            public void run() {
                try {
                    CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(CheckListActivity.this
                            , "ap-south-1:61aae02f-4102-4e79-9d88-3566b8301aae", Regions.AP_SOUTH_1);
                    AmazonSimpleEmailServiceClient ses = new AmazonSimpleEmailServiceClient(credentials);
                    ListVerifiedEmailAddressesResult listVerifiedEmailAddressesResult = ses.listVerifiedEmailAddresses();
                    List l_verifiedids = listVerifiedEmailAddressesResult.getVerifiedEmailAddresses();
                    List l_ids = ses.listIdentities().getIdentities();
                    Log.e(TAG, "List Ids: " + l_ids);
                    Log.e(TAG, "List VerifiedIds: " + l_verifiedids);

                    if (l_verifiedids.contains(email)) {
                        //the address is verified so
                        returnval_emaivalidation = true;
                    } else if (l_ids.contains(email)) {
                        Toast.makeText(CheckListActivity.this, "Mail is already sent to your registered mail please go through the link to complete the verification process", Toast.LENGTH_LONG).show();

                    }

                } catch (Exception e) {
                    Log.e(TAG, "Is Email Verified Error: " + e.getMessage());
                }
            }
        });

        // RUNS SEND EMAIL THREAD
        EmailValidationThread.start();
        try {
            // WAITS THREAD TO COMPLETE TO ACT ON RESULT
            EmailValidationThread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return returnval_emaivalidation;
    }

    private void VerifyEmail(final String email) {

        Thread verifyEmailThread = new Thread(new Runnable() {
            public void run() {
                try {
                    CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(CheckListActivity.this
                            , "ap-south-1:61aae02f-4102-4e79-9d88-3566b8301aae", Regions.AP_SOUTH_1);
                    final AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
                    client.setRegion(Region.getRegion(Regions.US_EAST_1));
//                    VerifyEmailIdentityRequest request = new VerifyEmailIdentityRequest().withEmailAddress(email);
                    SendCustomVerificationEmailRequest EmailRequest = new SendCustomVerificationEmailRequest().withEmailAddress(email).withTemplateName("VerificationTemplate");
                    SendCustomVerificationEmailResult result = client.sendCustomVerificationEmail(EmailRequest);
                    Log.e(TAG, "Verify Email result: " + result.getMessageId());

//                 VerifyEmailIdentityResult response = client.verifyEmailIdentity(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Verify Email Exception: " + e.getMessage());
                }
            }
        });

        // RUNS SEND EMAIL THREAD
        verifyEmailThread.start();
        try {
            // WAITS THREAD TO COMPLETE TO ACT ON RESULT
            verifyEmailThread.join();

            if (result == SUCCESS) {
                Toast.makeText(CheckListActivity.this, "Mail is sent to your registered mail please go through the link to complete the verification process", Toast.LENGTH_LONG).show();

            } else if (result == ERROR) {
                Toast.makeText(CheckListActivity.this, "error", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(CheckListActivity.this, "unkown error", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(Set articleids, String vendor_email_text, String useremail) {
        Log.e(TAG, "article ids of vendor" + articleids);
        String article_id, article_name = null, article_desc, article_price;
        String vendor_email = vendor_email_text;
        String user_email = useremail;
        int Index;
        RECIPIENT = vendor_email;
        RECIPIENT_CC = user_email;
        Session session = Session.getDefaultInstance(new Properties());
        final MimeMessage mimeMessage = new MimeMessage(session);

        HashMap userDetails = sessionManager.getUserDetails();
        String username = userDetails.get(SessionManager.KEY_NAME).toString().toUpperCase();
        stringBuffer = new StringBuffer();
        stringBuffer.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table {\n" +
                "    width:100%;\n" +
                "}\n" +
                "table, th, td {\n" +
                "    border: 1px solid black;\n" +
                "    border-collapse: collapse;\n" +
                "}\n" +
                "th, td {\n" +
                "    padding: 15px;\n" +
                "    text-align: left;\n" +
                "}\n" +
                "table#t01 tr:nth-child(even) {\n" +
                "    background-color: #eee;\n" +
                "}\n" +
                "table#t01 tr:nth-child(odd) {\n" +
                "   background-color: #fff;\n" +
                "}\n" +
                "table#t01 th {\n" +
                "    background-color: #004D40;\n" +
                ";\n" +
                "    color: white;\n" +
                "}\n" +
                "\n" +
                ".fontss{\n" +
                "font-size:15px;\n" +
                "color:#004D40;\n" +
                "\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h2 class =\"fontss\">Here is  " + username + "'S   CheckList:</h2>\n" +
                "<br>\n" +
                "\n" +
                "<table id=\"t01\">\n" +
                "  <tr>\n" +
                "    <th class=\"fontss\">Product Name</th>\n" +
                "    <th class=\"fontss\">Product Price</th> \n" +
                "    <th class=\"fontss\">Product Description</th>\n" +
                "    <th class=\"fontss\">Product Link</th>\n" +
                "    \n" +
                "  </tr>");

        Iterator iterator = articleids.iterator();
        String Article_size = Integer.toString(articleids.size());
        int count = 0;

        while (iterator.hasNext()) {
            try {
                count++;
                article_id = iterator.next().toString();
                Index = item_ids.indexOf(article_id);
                Log.e(TAG, "ITEM_IDS" + item_ids);
                Log.e(TAG, "ARTICLE_ID" + article_id);

                article_name = item_names.get(Index);
                article_desc = item_descriptions.get(Index);
                article_price = item_prices.get(Index);

                stringBuffer.append("  <tr>\n" +
                        "    <td>" + article_name + "</td>\n" +
                        "    <td>" + article_price + "</td>\n" +
                        "    <td>" + article_desc + "</td>\n" +
                        "    <td>" + "https://lcatalog.immersionslabs.com/#/articleDetails/" + article_id + "</td>\n" +
                        "  </tr>");

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        stringBuffer.append("</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>");
        BODY_HTML = stringBuffer.toString();
        SUBJECT = username + "'s" + " CheckList";

        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(CheckListActivity.this
                , "ap-south-1:61aae02f-4102-4e79-9d88-3566b8301aae", Regions.AP_SOUTH_1);

        final AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
        client.setRegion(Region.getRegion(Regions.US_EAST_1));

        Content subject = new Content(SUBJECT);
        Body body = new Body(new Content(BODY_HTML));
        try {
            mimeMessage.setSubject(SUBJECT, "UTF-8");
            mimeMessage.setFrom(new InternetAddress(SENDER));
            mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(RECIPIENT));
            mimeMessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(RECIPIENT_CC));

            MimeMultipart msg_body = new MimeMultipart("alternative");
            MimeBodyPart wrap = new MimeBodyPart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(BODY_HTML, "text/html; charset=UTF-8");
            msg_body.addBodyPart(htmlPart);
            mimeMessage.setContent(msg_body);
            wrap.setContent(msg_body);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
//        final Message message = new Message(subject, body);

        // CREATES SEPARATE THREAD TO ATTEMPT TO SEND EMAIL
        Thread sendEmailThread = new Thread(new Runnable() {
            public void run() {
                try {

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    mimeMessage.writeTo(outputStream);
                    RawMessage rawMessage =
                            new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
                    SendRawEmailRequest rawEmailRequest =
                            new SendRawEmailRequest(rawMessage);
                    client.sendRawEmail(rawEmailRequest);
                    result = SUCCESS;

                } catch (Exception e) {
                    result = ERROR;
                    Log.e(TAG, "Email Error: " + e.getMessage());
                }
            }
        });

        // RUNS SEND EMAIL THREAD
        sendEmailThread.start();
        try {
            // WAITS THREAD TO COMPLETE TO ACT ON RESULT
            sendEmailThread.join();

            if (result == SUCCESS) {
                Toast.makeText(CheckListActivity.this, "Email Sent Successfully", Toast.LENGTH_LONG)
                        .show();
            } else if (result == ERROR) {
                Toast.makeText(CheckListActivity.this, "Email Sending Failed,Please Try Again", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(CheckListActivity.this, "UnExpected Error, Please Try again", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

        linearlayoutmanager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_checklist.setLayoutManager(linearlayoutmanager);
        CheckListAdapter adapter = new CheckListAdapter(this, item_ids, item_names, item_descriptions, item_prices, item_discounts, item_dimensions, item_images, item_3ds, item_vendors);
        recycler_checklist.setAdapter(adapter);
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

        commongetData();
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