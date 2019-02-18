package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.model.BasePublicModel.BasicSalesAmazonCsvTxtXslHeader;
import com.dt.user.provider.BasicSalesAmazonCsvTxtXslHeaderProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface BasicSalesAmazonCsvTxtXslHeaderMapper {
    /**
     * 通过seId获取 header信息
     *
     * @param seId
     * @return
     */
    @SelectProvider(type = BasicSalesAmazonCsvTxtXslHeaderProvider.class, method = "findHeadInfo")
    List<String> headerList(@Param("seId") Long seId, @Param("tbId") Integer tbId, @Param("areaId") Integer areaId, @Param("shopId") Long shopId);

    /**
     * 获得对象
     *
     * @param seId
     * @param tbId
     * @param areaId
     * @return
     */
    @SelectProvider(type = BasicSalesAmazonCsvTxtXslHeaderProvider.class, method = "getHead")
    List<BasicSalesAmazonCsvTxtXslHeader> sqlHead(@Param("seId") Long seId, @Param("tbId") Integer tbId,
                                                  @Param("areaId") Integer areaId, @Param("shopId") Long shopId);
}
