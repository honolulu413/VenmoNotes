package com.example.lulu.venmonotes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joseph on 2015/8/17.
 */
public class SubEvent {
    private String date;
    private String userName;
    private String displayName;
    private String profileUrl;

    private double amount;
    private String action;
    private boolean isPositive = false;
    private boolean isSettled = false;

    private static final String JSON_DATE = "date";
    private static final String JSON_USER_NAME = "userName";
    private static final String JSON_DISPLAY_NAME = "displayName";
    private static final String JSON_PROFILE_URL = "profileUrl";

    private static final String JSON_AMOUNT = "amount";
    private static final String JSON_ACTION = "action";
    private static final String JSON_POSITIVE = "positive";
    private static final String JSON_SETTLED = "settled";


    public SubEvent(JSONObject json) throws JSONException {
        date = json.getString(JSON_DATE);
        userName = json.getString(JSON_USER_NAME);
        displayName = json.getString(JSON_DISPLAY_NAME);
        profileUrl = json.getString(JSON_PROFILE_URL);
        amount = json.getDouble(JSON_AMOUNT);
        action = json.getString(JSON_ACTION);
        isPositive = json.getBoolean(JSON_POSITIVE);
        isSettled = json.getBoolean(JSON_SETTLED);
    }


    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_DATE, date);
        json.put(JSON_USER_NAME, userName);
        json.put(JSON_DISPLAY_NAME, displayName);

        json.put(JSON_PROFILE_URL, profileUrl);
        json.put(JSON_AMOUNT, amount);
        json.put(JSON_ACTION, action);

        json.put(JSON_POSITIVE, isPositive);
        json.put(JSON_SETTLED, isSettled);

        return json;
    }


    public String getDate() {
        return date;
    }


    public double getAmount() {
        return amount;
    }

    public double getRealAmount() {
        int a = isPositive ? 1 : -1;
        return a * amount;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public boolean isSettled() {
        return isSettled;
    }

    public void setIsSettled(Boolean in) {
        isSettled = in;
    }

    @Override
    public String toString() {
        return "You " + action + " " + displayName + " " + amount + "$ on " + date;
    }
}
