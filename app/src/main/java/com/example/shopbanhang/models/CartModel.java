package com.example.shopbanhang.models;

public class CartModel {
    String productImg;
    String productName;
    String productPrice;
    String currentDate;
    String currentTime;
    String total_quantity;
    double total_price;
    String documentID;


    public CartModel() {

    }

    public CartModel(String productImg, String productName, String productPrice, String currentDate, String currentTime, String total_quantity, double total_price) {
        this.productImg = productImg;
        this.productName = productName;
        this.productPrice = productPrice;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.total_quantity = total_quantity;
        this.total_price = total_price;
    }


    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }




}
