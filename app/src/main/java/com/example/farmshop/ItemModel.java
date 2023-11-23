package com.example.farmshop;
public class ItemModel {
    private int imageResource; // Resource identifier for the image
    private String name;
    private String price;
    private String description;

    public ItemModel(int imageResource, String name, String price, String description, int id) {
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

