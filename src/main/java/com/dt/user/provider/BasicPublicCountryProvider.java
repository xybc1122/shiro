package com.dt.user.provider;

import com.dt.user.dto.CountryDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class BasicPublicCountryProvider {


    public String findCountry(CountryDto countryDto) {
        return new SQL() {{
            SELECT("c.country_id,c.country_name,p.province_name," +
                    "pc.city_name,pcc.`county_name` FROM `basic_public_country` AS c");
            LEFT_OUTER_JOIN("`basic_public_province` AS p ON p.`country_id`=c.`country_id`");
            LEFT_OUTER_JOIN("`basic_public_province_city`AS pc ON pc.province_number=p.`province_number`");
            LEFT_OUTER_JOIN("`basic_public_province_city_county`AS pcc ON pcc.city_number=pc.`city_number`");
            //国家名称
            if (StringUtils.isNotBlank(countryDto.getCountryName())) {
                WHERE("c.country_name=#{countryName}");
            }
            //国家编号
            if (countryDto.getCountryNumber() != null) {
                WHERE("c.country_number=#{countryNumber}");
            }
            //国家英文
            if (StringUtils.isNotBlank(countryDto.getCountryEng())) {
                WHERE("c.country_eng=#{countryEng}");
            }
            //国家英文简写
            if (StringUtils.isNotBlank(countryDto.getCountryShortEng())) {
                WHERE("c.country_short_eng=#{countryShortEng}");
            }
            //VAT税率
            if (countryDto.getVat() != null) {
                WHERE("c.vat=#{vat}");
            }
            //备注
            if (StringUtils.isNotBlank(countryDto.getRemark())) {
                WHERE("c.remark=#{remark}");
            }
            //状态
            if (countryDto.getStatus() != null) {
                WHERE("c.status=#{status}");
            }
            //创建时间
            if (countryDto.getCreateDate() != null) {
                WHERE("c.create_date=#{createDate}");
            }
            //创建人
            if (countryDto.getCreateIdUser() != null) {
                WHERE("c.create_id_user=#{createIdUser}");
            }
            //修改日期
            if (countryDto.getModifyDate() != null) {
                WHERE("c.modify_date=#{modifyDate}");
            }
            //修改人
            if (countryDto.getModifyIdUser() != null) {
                WHERE("c.modify_id_user=#{modifyIdUser}");
            }
            //审核时间
            if (countryDto.getAuditDate() != null) {
                WHERE("c.audit_date=#{auditDate}");
            }
            //审核人
            if (countryDto.getAuditIdUser() != null) {
                WHERE("c.audit_id_user=#{auditIdUser}");
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
        }}.toString();
    }

}
