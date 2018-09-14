package com.immersionslabs.lcatalogpro.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class Fragment_ProductFeedback extends Fragment implements View.OnClickListener, ApiCommunication {

    private static final String TAG = Fragment_ProductFeedback.class.getSimpleName();

    public static final String RATING_URL = EnvConstants.APP_BASE_URL + "/articleRating";
    public static final String ARTICLE_FEEDBACK_URL = EnvConstants.APP_BASE_URL + "/articleFeedback";
    public static final String GET_RATING_URL = EnvConstants.APP_BASE_URL + "/getUserRating";

    SessionManager sessionManager;

    EditText feedback_article_id, feedback_vendor_id, feedback_message, feedback_article_name;
    TextInputLayout feedback_article_id_area, feedback_vendor_id_area, feedback_message_area, feedback_article_name_area;
    Button feedback_submit, ratings_submit;
    RatingBar ratingBar;
    RelativeLayout feedback_layout, ratings_layout;
    AppCompatImageView feedback_imageview, ratings_imageview;

    String f_article_id, f_vendor_id, f_article_name, user_id;
    String resp, code, message;
    double rating;
    private String global_user_id, f_vendor_id_mongo;
    private boolean _rating;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_feedback, container, false);

        feedback_article_id = view.findViewById(R.id.feedback_article_id);
        feedback_vendor_id = view.findViewById(R.id.feedback_vendor_id);
        feedback_message = view.findViewById(R.id.feedback_article_message);
        feedback_article_name = view.findViewById(R.id.feedback_article_name);
        feedback_article_id_area = view.findViewById(R.id.feedback_article_id_area);
        feedback_vendor_id_area = view.findViewById(R.id.feedback_vendor_id_area);
        feedback_message_area = view.findViewById(R.id.feedback_article_message_area);
        feedback_article_name_area = view.findViewById(R.id.feedback_article_name_area);
        feedback_layout = view.findViewById(R.id.feedback_relative_layout);
        feedback_imageview = view.findViewById(R.id.feedback_imageview);
        feedback_submit = view.findViewById(R.id.feedback_submit);

        ratingBar = view.findViewById(R.id.ratingsbar);
        ratings_layout = view.findViewById(R.id.ratings_relative_layout);
        ratings_submit = view.findViewById(R.id.ratings_submit);
        ratings_imageview = view.findViewById(R.id.ratings_imageview);

        sessionManager = new SessionManager(Objects.requireNonNull(getContext()));

        feedback_article_id_area.setVisibility(View.GONE);
        feedback_vendor_id_area.setVisibility(View.GONE);
//        feedback_message_area.setVisibility(View.GONE);
//        feedback_article_name_area.setVisibility(View.GONE);
//        feedback_submit.setVisibility(View.GONE);
//        ratingBar.setVisibility(View.GONE);
//        ratings_submit.setVisibility(View.GONE);

        feedback_layout.setTag("up");
        ratings_layout.setTag("up");

        feedback_submit.setOnClickListener(this);

        assert getArguments() != null;
        f_article_id = getArguments().getString("article_id");
        Log.e(TAG, "--" + f_article_id);
        feedback_article_id.setText(f_article_id);

        f_vendor_id = getArguments().getString("article_vendor_id");
        Log.e(TAG, "--" + f_vendor_id);

        f_vendor_id_mongo = get_vendorid_apicall();

        Log.e(TAG, "--" + f_vendor_id_mongo);

        f_article_name = getArguments().getString("article_title");
        Log.e(TAG, "--" + f_article_name);
        feedback_article_name.setText(f_article_name);

        HashMap userdetails;
        userdetails = sessionManager.getUserDetails();
        user_id = String.valueOf(userdetails.get(SessionManager.KEY_USER_ID));
        global_user_id = String.valueOf(userdetails.get(SessionManager.KEY_GLOBAL_USER_ID));

        feedback_layout.setOnClickListener(v -> {

            if (feedback_layout.getTag().toString().equals("down")) {
                feedback_article_id_area.setVisibility(View.GONE);
                feedback_vendor_id_area.setVisibility(View.GONE);
                feedback_message_area.setVisibility(View.VISIBLE);
                feedback_article_name_area.setVisibility(View.VISIBLE);
                feedback_submit.setVisibility(View.VISIBLE);
                feedback_imageview.setImageResource(R.mipmap.ic_faq_up);
                feedback_layout.setTag("up");
            } else {
                feedback_imageview.setImageResource(R.mipmap.ic_faq_down);
                feedback_article_id_area.setVisibility(View.GONE);
                feedback_vendor_id_area.setVisibility(View.GONE);
                feedback_message_area.setVisibility(View.GONE);
                feedback_article_name_area.setVisibility(View.GONE);
                feedback_submit.setVisibility(View.GONE);
                feedback_layout.setTag("down");
            }
        });

        ratings_layout.setOnClickListener(v -> {
            if (ratings_layout.getTag().toString().equals("down")) {
                ratingBar.setVisibility(View.VISIBLE);
                ratings_submit.setVisibility(View.VISIBLE);
                ratings_imageview.setImageResource(R.mipmap.ic_faq_up);
                ratings_layout.setTag("up");

            } else {
                ratings_imageview.setImageResource(R.mipmap.ic_faq_down);
                ratingBar.setVisibility(View.GONE);
                ratings_submit.setVisibility(View.GONE);
                ratings_layout.setTag("down");
            }
        });

        ratings_submit.setOnClickListener(v -> {
            try {
                set_rating_apicall();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();
        get_rating_apicall();
    }

    public String get_vendorid_apicall() {

        String UNIQUE_VENDORID_CALL = EnvConstants.APP_BASE_URL + "/vendors/specific/" + f_vendor_id;
        ApiService.getInstance(getContext()).getData(this, false, "get_mongo_vendorid", UNIQUE_VENDORID_CALL, "GET_MONGO_VENDORID");
        return f_vendor_id_mongo;
    }

    public void get_rating_apicall() {

        String UNIQUE_GETRATING_CALL = GET_RATING_URL + "?article_id=" + f_article_id + "&user_id=" + user_id;
        ApiService.getInstance(getContext()).getData(this, false, "get_rating", UNIQUE_GETRATING_CALL, "GET_ARTICLE_RATING");
    }

    private void set_rating_apicall() throws JSONException {

        float rating = ratingBar.getRating();
        JSONObject rating_parameters = new JSONObject();
        rating_parameters.put("article_id", f_article_id);
        rating_parameters.put("user_id", user_id);
        rating_parameters.put("rating", rating);
        ApiService.getInstance(getContext()).postData(this, RATING_URL, rating_parameters, "RATING", "ARTICLE_RATING");
    }

    private void post_feedback_apicall() throws JSONException {

        String message = feedback_message.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(getContext(), "Enter feedback.It should not be empty.", Toast.LENGTH_LONG).show();
        } else {
            JSONObject feedback_parameters = new JSONObject();
            feedback_parameters.put("article_id", f_article_id);
            feedback_parameters.put("user_id", global_user_id);
            feedback_parameters.put("vendor_id", f_vendor_id_mongo);
            feedback_parameters.put("feedbacks", message);
            Log.e(TAG, "post_feedback_apicall: Request" + feedback_parameters);

            ApiService.getInstance(getContext()).postData(this, ARTICLE_FEEDBACK_URL, feedback_parameters, "FEEDBACK_ARTICLE", "ARTICLE_FEEDBACK");
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.feedback_submit) {
            try {
                post_feedback_apicall();
                Toast.makeText(getActivity(), "Your submit form is accepted", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        switch (flag) {
            case "ARTICLE_RATING":
                try {
                    resp = response.getString("success");
                    message = response.getString("message");
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "resp   " + resp + " Status Code--" + code + " Message--" + message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "ARTICLE_FEEDBACK":
                try {
                    resp = response.getString("success");
                    code = response.getString("status_code");
                    message = response.getString("message");
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "resp   " + resp + " Status Code--" + code + " Message--" + message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "GET_MONGO_VENDORID":
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    f_vendor_id_mongo = jsonObject.getString("_id");
                    feedback_vendor_id.setText(f_vendor_id_mongo);
                    Log.e(TAG, "vendor id mongo --" + f_vendor_id_mongo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "GET_ARTICLE_RATING":
                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    rating = jsonObject.getDouble("rate");
                    _rating = true;
                    if (_rating) {
                        float ratings = (float) rating;
                        ratingBar.setRating(ratings);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "Rating Stars--" + rating + " Is rating true" + _rating);
                break;
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(getContext(), "Internal Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}