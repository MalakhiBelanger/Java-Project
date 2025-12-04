package com.example.esmanager.UserStuff;

public class User {
    private String id;
    // When created, do something like this:
    // private ArrayList<Game> games;
    // private ArrayList<transaction> finances;

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
