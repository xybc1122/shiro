package com.dt.user.mapper;

import com.dt.user.model.BasicPublicSite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface BasicPublicSiteMapper {
    /**
     * 查询所有站点信息
     * @return
     */
    @Select("SELECT\n" +
            "  s.`site_id`,\n" +
            "  s.`site_number`,\n" +
            "  s.`site_name`,\n" +
            "  s.`site_eng`,\n" +
            "  s.`url`,\n" +
            "  s.`vat`,\n" +
            "  s.`principal`,\n" +
            "  s.`remark`,\n" +
            "  s.`status`,\n" +
            "  s.`create_date`,\n" +
            "  s.`create_id_user`,\n" +
            "  s.`modify_date`,\n" +
            "  s.`modify_id_user`,\n" +
            "  s.`audit_date`,\n" +
            "  s.`audit_id_user`,\n" +
            "  c.`currency_name`,\n" +
            "  c.`currency_eng_short`,\n" +
            "  a.`area_name`\n" +
            "FROM `basic_public_site` AS s\n" +
            "INNER JOIN `basic_public_currency` AS c ON c.`currency_id`=s.`currency_id`\n" +
            "INNER JOIN `basic_public_area` AS a ON a.`area_id`=s.`area_id`\n")
    List<BasicPublicSite> findBySiteList();


}
