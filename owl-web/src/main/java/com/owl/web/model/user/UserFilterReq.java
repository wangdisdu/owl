package com.owl.web.model.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.owl.web.dao.entity.TbUser;
import com.owl.web.model.CommonFilter;

public class UserFilterReq extends CommonFilter<TbUser> {
    private String account;
    private String search;
    private String name;
    private String email;
    private String phone;

    public QueryWrapper<TbUser> where(QueryWrapper<TbUser> wrapper) {
        wrapper.lambda()
                .like(StrUtil.isNotBlank(account), TbUser::getAccount, escapeLike(account))
                .like(StrUtil.isNotBlank(name), TbUser::getName, escapeLike(name))
                .like(StrUtil.isNotBlank(email), TbUser::getEmail, escapeLike(email))
                .like(StrUtil.isNotBlank(phone), TbUser::getPhone, escapeLike(phone))
                .and(StrUtil.isNotBlank(search), q -> q
                        .or().like(TbUser::getAccount, escapeLike(search))
                        .or().like(TbUser::getName, escapeLike(search))
                        .or().like(TbUser::getEmail, escapeLike(search))
                        .or().like(TbUser::getPhone, escapeLike(search)));
        return wrapper;
    }

    public String getAccount() {
        return account;
    }

    public UserFilterReq setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getSearch() {
        return search;
    }

    public UserFilterReq setSearch(String search) {
        this.search = search;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserFilterReq setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserFilterReq setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserFilterReq setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}
