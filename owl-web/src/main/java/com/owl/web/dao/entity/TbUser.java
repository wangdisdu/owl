package com.owl.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
}