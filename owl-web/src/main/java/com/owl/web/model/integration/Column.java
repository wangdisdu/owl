package com.owl.web.model.integration;

import com.owl.web.common.consts.ColumnType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Column {
    private String name;
    private ColumnType type;
    private int jdbcType;
}
