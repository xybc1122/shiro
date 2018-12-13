package com.dt.user.mapper.BasePublicMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicSalesAmazonCsvTxtXslHeaderMapper {
    /**
     * 通过seId获取 header信息
     * @param seId
     * @return
     */
    @Select("SELECT\n" +
            "  `import_templet`\n" +
            "FROM `basic_sales_amazon_csv_txt_xsl_header`\n" +
            "WHERE site_id=#{seId}\n")
    List<String> headerList(@Param("seId") Long seId);
}
