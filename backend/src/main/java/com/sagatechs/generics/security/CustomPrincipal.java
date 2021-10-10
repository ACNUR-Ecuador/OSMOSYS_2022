package com.sagatechs.generics.security;

import com.sagatechs.generics.security.model.User;

import javax.security.enterprise.CallerPrincipal;

public class CustomPrincipal extends CallerPrincipal {
    User user;

    public CustomPrincipal(String name) {
        super(name);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
