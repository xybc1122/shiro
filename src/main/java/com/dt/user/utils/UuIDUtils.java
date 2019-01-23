package com.dt.user.utils;

import java.util.UUID;

public class UuIDUtils {
    /**
     * 获得随即uuid
     * @param fileName
     * @return
     */
    public static String uuId(String fileName) {
        int c = fileName.indexOf(".");
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid + fileName.substring(c);
    }
}
