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
    @Select("SELECT\n" +
            "  `currency_id`,\n" +
            "  `currency_number`,\n" +
            "  `currency_name`,\n" +
            "  `currency_eng_short`,\n" +
            "  `remark`,\n" +
            "  `status`,\n" +
            "  `create_date`,\n" +
            "  `create_id_user`,\n" +
            "  `modify_date`,\n" +
            "  `modify_id_user`,\n" +
            "  `audit_date`,\n" +
            "  `audit_id_user`\n" +
            "FROM `basic_public_currency`")
    List<BasicPublicCurrency> findByListCurrency();
}
