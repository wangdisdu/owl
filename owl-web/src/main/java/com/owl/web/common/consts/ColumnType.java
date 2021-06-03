package com.owl.web.common.consts;

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
}
