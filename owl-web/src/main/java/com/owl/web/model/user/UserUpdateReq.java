package com.owl.web.model.user;

import com.owl.web.dao.entity.TbUser;
import com.owl.web.model.BaseModel;

public class UserUpdateReq implements BaseModel<TbUser> {
    private String account;
    private String name;
    private Integer role;
    private String email;
    private String phone;

    public String getAccount() {
        return account;
    }

    public UserUpdateReq setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserUpdateReq setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getRole() {
        return role;
    }

    public UserUpdateReq setRole(Integer role) {
        this.role = role;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserUpdateReq setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserUpdateReq setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}
