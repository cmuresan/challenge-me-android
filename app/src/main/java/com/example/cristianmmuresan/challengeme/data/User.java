package com.example.cristianmmuresan.challengeme.data;

import com.parse.ParseUser;

/**
 * Created by Cristian M. Muresan on 5/8/2016.
 */
public class User {
    private String token;
    private String email;
    private String firstname;
    private String lastname;
    private String bike;


    public User() {

    }

    public User(ParseUser user) {
        this.token = user.getObjectId();
        this.email = user.getEmail();
        this.firstname = user.getString("firstname");
        this.lastname = user.getString("lastname");
        this.bike = user.getString("bike");
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getBike() {
        return bike;
    }
}