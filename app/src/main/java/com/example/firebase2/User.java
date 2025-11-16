package com.example.firebase2;

import java.io.Serializable;


public class User implements Serializable {
    private String name, email, phone, uid, type;

    public User()
    {
        name="";
        email="";
        phone="";
        type="";
        uid="";
    }

    public User (String name, String email, String phone, String uid,String type) {
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.type=type;
        this.uid=uid;
    }

    public void setType(String type) {
        this.type=type;
    }
    public String getType() {
        return type;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email=email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone=phone;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) {
        this.uid=uid;
    }
}