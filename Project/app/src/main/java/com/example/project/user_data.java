package com.example.project;

public class user_data {
    String Email;
    String Phone;
    String uid;
    String Name;

    public user_data() {
    }

    public user_data(String Email, String Phone, String uid, String Name) {
        this.Email = Email;
        this.Phone = Phone;
        this.uid = uid;
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
    this.Email = Email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
       this.Name = Name;
    }
}
