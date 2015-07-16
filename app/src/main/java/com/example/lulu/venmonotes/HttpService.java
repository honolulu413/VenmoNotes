package com.example.lulu.venmonotes;

import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lulu on 6/24/2015.
 */
public class HttpService {
    public static final String TAG = "HttpService";

    byte[] getPostUrlBytes(String urlSpec, String parameters) throws IOException {
        Log.d(TAG, "visiting: " + urlSpec);
        Log.d(TAG, parameters);

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        // Set the post method. Default is GET
        connection.setRequestMethod("POST");
        connection.connect();
        DataOutputStream pOut = new DataOutputStream(connection
                .getOutputStream());

        pOut.writeBytes(parameters);
        pOut.flush();
        pOut.close(); // flush and close

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    byte[] getUrlBytes(String urlSpec) throws IOException {
        Log.d(TAG, "visiting: " + urlSpec);

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.connect();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getPostUrl(String urlSpec, String parameters){
        try {
            return new String(getPostUrlBytes(urlSpec, parameters));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getUrl(String urlSpec){
        try {
            return new String(getUrlBytes(urlSpec));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

