package com.immersionslabs.lcatalogpro.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager_CheckList {

    private static Long Current_Value = 0L;
    private static ArrayList<String> Articles = new ArrayList<String>();
    private static HashMap<String,String> vendorarticle = new HashMap<>();

    public Long CHECKLIST_GET_CURRENT() {
        return Current_Value;
    }

    public void CHECKLIST_SET_CURRENT(Long current_Value) {
        Current_Value = current_Value;
    }

    public void CHECKLIST_ADD_ARTICLE(String article_id, String article_vendor_id) {
        Articles.add(article_id);
        vendorarticle.put(article_id,article_vendor_id);
    }

    public void CHECKLIST_REMOVE_ARTICLE(String article_id, Long price) {
        if (Articles.contains(article_id)) {
            Articles.remove(article_id);
            Current_Value -= price;
        }
    }

    public boolean CHECKLIST_IS_ARTICLE_EXISTS(String article_id) {
        Articles.add("empty");
        boolean flag;
        if (Articles.contains(article_id)) {
            flag = true;
        } else {
            flag = false;
        }
        Articles.remove("empty");
        return flag;
    }

    public ArrayList CHECKLIST_GET_ARTICLE_IDS() {
        return Articles;
    }

    public void CHECKLIST_CLEAR_ARRAY_ARTICLES() {
        Articles.clear();
        Current_Value = 0L;
    }

    public HashMap CHECKLIST_GET_VENDORARTICLES()
    {
        return vendorarticle;
    }
}