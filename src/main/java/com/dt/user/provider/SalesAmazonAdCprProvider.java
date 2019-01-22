package com.dt.user.provider;

import com.dt.user.model.SalesAmazonAd.SalesAmazonAdCpr;
import com.dt.user.utils.StrUtils;

import java.util.List;
import java.util.Map;

public class SalesAmazonAdCprProvider {

    @SuppressWarnings("unchecked")
    public String addAmazonAdCpr(Map<String, Object> mapCpr) {
        List<SalesAmazonAdCpr> cprList = (List<SalesAmazonAdCpr>) mapCpr.get("cprList");
        StringBuilder sb = new StringBuilder();
        sb.append("insert into sales_amazon_ad_cpr(`date`,shop_id,site_id,sku_id,advertised_sku,advertised_asin," +
                "campaign_name," +
                "ad_group_name,keyword,match_type,impressions,clicks,total_spend,orders_placed,sales,roas," +
                "total_units,samesku_units_ordered,othersku_units_ordered,samesku_units_sales,othersku_units_sales," +
                "remark,`status`,create_date," +
                "create_id_user,modify_date,modify_id_user,audit_date,audit_id_user,`recording_id`) values");
        for (SalesAmazonAdCpr cpr : cprList) {
            sb.append("(" + cpr.getDate() + "," + cpr.getShopId() + "," + cpr.getSiteId() + "");
            sb.append(",");
            sb.append("" + cpr.getSkuId() + "");
            sb.append(",");
            StrUtils.appBuider(sb, cpr.getAdvertisedSku());
            sb.append(",");
            StrUtils.appBuider(sb, cpr.getAdvertisedAsin());
            sb.append(",");
            StrUtils.appBuider(sb, cpr.getCampaignName());
            sb.append(",");
            StrUtils.appBuider(sb, cpr.getAdGroupName());
            sb.append(",");
            StrUtils.appBuider(sb, cpr.getKeyword());
            sb.append(",");
            StrUtils.appBuider(sb, cpr.getMatchType());
            sb.append(",");
            sb.append("" + cpr.getImpressions() + ","
                    + cpr.getClicks() + "," + cpr.getTotalSpend() + ","
                    + cpr.getOrdersPlaced() + "," + cpr.getSales() + "," + cpr.getRoas() + ","
                    + cpr.getTotalUnits() + "," + cpr.getSameskuUnitsOrdered() + ","
                    + cpr.getOtherskuUnitsOrdered() + "," + cpr.getSameskuUnitsSales() + ","
                    + cpr.getOtherskuUnitsSales() + ",");
            StrUtils.appBuider(sb, cpr.getRemark());
            sb.append(",");
            sb.append(cpr.getStatus() + "," + cpr.getCreateDate() + ","
                    + cpr.getCreateIdUser() + "," + cpr.getModifyDate() + ","
                    + cpr.getModifyIdUser() + "," + cpr.getAuditDate() + ","
                    + cpr.getAuditIdUser() + "," + cpr.getRecordingId() + "),");
        }
        String sql = sb.toString().substring(0, sb.length() - 1);
        return sql;
    }
}
