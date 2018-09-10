package com.immersionslabs.lcatalogpro.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    // Shared preferences file name
    private static final String PREF_NAME_1 = " L_Catalog_welcome_Screen ";
    private static final String PREF_NAME_2 = " L_Catalog_UserTypeActivityScreen ";
    private static final String PREF_NAME_6 = " L_Catalog_LoginActivityScreen";
    private static final String PREF_NAME_3 = " L_Catalog_GuestActivityScreen ";
    private static final String PREF_NAME_4 = " L_Catalog_MainActivityScreen ";
    private static final String PREF_NAME_5 = " L_Catalog_ProductPageActivityScreen ";

    private static final String WELCOMEACTIVITY_SCREEN_LAUNCH = "WelcomeActivityScreenLaunch";
    private static final String USERTYPEACTIVITY_LAUNCH_SCREEN = "UserTypeActivityLaunchScreen";
    private static final String GUESTACTIVITY_LAUNCH_SCREEN = "GuestActivityLaunchScreen";
    private static final String MAINACTIVITY_LAUNCH_SCREEN = "MainActivityLaunchScreen";
    private static final String PRODUCTPAGEACTIVITY_LAUNCH_SCREEN = "ProductPageActivityLaunchScreen";
    private static final String LOGINACTIVITY_LAUNCH_SCREEN = "LoginActivityLaunchScreen";

    private SharedPreferences pref, pref1, pref2, pref3, pref4, pref5;
    private SharedPreferences.Editor editor, editor1, editor2, editor3, editor4, editor5;

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        int PRIVATE_MODE = 0;

        pref = context.getSharedPreferences(PREF_NAME_1, PRIVATE_MODE);
        editor = pref.edit();
        pref1 = context.getSharedPreferences(PREF_NAME_2, PRIVATE_MODE);
        editor1 = pref1.edit();
        pref2 = context.getSharedPreferences(PREF_NAME_3, PRIVATE_MODE);
        editor2 = pref2.edit();
        pref3 = context.getSharedPreferences(PREF_NAME_4, PRIVATE_MODE);
        editor3 = pref3.edit();
        pref4 = context.getSharedPreferences(PREF_NAME_5, PRIVATE_MODE);
        editor4 = pref4.edit();
        pref5 = context.getSharedPreferences(PREF_NAME_6, PRIVATE_MODE);
        editor5 = pref5.edit();
    }

    /*welcomeScreen Pref*/
    public void SetWelcomeActivityScreenLaunch(boolean WelcomeScreen) {
        editor.putBoolean(WELCOMEACTIVITY_SCREEN_LAUNCH, WelcomeScreen);
        editor.commit();
    }

    public boolean WelcomeActivityScreenLaunch() {
        return pref.getBoolean(WELCOMEACTIVITY_SCREEN_LAUNCH, true);
    }

    /*UserTypeActivity Screen Pref*/
    public void setUserTypeActivityScreenLaunch() {
        editor1.putBoolean(USERTYPEACTIVITY_LAUNCH_SCREEN, false);
        editor1.commit();
    }

    public boolean UserTypeActivityScreenLaunch() {
        return pref1.getBoolean(USERTYPEACTIVITY_LAUNCH_SCREEN, true);
    }

    /*GuestActivity Screen pref*/
    public void setGuestActivityScreenLaunch() {
        editor2.putBoolean(GUESTACTIVITY_LAUNCH_SCREEN, false);
        editor2.commit();
    }

    public boolean GuestActivityScreenLaunch() {
        return pref2.getBoolean(GUESTACTIVITY_LAUNCH_SCREEN, true);
    }

    /*MainActivity Screen  Pref*/
    public void SetMainActivityScreenLaunch() {
        editor3.putBoolean(MAINACTIVITY_LAUNCH_SCREEN, false);
        editor3.commit();
    }

    public boolean MainActivityScreenLaunch() {
        return pref3.getBoolean(MAINACTIVITY_LAUNCH_SCREEN, true);
    }

    /*ProductPageActivity Screen Pref*/
    public void setProductPageActivityScreenLaunch() {
        editor4.putBoolean(PRODUCTPAGEACTIVITY_LAUNCH_SCREEN, false);
        editor4.commit();
    }

    public boolean ProductPageActivityScreenLaunch() {
        return pref4.getBoolean(PRODUCTPAGEACTIVITY_LAUNCH_SCREEN, true);
    }

    /*LoginActivity Screen Pref*/
    public void SetLoginActivityScreenLaunch() {
        editor5.putBoolean(LOGINACTIVITY_LAUNCH_SCREEN, false);
        editor5.commit();
    }

    public boolean LoginActivityScreenLaunch() {
        return pref5.getBoolean(LOGINACTIVITY_LAUNCH_SCREEN, true);
    }
}