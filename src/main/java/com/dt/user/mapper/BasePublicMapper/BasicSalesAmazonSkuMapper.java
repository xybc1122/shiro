package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.model.BasePublicModel.BasicSalesAmazonSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BasicSalesAmazonSkuMapper {
    /**
     * 通过店铺ID  站点ID  skuName 查找 skuId
     * @param sId
     * @param siteId
     * @param skuName
     * @return
     */
    @Select("SELECT `sku_id`\n" +
            "FROM `basic_sales_amazon_sku`\n" +
            "WHERE shop_id=#{sId} AND site_id=#{siteId} AND sku=#{skuName}")
    BasicSalesAmazonSku getSkuId(@Param("sId") Long sId, @Param("siteId") Long siteId, @Param("skuName") String skuName);
}
