package com.immersionslabs.lcatalogpro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalogpro.Adapters.MainListViewAdapter;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Fragment_Overview extends Fragment implements ApiCommunication {

    private static final String TAG = "Fragment_Overview";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/vendorArticles";

    RecyclerView main_recycler;

    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_names;
    private ArrayList<String> item_images;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_overview, container, false);

        item_names = new ArrayList<>();
        item_descriptions = new ArrayList<>();
        item_images = new ArrayList<>();

        getData();

        return view;
    }

    private void getData() {
        ApiService.getInstance(getContext()).getData(this, false, "CATALOGUE ACTIVITY", REGISTER_URL, "GETDATA");
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {

        if (flag.equals("GETDATA")) {
            Log.e(TAG, "response--" + response);
            JSONArray resp = null;
            try {
                resp = response.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mainRecyclerView(resp);
        }
    }

    private void mainRecyclerView(JSONArray m_jsonArray) {

        main_recycler = Objects.requireNonNull(getView()).findViewById(R.id.main_recycler);
        main_recycler.setHasFixedSize(true);

        for (int i = 0; i < m_jsonArray.length(); i++) {
            JSONObject obj;
            try {
                obj = m_jsonArray.getJSONObject(i);

                item_descriptions.add(obj.getString("description"));
                item_images.add(obj.getString("img"));
                item_names.add(obj.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "images******" + item_images);
            Log.e(TAG, "names******" + item_names);
            Log.e(TAG, "descriptions*******" + item_descriptions);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
            main_recycler.setLayoutManager(gridLayoutManager);
            MainListViewAdapter gridAdapter = new MainListViewAdapter(this, item_names, item_images, item_descriptions);
            main_recycler.setAdapter(gridAdapter);
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(getContext(), "Internal Error", Toast.LENGTH_SHORT).show();
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
