package com.immersionslabs.lcatalogpro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.immersionslabs.lcatalogpro.utils.NetworkConnectivity;
import com.immersionslabs.lcatalogpro.utils.PrefManager;

public class OnBoarding extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private Button btnSkip, btnNext;

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.WelcomeActivityScreenLaunch()) {
            launchHomeScreen();
            finish();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Making notification bar transparent
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.onboarding_slide1,
                R.layout.onboarding_slide2,
                R.layout.onboarding_slide3,
                R.layout.onboarding_slide4};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(v -> launchHomeScreen());

        btnNext.setOnClickListener(v -> {
            // checking for last page
            // if last page home screen will be launched
            int current = getItem();
            if (current < layouts.length) {
                // move to next screen
                viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        });

        if (NetworkConnectivity.checkInternetConnection(OnBoarding.this)) {
        } else {
            InternetMessage();
        }
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private void InternetMessage() {
        final View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(view, "Please Check Your Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.red));
        snackbar.setAction("RETRY", v -> {
            snackbar.dismiss();
            if (NetworkConnectivity.checkInternetConnection(OnBoarding.this)) {

            } else {
                InternetMessage();
            }
        });
        snackbar.show();
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            String dotscolor = getString(R.string.dotscolor);
            dots[i].setText(Html.fromHtml(dotscolor, Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.WHITE);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#004D40"));
    }

    private int getItem() {
        return viewPager.getCurrentItem() + 1;
    }

    private void launchHomeScreen() {
        prefManager.SetWelcomeActivityScreenLaunch(false);
        Intent intent = new Intent(OnBoarding.this, UserTypeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        MyViewPagerAdapter() {
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert layoutInflater != null;
            View view = layoutInflater.inflate(layouts[position], container, false);

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}