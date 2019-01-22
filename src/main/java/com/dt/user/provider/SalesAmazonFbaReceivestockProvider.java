package com.dt.user.provider;

import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaReceivestock;
import com.dt.user.utils.StrUtils;

import java.util.List;
import java.util.Map;

public class SalesAmazonFbaReceivestockProvider {


    @SuppressWarnings("unchecked")
    public String addReceives(Map<String, Object> mapStr) {
        List<SalesAmazonFbaReceivestock> receivesList = (List<SalesAmazonFbaReceivestock>) mapStr.get("receivesList");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO`sales_amazon_fba_receivestock`\n" +
                "(`date`,`shop_id`,`site_id`,`fba_shipment_id`,`sku`,`fnsku`,\n" +
                "`sku_id`,`ProductName`,`quantity`,`fc`,`aw_id`,`remark`,`status`,\n" +
                "`create_date`,`create_id_user`,`modify_date`,`modify_id_user`,`audit_date`,`audit_id_user`,`recordingId`)values");
        for (SalesAmazonFbaReceivestock receives : receivesList) {
            sb.append("(" + receives.getDate() + "," + receives.getShopId() + "," + receives.getSiteId() + ",");
            StrUtils.appBuider(sb, receives.getFbaShipmentId());
            sb.append(",");
            StrUtils.appBuider(sb, receives.getSku());
            sb.append(",");
            StrUtils.appBuider(sb, receives.getFnsku());
            sb.append(",");
            sb.append("" + receives.getSkuId() + ",");
            StrUtils.appBuider(sb, receives.getProductName());
            sb.append(",");
            sb.append("" + receives.getQuantity() + ",");
            StrUtils.appBuider(sb, receives.getFc());
            sb.append(",");
            sb.append("" + receives.getAwId() + ",");
            StrUtils.appBuider(sb, receives.getRemark());
            sb.append(",");
            sb.append(receives.getStatus() + "," + receives.getCreateDate() + ","
                    + receives.getCreateIdUser() + "," + receives.getModifyDate() + ","
                    + receives.getModifyIdUser() + "," + receives.getAuditDate() + ","
                    + receives.getAuditIdUser() + "," + receives.getRecordingId() + "),");
        }
        String sql = sb.toString().substring(0, sb.length() - 1);
        return sql;
    }
}

