package com.owl.web.model.user;

import com.owl.web.dao.entity.TbUser;
import com.owl.web.model.BaseModel;

public class UserCreateReq implements BaseModel<TbUser> {
    private String account;
    private String password;
    private String name;
    private Integer role;
    private String email;
    private String phone;

    public String getAccount() {
        return account;
    }

    public UserCreateReq setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserCreateReq setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserCreateReq setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getRole() {
        return role;
    }

    public UserCreateReq setRole(Integer role) {
        this.role = role;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserCreateReq setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserCreateReq setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}
