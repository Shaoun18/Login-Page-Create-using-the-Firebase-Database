package com.seip.firebaseapp.models;

public class Expense {
    private String id;
    private String tille;
    private double amount;
    private long timestamp;
    private String usid;
    private String imageUrl;

    public Expense() {

    }

    public Expense(String id, String tille, double amount, long timestamp, String usid, String imageUrl) {
        this.id = id;
        this.tille = tille;
        this.amount = amount;
        this.timestamp = timestamp;
        this.usid = usid;
        this.imageUrl = imageUrl;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTille() {
        return tille;
    }

    public void setTille(String tille) {
        this.tille = tille;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id='" + id + '\'' +
                ", tille='" + tille + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", usid='" + usid + '\'' +
                '}';
    }
}
