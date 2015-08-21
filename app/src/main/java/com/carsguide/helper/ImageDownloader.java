package com.carsguide.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.carsguide.Constants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageDownloader {

    private static ImageDownloader instance;

    public static final ImageDownloader getInstance() {
        if (instance == null) {
            instance = new ImageDownloader();
        }
        return instance;
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private String imageUrl;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... sUrl) {
            try {
                imageUrl = sUrl[0];
                String filename = md5(imageUrl);
                URL url = new URL(imageUrl);
                String dirPath = Constants.STORAGE_PATH_IMAGES;

                File file = new File(dirPath);
                file.mkdirs();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                File outputFile = new File(dirPath, filename);
                InputStream input = new BufferedInputStream(connection.getInputStream());

                OutputStream output = null;
                if (TextUtils.isEmpty(dirPath)) {
                    // save to local cache
                    output = context.openFileOutput(filename, Context.MODE_PRIVATE);
                } else {
                    output = new FileOutputStream(outputFile);
                }

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                connection.disconnect();
                output.flush();
                output.close();
                input.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }

    public void downloadImage(Context context, String urlStr) {
        new DownloadTask(context).execute(urlStr);
    }

    public File getFile(String fileName) {
        File file = new File(Constants.STORAGE_PATH_IMAGES, md5(fileName));
        return file;
    }

    public String getImagePath(String fileName) {
        return Constants.STORAGE_PATH_IMAGES + md5(fileName);
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString() + ".png";

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}