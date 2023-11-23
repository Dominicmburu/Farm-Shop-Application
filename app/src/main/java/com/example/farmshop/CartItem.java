package com.example.farmshop;
public class CartItem {
    private String itemImage;
    private int quantity;
    private String price;
    private String userEmail;
    private String itemName;

    public CartItem(String itemName, String itemImage, int quantity, String price, String userEmail) {
        this.itemImage = itemImage;
        this.quantity = quantity;
        this.price = price;
        this.userEmail = userEmail;
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getUserEmail() {
        return userEmail;
    }
}

