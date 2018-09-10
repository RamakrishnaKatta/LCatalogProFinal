package com.immersionslabs.lcatalogpro.utils;

import android.os.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManager_AR {

    private String DOWNLOAD_URL;

    public DownloadManager_AR(String url) {

        DOWNLOAD_URL = url;
        try {
            Download();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Download() throws IOException {
        URL u = new URL(DOWNLOAD_URL);
        URLConnection conn = u.openConnection();
        int contentLength = conn.getContentLength();

        DataInputStream stream = new DataInputStream(u.openStream());

        byte[] buffer = new byte[contentLength];
        stream.readFully(buffer);
        stream.close();

        DataOutputStream file_out = new DataOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory() + "/L_CATALOG/cache/Data/ar_files.zip"));
        file_out.write(buffer);
        file_out.flush();
        file_out.close();
    }
}