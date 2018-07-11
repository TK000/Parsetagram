package com.tomoka.parsetagram.model;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser {

    // list the attributes
    private final static String KEY_USERNAME = "username";

    public String getUsername() {
        return getString(KEY_USERNAME);
    }

}
