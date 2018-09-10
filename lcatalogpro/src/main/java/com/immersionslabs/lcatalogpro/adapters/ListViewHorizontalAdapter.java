package com.immersionslabs.lcatalogpro.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalogpro.ProductPageActivity;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.utils.CustomMessage;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ListViewHorizontalAdapter extends RecyclerView.Adapter<ListViewHorizontalAdapter.ViewHolder> {

    private static final String TAG = "HorizontalAdapter";
    private Activity activity;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_vendors;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_3ds;
    private ArrayList<String> item_patterns;

    public ListViewHorizontalAdapter(Activity activity,
                                     ArrayList<String> item_ids,
                                     ArrayList<String> item_names,
                                     ArrayList<String> item_descriptions,
                                     ArrayList<String> item_prices,
                                     ArrayList<String> item_discounts,
                                     ArrayList<String> item_vendors,
                                     ArrayList<String> item_images,
                                     ArrayList<String> item_dimensions,
                                     ArrayList<String> item_3ds,
                                     ArrayList<String> item_patterns) {

        this.item_ids = item_ids;
        this.item_names = item_names;
        this.item_descriptions = item_descriptions;
        this.item_prices = item_prices;
        this.item_discounts = item_discounts;
        this.item_vendors = item_vendors;
        this.item_images = item_images;
        this.item_dimensions = item_dimensions;
        this.item_3ds = item_3ds;
        this.item_patterns = item_patterns;

        Log.e(TAG, "id----" + item_ids);
        Log.e(TAG, "names----" + item_names);
        Log.e(TAG, "descriptions----" + item_descriptions);
        Log.e(TAG, "prices----" + item_prices);
        Log.e(TAG, "discounts----" + item_discounts);
        Log.e(TAG, "vendors----" + item_vendors);
        Log.e(TAG, "images----" + item_images);
        Log.e(TAG, "dimensions----" + item_dimensions);
        Log.e(TAG, "3ds----" + item_3ds);
        Log.e(TAG, "patterns ---- " + item_patterns);

        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_horizontal_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHorizontalAdapter.ViewHolder holder, final int position) {

        final Context[] context = new Context[1];

        String im1 = null;
        String get_image = item_images.get(position);

        try {
            JSONArray images_json = new JSONArray(get_image);
            if (images_json.length() > 0) {
                im1 = images_json.getString(0);
            }
            Log.e(TAG, "Image Thumbnail(1) >>>>>>" + im1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/images/" + im1)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.item_image);

        Integer x = Integer.parseInt(item_prices.get(position));
        Integer y = Integer.parseInt(item_discounts.get(position));
        Integer z = (x * (100 - y)) / 100;
        String itemNewPrice = Integer.toString(z);

        holder.item_name.setText(item_names.get(position));
        holder.item_description.setText(item_descriptions.get(position));
        holder.item_price.setText((Html.fromHtml("<strike>" + item_prices.get(position) + "</strike>")));
        holder.item_price.setPaintFlags(holder.item_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.item_discount.setText(item_discounts.get(position));
        holder.item_price_new.setText(itemNewPrice);

        holder.h_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        b.putString("article_vendor", item_vendors.get(position));
                        b.putString("article_images", item_images.get(position));
                        b.putString("article_dimensions", item_dimensions.get(position));
                        b.putString("article_3ds", item_3ds.get(position));
                        b.putString("article_pattern", item_patterns.get(position));
                        b.putString("article_position", String.valueOf(position));

                        intent.putExtras(b);
                        context[0].startActivity(intent);
                    } catch (IndexOutOfBoundsException e) {
                        Toast.makeText(activity, "Something went wrong Please Try again", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    CustomMessage.getInstance().CustomMessage(activity, "Internet is not available!");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_name, item_description, item_price, item_discount, item_price_new;
        private AppCompatImageView item_image;
        private RelativeLayout h_container;

        ViewHolder(View itemView) {
            super(itemView);

            h_container = itemView.findViewById(R.id.h_container);
            item_image = itemView.findViewById(R.id.h_item_image);
            item_name = itemView.findViewById(R.id.h_item_name);
            item_description = itemView.findViewById(R.id.h_item_description);
            item_price = itemView.findViewById(R.id.h_item_price);
            item_discount = itemView.findViewById(R.id.h_item_discount_value);
            item_price_new = itemView.findViewById(R.id.h_item_price_new);
        }
    }
}