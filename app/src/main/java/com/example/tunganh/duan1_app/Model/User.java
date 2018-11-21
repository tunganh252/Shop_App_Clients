package com.example.tunganh.duan1_app.Model;

public class User {
    private String Pass;
    private String Name;
    private String Phone;
    private String IsStaff;

    public User() {
    }

    public User(String pass, String name, String phone) {
        Pass = pass;
        Name = name;
        Phone = phone;
        IsStaff="false";
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
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
}
