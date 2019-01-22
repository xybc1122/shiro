package com.dt.user.mapper.SalesAmazonAdMapper;

import com.dt.user.model.SalesAmazonAd.SalesAmazonAdOar;
import com.dt.user.provider.SalesAmazonAdOarProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesAmazonAdOarMapper {
    /**
     * 存入广告Oar数据
     *
     * @return
     */
    @InsertProvider(type = SalesAmazonAdOarProvider.class, method = "addAmazonAdOar")
    int AddSalesAmazonAdOarList(@Param("oarList") List<SalesAmazonAdOar> oarList);

}
