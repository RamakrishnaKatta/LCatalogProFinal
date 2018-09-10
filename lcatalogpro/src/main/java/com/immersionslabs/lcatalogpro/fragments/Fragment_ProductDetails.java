package com.immersionslabs.lcatalogpro.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.VendorProfileActivity;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_ProductDetails extends Fragment implements View.OnClickListener, ApiCommunication {

    private static final String TAG = "Fragment_ProductDetails";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/vendors/specific/";
    private static String VENDOR_URL = null;

    //String Values assigned from the Bundle Arguments
    String a_title, a_description, a_new_price, a_discount, a_old_price, a_width, a_height, a_length, a_dimensions, a_vendor_id, a_pattern;

    //String Values assigned from the JSON Object using Vendor ID
    String vendor_name, vendor_address, vendor_image, vendor_id;

    TextView article_title, article_description, article_old_price, article_discount, article_width, article_height, article_length, article_new_price;
    TextView article_vendor_name, article_vendor_location;
    AppCompatImageView article_vendor_logo, article_pattern_image;

    LinearLayout vendor_details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        article_title = view.findViewById(R.id.article_title_text);
        article_description = view.findViewById(R.id.article_description_text);
        article_old_price = view.findViewById(R.id.article_price_value);
        article_discount = view.findViewById(R.id.article_price_discount_value);
        article_width = view.findViewById(R.id.article_width_text);
        article_height = view.findViewById(R.id.article_height_text);
        article_length = view.findViewById(R.id.article_length_text);
        article_new_price = view.findViewById(R.id.article_price_value_new);

        article_vendor_name = view.findViewById(R.id.article_vendor_text);
        article_vendor_location = view.findViewById(R.id.article_vendor_address_text);

        article_vendor_logo = view.findViewById(R.id.article_vendor_logo);
        article_pattern_image = view.findViewById(R.id.article_pattern_image);

        vendor_details = view.findViewById(R.id.vendor_details);
        vendor_details.setOnClickListener(this);

        a_title = getArguments().getString("article_title");
        Log.e(TAG, "--" + a_title);
        article_title.setText(a_title);

        a_description = getArguments().getString("article_description");
        Log.e(TAG, "--" + a_description);
        article_description.setText(a_description);

        a_old_price = getArguments().getString("article_old_price");
        Log.e(TAG, "--" + a_old_price);
        article_old_price.setText(Html.fromHtml("<strike>" + (a_old_price) + "</strike>"));
        article_old_price.setPaintFlags(article_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        a_discount = getArguments().getString("article_discount");
        Log.e(TAG, "--" + a_discount);
        article_discount.setText(a_discount);

        a_new_price = getArguments().getString("article_new_price");
        Log.e(TAG, "--" + a_new_price);
        article_new_price.setText(a_new_price);

        a_dimensions = getArguments().getString("article_dimensions");
        a_width = getArguments().getString("article_width");
        a_height = getArguments().getString("article_height");
        a_length = getArguments().getString("article_length");
        Log.e(TAG, "--" + a_width + "--" + a_height + "--" + a_length);
        article_width.setText(a_width);
        article_height.setText(a_height);
        article_length.setText(a_length);

        a_pattern = getArguments().getString("article_pattern_file");
        Log.e(TAG, "--" + a_pattern);

        Glide.with(getContext())
                .load(EnvConstants.APP_BASE_URL + "/upload/pattern/" + a_pattern)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(article_pattern_image);

        a_vendor_id = getArguments().getString("article_vendor_id");
        Log.e(TAG, "--" + a_vendor_id);

        VENDOR_URL = REGISTER_URL + a_vendor_id;
        Log.e(TAG, "VENDOR_URL--" + VENDOR_URL);

        try {
            getVendorData();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void getVendorData() throws JSONException {

        ApiService.getInstance(getContext()).getData(this, false, "PRODUCT DETAILS FRAGMENT", VENDOR_URL, "VENDOR_DATA");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.vendor_details:

                Bundle vendor_data = new Bundle();
                vendor_data.putString("vendor_id", vendor_id);

                Intent intent = new Intent(getActivity(), VendorProfileActivity.class).putExtras(vendor_data);
                startActivity(intent);
        }
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("VENDOR_DATA")) {
            String response_type = null;
            try {
                response_type = response.getString("success");

                Log.e(TAG, "Vendor Response Type--" + response_type);
                String response_message = response.getString("message");
                Log.e(TAG, "Vendor Response Message--" + response_message);

                JSONArray array = response.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);
                    vendor_id = object.getString("id");
                    vendor_name = object.getString("name");
                    vendor_address = object.getString("location");
                    vendor_image = object.getString("logo");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG, "Article Vendor ID--" + vendor_id);
        Log.e(TAG, "Article Vendor Name--" + vendor_name);
        Log.e(TAG, "Article Vendor Address--" + vendor_address);
        Log.e(TAG, "Article Vendor Image--" + vendor_image);

        article_vendor_name.setText(vendor_name);
        article_vendor_location.setText(vendor_address);

        Glide.with(getContext())
                .load(EnvConstants.APP_BASE_URL + "/upload/vendorLogos/" + vendor_image)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(article_vendor_logo);
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(getContext(), "Internal Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
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