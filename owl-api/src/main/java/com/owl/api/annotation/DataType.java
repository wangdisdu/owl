package com.owl.api.annotation;

import java.sql.Types;

public enum DataType {
    STRING("STRING"),
    INT("INT"),
    LONG("LONG"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    BOOL("BOOL"),
    KV("KV"),
    LIST("LIST");

    public final String code;

    DataType(String code) {
        this.code = code;
    }

    public static DataType fromJdbc(int jdbcType) {
        switch (jdbcType) {
            case Types.BOOLEAN:
            case Types.BIT:
                return DataType.BOOL;
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
                return DataType.INT;
            case Types.BIGINT:
                return DataType.LONG;
            case Types.REAL:
                return DataType.FLOAT;
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                return DataType.DOUBLE;
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.DATALINK:
            case Types.SQLXML:
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                return DataType.STRING;
            default:
                return DataType.STRING;
        }
    }
}
