package com.dt.user.mapper.SalesAmazonAdMapper;

import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaBusinessreport;
import com.dt.user.provider.SalesAmazonFbaBusinessreporProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesAmazonFbaBusinessreportMapper {


    /**
     * 存业务数据
     *
     * @return
     */
    @InsertProvider(type = SalesAmazonFbaBusinessreporProvider.class, method = "addAmazonAdBus")
    int AddSalesAmazonAdHlList(@Param("busList") List<SalesAmazonFbaBusinessreport> busList);
}
