package com.example.cristianmmuresan.challengeme.data;

import com.parse.ParseUser;

/**
 * Created by Cristian M. Muresan on 5/8/2016.
 */
public class User {
    private String token;

    public User(){

    }

    public User(ParseUser user) {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
