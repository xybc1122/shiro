package com.dt.user.mapper;

import com.dt.user.model.BasicPublicArea;
import com.dt.user.model.BasicPublicCurrency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicPublicCurrencyMapper {
    /**
     * 查询币别所有相关信息
     * @return
     */
    @Select("SELECT `currency_id`,`currency_number`,`currency_name`,`currency_eng_short`,\n" +
            "`remark`,`status`,`create_date`,`create_id_user`,`modify_date`,\n" +
            "`modify_id_user`,`audit_date`,`audit_id_user`\n" +
            "FROM `basic_public_currency`")
    List<BasicPublicCurrency> findByListCurrency();
}
