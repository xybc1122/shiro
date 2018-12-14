package com.dt.user.utils;

public class StrUtils {
    /**
     * 替换字符串
     * s
     *
     * @param number
     * @return
     */
    public static Double replaceDouble(String number) {
        if (org.apache.commons.lang3.StringUtils.isBlank(number)) {
            return null;
        }
        int i = number.indexOf(".");
        int j = number.indexOf(",");
        //如果都有有
        if (i != -1 && j != -1) {
            Double.parseDouble(number.replace(".", "").replace(',', '.'));
        }
        return Double.parseDouble(number.replace(',', '.'));
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
