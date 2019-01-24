package com.dt.user.utils;

import java.security.MessageDigest;
/**
 * MD5加盐
 * @author Administrator
 *
 */
public class MD5Util {


    /**
     * md5常用工具类
     *
     * @param data
     * @return
     */
    public static String MD5(String data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] array = md5.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}  
