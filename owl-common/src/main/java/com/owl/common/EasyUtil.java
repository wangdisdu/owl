package com.owl.common;

import java.math.BigDecimal;

public class EasyUtil {

    /**
     * 0-9, a-b, A-B
     */
    public static boolean isLetterOrDigit(char v) {
        return Character.isDigit(v) || Character.isLowerCase(v) || Character.isUpperCase(v);
    }

    /**
     * human readable
     */
    public static String formatDouble(Double value) {
        return new BigDecimal(value.toString()).stripTrailingZeros().toPlainString();
    }

    /**
     * human readable
     */
    public static String formatFloat(Float value) {
        return new BigDecimal(value.toString()).stripTrailingZeros().toPlainString();
    }
}
