package com.scms.learning.vo;

public class LoginUserVO {

    private Long id;

    private String username;

    private String realName;

    private String role;

    private String className;

    private String token;

    public LoginUserVO(Long id, String username, String realName, String role, String className, String token) {
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.role = role;
        this.className = className;
        this.token = token;
    }

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
}