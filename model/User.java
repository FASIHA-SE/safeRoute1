package com.example.saferoute.model;

public class User {

    String username;
    String email;
    String password;

    public User() {

    }

    public User(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}