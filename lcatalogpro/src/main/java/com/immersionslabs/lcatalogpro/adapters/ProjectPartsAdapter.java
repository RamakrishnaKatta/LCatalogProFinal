package com.immersionslabs.lcatalogpro.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.immersionslabs.lcatalogpro.ProjectPartsActivity;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.utils.CustomMessage;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ProjectPartsAdapter extends RecyclerView.Adapter<ProjectPartsAdapter.ViewHolder> {

    private static final String TAG = ProjectPartsAdapter.class.getSimpleName();

    private Activity activity;

    private ArrayList<String> project_part;
    private ArrayList<String> project_partName;
    private ArrayList<String> project_partDesc;
    private ArrayList<String> project_part_articlesIds;
    private ArrayList<String> project_part_articlesData;
    private ArrayList<String> project_partimages;
    private ArrayList<String> project_ids;
    private ArrayList<String> project_part_3ds;

    public ProjectPartsAdapter(FragmentActivity activity,
                               ArrayList<String> project_part,
                               ArrayList<String> project_partName,
                               ArrayList<String> project_partDesc,
                               ArrayList<String> project_partimages,
                               ArrayList<String> project_part_articlesIds,
                               ArrayList<String> project_part_articlesData,
                               ArrayList<String> project_ids,
                               ArrayList<String> project_part_3ds) {

        this.project_part = project_part;
        this.project_partName = project_partName;
        this.project_partDesc = project_partDesc;
        this.project_part_articlesIds = project_part_articlesIds;
        this.project_part_articlesData = project_part_articlesData;
        this.project_partimages = project_partimages;
        this.project_ids = project_ids;
        this.project_part_3ds = project_part_3ds;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_project_parts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectPartsAdapter.ViewHolder holder, final int position) {
        final Context[] context = new Context[1];
        String im1 = null;
        String get_image = project_partimages.get(position);
        String get_project_id = project_ids.get(0);
        Log.e(TAG, " project_ids " + get_project_id);
        Log.e(TAG, " project_images " + get_image);

        holder.projectpart_name.setText(project_partName.get(position));
        holder.projectpart_Desc.setText(project_partDesc.get(position));
        try {
            JSONArray images_json = new JSONArray(get_image);
            if (images_json.length() > 0) {
                im1 = images_json.getString(0);
                Log.e(TAG, "project_part_Image  " + im1);


                RequestOptions glideoptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.IMMEDIATE)
                        .placeholder(R.drawable.dummy_icon);
                Glide.with(activity)
                        .load(EnvConstants.APP_BASE_URL + "/upload/projectpartimages/partimages_" + get_project_id + "_" + im1)
                        .apply(glideoptions)
                        .into(holder.projectpart_image);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.projectpart_container.setOnClickListener(v -> {

            if (NetworkConnectivity.checkInternetConnection(activity)) {
                try {
                    context[0] = v.getContext();
                    Intent intent = new Intent(context[0], ProjectPartsActivity.class);
                    Bundle b = new Bundle();
                    b.putString("_id", project_ids.get(0));
                    b.putString("part", project_part.get(position));
                    b.putString("partName", project_partName.get(position));
                    b.putString("partDesc", project_partDesc.get(position));
                    b.putString("partimages", project_partimages.get(position));
                    b.putString("articlesId", project_part_articlesIds.get(position));
                    b.putString("articlesData", project_part_articlesData.get(position));
                    b.putString("partview_3d", project_part_3ds.get(position));

                    intent.putExtras(b);
                    context[0].startActivity(intent);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            } else {
                CustomMessage.getInstance().CustomMessage(activity, "Internet is Not Available");
            }
        });
    }

    @Override
    public int getItemCount() {
        return project_partName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView projectpart_name, projectpart_Desc;
        private AppCompatImageView projectpart_image;
        private RelativeLayout projectpart_container;

        public ViewHolder(View itemView) {
            super(itemView);
            projectpart_container = itemView.findViewById(R.id.project_part_container);
            projectpart_name = itemView.findViewById(R.id.project_part_name);
            projectpart_Desc = itemView.findViewById(R.id.project_part_description);
            projectpart_image = itemView.findViewById(R.id.project_part_image);
        }
    }
}