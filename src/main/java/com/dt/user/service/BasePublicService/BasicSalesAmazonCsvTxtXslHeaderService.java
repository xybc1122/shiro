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
    List<String> headerList(Integer seId, Integer tbId, Integer areaId,Integer shopId);

    /**
     * 获得对象
     *
     * @param seId
     * @param tbId
     * @param areaId
     * @return
     */
    List<BasicSalesAmazonCsvTxtXslHeader> sqlHead(Integer seId, Integer tbId, Integer areaId, Integer shopId);
}
