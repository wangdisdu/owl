package com.owl.web.model.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResetPwdReq {
    private String oldPassword;
    private String newPassword;
}
