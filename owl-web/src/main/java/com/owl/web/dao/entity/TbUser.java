package com.owl.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName
public class TbUser {

    @TableId
    private String account;

    @TableField
    private String password;

    @TableField
    private String name;

    @TableField
    private Integer role;

    @TableField
    private String email;

    @TableField
    private String phone;

    @TableField
    private Long sessionTime;

    @TableField
    private String sessionToken;

    @TableField
    private Long sessionExpire;

    @TableField
    private Long createTime;

    @TableField
    private String createBy;

    @TableField
    private Long updateTime;

    @TableField
    private String updateBy;

    public String getAccount() {
        return account;
    }

    public TbUser setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public TbUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public TbUser setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getRole() {
        return role;
    }

    public TbUser setRole(Integer role) {
        this.role = role;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public TbUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public TbUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Long getSessionTime() {
        return sessionTime;
    }

    public TbUser setSessionTime(Long sessionTime) {
        this.sessionTime = sessionTime;
        return this;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public TbUser setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
        return this;
    }

    public Long getSessionExpire() {
        return sessionExpire;
    }

    public TbUser setSessionExpire(Long sessionExpire) {
        this.sessionExpire = sessionExpire;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public TbUser setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public TbUser setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public TbUser setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public TbUser setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }
}