package com.example.lulu.venmonotes;

/**
 * Created by lulu on 7/16/2015.
 */
public class Transaction {
    private String date;
    private User targetUser;
    private User actor;
    private String note;
    private double amount;
    private String action;
    private boolean isPositive = false;

    public Transaction(String date, User targetUser, User actor, String note, double amount, String action, String currentUser) {
        this.date = date;
        this.targetUser = targetUser;
        this.actor = actor;
        this.note = note;
        this.amount = amount;
        this.action = action;
        if (actor.getUserName().equals(currentUser) && action.equals("charge") ||
               targetUser.getUserName().equals(currentUser) && action.equals("pay"))
            isPositive = true;

    }

    @Override
    public String toString() {
        return actor + " " +  action + " " +  targetUser + " " + amount + "$";
    }

    public String getNode() {
        return note;
    }


    public boolean isPositive() {
        return isPositive;
    }


}
