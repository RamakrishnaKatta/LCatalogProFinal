package com.immersionslabs.lcatalogpro.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ProjectFragmentAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "ProjectFragmentAdapter";

    private String p_name, p_id, p_desc, p_sub_desc, p_images, p_3ds, p_pattern, p_vendor_id;

    private int mNumOfTabs;

    public ProjectFragmentAdapter(FragmentManager projectFragmentManager,
                                  int tabCount,
                                  String project_name,
                                  String project_id,
                                  String project_description,
                                  String project_sub_desc,
                                  String project_images,
                                  String project_3ds,
                                  String project_pattern,
                                  String project_vendor_id) {

        super(projectFragmentManager);

        this.mNumOfTabs = tabCount;
        this.p_name = project_name;
        this.p_id = project_id;
        this.p_desc = project_description;
        this.p_sub_desc = project_sub_desc;
        this.p_images = project_images;
        this.p_3ds = project_3ds;
        this.p_pattern = project_pattern;
        this.p_vendor_id = project_vendor_id;

        Log.e(TAG, "Acquired Article Details: " + p_name
                + " ---" + p_id
                + " ---" + p_desc
                + " ---" + p_sub_desc
                + " ---" + p_images
                + " ---" + p_3ds
                + " ---" + p_pattern
                + " ---" + p_vendor_id);
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

                b_tab1.putString("images", p_images);
                b_tab1.putString("_id", p_id);
                b_tab1.putString("projectView_3d", p_3ds);

                Fragment_ProjectDesign tab1 = new Fragment_ProjectDesign();
                tab1.setArguments(b_tab1);
                return tab1;

            case 1:
                Bundle b_tab2 = new Bundle();

                b_tab2.putString("projectName", p_name);
                b_tab2.putString("projectDescription", p_desc);
                b_tab2.putString("projectSubDescription", p_sub_desc);
                b_tab2.putString("projectvendorid", p_vendor_id);
                b_tab2.putString("projectvendorpattern", p_pattern);
                b_tab2.putString("projectid", p_id);

                Fragment_ProjectDetails tab2 = new Fragment_ProjectDetails();
                tab2.setArguments(b_tab2);
                return tab2;

            case 2:
                Bundle b_tab3 = new Bundle();
                b_tab3.putString("projectid", p_id);

                Fragment_ProjectParts tab3 = new Fragment_ProjectParts();
                tab3.setArguments(b_tab3);
                return tab3;

            default:
                return null;
        }
    }
}
