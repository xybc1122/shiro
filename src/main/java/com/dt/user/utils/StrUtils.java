package com.dt.user.utils;

import org.apache.commons.lang3.StringUtils;

public class StrUtils {
    /**
     * 字符串替换Double
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
        //如果都有 并且 j > i 等于德国的
        if (i != -1 && j != -1 && j > i) {
            String newNumber = number.replace(".", "").replace(',', '.');
            return Double.parseDouble(newNumber);
        }
        //如果都有 并且 j > i 等于加拿大的
        if (i != -1 && j != -1 && j < i) {
            String newNumber = number.replace(",", "");
            return Double.parseDouble(newNumber);
        }
        return Double.parseDouble(number.replace(',', '.'));
    }

    /**
     * 返回字符串
     *
     * @param number
     * @return
     */
    public static String replaceString(String number) {
        if (org.apache.commons.lang3.StringUtils.isBlank(number)) {
            return null;
        }
        int i = number.indexOf("'");
        if (i == -1) {
            return number;
        }
        return number.replace("'", "");
    }

    /**
     * 返回Long 类型
     *
     * @param number
     * @return
     */
    public static Long replaceLong(String number) {
        if (org.apache.commons.lang3.StringUtils.isBlank(number)) {
            return null;
        }
        return Long.parseLong(number);
    }

    /**
     * 封装Append
     */
    public static StringBuilder appBuider(StringBuilder sb, String str) {
        if (StringUtils.isEmpty(str)) {
            sb.append(str);
        } else {
            sb.append("'" + str + "'");
        }
        return sb;
    }

    public static void main(String[] args) {
        String b = "-12.8624,00";
        String a = "-22,224.88";
        System.out.println(StrUtils.replaceDouble(a));
    }
}
