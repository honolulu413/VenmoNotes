package com.example.lulu.venmonotes;

/**
 * Created by Joseph on 2015/7/9.
 */
public class VenmoResponse {
    public String amount, action, actor, audience;

    public VenmoResponse(String amount, String action, String actor, String audience) {
        this.action = action;
        this.actor = actor;
        this.audience = audience;
        this.amount = amount;
     }

}

