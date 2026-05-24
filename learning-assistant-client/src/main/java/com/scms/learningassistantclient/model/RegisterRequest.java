package com.scms.learningassistantclient.model;

public class RegisterRequest {

    private String username;
    private String password;
    private String realName;
    private String role;
    private String className;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String realName, String role, String className) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.role = role;
        this.className = className;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}