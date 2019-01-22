package com.dt.user.provider;

import com.dt.user.model.SalesAmazonAd.SalesAmazonAdHl;
import com.dt.user.utils.StrUtils;

import java.util.List;
import java.util.Map;

public class SalesAmazonAdHlProvider {

    @SuppressWarnings("unchecked")
    public String addAmazonAdHl(Map<String, Object> mapHl) {
        List<SalesAmazonAdHl> hlList = (List<SalesAmazonAdHl>) mapHl.get("hlList");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `sales_amazon_ad_hl`\n" +
                "(`date`,`shop_id`,`site_id`,`campaign_name`,`impressions`,`clicks`,`ctr`,`cpc`,\n" +
                " `spend`,`acos`,`roas`,`total_sales`,`total_orders`,`total_units`,`conversion_rate`,`remark`,\n" +
                " `status`,`create_date`, `create_id_user`, `modify_date`, `modify_id_user`,`audit_date`, `audit_id_user`, `recording_id`) values");
        for (SalesAmazonAdHl hl : hlList) {
            sb.append("(" + hl.getDate() + "," + hl.getShopId() + "," + hl.getSiteId() + "");
            sb.append(",");
            StrUtils.appBuider(sb, hl.getCampaignName());
            sb.append("," + hl.getImpressions() + "," + hl.getClicks() + ",");
            sb.append("" + hl.getCtr() + ","
                    + hl.getCpc() + "," + hl.getSpend() + ","
                    + hl.getAcos() + "," + hl.getRoas() + "," + hl.getTotalSales() + ","
                    + hl.getTotalOrders() + ","
                    + hl.getTotalUnits() + "," + hl.getConversionRate() + ",");
            StrUtils.appBuider(sb, hl.getRemark());
            sb.append(",");
            sb.append(hl.getStatus() + "," + hl.getCreateDate() + ","
                    + hl.getCreateIdUser() + "," + hl.getModifyDate() + ","
                    + hl.getModifyIdUser() + "," + hl.getAuditDate() + ","
                    + hl.getAuditIdUser() + "," + hl.getRecordingId() + "),");
        }
        String sql = sb.toString().substring(0, sb.length() - 1);
        return sql;
    }
}
