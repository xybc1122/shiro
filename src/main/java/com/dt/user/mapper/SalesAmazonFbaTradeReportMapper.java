package com.dt.user.mapper;

import com.dt.user.model.SalesAmazonFbaTradeReport;
import com.dt.user.provider.SalesAmazonFbaTradeReportProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单报告表
 */
@Mapper
public interface SalesAmazonFbaTradeReportMapper {


    /**
     * 存入订单数据 txt
     *
     * @return
     */
    @InsertProvider(type = SalesAmazonFbaTradeReportProvider.class, method = "addInfo")
    int AddSalesAmazonAdTrdList(@Param("trdList") List<SalesAmazonFbaTradeReport> trdList);

}
