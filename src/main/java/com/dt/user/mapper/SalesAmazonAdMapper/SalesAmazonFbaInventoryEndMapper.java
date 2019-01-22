package com.dt.user.mapper.SalesAmazonAdMapper;

import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaInventoryEnd;
import com.dt.user.provider.SalesAmazonFbaInventoryEndProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesAmazonFbaInventoryEndMapper {

    /**
     * 存入接收库存数据
     *
     * @return
     */
    @InsertProvider(type = SalesAmazonFbaInventoryEndProvider.class, method = "addEndList")
    int AddSalesAmazonAdInventoryEndList(@Param("endList") List<SalesAmazonFbaInventoryEnd> endList);


}
