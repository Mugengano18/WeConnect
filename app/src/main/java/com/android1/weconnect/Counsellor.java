package com.android1.weconnect;

import com.google.firebase.database.DatabaseReference;

public class Counsellor {
    String fname;
    String country;
    String Email;
    String occupation;
    String id;


    public Counsellor(String country, String email,String fname,String occupation) {
        this.fname = fname;
        this.country = country;
        Email = email;
        this.occupation = occupation;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
