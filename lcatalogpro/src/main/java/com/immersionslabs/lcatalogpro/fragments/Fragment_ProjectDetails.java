package com.immersionslabs.lcatalogpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.SessionManager;

import java.util.HashMap;
import java.util.Objects;

public class Fragment_ProjectDetails extends Fragment {

    private static final String TAG = Fragment_ProjectDetails.class.getSimpleName();

    TextView project_name, project_description, project_sub_description, project_vendor_name, project_vendor_address;

    String name, desc, subdesc, vendor_id, vendor_address, vendor_image, project_pattern, project_id;

    AppCompatImageView vendor_logo, pattern_image;

    SessionManager sessionManager;

    private String vendor_name;

    public Fragment_ProjectDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_project_details, container, false);
        Bundle bundle = getArguments();

        sessionManager = new SessionManager(Objects.requireNonNull(getContext()));

        assert bundle != null;
        name = bundle.getString("projectName");
        desc = bundle.getString("projectDescription");
        subdesc = bundle.getString("projectSubDescription");
        vendor_id = bundle.getString("projectvendorid");
        project_pattern = bundle.getString("projectvendorpattern");
        project_id = bundle.getString("projectid");

        Log.e(TAG, "project vendor_id" + vendor_id);
        int vendor_id_int = Integer.parseInt(vendor_id) + 1;
        HashMap vendordetails = sessionManager.GetVendorDetails(Integer.toString(vendor_id_int));

        Log.e(TAG, " vendordetails" + vendordetails);
        vendor_address = (String) vendordetails.get(Integer.toString(vendor_id_int) + SessionManager.KEY_VENDOR_ADDRESS);
        vendor_image = (String) vendordetails.get(Integer.toString(vendor_id_int) + SessionManager.KEY_VENDOR_LOGO);
        Log.e(TAG, "vendorimage " + vendor_image);
        vendor_name = (String) vendordetails.get(Integer.toString(vendor_id_int) + SessionManager.KEY_VENDOR_NAME);

        project_name = view.findViewById(R.id.project_title_text);
        project_description = view.findViewById(R.id.project_description_text);
        project_sub_description = view.findViewById(R.id.project_subdescription_text);
        project_vendor_name = view.findViewById(R.id.project_vendor_text);
        project_vendor_address = view.findViewById(R.id.project_vendor_address_text);
        vendor_logo = view.findViewById(R.id.project_vendor_logo);
        pattern_image = view.findViewById(R.id.project_pattern_image);

        project_name.setText(name);
        project_description.setText(desc);
        project_sub_description.setText(subdesc);
        project_vendor_address.setText(vendor_address);
        project_vendor_name.setText(vendor_name);
        Log.e(TAG, " Pattern URL" + project_pattern);


        RequestOptions glideoptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .placeholder(R.drawable.dummy_icon);

        Glide.with(getContext())
                .load(EnvConstants.APP_BASE_URL + "/upload/vendorLogos/" + vendor_image)
                .apply(glideoptions)
                .into(vendor_logo);

        Glide.with(getContext())
                .load(EnvConstants.APP_BASE_URL + "/upload/projectpatternimg/" + project_id + project_pattern)
                .apply(glideoptions)
                .into(pattern_image);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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