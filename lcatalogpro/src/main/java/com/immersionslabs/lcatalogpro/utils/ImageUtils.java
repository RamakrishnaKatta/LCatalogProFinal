package com.immersionslabs.lcatalogpro.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ImageUtils {

    // SD card screenshots_directory
    private static final String PHOTO_ALBUM = "/L_CATALOG/Screenshots/";

    private static final String TAG = "Utils";

    // supported file formats
    private static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");

    private Context _context;
    private File screenshots_directory;

    // constructor
    public ImageUtils(Context context) {
        this._context = context;
    }

    //Reading file paths from SDCard
    public ArrayList<String> getFilePaths() {

        ArrayList<String> ImageFilePaths = new ArrayList<>();

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "ERROR, No Card Found------");
        } else {
            screenshots_directory = new File(android.os.Environment.getExternalStorageDirectory() + PHOTO_ALBUM);
            screenshots_directory.mkdirs();
        }

        // check for screenshots_directory
        if (screenshots_directory.isDirectory()) {

            // getting list of file paths
            File[] listOfImages = screenshots_directory.listFiles();
            //Log.e(TAG, "List Of files------" + Arrays.toString(listOfFiles));

            // Check for count
            if (listOfImages.length > 0) {

                // screenshots_directory is empty
                Toast.makeText(_context, "Here are your screenshots !", Toast.LENGTH_LONG).show();

                // loop through all files
                for (File listOfImageFiles : listOfImages) {

                    // get file path
                    String ImagePath = listOfImageFiles.getAbsolutePath();
                    // check for supported file extension
                    if (IsSupportedFile(ImagePath)) {
                        // Add image path to array list
                        ImageFilePaths.add(ImagePath);
                    }
                }

            } else {
                // screenshots_directory is empty
                Toast.makeText(_context, "You haven't taken any screenshots Yet !", Toast.LENGTH_LONG).show();
            }

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(_context, R.style.AppCompatAlertDialogStyle);
            alert.setTitle("Permission Error!");
            alert.setMessage("We are unable to read your Storage, have you given the permissions ?");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        return ImageFilePaths;
    }

    /*
     * Check supported file extensions
     * @returns boolean
     */
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1), filePath.length());

        return FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault()));
    }

    //getting screen width
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) {
            // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
}

