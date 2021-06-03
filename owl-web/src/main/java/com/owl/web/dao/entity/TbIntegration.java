package com.owl.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName
public class TbIntegration {

    @TableId
    private String name;

    @TableField
    private String description;

    @TableField
    private String config;

    @TableField
    private String builder;

    @TableField
    private String meta;

    @TableField
    private Long createTime;

    @TableField
    private String createBy;

    @TableField
    private Long updateTime;

    @TableField
    private String updateBy;
}