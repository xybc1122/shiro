package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.model.BasePublicModel.BasicPublicCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicPublicCompanyMapper {
    /**
     * 查询公司所有相关信息
     * @return
     */
    @Select("SELECT `company_id`,`company_number`,`company_full_name`,`company_short_name`,\n" +
            "`credit_code`,`bank_of_deposit`,`bank_account`,`account_type`,`address`,\n" +
            "`tel_phone`,`remark`,`status`,`create_date`,`create_id_user`,\n" +
            "`modify_date`,`modify_id_user`,`audit_date`,`audit_id_user`\n" +
            "FROM `basic_public_company`\n")
    List<BasicPublicCompany> findByListCompany();
}
