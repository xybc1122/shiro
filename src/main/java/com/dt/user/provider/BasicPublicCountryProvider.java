package com.dt.user.provider;

import com.dt.user.dto.CountryDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class BasicPublicCountryProvider {


    public String findCountry(CountryDto countryDto) {
        return new SQL() {{
            SELECT("c.country_id,c.number,c.country_name,c.country_name_eng,c.status_id," +
                    "c.country_short_name_eng,c.vat,p.province_name," +
                    "pc.city_name,pcc.`county_name`,l.language_name FROM `basic_public_country` AS c");
            LEFT_OUTER_JOIN("`basic_public_province` AS p ON p.`country_id`=c.`country_id`");
            LEFT_OUTER_JOIN("`basic_public_province_city`AS pc ON pc.province_number=p.`province_number`");
            LEFT_OUTER_JOIN("`basic_public_province_city_county`AS pcc ON pcc.city_number=pc.`city_number`");
            LEFT_OUTER_JOIN("`basic_public_language`AS l ON l.language_id=c.`language_id`");
            if (countryDto.getSystemLogStatus() != null) {
                LEFT_OUTER_JOIN("`system_log_status` AS ls ON ls.status_id=c.`status_id`");
                //备注
                if (StringUtils.isNotBlank(countryDto.getSystemLogStatus().getRemark())) {
                    WHERE("ls.remark=#{systemLogStatus.remark}");
                }
                //状态
                if (countryDto.getSystemLogStatus().getStatus() != null) {
                    WHERE("ls.status=#{systemLogStatus.status}");
                }
                //创建时间
                if (countryDto.getSystemLogStatus().getCreateDate() != null) {
                    WHERE("ls.create_date=#{systemLogStatus.createDate}");
                }
                //创建人
                if (countryDto.getSystemLogStatus().getCreateUser() != null) {
                    WHERE("ls.create_user=#{systemLogStatus.createUser}");
                }
                //修改日期
                if (countryDto.getSystemLogStatus().getModifyDate() != null) {
                    WHERE("ls.modify_date=#{systemLogStatus.modifyDate}");
                }
                //修改人
                if (countryDto.getSystemLogStatus().getModifyUser() != null) {
                    WHERE("ls.modify_user=#{systemLogStatus.modifyUser}");
                }
                //审核时间
                if (countryDto.getSystemLogStatus().getAuditDate() != null) {
                    WHERE("ls.audit_date=#{systemLogStatus.auditDate}");
                }
                //审核人
                if (countryDto.getSystemLogStatus().getAuditUser() != null) {
                    WHERE("ls.audit_user=#{systemLogStatus.auditUser}");
                }
            }
            //国家名称
            if (StringUtils.isNotBlank(countryDto.getCountryName())) {
                WHERE("c.country_name=#{countryName}");
            }
            //国家编号
            if (countryDto.getNumber() != null) {
                WHERE("c.number=#{number}");
            }
            //国家英文
            if (StringUtils.isNotBlank(countryDto.getCountryNameEng())) {
                WHERE("c.country_name_eng=#{countryNameEng}");
            }
            //国家英文简写
            if (StringUtils.isNotBlank(countryDto.getCountryShortNameEng())) {
                WHERE("c.country_short_name_eng=#{countryShortNameEng}");
            }
            //VAT税率
            if (countryDto.getVat() != null) {
                WHERE("c.vat=#{vat}");
            }
            //洲名字
            if (StringUtils.isNotBlank(countryDto.getProvinceName())) {
                WHERE("p.province_name=#{provinceName}");
            }
            //市名字
            if (StringUtils.isNotBlank(countryDto.getCityName())) {
                WHERE("pc.city_name=#{cityName}");
            }
            //县名字
            if (StringUtils.isNotBlank(countryDto.getCountyName())) {
                WHERE("pcc.`county_name`=#{countyName}");
            }
            //语言
            if (StringUtils.isNotBlank(countryDto.getLanguageName())) {
                WHERE("l.`language_name`=#{languageName}");
            }
        }}.toString();
    }

}
