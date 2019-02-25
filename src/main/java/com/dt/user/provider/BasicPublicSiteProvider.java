package com.dt.user.provider;

import com.dt.user.dto.SiteDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class BasicPublicSiteProvider {

    public String findSite(SiteDto siteDto) {
        return new SQL() {{
            SELECT("s.`site_id`,s.`site_number`,s.`site_name`,s.`site_eng`,s.`url`,s.`vat`," +
                    "s.`principal`,s.`remark`,s.`status`,s.`create_date`,s.`create_id_user`,s.`modify_date`," +
                    "s.`modify_id_user`,s.`audit_date`,s.`audit_id_user`," +
                    "c.`currency_name`,c.`currency_eng_short`,a.`area_name`FROM `basic_public_site` AS s");
            LEFT_OUTER_JOIN("`basic_public_currency` AS c ON c.`currency_id`=s.`currency_id`");
            LEFT_OUTER_JOIN("`basic_public_area` AS a ON a.`area_id`=s.`area_id`");
            //站点编号
            if (siteDto.getSiteNumber() != null) {
                WHERE("s.site_number=#{siteNumber}");
            }
            //站点名称
            if (StringUtils.isNotBlank(siteDto.getSiteName())) {
                WHERE("s.site_name=#{siteName}");
            }
            //英文简称
            if (StringUtils.isNotBlank(siteDto.getSiteEng())) {
                WHERE("s.site_eng=#{siteEng}");
            }
            //URL
            if (StringUtils.isNotBlank(siteDto.getUrl())) {
                WHERE("s.url=#{url}");
            }
            //VAT税率
            if (siteDto.getVat() != null) {
                WHERE("s.vat=#{vat}");
            }
            //备注
            if (StringUtils.isNotBlank(siteDto.getRemark())) {
                WHERE("s.remark=#{remark}");
            }
            //状态
            if (siteDto.getStatus() != null) {
                WHERE("s.status=#{status}");
            }
            //创建时间
            if (siteDto.getCreateDate() != null) {
                WHERE("s.create_date=#{createDate}");
            }
            //创建人
            if (siteDto.getCreateIdUser() != null) {
                WHERE("s.create_id_user=#{createIdUser}");
            }
            //修改日期
            if (siteDto.getModifyDate() != null) {
                WHERE("s.modify_date=#{modifyDate}");
            }
            //修改人
            if (siteDto.getModifyIdUser() != null) {
                WHERE("s.modify_id_user=#{modifyIdUser}");
            }
            //审核时间
            if (siteDto.getAuditDate() != null) {
                WHERE("s.audit_date=#{auditDate}");
            }
            //审核人
            if (siteDto.getAuditIdUser() != null) {
                WHERE("s.audit_id_user=#{auditIdUser}");
            }
            //币别名称
            if (StringUtils.isNotBlank(siteDto.getCurrencyName())) {
                WHERE("c.currency_name=#{currencyName}");
            }
            //币别英文简写
            if (StringUtils.isNotBlank(siteDto.getCurrencyEngShort())) {
                WHERE("c.currency_eng_short=#{currencyEngShort}");
            }
           //区域名称
            if (StringUtils.isNotBlank(siteDto.getAreaName())) {
                WHERE("a.`area_name`=#{areaName}");
            }
        }}.toString();
    }

}
