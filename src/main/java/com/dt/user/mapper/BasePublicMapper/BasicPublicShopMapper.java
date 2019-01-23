package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.model.BasePublicModel.BasicPublicShop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicPublicShopMapper {

    /**
     * 查询店铺所有相关信息
     * @return
     */
    @Select("SELECT s.`shop_id`,s.`shop_number`,s.`shop_name`,s.`shop_eng`,s.`principal`,s.`remark`,\n" +
            "s.`status`,s.`create_date`,s.`create_id_user`,s.`modify_date`,s.`modify_id_user`,\n" +
            "s.`audit_date`,s.`audit_id_user`,c.company_full_name,p.platform_type_name\n" +
            "FROM `basic_public_shop`AS s\n" +
            "LEFT JOIN `basic_public_company`AS c ON c.`company_id`=s.`company_id`" +
            "LEFT JOIN `basic_public_platform_type`AS p ON p.`platform_type_id`=s.`platform_type_id`")
    List<BasicPublicShop> findByListShop();
}
