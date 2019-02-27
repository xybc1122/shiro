package com.dt.user.provider;

import com.dt.user.dto.SiteDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class BasicPublicSiteProvider {

    public String findSite(SiteDto siteDto) {
        return new SQL() {{
            SELECT("s.`site_id`,s.`number`,s.`site_name`,s.`site_name_eng`,s.`site_short_name_eng`,s.`url`,s.`vat`," +
                    "s.`principal`,s.status_id, c.`country_name`, cu.currency_name,cu.currency_short_name_eng,a.`area_name`,GROUP_CONCAT(e.employee_name) as employee_name FROM `basic_public_site` AS s");
            LEFT_OUTER_JOIN("`basic_public_country` AS c ON c.`country_id`=s.`country_id`");
            LEFT_OUTER_JOIN("`basic_public_area` AS a ON a.`area_id`=s.`area_id`");
            LEFT_OUTER_JOIN("`hr_archives_employee` AS e ON e.`pt_id`=s.`principal`");
            LEFT_OUTER_JOIN("`basic_public_currency` AS cu ON cu.`currency_id`=s.country_id");
            if (siteDto.getSystemLogStatus() != null) {
                LEFT_OUTER_JOIN("`system_log_status` AS ls ON ls.status_id=s.`status_id`");
                //备注
                if (StringUtils.isNotBlank(siteDto.getSystemLogStatus().getRemark())) {
                    WHERE("ls.remark=#{systemLogStatus.remark}");
                }
                //状态
                if (siteDto.getSystemLogStatus().getStatus() != null) {
                    WHERE("ls.status=#{systemLogStatus.status}");
                }
                //创建时间
                if (siteDto.getSystemLogStatus().getCreateDate() != null) {
                    WHERE("ls.create_date=#{systemLogStatus.createDate}");
                }
                //创建人
                if (siteDto.getSystemLogStatus().getCreateUser() != null) {
                    WHERE("ls.create_user=#{systemLogStatus.createUser}");
                }
                //修改日期
                if (siteDto.getSystemLogStatus().getModifyDate() != null) {
                    WHERE("ls.modify_date=#{systemLogStatus.modifyDate}");
                }
                //修改人
                if (siteDto.getSystemLogStatus().getModifyUser() != null) {
                    WHERE("ls.modify_user=#{systemLogStatus.modifyUser}");
                }
                //审核时间
                if (siteDto.getSystemLogStatus().getAuditDate() != null) {
                    WHERE("ls.audit_date=#{systemLogStatus.auditDate}");
                }
                //审核人
                if (siteDto.getSystemLogStatus().getAuditUser() != null) {
                    WHERE("ls.audit_user=#{systemLogStatus.auditUser}");
                }
            }
            //站点编号
            if (siteDto.getNumber() != null) {
                WHERE("s.number=#{number}");
            }
            //站点名称
            if (StringUtils.isNotBlank(siteDto.getSiteName())) {
                WHERE("s.site_name=#{siteName}");
            }
            //站点英文名称
            if (StringUtils.isNotBlank(siteDto.getSiteNameEng())) {
                WHERE("s.site_name_eng=#{siteNameEng}");
            }
            //站点英文简称
            if (StringUtils.isNotBlank(siteDto.getSiteShortNameEng())) {
                WHERE("s.site_short_name_eng=#{siteShortNameEng}");
            }
            //URL
            if (StringUtils.isNotBlank(siteDto.getUrl())) {
                WHERE("s.url=#{url}");
            }
            //VAT税率
            if (siteDto.getVat() != null) {
                WHERE("s.vat=#{vat}");
            }
            //币别英文名称
            if (StringUtils.isNotBlank(siteDto.getCurrencyShortNameEng())) {
                WHERE("cu.currency_short_name_eng=#{currencyShortNameEng}");
            }
            //币别名称
            if (StringUtils.isNotBlank(siteDto.getCurrencyName())) {
                WHERE("cu.currency_name=#{currencyName}");
            }
           //区域名称
            if (StringUtils.isNotBlank(siteDto.getAreaName())) {
                WHERE("a.`area_name`=#{areaName}");
            }
            //国家名称
            if(StringUtils.isNotBlank(siteDto.getCountryName())) {
                WHERE("c.`country_name`=#{countryName}");
            }
            //负责人
            if(StringUtils.isNotBlank(siteDto.getEmployeeName())) {
                WHERE("e.`employee_name`=#{employeeName}");
            }
            GROUP_BY("s.`site_id`");
        }}.toString();
    }

}
