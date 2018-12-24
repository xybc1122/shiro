package com.dt.user.mapper;

import com.dt.user.model.SalesAmazonAdCpr;
import com.dt.user.provider.SalesAmazonAdCprProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesAmazonAdCprMapper {
    /**
     * 存入广告数据
     * @param cprList
     * @return
     */
    @InsertProvider(type = SalesAmazonAdCprProvider.class, method = "addAmazonAdCpr")
    int AddSalesAmazonAdCprList(@Param("cprList") List<SalesAmazonAdCpr> cprList);
}
