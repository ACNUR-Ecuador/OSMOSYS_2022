package com.sagatechs.generics.security;

import com.sagatechs.generics.webservice.webModel.UserWeb;

import javax.security.enterprise.CallerPrincipal;

public class CustomPrincipal extends CallerPrincipal {
    UserWeb user;

    public CustomPrincipal(UserWeb user) {
        super(user.getName());
        this.user=user;
    }

    public UserWeb getUser() {
        return user;
    }

    public void setUser(UserWeb user) {
        this.user = user;
    }


}
