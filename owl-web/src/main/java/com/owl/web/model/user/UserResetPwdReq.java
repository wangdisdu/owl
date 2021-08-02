package com.owl.web.model.user;

public class UserResetPwdReq {
    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public UserResetPwdReq setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public UserResetPwdReq setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }
}
