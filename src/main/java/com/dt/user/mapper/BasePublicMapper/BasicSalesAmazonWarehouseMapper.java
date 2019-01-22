package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.model.BasePublicModel.BasicSalesAmazonWarehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BasicSalesAmazonWarehouseMapper {

    /**
     * 通过fc 查找站点ID 仓库id
     *
     * @return
     */
    @Select("SELECT\n" +
            "amazon_warehouse_id,`site_id`\n" +
            "FROM `basic_sales_amazon_warehouse`\n" +
            "WHERE warehouse_code=#{fc}\n" +
            "LIMIT 0 ,1\n")
    BasicSalesAmazonWarehouse getWarehouse(@Param("fc") String fc);

}
