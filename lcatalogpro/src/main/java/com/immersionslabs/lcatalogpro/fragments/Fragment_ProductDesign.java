package com.immersionslabs.lcatalogpro.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.immersionslabs.lcatalogpro.Article3dViewActivity;
import com.immersionslabs.lcatalogpro.AugmentActivity;
import com.immersionslabs.lcatalogpro.OnBoarding;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.adapters.ProductImageSliderAdapter;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;
import com.immersionslabs.lcatalogpro.utils.Manager_BudgetList;
import com.immersionslabs.lcatalogpro.utils.Manager_CheckList;
import com.immersionslabs.lcatalogpro.utils.PrefManager;
import com.immersionslabs.lcatalogpro.utils.SessionManager;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

import static com.immersionslabs.lcatalogpro.utils.EnvConstants.user_Favourite_list;

public class Fragment_ProductDesign extends Fragment implements OnAnimationEndListener, OnLikeListener, ApiCommunication {

    private static final String TAG = Fragment_ProductDesign.class.getSimpleName();

    private static String LIKE_URL = EnvConstants.APP_BASE_URL + "/users/favouriteArticles";

    private PrefManager prefManager;

    AppCompatImageButton article_share, article_3d_view, article_augment, article_budgetlist, article_removelist, article_checklist;
    LinearLayout article_share_area, article_3d_view_area, article_augment_area,
            article_budgetlist_area, article_checklist_area;

    String article_images, article_id;
    // article_images is split in to five parts and assigned to each string
    String image1, image2, image3, image4, image5;

    String article_name, article_3ds, article_price, article_vendor_id;

    String resp, code, message;

    String user_id;
    Manager_BudgetList manager_budgetList;
    Manager_CheckList manager_checkList;

    private ViewPager ArticleViewPager;
    private LinearLayout Slider_dots;
    ProductImageSliderAdapter imagesliderAdapterProduct;
    ArrayList<String> slider_images = new ArrayList<>();
    TextView[] dots;
    TextView Add_Text;
    int page_position = 0;
    int value;

    LikeButton likeButton;
    SessionManager sessionmanager;
    String user_log_type;
    String article_3ds_file_name;

    public Fragment_ProductDesign() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_product_design, container, false);

        likeButton = view.findViewById(R.id.article_fav_icon);
        likeButton.setOnLikeListener(this);
        likeButton.setOnAnimationEndListener(this);
        article_share = view.findViewById(R.id.article_share_icon);
        article_3d_view = view.findViewById(R.id.article_3dview_icon);
        article_augment = view.findViewById(R.id.article_augment_icon);
        article_budgetlist = view.findViewById(R.id.article_budget_icon);
        article_removelist = view.findViewById(R.id.article_remove_icon);
        article_checklist = view.findViewById(R.id.article_checklist_icon);
        Add_Text = view.findViewById(R.id.add_text);

        article_share_area = view.findViewById(R.id.article_share_icon_area);
        article_3d_view_area = view.findViewById(R.id.article_3dview_icon_area);
        article_augment_area = view.findViewById(R.id.article_augment_icon_area);
        article_budgetlist_area = view.findViewById(R.id.article_budget_icon_area);
        article_checklist_area = view.findViewById(R.id.article_checklist_icon_area);
        sessionmanager = new SessionManager(getContext());
        HashMap hashmap = new HashMap();
        manager_budgetList = new Manager_BudgetList();
        manager_checkList = new Manager_CheckList();
        hashmap = sessionmanager.getUserDetails();
        user_id = (String) hashmap.get(SessionManager.KEY_USER_ID);
        user_log_type = (String) hashmap.get(SessionManager.KEY_USER_TYPE);

        Log.e(TAG, "User Log Type:  " + user_log_type);

        assert getArguments() != null;
        article_images = getArguments().getString("article_images");
        article_name = getArguments().getString("article_name");
        article_3ds = getArguments().getString("article_3ds");
        article_3ds_file_name = getArguments().getString("article_3ds_file");
        article_id = getArguments().getString("article_id");
        article_price = getArguments().getString("article_new_price");
        article_vendor_id = getArguments().getString("article_vendor_id");

        Log.e(TAG, "onCreateView: vendor id" + article_vendor_id);

        Log.d(TAG, "onCreateView:3ds" + article_3ds);
        Log.d(TAG, "onCreateView:3dsfile" + article_3ds_file_name);
        Log.d(TAG, "onCreateView:name" + article_name);

        if (!Objects.equals(user_log_type, "CUSTOMER")) {

            Toast.makeText(getContext(), "This Favourite List is temporary, will be removed after this session", Toast.LENGTH_SHORT).show();

            if (user_Favourite_list.contains(article_id)) {
                Log.e(TAG, "Favourite Article List: " + user_Favourite_list + " Article id: " + article_id + "  --Article Exists in the ArrayList");
                likeButton.setLiked(true);
            } else if (!user_Favourite_list.contains(article_id)) {
                Log.e(TAG, "Favourite Article List: " + user_Favourite_list + " Article id: " + article_id + "  --Article Doesn't Exist in the ArrayList");
                likeButton.setLiked(false);
            }
        }

        if (Objects.equals(user_log_type, "CUSTOMER")) {
            Set set = sessionmanager.getuserfavoirites();
            try {
                if (set.contains(article_id)) {
                    Log.e(TAG, "Favourite Article List: " + user_Favourite_list + " Article id: " + article_id + "  --Article Exists in the ArrayList");
                    likeButton.setLiked(true);
                } else if (!set.contains(article_id)) {
                    Log.e(TAG, "Favourite Article List: " + user_Favourite_list + " Article id: " + article_id + "  --Article Doesn't Exist in the ArrayList");
                    likeButton.setLiked(false);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        try {
            JSONArray image_json = new JSONArray(article_images);
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

        Log.e(TAG, "Article Image 1----" + image1);
        Log.e(TAG, "Article Image 2----" + image2);
        Log.e(TAG, "Article Image 3----" + image3);
        Log.e(TAG, "Article Image 4----" + image4);
        Log.e(TAG, "Article Image 5----" + image5);

        final String[] Images = {image1, image2, image3, image4, image5};

        Collections.addAll(slider_images, Images);

        ArticleViewPager = view.findViewById(R.id.article_view_pager);
        imagesliderAdapterProduct = new ProductImageSliderAdapter(getContext(), slider_images);
        ArticleViewPager.setAdapter(imagesliderAdapterProduct);

        Slider_dots = view.findViewById(R.id.article_slider_dots);

        ArticleViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            private void addBottomDots(int currentPage) {

                dots = new TextView[slider_images.size()];

                Slider_dots.removeAllViews();

                for (int i = 0; i < dots.length; i++) {
                    dots[i] = new TextView(view.getContext());
                    dots[i].setText(Html.fromHtml("&#8226;"));
                    dots[i].setTextSize(35);
                    dots[i].setTextColor(Color.WHITE);
                    Slider_dots.addView(dots[i]);
                }

                if (dots.length > 0)
                    dots[currentPage].setTextColor(Color.parseColor("#004D40"));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        article_checklist_area.setOnClickListener(v -> {
            Long price = Long.parseLong(article_price);
            if (EnvConstants.user_type.equals("CUSTOMER")) {
                if (sessionmanager.CHECKLIST_IS_ARTICLE_EXISTS(article_id)) {
                    Toast.makeText(getContext(), "Article added in the CheckList", Toast.LENGTH_LONG).show();
                } else {
                    sessionmanager.CHECKLIST_ADD_ARTICLE(article_id, article_vendor_id, price);
                    Toast.makeText(getContext(), "Article added successfully", Toast.LENGTH_LONG).show();
                }
            } else {
                Long currentvalue;
                if (manager_checkList.CHECKLIST_IS_ARTICLE_EXISTS(article_id)) {
                    Toast.makeText(getContext(), "Article added in the CheckList", Toast.LENGTH_LONG).show();
                } else {
                    currentvalue = manager_checkList.CHECKLIST_GET_CURRENT() + price;
                    manager_checkList.CHECKLIST_ADD_ARTICLE(article_id, article_vendor_id);
                    manager_checkList.CHECKLIST_SET_CURRENT(currentvalue);
                    Toast.makeText(getContext(), "Article added successfully", Toast.LENGTH_LONG).show();
                }
            }
        });

        article_3d_view_area.setOnClickListener(v -> {
            Bundle b3 = new Bundle();
            b3.putString("article_name", article_name);
            b3.putString("article_3ds_file_name", article_3ds);
            Intent _3d_intent = new Intent(getContext(), Article3dViewActivity.class).putExtras(b3);
            startActivity(_3d_intent);
        });

        article_share_area.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey Check this out!!");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://lcatalog.immersionslabs.com/#/articleDetails/" + article_id);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });

        article_augment_area.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Do you have marker with you");
            builder.setMessage("Please Place the marker at the smooth Surfing Place");
            builder.setPositiveButton("YES", (dialog, which) -> {

                Intent intent = new Intent(getContext(), AugmentActivity.class);
                startActivity(intent);
            });
            builder.setNegativeButton("NO", (dialog, which) ->{
                Intent intent = new Intent(getContext(),AugmentActivity.class);
                startActivity(intent);
//                        startActivity(new Intent(getContext(), AugmentActivity.class));
                    });

            builder.show();
        });

        article_budgetlist_area.setOnClickListener(v -> {
            if (EnvConstants.user_type.equals("CUSTOMER")) {
                if (sessionmanager.BUDGET_IS_ARTICLE_EXISTS(article_id)) {
                    Toast.makeText(getContext(), "Article already added in the BudgetList", Toast.LENGTH_LONG).show();

                } else {
                    HashMap<String, Long> getdetails;
                    getdetails = sessionmanager.getBudgetDetails();
                    Long totalbudget = getdetails.get(SessionManager.KEY_TOTAL_BUDGET_VALUE);
                    if (totalbudget == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                        builder.setTitle("Enter Your Budget");

                        final EditText Total_budget_val = new EditText(getContext());

                        Total_budget_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(Total_budget_val);

                        builder.setPositiveButton("OK", (dialog, which) -> {
                            String budget_value;
                            budget_value = Total_budget_val.getText().toString();
                            if (budget_value.isEmpty()) {
                                Toast.makeText(getContext(), "Enter a value first", Toast.LENGTH_LONG).show();
                            } else {
                                sessionmanager.BUDGET_SET_TOTAL_VALUE(Long.parseLong(budget_value));
                                assert getFragmentManager() != null;
                                getFragmentManager()
                                        .beginTransaction()
                                        .detach(Fragment_ProductDesign.this)
                                        .attach(Fragment_ProductDesign.this)
                                        .commit();
                            }
                        });
                        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
                        builder.show();

                    } else {
                        Long price = Long.parseLong(article_price);
                        Long prevprice = sessionmanager.BUDGET_GET_CURRENT_VALUE();
                        Long temptotalbudget = sessionmanager.BUDGET_GET_TOTAL_VALUE();
                        Long tempcurrentprice = price + prevprice;
                        Long Remaining = temptotalbudget - tempcurrentprice;

                        if (Remaining >= 0) {

                            sessionmanager.BUDGET_ADD_ARTICLE(article_id, price);
                            article_budgetlist.setVisibility(View.GONE);
                            article_removelist.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "ADDED TO THE BUDGET LIST", Toast.LENGTH_LONG).show();
                            getFragmentManager()
                                    .beginTransaction()
                                    .detach(Fragment_ProductDesign.this)
                                    .attach(Fragment_ProductDesign.this)
                                    .commit();
                        } else if (Remaining < 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("Enter Your Budget");

                            final EditText Total_budget_val = new EditText(getContext());
                            String hinttext = sessionmanager.BUDGET_GET_TOTAL_VALUE().toString();
                            Total_budget_val.setHint(hinttext);
                            Total_budget_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                            builder.setView(Total_budget_val);

                            builder.setPositiveButton("OK", (dialog, which) -> {
                                String budget_value;
                                budget_value = Total_budget_val.getText().toString();
                                if (budget_value.isEmpty()) {
                                    Toast.makeText(getContext(), "Enter a value first", Toast.LENGTH_LONG).show();
                                } else {
                                    sessionmanager.BUDGET_SET_TOTAL_VALUE(Long.parseLong(budget_value));
                                    getFragmentManager()
                                            .beginTransaction()
                                            .detach(Fragment_ProductDesign.this)
                                            .attach(Fragment_ProductDesign.this)
                                            .commit();
                                }
                            });
                            builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
                            builder.show();
                            Toast.makeText(getContext(), "Budget crossed,try increasing the budget", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            } else {
                if (manager_budgetList.BUDGET_IS_ARTICLE_EXISTS(article_id)) {
                    Toast.makeText(getContext(), "Article already added in the BudgetList", Toast.LENGTH_LONG).show();
                } else {
                    if (manager_budgetList.BUDGET_GET_TOTAL() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                        builder.setTitle("Enter Your Budget");

                        final EditText Total_budget_val = new EditText(getContext());

                        Total_budget_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(Total_budget_val);

                        builder.setPositiveButton("OK", (dialog, which) -> {
                            String budget_value;
                            budget_value = Total_budget_val.getText().toString();

                            if (budget_value.isEmpty()) {
                                Toast.makeText(getContext(), "Enter a value first", Toast.LENGTH_LONG).show();
                            } else {
                                manager_budgetList.BUDGET_SET_TOTAL(Long.parseLong(budget_value));
                                assert getFragmentManager() != null;
                                getFragmentManager()
                                        .beginTransaction()
                                        .detach(Fragment_ProductDesign.this)
                                        .attach(Fragment_ProductDesign.this)
                                        .commit();
                            }
                        });
                        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
                        builder.show();

                    } else {
                        Long price = Long.parseLong(article_price);
                        Long prevprice = manager_budgetList.BUDGET_GET_CURRENT();
                        Long totalbudget = manager_budgetList.BUDGET_GET_TOTAL();
                        Long currentprice = price + prevprice;
                        Long Remaining = totalbudget - currentprice;

                        if (Remaining > 0) {
                            manager_budgetList.BUDGET_SET_CURRENT(currentprice);
                            manager_budgetList.BUDGET_ADD_ARTICLE(article_id);
                            article_budgetlist.setVisibility(View.GONE);
                            article_removelist.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "ADDED TO THE BUDGET LIST", Toast.LENGTH_LONG).show();
                            getFragmentManager()
                                    .beginTransaction()
                                    .detach(Fragment_ProductDesign.this)
                                    .attach(Fragment_ProductDesign.this)
                                    .commit();
                        }

                        if (Remaining <= 0) {
                            Toast.makeText(getContext(), "Budget crossed,try increasing the budget", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("Enter Your Budget");
                            final EditText Total_budget_val = new EditText(getContext());
                            String totalval_text = manager_budgetList.BUDGET_GET_TOTAL().toString();
                            Total_budget_val.setHint(totalval_text);
                            Total_budget_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                            builder.setView(Total_budget_val);

                            builder.setPositiveButton("OK", (dialog, which) -> {
                                String budget_value;
                                budget_value = Total_budget_val.getText().toString();

                                if (budget_value.isEmpty()) {
                                    Toast.makeText(getContext(), "Enter a value first", Toast.LENGTH_LONG).show();
                                } else {
                                    manager_budgetList.BUDGET_SET_TOTAL(Long.parseLong(budget_value));
                                    getFragmentManager()
                                            .beginTransaction()
                                            .detach(Fragment_ProductDesign.this)
                                            .attach(Fragment_ProductDesign.this)
                                            .commit();
                                }
                            });
                            builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
                            builder.show();
                        }
                    }
                }
            }
        });

        article_removelist.setOnClickListener(v -> {
            if (EnvConstants.user_type.equals("CUSTOMER")) {
                Long price = Long.parseLong(article_price);
                sessionmanager.BUDGET_REMOVE_ARTICLE(article_id, price);
                Toast.makeText(getContext(), "Artcle Removed Successfully", Toast.LENGTH_LONG).show();
                article_budgetlist.setVisibility(View.VISIBLE);
                article_removelist.setVisibility(View.GONE);
                assert getFragmentManager() != null;
                getFragmentManager()
                        .beginTransaction()
                        .detach(Fragment_ProductDesign.this)
                        .attach(Fragment_ProductDesign.this)
                        .commit();
            } else {
                Long price = Long.parseLong(article_price);
                Long prevprice = manager_budgetList.BUDGET_GET_CURRENT();
                Long currentprice = prevprice - price;
                manager_budgetList.BUDGET_SET_CURRENT(currentprice);
                manager_budgetList.BUDGET_REMOVE_ARTICLE(article_id);
                Toast.makeText(getContext(), "Artcle Removed Successfully", Toast.LENGTH_LONG).show();
                article_budgetlist.setVisibility(View.VISIBLE);
                article_removelist.setVisibility(View.GONE);
                getFragmentManager()
                        .beginTransaction()
                        .detach(Fragment_ProductDesign.this)
                        .attach(Fragment_ProductDesign.this)
                        .commit();
            }
        });

        final Handler handler = new Handler();
        final Runnable update = () -> {
            if (page_position == slider_images.size()) {
                page_position = 0;
            } else {
                page_position = page_position + 1;
            }
            ArticleViewPager.setCurrentItem(page_position, true);
        };

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(update);
//            }
//        }, 2000, 5000);

        prefManager = new PrefManager(Objects.requireNonNull(getActivity()));
        Log.e(TAG, " " + prefManager.ProductPageActivityScreenLaunch());
        if (prefManager.ProductPageActivityScreenLaunch()) {
            ShowcaseView(view);
        }
        return view;
    }

    private void ShowcaseView(View view) {
        prefManager.setProductPageActivityScreenLaunch();
        Log.e(TAG, " " + prefManager.ProductPageActivityScreenLaunch());
        Typeface text_font = ResourcesCompat.getFont(Objects.requireNonNull(getActivity()), R.font.assistant_semibold);
        assert text_font != null;
        final TapTargetSequence sequence = new TapTargetSequence(getActivity()).targets(
                TapTarget.forView(view.findViewById(R.id.article_augment_icon), "AUGMENT", "Click Here to Augment the Object")
                        .cancelable(true)
                        .transparentTarget(true)
                        .outerCircleColor(R.color.primary_dark)
                        .targetRadius(25)
                        .textTypeface(text_font)
                        .textColor(R.color.white)
                        .tintTarget(true)
                        .id(1),
                TapTarget.forView(view.findViewById(R.id.article_3dview_icon), "3D", "Click Here see the object in 3d View")
                        .cancelable(true)
                        .transparentTarget(true)
                        .outerCircleColor(R.color.primary_dark)
                        .targetRadius(25)
                        .textTypeface(text_font)
                        .textColor(R.color.white)
                        .tintTarget(true)
                        .id(2),
                TapTarget.forView(view.findViewById(R.id.article_checklist_icon), "CHECK LIST", "The article can be placed in the Check list for further enquiries")
                        .cancelable(true)
                        .transparentTarget(true)
                        .outerCircleColor(R.color.primary_dark)
                        .targetRadius(25)
                        .textTypeface(text_font)
                        .textColor(R.color.white)
                        .tintTarget(true)
                        .id(3)
        ).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
            }
        });
        sequence.start();
    }

    @Override
    public void onAnimationEnd(LikeButton likeButton) {
    }

    @Override
    public void liked(LikeButton likeButton) {
        if (Objects.equals(user_log_type, "CUSTOMER")) {
            likeApiCall(1);
            sessionmanager.updateuserfavoirites(article_id);
            Toast.makeText(getContext(), "liked!", Toast.LENGTH_SHORT).show();
        } else if (!Objects.equals(user_log_type, "CUSTOMER")) {
            user_Favourite_list.add(article_id);
            Log.e(TAG, "GUEST FAV LIST" + user_Favourite_list);
            Toast.makeText(getContext(), "liked!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        if (Objects.equals(user_log_type, "CUSTOMER")) {
            likeApiCall(0);
            sessionmanager.removeuserfavouirites(article_id);
            Toast.makeText(getContext(), "Disliked!", Toast.LENGTH_SHORT).show();
        } else if (!Objects.equals(user_log_type, "CUSTOMER")) {
            user_Favourite_list.remove(article_id);
            Log.e(TAG, "GUEST FAV LIST" + user_Favourite_list);
            Toast.makeText(getContext(), "Disliked!", Toast.LENGTH_SHORT).show();
        }
    }

    public void likeApiCall(final int value) {
        JSONObject fav_Request = new JSONObject();
        try {
            fav_Request.put("liked", value);
            fav_Request.put("userid", user_id);
            fav_Request.put("article_id", article_id);

            Log.e(TAG, "likeApiCall: UserId-- " + user_id);
            Log.e(TAG, "likeApiCall: Article Id-- " + article_id);
            Log.e(TAG, "Request-- " + fav_Request);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiService.getInstance(getContext()).postData(this, LIKE_URL, fav_Request, "FAVORITE", "FAVORITE_SELECT");
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("FAVORITE_SELECT")) {
            Log.e(TAG, "response " + response);
            try {
                resp = response.getString("success");
                code = response.getString("status_code");
                message = response.getString("message");

                Log.e(TAG, "Response -- " + response);
                Log.e(TAG, "Response -- " + resp + " \n code-- " + code + " \n message-- " + message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(getContext(), "Internal Error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (EnvConstants.user_type.equals("GUEST")) {
            if (manager_budgetList.BUDGET_IS_ARTICLE_EXISTS(article_id)) {
                article_budgetlist.setVisibility(View.GONE);
                article_removelist.setVisibility(View.VISIBLE);
            } else {
                article_budgetlist.setVisibility(View.VISIBLE);
                article_removelist.setVisibility(View.GONE);
            }

            if (manager_budgetList.BUDGET_RED_MARKER()) {
                Add_Text.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this.getActivity()), R.color.red));
            } else {
                Add_Text.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this.getActivity()), R.color.white));
            }
        }
        if (EnvConstants.user_type.equals("CUSTOMER")) {

            if (sessionmanager.BUDGET_IS_ARTICLE_EXISTS(article_id)) {
                article_budgetlist.setVisibility(View.GONE);
                article_removelist.setVisibility(View.VISIBLE);
            } else {
                article_budgetlist.setVisibility(View.VISIBLE);
                article_removelist.setVisibility(View.GONE);
            }

            if (sessionmanager.BUDGET_RED_MARKER()) {
                Add_Text.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.red));
            } else {
                Add_Text.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.white));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}