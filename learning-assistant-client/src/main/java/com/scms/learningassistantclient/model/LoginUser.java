package com.scms.learningassistantclient.model;

public class LoginUser {

    private Long id;
    private String username;
    private String realName;
    private String role;
    private String className;
    private String token;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRealName() {
        return realName;
    }

    public String getRole() {
        return role;
    }

    public String getClassName() {
        return className;
    }

    public String getToken() {
        return token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setToken(String token) {
        this.token = token;
    }
}