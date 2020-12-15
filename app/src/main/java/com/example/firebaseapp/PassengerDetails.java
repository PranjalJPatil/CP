package com.example.firebaseapp;

public class PassengerDetails {
    String fName,phoneno;

    public PassengerDetails(){

    }

    public PassengerDetails(String fName, String phoneno) {
        this.fName = fName;
        this.phoneno = phoneno;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getphoneno() {
        return phoneno;
    }

    public void setphoneno(String phoneno) {
        this.phoneno = phoneno;
    }
}
