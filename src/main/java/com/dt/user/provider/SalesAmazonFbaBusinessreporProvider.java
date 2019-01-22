package com.dt.user.provider;

import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaBusinessreport;
import com.dt.user.utils.StrUtils;

import java.util.List;
import java.util.Map;

public class SalesAmazonFbaBusinessreporProvider {

    @SuppressWarnings("unchecked")
    public String addAmazonAdBus(Map<String, Object> mapStr) {
        List<SalesAmazonFbaBusinessreport> busList = (List<SalesAmazonFbaBusinessreport>) mapStr.get("busList");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO`sales_amazon_fba_businessreport`\n" +
                "(`date`,`shop_id`,`site_id`,`sku`,`sku_id`, `f_asin`, `s_asin`, `p_name`, `sessions_visit`,\n" +
                "`sessions_per`,`page_views`,`buy_box_per`,`order`,`order_b2b`, `sales`,`sales_b2b`,`order_items`,`order_items_b2b`," +
                "`remark`,`status`,`create_date`,`create_id_user`,\n" +
                "`modify_date`,`modify_id_user`,`audit_date`,`audit_id_user`,`recording_id`)values");
        for (SalesAmazonFbaBusinessreport bus : busList) {
            sb.append("(" + bus.getDate() + "," + bus.getShopId() + "," + bus.getSiteId() + ",");
            StrUtils.appBuider(sb, bus.getSku());
            sb.append("," + bus.getSkuId() + ",");
            StrUtils.appBuider(sb, bus.getfAsin());
            sb.append(",");
            StrUtils.appBuider(sb, bus.getsAsin());
            sb.append(",");
            StrUtils.appBuider(sb, bus.getpName());
            sb.append(",");
            sb.append("" + bus.getSessionsVisit() + ","
                    + bus.getSessionsPer() + "," + bus.getPageViews() + ","
                    + bus.getBuyBoxPer() + "," + bus.getOrder() + ","
                    + bus.getOrderB2B() + ","
                    + bus.getSales() + "," + bus.getSalesB2B() + ","
                    + bus.getOrderItems() + "," + bus.getOrderItemsB2B() + ",");
            StrUtils.appBuider(sb, bus.getRemark());
            sb.append(",");
            sb.append(bus.getStatus() + "," + bus.getCreateDate() + ","
                    + bus.getCreateIdUser() + "," + bus.getModifyDate() + ","
                    + bus.getModifyIdUser() + "," + bus.getAuditDate() + ","
                    + bus.getAuditIdUser() + "," + bus.getRecordingId() + "),");
        }
        String sql = sb.toString().substring(0, sb.length() - 1);
        return sql;
    }
}
