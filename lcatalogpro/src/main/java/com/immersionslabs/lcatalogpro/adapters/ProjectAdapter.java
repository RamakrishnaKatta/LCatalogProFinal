package com.immersionslabs.lcatalogpro.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalogpro.ProjectCatalogActivity;
import com.immersionslabs.lcatalogpro.ProjectPageActivity;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private static final String TAG = "ProjectAdapter";

    private Activity activity;

    private ArrayList<String> project_ids;
    private ArrayList<String> project_names;
    private ArrayList<String> project_descriptions;
    private ArrayList<String> project_subDescriptions;
    private ArrayList<String> project_images;
    private ArrayList<String> project_3ds;
    private ArrayList<String> project_patterns;
    private ArrayList<String> project_vendor;

    public ProjectAdapter(ProjectCatalogActivity activity,
                          ArrayList<String> project_ids,
                          ArrayList<String> project_name,
                          ArrayList<String> project_description,
                          ArrayList<String> project_subDescription,
                          ArrayList<String> project_images,
                          ArrayList<String> project_3ds,
                          ArrayList<String> project_pattern,
                          ArrayList<String> project_vendor) {

        this.project_ids = project_ids;
        this.project_names = project_name;
        this.project_descriptions = project_description;
        this.project_subDescriptions = project_subDescription;
        this.project_images = project_images;
        this.project_3ds = project_3ds;
        this.project_patterns = project_pattern;
        this.project_vendor = project_vendor;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ViewHolder holder, final int position) {

        final Context[] context = new Context[1];

        String im1 = null;
        String get_image = project_images.get(position);
        String get_project_id = project_ids.get(position);
        Log.e(TAG, " projectid  " + get_project_id);

        try {
            JSONArray images_json = new JSONArray(get_image);
            if (images_json.length() > 0) {
                im1 = images_json.getString(0);
                Log.e(TAG, "ProjectImage " + im1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/projectimages/" + get_project_id + im1)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.tv_project_image);

        holder.tv_project_name.setText(project_names.get(position));
        holder.tv_project_description.setText(project_descriptions.get(position));

        holder.ll_project_container.setOnClickListener(v -> {
            context[0] = v.getContext();
            Intent intent = new Intent(context[0], ProjectPageActivity.class);

            Bundle b = new Bundle();

            b.putString("Project_id", project_ids.get(position));
            b.putString("Project_Name", project_names.get(position));
            b.putString("Project_Description", project_descriptions.get(position));
            b.putString("Project_SubDescription", project_subDescriptions.get(position));
            b.putString("Project_Image", project_images.get(position));
            b.putString("Project_View3d", project_3ds.get(position));
            b.putString("Project_PatternImg", project_patterns.get(position));
            b.putString("Project_Position", String.valueOf(position));
            b.putString("Project_VendorId", project_vendor.get(position));

            intent.putExtras(b);
            context[0].startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return project_names.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_project_name, tv_project_description;
        private AppCompatImageView tv_project_image;

        private LinearLayout ll_project_container;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_project_container = itemView.findViewById(R.id.project_container);
            tv_project_name = itemView.findViewById(R.id.project_title);
            tv_project_description = itemView.findViewById(R.id.project_data);
            tv_project_image = itemView.findViewById(R.id.project_image);
        }
    }
}