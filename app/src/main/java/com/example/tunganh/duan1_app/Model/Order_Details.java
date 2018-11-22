package com.example.tunganh.duan1_app.Model;

import java.util.List;

public class Order_Details {

    private String name;
    private String phone;
    private String email;
    private String address;
    private String total;
    private String status; ///// trạng thái khi mua hàng
    private List<Order> details; //// Danh sách hàng đã mua

    public Order_Details() {
    }

    public Order_Details(String name, String phone, String email, String address, String total, List<Order> details) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.total = total;
        this.details = details;
        this.status = "0";////// default 0 ---> 0: Order  1: Shipping   2: Shipped
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getDetails() {
        return details;
    }

    public void setDetails(List<Order> details) {
        this.details = details;
    }

    //    public Order_Details() {
//    }
//
//    public Order_Details(String name, String phone,String email, String address, String total, List<Order> details) {
//        this.name = name;
//        this.phone = phone;
//        this.phone = email;
//        this.address = address;
//        this.total = total;
//        this.details = details;
//        this.status = "0"; ////// default 0 ---> 0: Order  1: Shipping   2: Shipped
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getTotal() {
//        return total;
//    }
//
//    public void setTotal(String total) {
//        this.total = total;
//    }
//
//    public List<Order> getDetails() {
//        return details;
//    }
//
//    public void setDetails(List<Order> details) {
//        this.details = details;
//    }
}
