package com.immersionslabs.lcatalogpro;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.immersionslabs.lcatalogpro.adapters.ProductFragmentAdapter;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductPageActivity extends AppCompatActivity {

    private static final String TAG = "ProductPageActivity";

    String images;

    String oldPrice, discount;
    // calculate the new price using the old price and discount and assign ti newPrice
    String newPrice;

    String dimensions;
    //Split the dimensions into three parts and assign to width, height and length
    String width, length, height;

    String position, name, id, description;

    String article_vendor_id;

    String article_3ds, article_pattern;

    String article_3ds_file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = findViewById(R.id.toolbar_article);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (EnvConstants.flag_article_details) {

            name = EnvConstants.part_article_name_var;
            description = EnvConstants.part_articles_description_var;
            position = Integer.toString(EnvConstants.position);
            id = EnvConstants.part_articles_id_var;
            article_pattern = EnvConstants.part_articles_pattern_var;

            article_3ds = EnvConstants.part_articles_3ds_var;
            oldPrice = EnvConstants.part_articles_price_var;

            discount = EnvConstants.part_article__discounts_var;
            Integer x = Integer.parseInt(oldPrice);
            Integer y = Integer.parseInt(discount);
            Integer z = (x * (100 - y)) / 100;
            newPrice = Integer.toString(z);

            dimensions = EnvConstants.part_article_dimensions_var;

            try {
                JSONObject dimension_json = new JSONObject(dimensions);
                width = dimension_json.getString("width");
                length = dimension_json.getString("depth");
                height = dimension_json.getString("height");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            article_vendor_id = EnvConstants.part_articles_vendor_id_var;

            images = EnvConstants.part_article_images_var;

            EnvConstants.flag_article_details = false;

        } else {
            final Bundle b = getIntent().getExtras();

            assert b != null;
            name = (String) b.getCharSequence("article_title");
            description = (String) b.getCharSequence("article_description");
            position = (String) b.getCharSequence("article_position");
            id = (String) b.getCharSequence("article_id");
            article_pattern = (String) b.getCharSequence("article_pattern");

            article_3ds = (String) b.getCharSequence("article_3ds");
            article_3ds_file = (String) b.getCharSequence("article_3dsfile");

            oldPrice = (String) b.getCharSequence("article_price");

            discount = (String) b.getCharSequence("article_discount");
            Integer x = Integer.parseInt(oldPrice);
            Integer y = Integer.parseInt(discount);
            Integer z = (x * (100 - y)) / 100;
            newPrice = Integer.toString(z);

            dimensions = (String) b.getCharSequence("article_dimensions");

            try {
                JSONObject dimension_json = new JSONObject(dimensions);
                width = dimension_json.getString("width");
                length = dimension_json.getString("depth");
                height = dimension_json.getString("height");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            article_vendor_id = (String) b.getCharSequence("article_vendor");

            images = (String) b.getCharSequence("article_images");

            // All this Data should be sent to fragments in the form of bundle !!
            Log.e(TAG, "Article Name----" + name);
            Log.e(TAG, "Article Description----" + description);
            Log.e(TAG, "Article NewPrice----" + newPrice);
            Log.e(TAG, "Article Dimensions----" + dimensions);
            Log.e(TAG, "Article Width----" + width);
            Log.e(TAG, "Article Height----" + height);
            Log.e(TAG, "Article Length----" + length);
            Log.e(TAG, "Article Position----" + position);
            Log.e(TAG, "Article Images----" + images);
            Log.e(TAG, "Article Vendor Id----" + article_vendor_id);
            Log.e(TAG, "Article 3ds Zip File----" + article_3ds);
            Log.e(TAG, "Article Pattern File----" + article_pattern);
            Log.e(TAG, "Article 3DS File----" + article_3ds_file);

        }

        TabLayout tabLayout = findViewById(R.id.product_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("DESIGN"));
        tabLayout.addTab(tabLayout.newTab().setText("OVERVIEW"));
        tabLayout.addTab(tabLayout.newTab().setText("REVIEWS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.product_pager);
        final ProductFragmentAdapter adapter = new ProductFragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                name, description, oldPrice, discount, newPrice, dimensions, width, height, length, position, id, images,
                article_vendor_id, article_3ds, article_pattern, article_3ds_file);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onBackPressed() {
//        setResult(RESULT_CANCELED);
//        super.onBackPressed();
//        Intent intent = new Intent(this, ProductCatalogActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        intent.putExtra("activity", "ProductPage");
//        startActivity(intent);
//        finish();
//    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        finish();
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