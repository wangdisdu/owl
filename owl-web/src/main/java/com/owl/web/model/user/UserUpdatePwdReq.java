package com.owl.web.model.user;

public class UserUpdatePwdReq {
    private String account;
    private String password;

    public String getAccount() {
        return account;
    }

    public UserUpdatePwdReq setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserUpdatePwdReq setPassword(String password) {
        this.password = password;
        return this;
    }
}
