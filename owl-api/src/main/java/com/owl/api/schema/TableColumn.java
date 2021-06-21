package com.owl.api.schema;

import com.owl.api.annotation.DataType;

public class TableColumn {
    private String name;
    private ColumnType type;
    private DataType javaType;
    private JdbcType jdbcType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    public DataType getJavaType() {
        return javaType;
    }

    public void setJavaType(DataType javaType) {
        this.javaType = javaType;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }

    public static TableColumn build(String name, int jdbcType) {
        TableColumn column = new TableColumn();
        column.setName(name);
        column.setType(ColumnType.fromJdbc(jdbcType));
        column.setJavaType(DataType.fromJdbc(jdbcType));
        column.setJdbcType(JdbcType.fromJdbc(jdbcType));
        return column;
    }
}
