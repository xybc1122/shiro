package com.dt.user.service.BasePublicService;

import com.dt.user.model.BasePublicModel.BasicSalesAmazonCsvTxtXslHeader;

import java.util.List;

public interface BasicSalesAmazonCsvTxtXslHeaderService {

    /**
     * 通过seId获取 header信息
     *
     * @param seId
     * @return
     */
    List<String> headerList(Long seId, Integer tbId, Integer areaId,Long shopId);


    /**
     * 获得对象
     *
     * @param seId
     * @param tbId
     * @param areaId
     * @return
     */
    List<BasicSalesAmazonCsvTxtXslHeader> sqlHead(Long seId, Integer tbId, Integer areaId, Long shopId);
}
