package com.dt.user.mapper;

import com.dt.user.model.BasicPublicShop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicPublicShopMapper {

    /**
     * 查询店铺所有相关信息
     * @return
     */
    @Select("SELECT\n" +
            "  s.`shop_id`,\n" +
            "  s.`shop_number`,\n" +
            "  s.`shop_name`,\n" +
            "  s.`shop_eng`,\n" +
            "  s.`principal`,\n" +
            "  s.`remark`,\n" +
            "  s.`status`,\n" +
            "  s.`create_date`,\n" +
            "  s.`create_id_user`,\n" +
            "  s.`modify_date`,\n" +
            "  s.`modify_id_user`,\n" +
            "  s.`audit_date`,\n" +
            "  s.`audit_id_user`,\n" +
            "  c.company_full_name\n" +
            "FROM `basic_public_shop`AS s\n" +
            "INNER JOIN `basic_public_company`AS c ON c.`company_id`=s.`company_id`")
    List<BasicPublicShop> findByListShop();
}
