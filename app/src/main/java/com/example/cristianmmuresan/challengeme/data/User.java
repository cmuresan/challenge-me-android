package com.example.cristianmmuresan.challengeme.data;

import com.parse.ParseUser;

/**
 * Created by Cristian M. Muresan on 5/8/2016.
 */
public class User {
    private String token;
    private String email;
    private String username;


    public User(){

    }

    public User(ParseUser user) {
        this.token = user.getObjectId();
        this.email = user.getEmail();
        this.username = user.getUsername();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
