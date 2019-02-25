package com.dt.user.provider;

import com.dt.user.dto.ExchangeRateDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class BasicPublicExchangeRateProvider {

    public String findRate(ExchangeRateDto rateDto) {
        return new SQL() {{
            SELECT("r.exchange_rate_id,r.exchange_rate_number, c.currency_name,\n" +
                    "r.`to_rmb`,r.`to_usd`,r.`effective_date`,r.`remark`,\n" +
                    "r.`status`,r.`create_date`,r.`create_id_user`,r.`modify_date`,\n" +
                    "r.`modify_id_user`,r.`audit_date`,r.`audit_id_user`\n" +
                    "FROM `basic_public_exchange_rate` AS r");
            LEFT_OUTER_JOIN("`basic_public_currency` AS c ON c.currency_id=r.`currency_id`");
            //汇率编号
            if (rateDto.getExchangeRateNumber() != null) {
                WHERE("r.exchange_rate_number=#{exchangeRateNumber}");
            }
            //币别名称
            if (StringUtils.isNotBlank(rateDto.getCurrencyName())) {
                WHERE("c.currency_name=#{currencyName}");
            }
            //兑人民币汇率
            if (rateDto.getToRmb() != null) {
                WHERE("r.to_rmb=#{toRmb}");
            }
            //兑美元汇率
            if (rateDto.getToUsd()!=null) {
                WHERE("r.to_usd=#{toUsd}");
            }
            //有效日期
            if (rateDto.getEffectiveDate() != null) {
                WHERE("r.effective_date=#{effectiveDate}");
            }
            //备注
            if (StringUtils.isNotBlank(rateDto.getRemark())) {
                WHERE("r.remark=#{remark}");
            }
            //状态
            if (rateDto.getStatus() != null) {
                WHERE("r.status=#{status}");
            }
            //创建时间
            if (rateDto.getCreateDate() != null) {
                WHERE("r.create_date=#{createDate}");
            }
            //创建人
            if (rateDto.getCreateIdUser() != null) {
                WHERE("r.create_id_user=#{createIdUser}");
            }
            //修改日期
            if (rateDto.getModifyDate() != null) {
                WHERE("r.modify_date=#{modifyDate}");
            }
            //修改人
            if (rateDto.getModifyIdUser() != null) {
                WHERE("r.modify_id_user=#{modifyIdUser}");
            }
            //审核时间
            if (rateDto.getAuditDate() != null) {
                WHERE("r.audit_date=#{auditDate}");
            }
            //审核人
            if (rateDto.getAuditIdUser() != null) {
                WHERE("r.audit_id_user=#{auditIdUser}");
            }
        }}.toString();
    }


}
