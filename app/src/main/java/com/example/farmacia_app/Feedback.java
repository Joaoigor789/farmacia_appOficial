package com.example.farmacia_app;

public class Feedback {

    private final String username;
    private String message;
    private final float rating;

    // Construtor
    public Feedback(String username, String message, float rating) {
        this.username = username;
        this.message = message;
        this.rating = rating;
    }



    // Getters
    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public float getRating() {
        return rating;
    }

    // Setters

    public void setMessage(String message) {
        this.message = message;
    }

}
