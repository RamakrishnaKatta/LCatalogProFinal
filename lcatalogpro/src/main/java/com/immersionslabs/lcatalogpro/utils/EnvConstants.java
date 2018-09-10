package com.immersionslabs.lcatalogpro.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class EnvConstants {

    public static final String APP_BASE_URL = "https://ladmin.immersionslabs.com";
    public static final String PORTAL_BASE_URL = "https://lcatalog.immersionslabs.com";

    public static ArrayList<String> user_Favourite_list = new ArrayList<String>();
    public static String user_type = null;
    public static boolean flag_article_details = false;
    public static int position = 0;

    public static String part_articles_id_var = null;
    public static String part_article_name_var = null;
    public static String part_article_images_var = null;
    public static String part_articles_vendor_id_var = null;
    public static String part_article_dimensions_var = null;
    public static String part_articles_pattern_var = null;
    public static String part_articles_3ds_var = null;
    public static String part_articles_description_var = null;
    public static String part_article__discounts_var = null;
    public static String part_articles_price_var = null;

    public static ArrayList<String> part_articles_id = new ArrayList<>();

    public static HashMap<String, String> part_article_name = new HashMap<>();
    public static HashMap<String, String> part_article_images = new HashMap<>();
    public static HashMap<String, String> part_articles_vendor_id = new HashMap<>();
    public static HashMap<String, String> part_article_dimensions = new HashMap<>();
    public static HashMap<String, String> part_articles_pattern = new HashMap<>();
    public static HashMap<String, String> part_articles_3ds = new HashMap<>();
    public static HashMap<String, String> part_articles_description = new HashMap<>();
    public static HashMap<String, String> part_article__discounts = new HashMap<>();
    public static HashMap<String, String> part_articles_price = new HashMap<>();
}

