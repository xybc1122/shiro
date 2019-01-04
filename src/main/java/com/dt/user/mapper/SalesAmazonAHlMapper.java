package com.dt.user.mapper;

import com.dt.user.model.SalesAmazonAdHl;
import com.dt.user.model.SalesAmazonAdOar;
import com.dt.user.provider.SalesAmazonAdHlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesAmazonAHlMapper {
    /**
     * 存入广告Hl数据
     *
     * @return
     */
    @InsertProvider(type = SalesAmazonAdHlProvider.class, method = "addAmazonAdHl")
    int AddSalesAmazonAdHlList(@Param("hlList") List<SalesAmazonAdHl> hlList);

}
