package com.immersionslabs.lcatalogpro.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.VendorProfileActivity;
import com.immersionslabs.lcatalogpro.utils.CustomMessage;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;

import java.util.ArrayList;

public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.ViewHolder> {

    private static final String TAG = VendorListAdapter.class.getSimpleName();

    private Activity activity;

    private ArrayList<String> vendor_ids;
    private ArrayList<String> vendor_names;
    private ArrayList<String> vendor_logos;

    public VendorListAdapter(Activity activity,
                             ArrayList<String> vendor_ids,
                             ArrayList<String> vendor_names,
                             ArrayList<String> vendor_logos) {

        this.vendor_ids = vendor_ids;
        this.vendor_names = vendor_names;
        this.vendor_logos = vendor_logos;

        Log.e(TAG, "ids----" + vendor_ids);
        Log.e(TAG, "names----" + vendor_names);
        Log.e(TAG, "logos----" + vendor_logos);

        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_vendor, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorListAdapter.ViewHolder viewHolder, final int position) {

        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/vendorLogos/" + vendor_logos.get(position))
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.vendor_logo);

        viewHolder.vendor_name.setText(vendor_names.get(position));

        viewHolder.grid_container.setOnClickListener(v -> {
            if (NetworkConnectivity.checkInternetConnection(activity)) {
                try {
                    Bundle vendor_data = new Bundle();
                    vendor_data.putString("vendor_id", vendor_ids.get(position));

                    Intent intent = new Intent(v.getContext(), VendorProfileActivity.class).putExtras(vendor_data);
                    v.getContext().startActivity(intent);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            } else {
                CustomMessage.getInstance().CustomMessage(activity, "Internet Not Available");
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendor_ids.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView vendor_name;
        private AppCompatImageView vendor_logo;
        private RelativeLayout grid_container;

        ViewHolder(View view) {
            super(view);
            grid_container = view.findViewById(R.id.vendor_list_container);
            vendor_logo = view.findViewById(R.id.vendor_list_logo);
            vendor_name = view.findViewById(R.id.vendor_list_name);
        }
    }
}