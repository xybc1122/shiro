package com.dt.user.provider;

import com.dt.user.dto.ExchangeRateDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.LinkedList;

public class BasicPublicExchangeRateProvider {

    public String findRate(ExchangeRateDto rateDto) {
        return new SQL() {{
            SELECT("r.exchange_rate_id, c.currency_name,\n" +
                    "r.`to_rmb`,r.`to_usd`,r.`effective_date`,r.status_id\n" +
                    "FROM `basic_public_exchange_rate` AS r");
            LEFT_OUTER_JOIN("`basic_public_currency` AS c ON c.currency_id=r.`currency_id`");
            if (rateDto.getSystemLogStatus() != null) {
                LEFT_OUTER_JOIN("`system_log_status` AS ls ON ls.status_id=r.`status_id`");
                //备注
                if (StringUtils.isNotBlank(rateDto.getSystemLogStatus().getRemark())) {
                    WHERE("ls.remark=#{systemLogStatus.remark}");
                }
                //状态
                if (rateDto.getSystemLogStatus().getStatus() != null) {
                    WHERE("ls.status=#{systemLogStatus.status}");
                }
                //创建时间
                if (rateDto.getSystemLogStatus().getCreateDate() != null) {
                    WHERE("ls.create_date=#{systemLogStatus.createDate}");
                }
                //创建人
                if (rateDto.getSystemLogStatus().getCreateUser() != null) {
                    WHERE("ls.create_user=#{systemLogStatus.createUser}");
                }
                //修改日期
                if (rateDto.getSystemLogStatus().getModifyDate() != null) {
                    WHERE("ls.modify_date=#{systemLogStatus.modifyDate}");
                }
                //修改人
                if (rateDto.getSystemLogStatus().getModifyUser() != null) {
                    WHERE("ls.modify_user=#{systemLogStatus.modifyUser}");
                }
                //审核时间
                if (rateDto.getSystemLogStatus().getAuditDate() != null) {
                    WHERE("ls.audit_date=#{systemLogStatus.auditDate}");
                }
                //审核人
                if (rateDto.getSystemLogStatus().getAuditUser() != null) {
                    WHERE("ls.audit_user=#{systemLogStatus.auditUser}");
                }
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
            if (rateDto.getToUsd() != null) {
                WHERE("r.to_usd=#{toUsd}");
            }
            //有效日期
            if (rateDto.getEffectiveDate() != null) {
                WHERE("r.effective_date=#{effectiveDate}");
            }
        }}.toString();
    }

    public void s(ExchangeRateDto rateDto) {
        SQL s = new SQL();


    }
}
