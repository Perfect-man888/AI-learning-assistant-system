package com.scms.learningassistantclient.util;

import com.scms.learningassistantclient.model.LoginUser;

public class AppContext {

    private static LoginUser currentUser;

    public static LoginUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(LoginUser user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
    }
}