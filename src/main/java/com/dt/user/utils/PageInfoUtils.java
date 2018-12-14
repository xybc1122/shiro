package com.dt.user.utils;

import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.Map;

public class PageInfoUtils {
    /**
     * 封装分页
     * @param pageInfo
     * @param currentPage
     * @return
     */
    public static Map<String, Object> getPage(PageInfo pageInfo,Integer currentPage) {
        Map<String, Object> data = new HashMap<>();
        data.put("total_size", pageInfo.getTotal());//总条数
        data.put("total_page", pageInfo.getPages());//总页数
        data.put("current_page", currentPage);//当前页
        data.put("dataList", pageInfo.getList());//数据
        return data;
    }
}
