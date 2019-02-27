package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.dto.CompanyDto;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface BasicPublicCompanyMapper {
    /**
     * 查询公司所有相关信息
     */
    @Select("SELECT `company_id`,`number`,`company_name`,`company_name_eng`,`company_short_name`,\n" +
            "`company_short_name_eng`,`credit_code`,company_short_code,`bank_of_deposit`,`bank_account`,`account_type`,`company_address`,`company_address_eng`,\n" +
            "`tel_phone`,status_id\n" +
            "FROM `basic_public_company`")
    @Results({
            @Result(column = "status_id", property = "systemLogStatus",
                    one = @One(
                            select = "com.dt.user.mapper.SystemLogStatusMapper.findSysStatusInfo",
                            fetchType = FetchType.EAGER
                    )
            )
    })
    List<CompanyDto> findByListCompany();
}
