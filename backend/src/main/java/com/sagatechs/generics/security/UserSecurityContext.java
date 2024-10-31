package com.sagatechs.generics.security;

import com.sagatechs.generics.webservice.webModel.UserWeb;

public class UserSecurityContext {
    private static final ThreadLocal<UserWeb> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(UserWeb principal) {
        currentUser.set(principal);
    }

    public static UserWeb getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }

}
