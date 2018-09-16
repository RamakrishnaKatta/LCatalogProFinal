package com.immersionslabs.lcatalogpro.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.immersionslabs.lcatalogpro.Article3dViewActivity;
import com.immersionslabs.lcatalogpro.AugmentActivity;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.adapters.ProjectImageSliderAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Fragment_ProjectDesign extends Fragment {

    private static final String TAG = Fragment_ProjectDesign.class.getSimpleName();

    AppCompatImageButton project_augment, project_3dview;
    private ViewPager viewpager;
    private LinearLayout slider_dots;
    ProjectImageSliderAdapter imageSliderAdapter;
    ArrayList<String> slider_images = new ArrayList<>();
    TextView[] dots;
    String image1, image2, image3, image4, image5;
    String project_id, project_images, project_3ds;

    public Fragment_ProjectDesign() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_project_design, container, false);

        project_3dview = view.findViewById(R.id.project_3dview_icon);
        project_augment = view.findViewById(R.id.project_augment_icon);

        assert getArguments() != null;
        project_id = getArguments().getString("_id");
        project_images = getArguments().getString("images");
        project_3ds = getArguments().getString("projectView_3d");

        try {
            JSONArray image_json = new JSONArray(project_images);
            for (int i = 0; i < image_json.length(); i++) {
                image1 = image_json.getString(0);
                image2 = image_json.getString(1);
                image3 = image_json.getString(2);
                image4 = image_json.getString(3);
                image5 = image_json.getString(4);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "ProjectImage 1----" + image1);
        Log.e(TAG, "ProjectImage 2----" + image2);
        Log.e(TAG, "ProjectImage 3----" + image3);
        Log.e(TAG, "ProjectImage 4----" + image4);
        Log.e(TAG, "ProjectImage 5----" + image5);

        final String[] Images = {image1, image2, image3, image4, image5};
        Collections.addAll(slider_images, Images);

        viewpager = view.findViewById(R.id.project_view_pager);
        imageSliderAdapter = new ProjectImageSliderAdapter(getContext(), slider_images, project_id);
        viewpager.setAdapter(imageSliderAdapter);

        slider_dots = view.findViewById(R.id.project_slide_dots);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        project_augment.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AppCompatAlertDialogStyle);
            builder.setTitle("You are about to enter Augment Enabled Camera");
            builder.setMessage("This requires 2min of your patience, Do you wish to enter ?");
            builder.setPositiveButton("OK", (dialog, which) -> {
                Intent intent = new Intent(getContext(), AugmentActivity.class);
                startActivity(intent);
            });
            builder.setNegativeButton("CANCEL", null);
            builder.show();
        });

        project_3dview.setOnClickListener(v -> {
            Bundle b4 = new Bundle();
            b4.putString("projectName", project_id);
            b4.putString("project_3ds_file_name", project_3ds);
            Intent _3d_intent = new Intent(getContext(), Article3dViewActivity.class).putExtras(b4);
            startActivity(_3d_intent);
        });

        return view;
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_images.size()];

        slider_dots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.WHITE);
            slider_dots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#004D40"));
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