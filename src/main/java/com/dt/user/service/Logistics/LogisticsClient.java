package com.dt.user.service.Logistics;

import com.alibaba.fastjson.JSONObject;
import com.dt.user.utils.HttpClientUtils;
import com.dt.user.utils.MD5Util;

import java.util.Date;

/**
 * 物流服务客户端
 */

public class LogisticsClient {
    /**
     * 封装请求数据
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    public static String packageData(int currentPage, int pageSize, Long dateTime, String appKey, String token, String md5Key) {
        JSONObject c = new JSONObject();
        c.put("currentPage", currentPage);
        c.put("pageSize", pageSize);
        c.put("dateTime", dateTime);
        c.put("appKey", appKey);
        c.put("token", token);
        c.put("md5Key", md5Key);
        return c.toJSONString();
    }

    /**
     * Post请求接口信息
     *
     * @param url
     * @param data
     * @param timeOut
     * @return
     */
    public static String postData(String url, String data, int timeOut) {
        String result = HttpClientUtils.doPost(url, data, timeOut);
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
       while (true){
           Thread.sleep(2000L);
           String url="http://127.0.0.1:9002/api/v1/wayList";
           String appKye = "tt";
           int currentPage = 0;
           int pageSize = 5;
           String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aWRlb1Rva2VuIiwiYXBwS2V5IjoidHQiLCJpYXQiOjE1NDgzMDY3MDh9.Wio7r0L-YUELZEFmEx-SbPkzlg2NYXZMmmC6vVlqUwU";
           System.out.println(token);
           Long time = new Date().getTime();
           String md = appKye + time+currentPage + pageSize;
           System.out.println(MD5Util.MD5(md));
           String data = LogisticsClient.packageData(currentPage, pageSize, time, appKye, token, MD5Util.MD5(md));
           System.out.println(LogisticsClient.postData(url,data,5000));
       }
    }

}
