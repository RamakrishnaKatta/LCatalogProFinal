package com.immersionslabs.lcatalogpro.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserCheckUtil {

    private static final String TAG = "UserCheckUtil";

    public static void writeToFile(String data, String user_type) {
        try {
            Log.e(TAG, "- writeToFile" + "Storage State: " + isExternalStorageReadOnly() + "Storage State(ExternalStorageAvailability): " + isExternalStorageAvailable());
            String text_file_location = Environment.getExternalStorageDirectory() + "/L_CATALOG/" + user_type + ".txt";
            FileOutputStream fileOutputStream = new FileOutputStream(text_file_location);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e(TAG + "-Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(String user_type) {

        StringBuilder ret = new StringBuilder();
        String text_file_location = Environment.getExternalStorageDirectory() + "/L_CATALOG/" + user_type + ".txt";

        try {
            Log.e(TAG, "- readFromFile" + "Storage State(ReadOnlyState): " + !isExternalStorageReadOnly() + " Storage State(ExternalStorageAvailability): " + isExternalStorageAvailable());
            FileInputStream fileInputStream = new FileInputStream(text_file_location);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
            String strLine;

            while ((strLine = bufferedReader.readLine()) != null) {
                ret.append(strLine);
            }
            dataInputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "- login activity" + "File not found: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }
}

