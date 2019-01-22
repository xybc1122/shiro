package com.dt.user.provider;
import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaInventoryEnd;
import com.dt.user.utils.StrUtils;

import java.util.List;
import java.util.Map;

public class SalesAmazonFbaInventoryEndProvider {

    @SuppressWarnings("unchecked")
    public String addEndList(Map<String, Object> mapStr) {
        List<SalesAmazonFbaInventoryEnd> endList = (List<SalesAmazonFbaInventoryEnd>) mapStr.get("endList");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `sales_amazon_fba_Inventory_End`\n" +
                "(`date`,`shop_id`,`site_id`,`sku`,`fnsku`,`sku_id`,\n" +
                "`ProductName`,`quantity`,`fc`,`aw_id`,`disposition`,`country`,\n" +
                "`remark`,`status`,`create_date`,`create_id_user`, `modify_date`,\n" +
                "`modify_id_user`,`audit_date`,`audit_id_user`,`recordingId`)values");
        for (SalesAmazonFbaInventoryEnd end : endList) {
            sb.append("(" + end.getDate() + "," + end.getShopId() + "," + end.getSiteId() + ",");
            StrUtils.appBuider(sb, end.getSku());
            sb.append(",");
            StrUtils.appBuider(sb, end.getFnsku());
            sb.append("," + end.getSkuId() + ",");
            StrUtils.appBuider(sb, end.getProductName());
            sb.append("," + end.getQuantity() + ",");
            StrUtils.appBuider(sb, end.getFc());
            sb.append("," + end.getAwId() + ",");
            StrUtils.appBuider(sb, end.getDisposition());
            sb.append(",");
            StrUtils.appBuider(sb, end.getCountry());
            sb.append(",");
            StrUtils.appBuider(sb, end.getRemark());
            sb.append(",");
            sb.append(end.getStatus() + "," + end.getCreateDate() + ","
                    + end.getCreateIdUser() + "," + end.getModifyDate() + ","
                    + end.getModifyIdUser() + "," + end.getAuditDate() + ","
                    + end.getAuditIdUser() + "," + end.getRecordingId() + "),");
        }
        String sql = sb.toString().substring(0, sb.length() - 1);
        return sql;
    }

}
