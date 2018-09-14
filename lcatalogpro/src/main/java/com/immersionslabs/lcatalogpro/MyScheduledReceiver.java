package com.immersionslabs.lcatalogpro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MyScheduledReceiver extends BroadcastReceiver {

    public static final String TAG = MyScheduledReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {

        Toast.makeText(context, "Your Guest Session will Expires in 2 minutes ", Toast.LENGTH_LONG).show();

        Log.e(TAG, "Intent Fired");

        new Handler().postDelayed(() -> {
            Log.e(TAG, "run:here we go ");
            System.out.println("here we go");

            Intent broadcastIntent = new Intent(context, UserTypeActivity.class);
            broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(broadcastIntent);

        }, 2 * 60 * 1000);
    }
}