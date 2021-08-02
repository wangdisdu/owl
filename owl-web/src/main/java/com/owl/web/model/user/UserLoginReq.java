package com.owl.web.model.user;

public class UserLoginReq {
    private String account;
    private String password;

    public String getAccount() {
        return account;
    }

    public UserLoginReq setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserLoginReq setPassword(String password) {
        this.password = password;
        return this;
    }
}
