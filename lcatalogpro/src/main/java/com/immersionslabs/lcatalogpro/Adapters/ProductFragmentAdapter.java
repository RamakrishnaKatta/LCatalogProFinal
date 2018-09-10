package com.immersionslabs.lcatalogpro.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ProductFragmentAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "ProductFragmentAdapter";

    private String a_name, a_description, a_old_price, a_discount, a_newPrice, a_dimensions,
            a_width, a_height, a_length, a_position, a_id, a_images, a_vendor_id, a_3ds, a_pattern, a_3ds_file;

    private int mNumOfTabs;

    public ProductFragmentAdapter(FragmentManager productFragmentManager,
                                  int tabCount,
                                  String name,
                                  String description,
                                  String oldPrice,
                                  String discount,
                                  String newPrice,
                                  String dimensions,
                                  String width,
                                  String height,
                                  String length,
                                  String position,
                                  String id,
                                  String images,
                                  String article_vendor_id,
                                  String article_3ds,
                                  String article_pattern,
                                  String article_3ds_file) {

        super(productFragmentManager);

        this.mNumOfTabs = tabCount;
        this.a_name = name;
        this.a_description = description;
        this.a_newPrice = newPrice;
        this.a_old_price = oldPrice;
        this.a_discount = discount;
        this.a_dimensions = dimensions;
        this.a_height = height;
        this.a_length = length;
        this.a_width = width;
        this.a_images = images;
        this.a_position = position;
        this.a_id = id;
        this.a_vendor_id = article_vendor_id;
        this.a_3ds = article_3ds;
        this.a_pattern = article_pattern;
        this.a_3ds_file = article_3ds_file;

        Log.e(TAG, "Acquired Article Details: " + a_name
                + " ---" + a_dimensions
                + " ---" + a_description
                + " ---" + a_newPrice
                + " ---" + a_id
                + " ---" + a_position
                + " ---" + a_3ds
                + " ---" + a_pattern
                + " ---" + a_3ds_file);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle b_tab1 = new Bundle();

                b_tab1.putString("article_images", a_images);
                b_tab1.putString("article_id", a_id);
                b_tab1.putString("article_name", a_name);
                b_tab1.putString("article_3ds", a_3ds);
                b_tab1.putString("article_new_price", a_newPrice);
                b_tab1.putString("article_3ds_file", a_3ds_file);
                b_tab1.putString("article_vendor_id", a_vendor_id);

                Fragment_ProductDesign tab1 = new Fragment_ProductDesign();
                tab1.setArguments(b_tab1);
                return tab1;

            case 1:
                Bundle b_tab2 = new Bundle();

                b_tab2.putString("article_title", a_name);
                b_tab2.putString("article_description", a_description);
                b_tab2.putString("article_new_price", a_newPrice);
                b_tab2.putString("article_old_price", a_old_price);
                b_tab2.putString("article_discount", a_discount);
                b_tab2.putString("article_dimensions", a_dimensions);
                b_tab2.putString("article_height", a_height);
                b_tab2.putString("article_width", a_width);
                b_tab2.putString("article_length", a_length);
                b_tab2.putString("article_position", a_position);
                b_tab2.putString("article_vendor_id", a_vendor_id);
                b_tab2.putString("article_pattern_file", a_pattern);

                Fragment_ProductDetails tab2 = new Fragment_ProductDetails();
                tab2.setArguments(b_tab2);
                return tab2;

            case 2:
                Bundle b_tab3 = new Bundle();

                b_tab3.putString("article_title", a_name);
                b_tab3.putString("article_id", a_id);
                b_tab3.putString("article_vendor_id", a_vendor_id);

                Fragment_ProductFeedback tab3 = new Fragment_ProductFeedback();
                tab3.setArguments(b_tab3);
                return tab3;

            default:
                return null;
        }
    }
}
