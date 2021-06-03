package com.owl.web.model.user;

import com.owl.web.dao.entity.TbUser;
import com.owl.web.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateReq implements BaseModel<TbUser> {
    private String account;
    private String name;
    private Integer role;
    private String email;
    private String phone;
}
