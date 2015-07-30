package com.example.lulu.venmonotes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lulu on 7/16/2015.
 */
public class Transaction {
    private Date date;
    private User targetUser;
    private User actor;
    private String note;
    private double amount;
    private String action;
    private boolean isPositive = false;

    public Transaction(String date, User targetUser, User actor, String note, double amount, String action, String currentUser) {
        String[] times = date.substring(0, date.indexOf("T")).split("-");
        int year = Integer.parseInt(times[0]);
        int month = Integer.parseInt(times[1]);
        int day = Integer.parseInt(times[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        this.date = calendar.getTime();

        this.targetUser = targetUser;
        this.actor = actor;
        this.note = note;
        this.amount = amount;
        this.action = action;
        this.note = note;
        if (actor.getUserName().equals(currentUser) && action.equals("charge") ||
                targetUser.getUserName().equals(currentUser) && action.equals("pay"))
            isPositive = true;

    }

//    @Override
//    public String toString() {
//        String newDate = date.replaceAll("T.+", "");
//        return actor + " " + action + " " + targetUser + " " + amount + "$" + "\n" + note + "\t\t\t@" + newDate;
//    }

    public String getDetail() {
        String tmp = isPositive ? "+" : "-";
        return actor + " " + action + " " + targetUser + " " + tmp + amount + "$";
    }

    public String getShortDate() {
        return getDateString().replaceAll("T.+", "");
    }

    public String getNote() {
        return note;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(date);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRealAmount() {
        int a = isPositive ? 1 : -1;
        return a * amount;
    }

    public User getActor() {
        return actor;
    }
}
