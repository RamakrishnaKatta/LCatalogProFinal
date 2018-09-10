package com.immersionslabs.lcatalogpro.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

public class CustomMessage {
    private static CustomMessage message = new CustomMessage();

    public CustomMessage() {
    }

    public static CustomMessage getInstance() {
        return message;
    }

    public void CustomMessage(Context context, String message) {
        try {
            View view = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}