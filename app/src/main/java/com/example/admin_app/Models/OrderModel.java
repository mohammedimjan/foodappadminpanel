package com.example.admin_app.Models;

import java.io.Serializable;

public class OrderModel implements Serializable {
    String CustomerAddress, CustomerName, FoodImage, FoodName, FoodPrice, OrderId;

    public OrderModel() {
    }

    public OrderModel(String customerAddress, String customerName, String foodImage, String foodName, String foodPrice, String orderId) {
        CustomerAddress = customerAddress;
        CustomerName = customerName;
        FoodImage = foodImage;
        FoodName = foodName;
        FoodPrice = foodPrice;
        OrderId = orderId;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getFoodImage() {
        return FoodImage;
    }

    public void setFoodImage(String foodImage) {
        FoodImage = foodImage;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        FoodPrice = foodPrice;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
