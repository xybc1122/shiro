package com.dt.user.utils;

public class StringUtils {
    /**
     * 替换字符串
     *
     * @param number
     * @return
     */
    public static Double replaceString(String number) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(number)) {
            return null;
        }
        return Double.parseDouble(number.replace(".", "").replace(',', '.'));
    }
}
