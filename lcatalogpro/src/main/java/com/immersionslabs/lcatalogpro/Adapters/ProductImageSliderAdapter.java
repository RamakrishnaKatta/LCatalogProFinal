package com.immersionslabs.lcatalogpro.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ProductImageSliderAdapter extends PagerAdapter {

    private static final String TAG = "ProductImageSliderAdapter";

    private ArrayList<String> Images;
    private LayoutInflater inflater;
    private AppCompatImageView images;
    private Context context;

    public ProductImageSliderAdapter(Context context,
                                     ArrayList<String> slider_images) {
        this.context = context;
        this.Images = slider_images;

        inflater = LayoutInflater.from(context);
    }

    @SuppressLint("LongLogTag")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.fragment_product_design, container, false);

        images = v.findViewById(R.id.article_image_view);
        String urls = Images.get(position);
        Log.e(TAG, "Image urls " + urls);

        Glide.with(context)
                .load(EnvConstants.APP_BASE_URL + "/upload/images/" + urls)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(images);

        container.addView(v);
        return v;
    }

    @Override
    public int getCount() {
        return Images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }
}
