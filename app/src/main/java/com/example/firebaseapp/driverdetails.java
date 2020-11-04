package com.example.firebaseapp;

public class driverdetails {
    String time,date,price;

    public driverdetails(){

    }

    public driverdetails(String time, String date, String price) {
        this.time = time;
        this.date = date;
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}