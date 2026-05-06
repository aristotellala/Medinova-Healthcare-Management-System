package com.medinova.model;

import java.sql.Date;

public class Inventory {
    private int itemId;
    private String itemName;
    private String category;
    private int quantity;
    private int minQuantity;
    private double unitPrice;
    private Date expiryDate;

    public Inventory() {
    }

    public Inventory(String name, String category, int quantity,
                     int minQuantity, double unitPrice, Date expiryDate) {
        this.itemName = name;
        this.category = category;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.unitPrice = unitPrice;
        this.expiryDate = expiryDate;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
