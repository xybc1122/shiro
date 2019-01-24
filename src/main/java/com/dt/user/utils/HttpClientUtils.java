package com.dt.user.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 封装 http get post
 */
public class HttpClientUtils {


    /**
     * 封装超时时间
     *
     * @return
     */
    public static RequestConfig httpTime(int timeOut) {
        return RequestConfig.custom().setConnectTimeout(timeOut)  //setConnectTimeout 设置建立连接超时  5秒
                .setConnectionRequestTimeout(timeOut)  //setConnectionRequestTimeout设置 请求超时时间 5秒
                .setSocketTimeout(timeOut) //setSocketTimeout socket连接超时 5秒
                .setRedirectsEnabled(true)//setRedirectsEnabled 允许重定向
                .build();//通过build去构建;
    }

    /**
     * 封装post
     *
     * @return
     */
    public static String doPost(String url, String data, int timeout) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //设置url请求
        HttpPost httpPost = new HttpPost(url);
        //超时设置
        httpPost.setConfig(httpTime(timeout));
        httpPost.setHeader("Content-type", "application/json");
        if (data != null && data instanceof String) { //使用字符串传参
            StringEntity stringEntity = new StringEntity(data, "UTF-8");
            httpPost.setEntity(stringEntity);
        }
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(httpEntity);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
