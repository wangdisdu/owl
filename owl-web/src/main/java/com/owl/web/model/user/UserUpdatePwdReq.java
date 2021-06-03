package com.owl.web.model.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdatePwdReq {
    private String account;
    private String password;
}
