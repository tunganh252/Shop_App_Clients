package com.example.tunganh.duan1_app.Model;

public class User {
    private String Pass;
    private String Name;
    private String Phone;
    private String Email;
    private String Admin;

    public User() {
    }

    public User(String pass, String name, String phone, String email) {
        Pass = pass;
        Name = name;
        Phone = phone;
        Email = email;
        Admin="false";
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAdmin() {
        return Admin;
    }

    public void setAdmin(String admin) {
        Admin = admin;
    }
}
