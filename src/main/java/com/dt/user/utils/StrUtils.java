package com.dt.user.utils;

public class StrUtils {
    /**
     * 替换字符串
     *
     * @param number
     * @return
     */
    public static Double replaceDouble(String number) {
        if (org.apache.commons.lang3.StringUtils.isBlank(number)) {
            return null;
        }
        return Double.parseDouble(number.replace(".", "").replace(',', '.'));
    }

    public static String replaceString(String number) {
        if (org.apache.commons.lang3.StringUtils.isBlank(number)) {
            return null;
        }
        return number;
    }

    public static Long replaceLong(String number) {
        if (org.apache.commons.lang3.StringUtils.isBlank(number)) {
            return null;
        }
        return Long.parseLong(number);
    }
}
