package com.example.neera.assignment.model;

/**
 * Created by neera on 31-01-2018.
 */

public class UserInformation {
    public String username;
    public String email;
    public String password;

    public UserInformation()
    {

    }

    public UserInformation(String username,String email,String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

