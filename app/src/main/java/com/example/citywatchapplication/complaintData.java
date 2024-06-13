package com.example.citywatchapplication;

import java.io.Serializable;

public class complaintData implements Serializable {
    private String title;
    private String description;
    private String location;
    private String category;
    private String userID;

    // Constructor
    public complaintData(String title, String description, String location, String category, String userID) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.userID = userID;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public String getUserID() {
        return userID;
    }
}
