package com.immersionslabs.lcatalogpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.adapters.ProjectPartsAdapter;
import com.immersionslabs.lcatalogpro.network.ApiCommunication;
import com.immersionslabs.lcatalogpro.network.ApiService;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_ProjectParts extends Fragment implements ApiCommunication {

    private static final String TAG = Fragment_ProjectParts.class.getSimpleName();

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/getProjectDetails/";

    private static String PROJECT_PART_URL = null;

    RecyclerView project_part_recycler;

    String project_id;

    ProjectPartsAdapter adapter;

    private ArrayList<String> project_ids;
    private ArrayList<String> project_part;
    private ArrayList<String> project_partName;
    private ArrayList<String> project_partDesc;
    private ArrayList<String> project_partimages;
    private ArrayList<String> project_part_articlesIds;
    private ArrayList<String> project_part_articlesData;
    private ArrayList<String> project_part_3ds;

    private GridLayoutManager Project_partManager;

    public Fragment_ProjectParts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_project_parts, container, false);
        Bundle bundle = getArguments();

        project_ids = new ArrayList<>();
        project_part = new ArrayList<>();
        project_partName = new ArrayList<>();
        project_partDesc = new ArrayList<>();
        project_partimages = new ArrayList<>();
        project_part_articlesIds = new ArrayList<>();
        project_part_articlesData = new ArrayList<>();
        project_part_3ds = new ArrayList<>();

        assert bundle != null;
        project_id = bundle.getString("projectid");

        project_part_recycler = view.findViewById(R.id.project_part_list_recycler);
        project_part_recycler.setHasFixedSize(true);
        project_part_recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        PROJECT_PART_URL = REGISTER_URL + project_id;
        Log.e(TAG, "PROJECT_PART_URL------" + PROJECT_PART_URL);

        getProjectData();
        return view;
    }

    private void getProjectData() {
        ApiService.getInstance(getContext()).getData(this, false, "project_part_data", PROJECT_PART_URL, "project_part_det");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("project_part_det")) {
            try {
                JSONObject resp = response.getJSONObject("data");
                project_ids.add(resp.getString("_id"));

                Log.e(TAG, "responseproject: " + response);
                JSONArray parts = resp.getJSONArray("parts");
                Log.e(TAG, "partsjson: " + parts);
                getdata(parts);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getdata(JSONArray parts) {
        for (int i = 0; i < parts.length(); i++) {
            JSONObject object = null;

            try {
                object = parts.getJSONObject(i);
                project_part.add(object.getString("part"));
                project_partName.add(object.getString("partName"));
                project_partDesc.add(object.getString("partDesc"));
                project_partimages.add(object.getString("partimages"));
                project_part_articlesIds.add(object.getString("articlesId"));
                project_part_articlesData.add(object.getString("articlesData"));
                project_part_3ds.add(object.getString("partview_3d"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "project_id" + project_ids);
        Log.e(TAG, "part" + project_part);
        Log.e(TAG, "partName" + project_partName);
        Log.e(TAG, "partDesc" + project_partDesc);
        Log.e(TAG, "partimages" + project_partimages);
        Log.e(TAG, "articlesId" + project_part_articlesIds);
        Log.e(TAG, "articlesData" + project_part_articlesData);
        Log.e(TAG, "part3ds" + project_part_3ds);

        Project_partManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        project_part_recycler.setLayoutManager(Project_partManager);
        adapter = new ProjectPartsAdapter(getActivity(), project_part, project_partName, project_partDesc, project_partimages, project_part_articlesIds, project_part_articlesData, project_ids, project_part_3ds);
        project_part_recycler.setAdapter(adapter);
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
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