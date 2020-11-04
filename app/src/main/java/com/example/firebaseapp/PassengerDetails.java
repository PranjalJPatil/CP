package com.example.firebaseapp;

public class PassengerDetails {
    String name,phoneno;

    public PassengerDetails(){

    }


    public PassengerDetails(String name, String phoneno) {
        this.name = name;
        this.phoneno = phoneno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
}
