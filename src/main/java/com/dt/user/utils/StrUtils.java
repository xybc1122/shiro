package com.dt.user.utils;

public class StrUtils {
    /**
     * 字符串替换Double
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

    /**
     * 返回字符串
     * @param number
     * @return
     */
    public static String replaceString(String number) {
        if (org.apache.commons.lang3.StringUtils.isBlank(number)) {
            return null;
        }
        return number;
    }

    /**
     * 返回Long 类型
     * @param number
     * @return
     */
    public static Long replaceLong(String number) {
        if (org.apache.commons.lang3.StringUtils.isBlank(number)) {
            return null;
        }
        return Long.parseLong(number);
    }
}
