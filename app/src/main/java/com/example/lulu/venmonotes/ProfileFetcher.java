package com.example.lulu.venmonotes;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lulu on 7/17/2015.
 */
public class ProfileFetcher extends AsyncTask<String, Void, JSONObject>{
    private final String url = "https://api.venmo.com/v1/me?access_token=";

    @Override
    protected JSONObject doInBackground(String... params) {
        String token = params[0];
        String content = new HttpService().getUrl(url + token);
        try {
            return new JSONObject(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {


    }
}
