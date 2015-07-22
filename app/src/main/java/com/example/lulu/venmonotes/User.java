package com.example.lulu.venmonotes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lulu on 7/16/2015.
 */
public class User {
    private String userName;
    private String display_name;
    private String profile_url;

    public User(JSONObject jsonObject) throws JSONException {
        userName = jsonObject.getString("username");
        display_name = jsonObject.getString("display_name");
        profile_url = jsonObject.getString("profile_picture_url");
    }

    public User(String userName, String display_name) {
        this.userName = userName;
        this.display_name = display_name;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return display_name;
    }

    public String getProfileUrl() { return display_name; }

    @Override
    public String toString() {
        return display_name;
    }


}
