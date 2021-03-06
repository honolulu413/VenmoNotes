package com.example.lulu.venmonotes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Joseph on 2015/8/17.
 */
public class SubEvent implements Serializable {
    private String userName;
    private String displayName;
    private String profileUrl;
    private String userID;

    private double amount;
    private String action;
    private boolean isPositive = false;
    private boolean isSettled = false;

    private static final String JSON_DATE = "date";
    private static final String JSON_USER_NAME = "userName";
    private static final String JSON_DISPLAY_NAME = "displayName";
    private static final String JSON_PROFILE_URL = "profileUrl";
    private static final String JSON_USER_ID = "userID";


    private static final String JSON_AMOUNT = "amount";
    private static final String JSON_ACTION = "action";
    private static final String JSON_POSITIVE = "positive";
    private static final String JSON_SETTLED = "settled";

    public void setUserID(String userID) {this.userID = userID; }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setIsPositive(boolean isPositive) {
        this.isPositive = isPositive;
    }

    public void setIsSettled(boolean isSettled) {
        this.isSettled = isSettled;
    }

    public SubEvent(User user) {
        userName = user.getUserName();
        displayName = user.getDisplayName();
        profileUrl = user.getProfileUrl();
        userID = user.getUser_id();
    }
    public SubEvent(JSONObject json) throws JSONException {
        userName = json.getString(JSON_USER_NAME);
        displayName = json.getString(JSON_DISPLAY_NAME);
        profileUrl = json.getString(JSON_PROFILE_URL);
        userID = json.getString(JSON_USER_ID);
        amount = json.getDouble(JSON_AMOUNT);
        action = json.getString(JSON_ACTION);
        isPositive = json.getBoolean(JSON_POSITIVE);
        isSettled = json.getBoolean(JSON_SETTLED);
    }


    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_USER_NAME, userName);
        json.put(JSON_DISPLAY_NAME, displayName);

        json.put(JSON_PROFILE_URL, profileUrl);
        json.put(JSON_USER_ID, userID);
        json.put(JSON_AMOUNT, amount);
        json.put(JSON_ACTION, action);

        json.put(JSON_POSITIVE, isPositive);
        json.put(JSON_SETTLED, isSettled);

        return json;
    }


    public String getUserID() {return userID; }

    public double getAmount() {
        return amount;
    }

    public double getRealAmount() {
        int a = isPositive ? -1 : 1;
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

    public String getAction() { return action; }

    @Override
    public String toString() {
        return "You " + action + " " + displayName + " " + amount + "$";
    }
}
