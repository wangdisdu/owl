package com.owl.web.model.user;

import com.owl.web.dao.entity.TbUser;
import com.owl.web.model.BaseModel;

public class UserResp implements BaseModel<TbUser> {
    private String account;
    private String name;
    private Integer role;
    private String email;
    private String phone;
    private Long sessionTime;
    private Long createTime;
    private String createBy;
    private Long updateTime;
    private String updateBy;

    public String getAccount() {
        return account;
    }

    public UserResp setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserResp setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getRole() {
        return role;
    }

    public UserResp setRole(Integer role) {
        this.role = role;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserResp setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserResp setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Long getSessionTime() {
        return sessionTime;
    }

    public UserResp setSessionTime(Long sessionTime) {
        this.sessionTime = sessionTime;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public UserResp setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public UserResp setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public UserResp setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public UserResp setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }
}
