package com.owl.api.reflect;

import com.owl.api.annotation.DataType;
import com.owl.api.annotation.KeyValue;
import com.owl.api.exception.InvalidTypeException;

import java.lang.reflect.Type;
import java.util.List;

public class ParamTypeAnalyzer {

    public static DataType analyze(Type type) {
        if(type == KeyValue.class) {
            return DataType.KV;
        } else if(type == String.class) {
            return DataType.STRING;
        } else if(type == Integer.class) {
            return DataType.INT;
        } else if(type == int.class) {
            return DataType.INT;
        } else if(type == Long.class) {
            return DataType.LONG;
        } else if(type == long.class) {
            return DataType.LONG;
        } else if(type == Float.class) {
            return DataType.FLOAT;
        }  else if(type == float.class) {
            return DataType.FLOAT;
        } else if(type == Double.class) {
            return DataType.DOUBLE;
        } else if(type == double.class) {
            return DataType.DOUBLE;
        } else if(type == Boolean.class) {
            return DataType.BOOL;
        } else if(type == boolean.class) {
            return DataType.BOOL;
        } else if(type == List.class) {
            return DataType.LIST;
        }
        throw new InvalidTypeException("unsupported data type: " + type.getClass().getName());
    }
}
