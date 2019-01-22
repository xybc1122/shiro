package com.dt.user.mapper.SalesAmazonAdMapper;

import com.dt.user.model.SalesAmazonAd.SalesAmazonAdCpr;
import com.dt.user.provider.SalesAmazonAdCprProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesAmazonAdCprMapper {
    /**
     * 存入广告Cpr数据
     * @param cprList
     * @return
     */
    @InsertProvider(type = SalesAmazonAdCprProvider.class, method = "addAmazonAdCpr")
    int AddSalesAmazonAdCprList(@Param("cprList") List<SalesAmazonAdCpr> cprList);
}
