package com.example.tunganh.duan1_app.Model;

public class Rating {
    private String userName; /// both key and value
    private String detailsId;
    private String rateValue;
    private String comment;
    private String product;

    public Rating() {
    }

    public Rating(String userName, String detailsId, String rateValue, String comment, String product) {
        this.userName = userName;
        this.detailsId = detailsId;
        this.rateValue = rateValue;
        this.comment = comment;
        this.product = product;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(String detailsId) {
        this.detailsId = detailsId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
