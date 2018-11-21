package com.dt.user.mapper;

import com.dt.user.model.BasicPublicCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicPublicCompanyMapper {
    /**
     * 查询公司所有相关信息
     * @return
     */
    @Select("SELECT\n" +
            "  `company_id`,\n" +
            "  `company_number`,\n" +
            "  `company_full_name`,\n" +
            "  `company_short_name`,\n" +
            "  `credit_code`,\n" +
            "  `bank_of_deposit`,\n" +
            "  `bank_account`,\n" +
            "  `account_type`,\n" +
            "  `address`,\n" +
            "  `tel_phone`,\n" +
            "  `remark`,\n" +
            "  `status`,\n" +
            "  `create_date`,\n" +
            "  `create_id_user`,\n" +
            "  `modify_date`,\n" +
            "  `modify_id_user`,\n" +
            "  `audit_date`,\n" +
            "  `audit_id_user`\n" +
            "FROM `basic_public_company`\n")
    List<BasicPublicCompany> findByListCompany();
}
