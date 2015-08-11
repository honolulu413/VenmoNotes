package com.example.lulu.venmonotes;

/**
 * Created by lulu on 8/10/2015.
 */

public class FriendTotal implements Comparable<FriendTotal>{
    private double total;

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    private double pay;
    private double gain;
    private String displayName;
    private int times;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void addPay(double a) {
        pay += a;
        total += a;
    }

    public void addGain(double a) {
        gain += a;
        total += a;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    @Override
    public int compareTo(FriendTotal another) {
        double d = another.total - this.total;
        if (d > 0) return 1;
        else if (d < 0) return -1;
        else return 0;
    }
}
