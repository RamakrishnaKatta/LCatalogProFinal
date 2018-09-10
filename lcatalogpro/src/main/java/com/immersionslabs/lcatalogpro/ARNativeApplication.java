package com.immersionslabs.lcatalogpro;

import android.annotation.SuppressLint;
import android.app.Application;

import com.immersionslabs.lcatalogpro.utils.ConnectionReceiver;

public class ARNativeApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Application sInstance;

    // Anywhere in the application where an instance is required, this method
    // can be used to retrieve it.
    public static ARNativeApplication getInstance() {
        return (ARNativeApplication) sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
//        ((ARNativeApplication) sInstance).initializeInstance();
    }

    public void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }
}