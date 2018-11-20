package com.example.tunganh.duan1_app.Model;

import java.util.List;

public class Order_Details {

    private String name;
    private String phone;
    private String address;
    private String total;

    private List<Order> details; //// Danh sách hàng đã mua

    public Order_Details() {
    }

    public Order_Details(String name, String phone, String address, String total, List<Order> details) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getDetails() {
        return details;
    }

    public void setDetails(List<Order> details) {
        this.details = details;
    }
}
