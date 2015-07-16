package com.example.lulu.venmonotes;

/**
 * Created by lulu on 7/16/2015.
 */
public class User {
    private String userName;
    private String display_name;

    public User(String userName, String display_name) {
        this.userName = userName;
        this.display_name = display_name;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplay_name() {
        return display_name;
    }

    @Override
    public String toString() {
        return display_name;
    }
}
