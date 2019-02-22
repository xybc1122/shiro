package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.model.BasePublicModel.BasicPublicExchangeRate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicPublicExchangeRateMapper {


    //查询汇率信息
    @Select("SELECT\n" +
            "r.exchange_rate_id,r.exchange_rate_number, c.currency_name,\n" +
            "r.`to_rmb`,r.`to_usd`,r.`effective_date`,r.`remark`,\n" +
            "r.`status`,r.`create_date`,r.`create_id_user`,r.`modify_date`,\n" +
            "r.`modify_id_user`,r.`audit_date`,r.`audit_id_user`\n" +
            "FROM `basic_public_exchange_rate` AS r\n" +
            "LEFT JOIN `basic_public_currency` AS c ON c.currency_id=r.`currency_id`\n")
    List<BasicPublicExchangeRate> getRateInfo();
}
