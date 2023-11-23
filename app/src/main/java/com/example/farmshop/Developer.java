package com.example.farmshop;
public class Developer {
    private String name;
    private String role;
    private String description;

    public Developer(String name, String role, String description) {
        this.name = name;
        this.role = role;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }
}
