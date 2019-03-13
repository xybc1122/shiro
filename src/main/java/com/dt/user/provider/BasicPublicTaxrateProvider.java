package com.dt.user.provider;

import com.dt.user.dto.SiteDto;
import com.dt.user.dto.TaxrateDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * @ClassName BasicPublicTaxrateProvider
 * Description TODO
 * @Author 陈恩惠
 * @Date 2019/3/13 14:45
 **/
public class BasicPublicTaxrateProvider {

    public String findTaxrate(TaxrateDto taxrateDto) {
        return new SQL() {{
            SELECT("st.`duties_taxrate_id`,st.status_id,st.`tax_rate`,st.`effective_date`,c.`country_name`,p.`products_name`\n" +
                    "FROM `basic_public_duties_taxrate` AS st");
            LEFT_OUTER_JOIN("`basic_public_country` AS c ON c.`country_id`=st.`country_id`");
            LEFT_OUTER_JOIN("`basic_public_products` AS p ON p.`products_id`=st.`products_id`");
            if (taxrateDto.getSystemLogStatus() != null) {
                LEFT_OUTER_JOIN("`system_log_status` AS ls ON ls.status_id=st.`status_id`");
                //备注
                if (StringUtils.isNotBlank(taxrateDto.getSystemLogStatus().getRemark())) {
                    WHERE("ls.remark=#{systemLogStatus.remark}");
                }
                //状态
                if (taxrateDto.getSystemLogStatus().getStatus() != null) {
                    WHERE("ls.status=#{systemLogStatus.status}");
                }
                //创建时间
                if (taxrateDto.getSystemLogStatus().getCreateDate() != null) {
                    WHERE("ls.create_date=#{systemLogStatus.createDate}");
                }
                //创建人
                if (taxrateDto.getSystemLogStatus().getCreateUser() != null) {
                    WHERE("ls.create_user=#{systemLogStatus.createUser}");
                }
                //修改日期
                if (taxrateDto.getSystemLogStatus().getModifyDate() != null) {
                    WHERE("ls.modify_date=#{systemLogStatus.modifyDate}");
                }
                //修改人
                if (taxrateDto.getSystemLogStatus().getModifyUser() != null) {
                    WHERE("ls.modify_user=#{systemLogStatus.modifyUser}");
                }
                //审核时间
                if (taxrateDto.getSystemLogStatus().getAuditDate() != null) {
                    WHERE("ls.audit_date=#{systemLogStatus.auditDate}");
                }
                //审核人
                if (taxrateDto.getSystemLogStatus().getAuditUser() != null) {
                    WHERE("ls.audit_user=#{systemLogStatus.auditUser}");
                }
            }
            //国家名称
            if (StringUtils.isNotBlank(taxrateDto.getCountryName())) {
                WHERE("c.country_name=#{countryName}");
            }
            //税率
            if (taxrateDto.getTaxRate() != null) {
                WHERE("st.tax_rate=#{taxRate}");
            }
            //生效日期
            if (taxrateDto.getEffectiveDate() != null) {
                WHERE("st.effective_date=#{effectiveDate}");
            }
            //产品类目
            if (StringUtils.isNotBlank(taxrateDto.getProductsName())) {
                WHERE("p.products_name=#{productsName}");
            }
        }}.toString();
    }

}
