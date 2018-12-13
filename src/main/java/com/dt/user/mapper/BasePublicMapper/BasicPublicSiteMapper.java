package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.model.BasePublicModel.BasicPublicSite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicPublicSiteMapper {
    /**
     * 查询所有站点信息
     *
     * @return
     */
    @Select("SELECT s.`site_id`,s.`site_number`,s.`site_name`,s.`site_eng`,s.`url`,\n" +
            "  s.`vat`,s.`principal`,s.`remark`,s.`status`,s.`create_date`,s.`create_id_user`,\n" +
            "  s.`modify_date`,s.`modify_id_user`,s.`audit_date`,s.`audit_id_user`,\n" +
            "  c.`currency_name`,c.`currency_eng_short`,a.`area_name`\n" +
            "FROM `basic_sales_amazon_site` AS s\n" +
            "INNER JOIN `basic_public_currency` AS c ON c.`currency_id`=s.`currency_id`\n" +
            "INNER JOIN `basic_public_area` AS a ON a.`area_id`=s.`area_id`\n")
    List<BasicPublicSite> findBySiteList();


    /**
     * 通过店铺id查询站点信息
     */
    @Select("SELECT\n" +
            "  se.`site_id`,\n" +
            "  se.`site_name`\n" +
            "FROM `basic_sales_amazon_site` AS se\n" +
            "LEFT JOIN `basic_sales_amazon_shop_site` AS ss ON ss.`site_id`=se.`site_id`\n" +
            "LEFT JOIN `basic_sales_amazon_shop` AS s ON s.`shop_id`=ss.`shop_id`\n" +
            "WHERE s.`shop_id`=#{sId}")
    List<BasicPublicSite> getShopIdTakeSiteList(@Param("sId") Long sId);


}
