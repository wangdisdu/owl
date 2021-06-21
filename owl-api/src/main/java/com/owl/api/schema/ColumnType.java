package com.owl.api.schema;

import java.sql.Types;

public enum ColumnType {
    STRING("STRING"),
    INT("INT"),
    LONG("LONG"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    BOOL("BOOL"),
    DATE("DATE"),           // Date is day + moth + year
    TIME("TIME"),           // Time is a time of day -- hour, minute, seconds, nanoseconds
    TIMESTAMP("TIMESTAMP"), // Timestamp is a date + time -- day + moth + year + hour, minute, seconds, nanoseconds
    OTHER("OTHER");         // unknown

    public final String code;

    ColumnType(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    public static ColumnType fromJdbc(int jdbcType) {
        switch (jdbcType) {
            case Types.BOOLEAN:
            case Types.BIT:
                return ColumnType.BOOL;
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
                return ColumnType.INT;
            case Types.BIGINT:
                return ColumnType.LONG;
            case Types.REAL:
                return ColumnType.FLOAT;
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                return ColumnType.DOUBLE;
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
                return ColumnType.STRING;
            case Types.DATE:
                return ColumnType.DATE;
            case Types.TIME:
                return ColumnType.TIME;
            case Types.TIMESTAMP:
                return ColumnType.TIMESTAMP;
            default:
                return ColumnType.OTHER;
        }
    }
}
