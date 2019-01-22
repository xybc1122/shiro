package com.dt.user.provider;
import com.dt.user.model.SalesAmazonAd.SalesAmazonAdStr;
import com.dt.user.utils.StrUtils;

import java.util.List;
import java.util.Map;

public class SalesAmazonAdStrProvider {

    @SuppressWarnings("unchecked")
    public String addAmazonAdStr(Map<String, Object> mapStr) {
        List<SalesAmazonAdStr> strList = (List<SalesAmazonAdStr>) mapStr.get("strList");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `sales_amazon_ad_str`\n" +
                "(`date`,`shop_id`,`site_id`,`campaign_name`, `ad_group_name`,`targeting`, `match_type`,`customer_search_term`,`impressions`,`clicks`,\n" +
                "`total_spend`,`sales`,`roas`,`orders_placed`, `total_units`,`advertised_sku_units_ordered`,`other_sku_units_ordered`,`advertised_sku_units_sales`,`other_sku_units_sales`,\n" +
                "`remark`,`status`, `create_date`,`create_id_user`,`modify_date`,`modify_id_user`,`audit_date`,`audit_id_user`,`recording_id`) values");
        for (SalesAmazonAdStr str : strList) {
            sb.append("(" + str.getDate() + "," + str.getShopId() + "," + str.getSiteId() + "");
            sb.append(",");
            StrUtils.appBuider(sb, str.getCampaignName());
            sb.append(",");
            StrUtils.appBuider(sb, str.getAdGroupName());
            sb.append(",");
            StrUtils.appBuider(sb, str.getTargeting());
            sb.append(",");
            StrUtils.appBuider(sb, str.getMatchType());
            sb.append(",");
            StrUtils.appBuider(sb, str.getCustomerSearchTerm());
            sb.append(",");
            sb.append(""+str.getImpressions()+"");
            sb.append(",");
            sb.append(""+str.getClicks()+"");
            sb.append(",");
            sb.append("" + str.getTotalSpend() + ","
                    + str.getSales() + "," + str.getRoas() + ","
                    + str.getOrdersPlaced() + "," + str.getTotalUnits() + ","
                    + str.getAdvertisedSkuUnitsOrdered() + ","
                    + str.getOtherSkuUnitsOrdered() + "," + str.getAdvertisedSkuUnitsSales() + ","
                    + str.getOtherSkuUnitsSales() + ",");
            StrUtils.appBuider(sb, str.getRemark());
            sb.append(",");
            sb.append(str.getStatus() + "," + str.getCreateDate() + ","
                    + str.getCreateIdUser() + "," + str.getModifyDate() + ","
                    + str.getModifyIdUser() + "," + str.getAuditDate() + ","
                    + str.getAuditIdUser() + "," + str.getRecordingId() + "),");
        }
        String sql = sb.toString().substring(0, sb.length() - 1);
        return sql;
    }
}
