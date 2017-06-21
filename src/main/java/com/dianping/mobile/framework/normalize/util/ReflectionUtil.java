package com.dianping.mobile.framework.normalize.util;

import com.dianping.mobile.framework.exception.ClientErrorException;
import org.apache.commons.lang.StringUtils;

public final class ReflectionUtil {

    private static final String STRING_TRUE = "true";
    private static final String STRING_FALSE = "false";
    private static final String INT_TRUE = "1";
    private static final String INT_FALSE = "0";

    private ReflectionUtil() {
    }

    public static Integer parseInt(String value) {
        try {
            value = value.replaceAll("　", "");
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ClientErrorException("parseInt but input illegal input=" + value, e);
        }
    }

    public static Double parseDouble(String value) {
        try {
            value = value.replaceAll("　", "");
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ClientErrorException("parseDouble but input illegal input=" + value, e);
        }
    }

    public static Float parseFloat(String value) {
        try {
            value = value.replaceAll("　", "");
            return Float.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ClientErrorException("parseFloat but input illegal input=" + value, e);
        }
    }

    public static Short parseShort(String value) {
        try {
            value = value.replaceAll("　", "");
            return Short.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ClientErrorException("parseShort but input illegal input=" + value, e);
        }
    }

    public static Long parseLong(String value) {
        try {
            value = value.replaceAll("　", "");
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ClientErrorException("parseLong but input illegal input=" + value, e);
        }
    }

    public static Byte parseByte(String value) {
        try {
            value = value.replaceAll("　", "");
            return Byte.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ClientErrorException("parseByte but input illegal input=" + value, e);
        }
    }

    public static Boolean parseBoolean(String value) {
        value = value.replaceAll("　", "");
        if (INT_TRUE.equals(value)) {
            return Boolean.TRUE;
        } else if (INT_FALSE.equals(value)) {
            return Boolean.FALSE;
        } else if (STRING_TRUE.equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        } else if (STRING_FALSE.equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        } else {
            throw new ClientErrorException("parseBoolean but input illegal input=" + value);
        }
    }

    public static Object parseValue(Class<?> type, String value) {
        if (StringUtils.isBlank(value))
            return null;
        value = value.trim();
        if (String.class.equals(type)) {
            return value;
        } else if (Integer.class.equals(type)) {
            return parseInt(value);
        } else if (Long.class.equals(type)) {
            return parseLong(value);
        } else if (Boolean.class.equals(type)) {
            return parseBoolean(value);
        } else if (Short.class.equals(type)) {
            return parseShort(value);
        } else if (Double.class.equals(type)) {
            return parseDouble(value);
        } else if (Float.class.equals(type)) {
            return parseFloat(value);
        } else if (Byte.class.equals(type)) {
            return parseByte(value);
        } else {
            throw new RuntimeException("request illeagal type, type must be Integer not int Long not long etc, type=" + type);
        }
    }

}
