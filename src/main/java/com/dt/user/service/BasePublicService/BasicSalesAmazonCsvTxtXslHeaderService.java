package com.dt.user.service.BasePublicService;

import java.util.List;

public interface BasicSalesAmazonCsvTxtXslHeaderService {

    /**
     * 通过seId获取 header信息
     *
     * @param seId
     * @return
     */
    List<String> headerList(Long seId, Integer tbId);
}
