package com.dt.user.mapper.BasePublicMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BasicPublicAmazonTypeMapper {
    /**
     * 查询type中文名称
     * @param site_id
     * @param type
     * @return
     */
    @Select("SELECT `order_type_name`\n" +
            "FROM `basic_sales_amazon_type`\n" +
            "WHERE site_id =#{site_id} AND order_type=#{type}\n")
    String getTypeName(@Param("site_id") Integer site_id, @Param("type") String type);
}
