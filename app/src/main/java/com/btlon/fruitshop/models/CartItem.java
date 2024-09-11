package com.btlon.fruitshop.models;

public class CartItem {
    private String productId;
    private String productName;
    private double productPrice;
    private double quantity;
    private double totalPrice;


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    private String img; // URL hoặc đường dẫn hình ảnh
    public CartItem() {
        // Constructor này cần thiết cho Firebase
    }
    // Constructor
    public CartItem(String productId, String productName, double productPrice, double quantity, double totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getters và Setters (nếu cần)
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

