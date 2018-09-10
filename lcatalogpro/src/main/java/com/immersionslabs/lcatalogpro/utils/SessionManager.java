package com.immersionslabs.lcatalogpro.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.immersionslabs.lcatalogpro.LoginActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SessionManager {

    private static final String TAG = "SessionManager";



    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    private static final String PREF_NAME = "LCatalog_Preferences";
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_CURRENT_VALUE = "current_budget_value";
    public static final String KEY_TOTAL_BUDGET_VALUE = "total_budget_value";
    public static final String KEY_REMAINING_VALUE = "remaining_budget_value";

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_NO = "mobile_no";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_GLOBAL_USER_ID = "global_user_id";
    public static final String KEY_USER_FAVOURITE_IDS = "customerfavouiriteids";

    public static final String KEY_CURRENT_CHECKLIST_VALUE = "current_checklist_value";
    public static final String KEY_CHECKLIST_GLOBAL_USER_ID = "global_checklist_user_id";
    public static final String KEY_CHECKLIST_VENDOR_ID = "checklist_vendor_id";
    public static final String KEY_CHECKLISTVENDORARTICLE = "CHECKLISTVENDORARTICLE";

    public static final String KEY_VENDOR_NAME = "vendor_name";
    public static final String KEY_VENDOR_EMAIL = "vendor_email";
    public static final String KEY_VENDOR_MOBILE = "vendor_mobilenum";
    public static final String KEY_VENDOR_ID = "vendor_ID";
    public static final String KEY_VENDOR_TYPE = "vendor_type";
    public static final String KEY_VENDOR_OTHERDETAILS = "vendor_otherdetails";
    public static final String KEY_VENDOR_ADDRESS ="vendoradresskey" ;
    public static final String KEY_VENDOR_LOGO = "vendorlogoimg";

    Set<String> set = new HashSet<String>();
    Set<String> checklistset = new HashSet<String>();
    Set<String> favoiriteset = new HashSet<String>();
    Set<String> checkvendorIdSet = new HashSet<>();
    Set<String> checkvendorAticleIdSet = new HashSet<>();

    public SessionManager(Context context) {
        this.context = context;
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String globalId, String id, String type, String name, String email, String mobile, String address, String password) {

        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_GLOBAL_USER_ID, globalId);
        editor.putString(KEY_CHECKLIST_GLOBAL_USER_ID, globalId);
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE_NO, mobile);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_USER_TYPE, type);
        editor.commit();
    }

    public void signupthings() {
        editor.remove(KEY_GLOBAL_USER_ID);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_NAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_MOBILE_NO);
        editor.remove(KEY_ADDRESS);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_USER_TYPE);
        editor.putBoolean(IS_USER_LOGIN, false);
        editor.commit();
    }

    public void updatedetails(String name, String email, String mobile, String address) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE_NO, mobile);
        editor.putString(KEY_ADDRESS, address);
        editor.commit();
    }

    public boolean checkLogin() {
        if (!this.isUserLoggedIn()) {

            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

            return true;
        }
        return false;
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_GLOBAL_USER_ID, pref.getString(KEY_GLOBAL_USER_ID, null));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_MOBILE_NO, pref.getString(KEY_MOBILE_NO, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_USER_TYPE, pref.getString(KEY_USER_TYPE, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        return user;
    }

    public void logoutUser() {
        editor.remove(KEY_GLOBAL_USER_ID);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_NAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_MOBILE_NO);
        editor.remove(KEY_ADDRESS);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_USER_TYPE);
        editor.remove(KEY_USER_FAVOURITE_IDS);
        editor.putBoolean(IS_USER_LOGIN, false);
        editor.commit();
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public void loginthings() {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.commit();
    }

    public void updatepassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public Set<String> ReturnID() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        set = pref.getStringSet(Global_id, null);

        return set;
    }

    public Set<String> ReturnCheckListID() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        Log.e(TAG, "returncheckid:  " + Global_id + KEY_CHECKLIST_GLOBAL_USER_ID);
        checklistset = pref.getStringSet(Global_id + KEY_CHECKLIST_GLOBAL_USER_ID, null);
        Log.e(TAG, "checklistset:  " + checklistset);

        return checklistset;
    }

    public HashMap<String, Long> getBudgetDetails() {
        String ID = pref.getString(KEY_GLOBAL_USER_ID, null);
        String id_current_budget = ID + KEY_CURRENT_VALUE;
        String id_total_budget = ID + KEY_TOTAL_BUDGET_VALUE;
        HashMap<String, Long> user = new HashMap<String, Long>();
        user.put(KEY_TOTAL_BUDGET_VALUE, pref.getLong(id_total_budget, 0));
        user.put(KEY_CURRENT_VALUE, pref.getLong(id_current_budget, 0));

        return user;
    }

    public void BUDGET_ADD_ARTICLE(String article_id, long price) {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Article_Id = article_id;
        Long currentvalue, total, remaining;
        String Unique_Current_Id = Global_id + KEY_CURRENT_VALUE;
        String Unique_Remaining_Id = Global_id + KEY_REMAINING_VALUE;
        String Unique_total_value_Id = Global_id + KEY_TOTAL_BUDGET_VALUE;
        String Unique_Article_Id = Global_id + Article_Id;
        total = pref.getLong(Unique_total_value_Id, 0);
        currentvalue = pref.getLong(Unique_Current_Id, 0);
        currentvalue = currentvalue + price;
        remaining = total - currentvalue;
        set = pref.getStringSet(Global_id, null);

        if (null == set)
            set = new HashSet<String>();
        set.add(Article_Id);
        editor.putLong(Unique_Current_Id, currentvalue);
        editor.putLong(Unique_Remaining_Id, remaining);
        editor.putBoolean(Unique_Article_Id, true);
        editor.putStringSet(Global_id, set);
        editor.commit();
    }

    public void BUDGET_REMOVE_ARTICLE(String article_id, Long price) {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String BudgetList_Article_Id = article_id;
        Long currentvalue, total, remaining;
        String Unique_Current_Id = Global_id + KEY_CURRENT_VALUE;
        String Unique_Remaining_Id = Global_id + KEY_REMAINING_VALUE;
        String Unique_total_value_Id = Global_id + KEY_TOTAL_BUDGET_VALUE;
        String Unique_Article_Id = Global_id + BudgetList_Article_Id;
        total = pref.getLong(Unique_total_value_Id, 0);
        currentvalue = pref.getLong(Unique_Current_Id, 0);
        currentvalue = currentvalue - price;
        remaining = total - currentvalue;
        set = pref.getStringSet(Global_id, null);
        if (null == set)
            set = new HashSet<String>();
        set.remove(BudgetList_Article_Id);
        editor.putLong(Unique_Current_Id, currentvalue);
        editor.putLong(Unique_Remaining_Id, remaining);
        editor.putBoolean(Unique_Article_Id, false);
        editor.putStringSet(Global_id, set);
        editor.commit();
    }

    public void BUDGET_SET_TOTAL_VALUE(Long total_bud_val) {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_total_value_Id = Global_id + KEY_TOTAL_BUDGET_VALUE;
        editor.putLong(Unique_total_value_Id, total_bud_val);
        editor.commit();
    }

    public Long BUDGET_GET_CURRENT_VALUE() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_Current_Id = Global_id + KEY_CURRENT_VALUE;
        Long returnval;
        returnval = pref.getLong(Unique_Current_Id, 0);

        return returnval;
    }

    public Long BUDGET_GET_TOTAL_VALUE() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_total_value_Id = Global_id + KEY_TOTAL_BUDGET_VALUE;
        Long returnval;
        returnval = pref.getLong(Unique_total_value_Id, 0);

        return returnval;
    }

    public Long BUDGET_GET_REMAINING_VALUE() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_Remaining_Id = Global_id + KEY_REMAINING_VALUE;
        Long returnval;
        returnval = pref.getLong(Unique_Remaining_Id, 0);

        return returnval;
    }

    public boolean BUDGET_IS_ARTICLE_EXISTS(String article_id) {
        boolean flag = false;

        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        set = pref.getStringSet(Global_id, null);
        if (null == set) {
            //do nothng
        } else {
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String value = iterator.next();
                if (value.equals(article_id)) {
                    flag = true;
                } else {
                    flag = false;
                }

                if (flag)
                    break;
            }
        }
        return flag;
    }

    public void BUDGET_CLEAR_ARTICLES() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_Current_Id = Global_id + KEY_CURRENT_VALUE;
        String Unique_Remaining_Id = Global_id + KEY_REMAINING_VALUE;
        String Unique_total_value_Id = Global_id + KEY_TOTAL_BUDGET_VALUE;
        editor.remove(Global_id);
        editor.remove(Unique_Current_Id);
        editor.remove(Unique_Remaining_Id);
        editor.remove(Unique_total_value_Id);
        editor.commit();
    }

    public boolean BUDGET_RED_MARKER() {
        boolean returnval;
        Long Remaining_value = BUDGET_GET_TOTAL_VALUE() - BUDGET_GET_CURRENT_VALUE();
        Long Total_value = BUDGET_GET_TOTAL_VALUE();
        Long Threshold_value = Total_value / 4;
        if (Remaining_value <= Threshold_value) {
            returnval = true;
        } else {
            returnval = false;
        }
        return returnval;
    }

    public void CHECKLIST_ADD_ARTICLE(String article_id, String vendor_id, long price) {
        Long currentvalue;
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Checklist_Article_Id = article_id;
        String Unique_Current_Id = Global_id + KEY_CURRENT_CHECKLIST_VALUE;
        String Unique_Article_Id = Global_id + Checklist_Article_Id;
        String Unique_check_vendorId = Global_id + KEY_CHECKLIST_VENDOR_ID;
        String Unique_check_vendor_ArticleId = Global_id + vendor_id + KEY_CHECKLIST_VENDOR_ID;
        currentvalue = pref.getLong(Unique_Current_Id, 0);
        currentvalue = currentvalue + price;

        checklistset = pref.getStringSet(Global_id + KEY_CHECKLIST_GLOBAL_USER_ID, null);
        if (null == checklistset)
            checklistset = new HashSet<String>();
        checklistset.add(Checklist_Article_Id);

        checkvendorIdSet = pref.getStringSet(Unique_check_vendorId, null);
        if (null == checkvendorIdSet)
            checkvendorIdSet = new HashSet<String>();
        checkvendorIdSet.add(vendor_id);

        checkvendorAticleIdSet = pref.getStringSet(Unique_check_vendor_ArticleId, null);
        if (null == checkvendorAticleIdSet)
            checkvendorAticleIdSet = new HashSet<String>();
        checkvendorAticleIdSet.add(Checklist_Article_Id);

        editor.putStringSet(Unique_check_vendorId, checkvendorIdSet);
        editor.putStringSet(Unique_check_vendor_ArticleId, checkvendorAticleIdSet);
        editor.putLong(Unique_Current_Id, currentvalue);
        editor.putBoolean(Unique_Article_Id, true);
        editor.putStringSet(Global_id + KEY_CHECKLIST_GLOBAL_USER_ID, checklistset);
        editor.commit();
    }

    public Set GetCheckVendorId() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_check_vendorId = Global_id + KEY_CHECKLIST_VENDOR_ID;

        return pref.getStringSet(Unique_check_vendorId, null);
    }

    public Set GetCheckArticleVendorId(String vendorId) {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_check_vendor_ArticleId = Global_id + vendorId + KEY_CHECKLIST_VENDOR_ID;
        Set set = pref.getStringSet(Unique_check_vendor_ArticleId, null);
        Log.e(TAG, "GetCheckArticleVendorId: set" + set);

        return set;
    }

    public void CHECKLIST_REMOVE_ARTICLE(String article_id, String vendor_id, Long price) {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Checklist_Article_Id = article_id;
        Long currentvalue;
        String Unique_Current_Id = Global_id + KEY_CURRENT_CHECKLIST_VALUE;
        String Unique_Article_Id = Global_id + Checklist_Article_Id;
        String Unique_check_vendorId = Global_id + KEY_CHECKLIST_VENDOR_ID;
        String Unique_check_vendor_ArticleId = Global_id + vendor_id + KEY_CHECKLIST_VENDOR_ID;
        currentvalue = pref.getLong(Unique_Current_Id, 0);
        currentvalue = currentvalue - price;

        checklistset = pref.getStringSet(Global_id + KEY_CHECKLIST_GLOBAL_USER_ID, null);
        checklistset.remove(Checklist_Article_Id);

        checkvendorIdSet = pref.getStringSet(Unique_check_vendorId, null);
        if (null == checkvendorIdSet)
            checkvendorIdSet = new HashSet<String>();
        checkvendorIdSet.remove(vendor_id);

        checkvendorAticleIdSet = pref.getStringSet(Unique_check_vendor_ArticleId, null);
        if (null == checkvendorAticleIdSet)
            checkvendorAticleIdSet = new HashSet<String>();
        checkvendorAticleIdSet.remove(Checklist_Article_Id);

        editor.putLong(Unique_Current_Id, currentvalue);
        editor.putBoolean(Unique_Article_Id, false);
        editor.putStringSet(Global_id + KEY_CHECKLIST_GLOBAL_USER_ID, checklistset);
        editor.putStringSet(Unique_check_vendorId, checkvendorIdSet);
        editor.putStringSet(Unique_check_vendor_ArticleId, checkvendorAticleIdSet);

        editor.commit();
    }

    public HashMap<String, String> GET_VENDOR_ARTICLE_IDS() {
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);

        HashMap<String, String> map = new HashMap<>();
        Set set = new HashSet();
        set = pref.getStringSet(Global_id + KEY_CHECKLIST_GLOBAL_USER_ID, null);
        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            String article_id = iterator.next().toString();
            map.put(article_id, pref.getString(article_id + KEY_CHECKLIST_VENDOR_ID, null));
        }

        return map;
    }

    public void CHECKLIST_CLEAR_ARTICLES() {
        String Global_id = pref.getString(KEY_CHECKLIST_GLOBAL_USER_ID, null);
        String Global_id_checklist = Global_id + KEY_CHECKLIST_GLOBAL_USER_ID;
        String Unique_Current_Id = Global_id + KEY_CURRENT_CHECKLIST_VALUE;
        String Unique_check_vendorId = Global_id + KEY_CHECKLIST_VENDOR_ID;

        Set set = pref.getStringSet(Unique_check_vendorId, null);
        if(set!=null) {
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                String vendor_id = iterator.next().toString();
                String Unique_check_vendor_ArticleId = Global_id + vendor_id + KEY_CHECKLIST_VENDOR_ID;
                editor.remove(Unique_check_vendor_ArticleId);
            }
        }

        editor.remove(Unique_check_vendorId);
        editor.remove(Global_id_checklist);
        editor.remove(Unique_Current_Id);
        editor.commit();
    }

    public boolean CHECKLIST_IS_ARTICLE_EXISTS(String article_id) {
        boolean flag = false;

        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        checklistset = pref.getStringSet(Global_id + KEY_CHECKLIST_GLOBAL_USER_ID, null);
        if (null == checklistset) {
            //do nothng
        } else {
            Iterator<String> iterator = checklistset.iterator();
            while (iterator.hasNext()) {
                String value = iterator.next();
                if (value.equals(article_id)) {
                    flag = true;
                } else {
                    flag = false;
                }

                if (flag)
                    break;
            }
        }
        return flag;
    }

    public Long CHECKLIST_GET_CURRENT_VALUE() {
        Long returnval;
        String Global_id = pref.getString(KEY_GLOBAL_USER_ID, null);
        String Unique_Current_Id = Global_id + KEY_CURRENT_CHECKLIST_VALUE;
        returnval = pref.getLong(Unique_Current_Id, 0);
        Log.e(TAG, "return value:  " + returnval);

        return returnval;
    }

    public HashMap<String, Long> getChecklistDetails() {
        String ID = pref.getString(KEY_GLOBAL_USER_ID, null);
        String id_current_budget = ID + KEY_CURRENT_CHECKLIST_VALUE;
        HashMap<String, Long> user = new HashMap<String, Long>();
        user.put(KEY_CURRENT_CHECKLIST_VALUE, pref.getLong(id_current_budget, 0));

        return user;
    }

    public void setuserfavoirites(Set userfavouirites) {
        editor.remove(KEY_USER_FAVOURITE_IDS);
        editor.putStringSet(KEY_USER_FAVOURITE_IDS, userfavouirites);

        editor.commit();
    }

    public Set getuserfavoirites() {
        return pref.getStringSet(KEY_USER_FAVOURITE_IDS, null);
    }

    public void updateuserfavoirites(String article_id) {
        Set set = pref.getStringSet(KEY_USER_FAVOURITE_IDS, null);
        assert set != null;
        set.add(article_id);
        editor.remove(KEY_USER_FAVOURITE_IDS);
        editor.putStringSet(KEY_USER_FAVOURITE_IDS, set);

        editor.commit();
    }

    public void removeuserfavouirites(String article_id) {
        Set set = pref.getStringSet(KEY_USER_FAVOURITE_IDS, null);
        assert set != null;
        set.remove(article_id);
        editor.remove(KEY_USER_FAVOURITE_IDS);
        editor.putStringSet(KEY_USER_FAVOURITE_IDS, set);

        editor.commit();
    }

    public void SetVendorDetails(String id, HashMap map) {

        Log.e(TAG, "SetVendorDetails: id" + id);

        String name = map.get(id + KEY_VENDOR_NAME).toString();
        String email = map.get(id + KEY_VENDOR_EMAIL).toString();
        String mobile = map.get(id + KEY_VENDOR_MOBILE).toString();
        String type = map.get(id + KEY_VENDOR_TYPE).toString();
        String otherDetails = map.get(id + KEY_VENDOR_OTHERDETAILS).toString();
        String vendorId = map.get(id + KEY_VENDOR_ID).toString();
        String address=map.get(id+KEY_VENDOR_ADDRESS).toString();
        String vendorLogo=map.get(id+KEY_VENDOR_LOGO).toString();
        Log.e(TAG, "SetVendorDetails: \n name, email, mobile, type, otherDetails, vendorId \n"
                + name + ", " + email + ", " + mobile + ", " + type + ", " + otherDetails + ", " + vendorId);

        editor.putString(id + KEY_VENDOR_NAME, name);
        editor.putString(id + KEY_VENDOR_EMAIL, email);
        editor.putString(id + KEY_VENDOR_MOBILE, mobile);
        editor.putString(id + KEY_VENDOR_TYPE, type);
        editor.putString(id + KEY_VENDOR_OTHERDETAILS, otherDetails);
        editor.putString(id + KEY_VENDOR_ID, vendorId);
        editor.putString(id+KEY_VENDOR_LOGO,vendorLogo);
        editor.putString(id+KEY_VENDOR_ADDRESS,address);
        editor.commit();
    }

    public HashMap<String, String> GetVendorDetails(String id) {

        HashMap<String, String> map = new HashMap<>();
        Log.e(TAG, "GetVendorDetails: id" + id);

        String name = pref.getString(id + KEY_VENDOR_NAME, null);
        String email = pref.getString(id + KEY_VENDOR_EMAIL, null);
        String mobile = pref.getString(id + KEY_VENDOR_MOBILE, null);
        String type = pref.getString(id + KEY_VENDOR_TYPE, null);
        String otherDetails = pref.getString(id + KEY_VENDOR_OTHERDETAILS, null);
        String vendorId = pref.getString(id + KEY_VENDOR_ID, null);
        String vendoraddress=pref.getString(id+KEY_VENDOR_ADDRESS,null);
        String vendorimage=pref.getString(id+KEY_VENDOR_LOGO,null);
        Log.e(TAG, "GetVendorDetails: \n name, email, mobile, type, otherDetails, vendorId \n"
                + name + ", " + email + ", " + mobile + ", " + type + ", " + otherDetails + ", " + vendorId);

        map.put(id + KEY_VENDOR_NAME, name);
        map.put(id + KEY_VENDOR_TYPE, type);
        map.put(id + KEY_VENDOR_EMAIL, email);
        map.put(id + KEY_VENDOR_MOBILE, mobile);
        map.put(id + KEY_VENDOR_OTHERDETAILS, otherDetails);
        map.put(id + KEY_VENDOR_ID, vendorId);
        map.put(id+KEY_VENDOR_ADDRESS,vendoraddress);
        map.put(id+KEY_VENDOR_LOGO,vendorimage);
        return map;
    }
}