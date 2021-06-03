package com.owl.web.common;

import com.owl.web.dao.entity.TbUser;

public class RequestContext {
    private TbUser login;

    public RequestContext(TbUser login) {
        this.login = login;
    }

    public TbUser getLogin() {
        return login;
    }

    public void setLogin(TbUser login) {
        this.login = login;
    }
}
