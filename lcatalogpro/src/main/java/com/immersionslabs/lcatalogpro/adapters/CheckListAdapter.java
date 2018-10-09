package com.immersionslabs.lcatalogpro.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.immersionslabs.lcatalogpro.ProductPageActivity;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.utils.CustomMessage;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.Manager_CheckList;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;
import com.immersionslabs.lcatalogpro.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {

    private static final String TAG = CheckListAdapter.class.getSimpleName();

    private Activity activity;
    private SessionManager sessionManager;
    private Manager_CheckList manager_checkList;

    private String str_total_checklist_value;
    private Long now_price;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_images;
    private ArrayList<String> item_3ds;
    private ArrayList<String> item_vendors;

    public CheckListAdapter(Activity activity,
                            ArrayList<String> item_ids,
                            ArrayList<String> item_names,
                            ArrayList<String> item_descriptions,
                            ArrayList<String> item_prices,
                            ArrayList<String> item_discounts,
                            ArrayList<String> item_dimensions,
                            ArrayList<String> item_images,
                            ArrayList<String> item_3ds,
                            ArrayList<String> item_vendors) {

        this.item_ids = item_ids;
        this.item_names = item_names;
        this.item_descriptions = item_descriptions;
        this.item_prices = item_prices;
        this.item_discounts = item_discounts;
        this.item_dimensions = item_dimensions;
        this.item_images = item_images;
        this.item_3ds = item_3ds;
        this.item_vendors = item_vendors;

        Log.e(TAG, "ids----" + item_ids);
        Log.e(TAG, "names----" + item_names);
        Log.e(TAG, "descriptions----" + item_descriptions);
        Log.e(TAG, "prices----" + item_prices);
        Log.e(TAG, "discounts----" + item_discounts);
        Log.e(TAG, "Dimensions----" + item_dimensions);
        Log.e(TAG, "images----" + item_images);
        Log.e(TAG, "3ds ---- " + item_3ds);
        Log.e(TAG, "vendors----" + item_vendors);

        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_checklist, parent, false);

        sessionManager = new SessionManager(activity);
        HashMap hashmap = new HashMap();
        manager_checkList = new Manager_CheckList();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListAdapter.ViewHolder viewHolder, final int position) {
        final Context[] context = new Context[1];
        String im1 = null;
        String get_image = item_images.get(position);

        try {
            JSONArray images_json = new JSONArray(get_image);
            for (int i = 0; i < images_json.length(); i++) {
                im1 = images_json.getString(0);
                Log.e(TAG, "onBindViewHolder: image1" + im1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestOptions glideoptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .placeholder(R.drawable.dummy_icon);
        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/images/" + im1)
                .apply(glideoptions)
                .into(viewHolder.item_image);

        Integer x = Integer.parseInt(item_prices.get(position));
        Integer y = Integer.parseInt(item_discounts.get(position));
        Integer z = (x * (100 - y)) / 100;
        final String itemNewPrice = Integer.toString(z);

        viewHolder.item_name.setText(item_names.get(position));
        viewHolder.item_price_new.setText(itemNewPrice);

        viewHolder.checklist_container.setOnClickListener(v -> {

            if (NetworkConnectivity.checkInternetConnection(activity)) {
                try {
                    context[0] = v.getContext();

                    Intent intent = new Intent(context[0], ProductPageActivity.class);
                    Bundle b = new Bundle();
                    b.putString("article_id", item_ids.get(position));
                    b.putString("article_title", item_names.get(position));
                    b.putString("article_description", item_descriptions.get(position));
                    b.putString("article_price", item_prices.get(position));
                    b.putString("article_discount", item_discounts.get(position));
                    b.putString("article_dimensions", item_dimensions.get(position));
                    b.putString("article_images", item_images.get(position));
                    b.putString("article_3ds", item_3ds.get(position));
                    b.putString("article_vendor", item_vendors.get(position));
                    b.putString("article_position", String.valueOf(position));
                    intent.putExtras(b);
                    context[0].startActivity(intent);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            } else {
                CustomMessage.getInstance().CustomMessage(activity, "Internet is not available!");
            }
        });

        viewHolder.item_remove.setOnClickListener(v -> {
            if (EnvConstants.user_type.equals("CUSTOMER")) {
                String vendor_id = item_vendors.get(position);
                Log.e(TAG, "onClick: vendor_id" + vendor_id);
                now_price = Long.parseLong(itemNewPrice);
                sessionManager.CHECKLIST_REMOVE_ARTICLE(item_ids.get(position), vendor_id, now_price);
                str_total_checklist_value = sessionManager.CHECKLIST_GET_CURRENT_VALUE().toString();

                Toast toast = Toast.makeText(activity, "Article Removed Successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else {
                now_price = Long.parseLong(itemNewPrice);
                manager_checkList.CHECKLIST_REMOVE_ARTICLE(item_ids.get(position), now_price);
                str_total_checklist_value = manager_checkList.CHECKLIST_GET_CURRENT().toString();
                Toast toast = Toast.makeText(activity, "Article Removed Successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            try {
                item_ids.remove(position);
                notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, item_ids.size());
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            TextView text_total_value = activity.findViewById(R.id.text_total_value);
            text_total_value.setText(str_total_checklist_value);
        });
    }

    @Override
    public int getItemCount() {
        return item_ids.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_name, item_price_new;
        private AppCompatImageView item_image;
        private RelativeLayout checklist_container;
        private LinearLayout item_remove;

        ViewHolder(View view) {
            super(view);

            checklist_container = view.findViewById(R.id.check_container);
            item_image = view.findViewById(R.id.check_item_image);
            item_name = view.findViewById(R.id.check_item_name);
            item_price_new = view.findViewById(R.id.check_item_price);
            item_remove = view.findViewById(R.id.check_item_delete);
        }
    }
}