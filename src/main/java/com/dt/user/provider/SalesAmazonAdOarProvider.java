package com.dt.user.provider;

import com.dt.user.model.SalesAmazonAd.SalesAmazonAdOar;
import com.dt.user.utils.StrUtils;

import java.util.List;
import java.util.Map;

public class SalesAmazonAdOarProvider {

    @SuppressWarnings("unchecked")
    public String addAmazonAdOar(Map<String, Object> mapOar) {
        List<SalesAmazonAdOar> oarList = (List<SalesAmazonAdOar>) mapOar.get("oarList");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `sales_amazon_ad_oar`\n" +
                "            (`date`,`shop_id`,`site_id`,`campaign_name`,`ad_group_name`,`advertised_sku`,`advertised_asin`,`targeting`,`match_type`,`other_asin`,`sku_id`,`other_asin_units`, `other_asin_units_ordered`,\n" +
                "             `other_asin_units_ordered_sales`,`remark`,`status`,`create_date`,`create_id_user`,`modify_date`,`modify_id_user`,`audit_date`,`audit_id_user`,`recording_id`)values");
        for (SalesAmazonAdOar oar : oarList) {
            sb.append("(" + oar.getDate() + "," + oar.getShopId() + "," + oar.getSiteId() + "");
            sb.append(",");
            StrUtils.appBuider(sb, oar.getCampaignName());
            sb.append(",");
            StrUtils.appBuider(sb, oar.getAdGroupName());
            sb.append(",");
            StrUtils.appBuider(sb, oar.getAdvertisedSku());
            sb.append(",");
            StrUtils.appBuider(sb, oar.getAdvertisedAsin());
            sb.append(",");
            StrUtils.appBuider(sb, oar.getTargeting());
            sb.append(",");
            StrUtils.appBuider(sb, oar.getMatchType());
            sb.append(",");
            StrUtils.appBuider(sb, oar.getOtherAsin());
            sb.append(",");
            sb.append("" + oar.getSkuId() + ","
                    + oar.getOtherAsinUnits() + "," + oar.getOtherAsinUnitsOrdered() + ","
                    + oar.getOtherAsinUnitsOrderedSales() + ",");
            StrUtils.appBuider(sb, oar.getRemark());
            sb.append(",");
            sb.append(oar.getStatus() + "," + oar.getCreateDate() + ","
                    + oar.getCreateIdUser() + "," + oar.getModifyDate() + ","
                    + oar.getModifyIdUser() + "," + oar.getAuditDate() + ","
                    + oar.getAuditIdUser() + "," + oar.getRecordingId() + "),");
        }
        String sql = sb.toString().substring(0, sb.length() - 1);
        return sql;
    }

}
