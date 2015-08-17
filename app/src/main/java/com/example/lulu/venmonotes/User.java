package com.example.lulu.venmonotes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lulu on 7/16/2015.
 */
public class User implements Serializable {
    private String userName;
    private String display_name;
    private String profile_url;
//    private static final long serialVersionUID = -7060210544600464481L;

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

    public String getProfileUrl() { return profile_url; }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof User)) return false;
        return this.userName.equals(((User)that).userName);
    }

    @Override
    public String toString() {
        return display_name;
    }

    public String getDisplay_name() { return display_name; }


}
