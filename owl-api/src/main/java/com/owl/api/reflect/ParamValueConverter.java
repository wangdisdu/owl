package com.owl.api.reflect;

import com.owl.api.annotation.DataType;
import com.owl.api.exception.InvalidTypeException;

public class ParamValueConverter {
    private ITypeConverter converter;

    public static Object convert(DataType dataType, Object dataValue) {
        return new ParamValueConverter(dataType).convert(dataValue);
    }

    public Object convert(Object dataValue) {
        if (dataValue == null) {
            return null;
        }
        try {
            return converter.convert(dataValue);
        } catch (Exception e) {
            throw new InvalidTypeException("convert data type failed", e);
        }
    }

    public static String convertString(Object value) {
        return (String) convert(DataType.STRING, value);
    }

    public ParamValueConverter(DataType dataType) {
        switch (dataType) {
            case STRING:
                converter = new StringConverter();
                break;
            case INT:
                converter = new IntConverter();
                break;
            case LONG:
                converter = new LongConverter();
                break;
            case FLOAT:
                converter = new FloatConverter();
                break;
            case DOUBLE:
                converter = new DoubleConverter();
                break;
            case BOOL:
                converter = new BooleanConverter();
                break;
            default:
                throw new InvalidTypeException("unsupported data type: " + dataType.name());
        }
    }

    interface ITypeConverter<T> {
        T convert(Object value) throws InvalidTypeException;
    }

    static class StringConverter implements ITypeConverter<String> {
        @Override
        public String convert(Object value) {
            if (value instanceof String) {
                return (String) value;
            }
            return String.valueOf(value);
        }
    }

    static class IntConverter implements ITypeConverter<Integer> {
        @Override
        public Integer convert(Object value) throws InvalidTypeException {
            if (value instanceof Integer) {
                return (Integer) value;
            } else if (value instanceof Long) {
                return ((Long) value).intValue();
            }
            try {
                return Integer.parseInt(String.valueOf(value));
            } catch (Exception ignore) {
                // do nothing
            }
            throw new InvalidTypeException("invalid data type: expect Integer, actual " + value.getClass().getName());
        }
    }

    static class LongConverter implements ITypeConverter<Long> {
        @Override
        public Long convert(Object value) throws InvalidTypeException {
            if (value instanceof Integer) {
                return ((Integer) value).longValue();
            } else if (value instanceof Long) {
                return (Long) value;
            }
            try {
                return Long.parseLong(String.valueOf(value));
            } catch (Exception ignore) {
                // do nothing
            }
            throw new InvalidTypeException("invalid data type: expect Long, actual " + value.getClass().getName());
        }
    }

    static class FloatConverter implements ITypeConverter<Float> {
        @Override
        public Float convert(Object value) throws InvalidTypeException {
            if (value instanceof Integer) {
                return ((Integer) value).floatValue();
            } else if (value instanceof Long) {
                return ((Long) value).floatValue();
            } else if (value instanceof Float) {
                return (Float) value;
            } else if (value instanceof Double) {
                return ((Double) value).floatValue();
            }
            try {
                return Float.parseFloat(String.valueOf(value));
            } catch (Exception ignore) {
                // do nothing
            }
            throw new InvalidTypeException("invalid data type: expect Float, actual " + value.getClass().getName());
        }
    }

    static class DoubleConverter implements ITypeConverter<Double> {
        @Override
        public Double convert(Object value) throws InvalidTypeException {
            if (value instanceof Integer) {
                return ((Integer) value).doubleValue();
            } else if (value instanceof Long) {
                return ((Long) value).doubleValue();
            } else if (value instanceof Float) {
                return ((Float) value).doubleValue();
            } else if (value instanceof Double) {
                return (Double) value;
            }
            try {
                return Double.parseDouble(String.valueOf(value));
            } catch (Exception ignore) {
                // do nothing
            }
            throw new InvalidTypeException("invalid data type: expect Double, actual " + value.getClass().getName());
        }
    }

    static class BooleanConverter implements ITypeConverter<Boolean> {
        @Override
        public Boolean convert(Object value) throws InvalidTypeException {
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            if ("true".equalsIgnoreCase(String.valueOf(value))
                    || "yes".equalsIgnoreCase(String.valueOf(value))
                    || "y".equalsIgnoreCase(String.valueOf(value))
                    || "1".equalsIgnoreCase(String.valueOf(value))) {
                return true;
            }
            if ("false".equalsIgnoreCase(String.valueOf(value))
                    || "no".equalsIgnoreCase(String.valueOf(value))
                    || "n".equalsIgnoreCase(String.valueOf(value))
                    || "0".equalsIgnoreCase(String.valueOf(value))) {
                return false;
            }
            throw new InvalidTypeException("invalid data type: expect Boolean, actual " + value.getClass().getName());
        }
    }
}
