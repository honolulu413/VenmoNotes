package com.example.lulu.venmonotes;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Joseph on 2015/8/11.
 */
public class FriendSearchFetcher {
    private String ENDPOINT;
    private String currentUser;
    private String token;

    public FriendSearchFetcher(String token, String currentUser) {
        this.token = token;
        this.currentUser = currentUser;
        ENDPOINT = "https://api.venmo.com/v1/users/" + currentUser + "/friends";
    }

    public ArrayList<User> getFriend() {
        ArrayList<User> friends = new ArrayList<User>();
        Uri.Builder builder = Uri.parse(ENDPOINT).buildUpon();
        builder.appendQueryParameter("access_token", token);
        String urlSpec = builder.build().toString();
        HttpService httpService = new HttpService();

        String jsonString = httpService.getUrl(urlSpec);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmpObject = jsonArray.getJSONObject(i);
                User friend = new User(tmpObject);
                friends.add(friend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(HttpService.TAG, "OMG IT IS DDDDDD" + e);
        }

        Collections.sort(friends, new Comparator<User>() {
            @Override
            public int compare(User f1, User f2) {
                return f1.getDisplay_name().compareTo(f2.getDisplay_name());
            }
        });

        return friends;

    }
}
