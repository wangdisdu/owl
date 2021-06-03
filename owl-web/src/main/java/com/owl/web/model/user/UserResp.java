package com.owl.web.model.user;

import com.owl.web.dao.entity.TbUser;
import com.owl.web.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
}
