package com.dt.user.provider;

import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaTradeReport;
import com.dt.user.utils.StrUtils;

import java.util.List;
import java.util.Map;

public class SalesAmazonFbaTradeReportProvider {


    public String addInfo(Map<String, Object> infoMap) {
        List<SalesAmazonFbaTradeReport> trdList = (List<SalesAmazonFbaTradeReport>) infoMap.get("trdList");
        // sql前缀
        String prefix = "INSERT INTO`sales_amazon_fba_trade_report`\n" +
                "(`amazon_order_id`,`merchant_order_id`,`date`,`last_updated_date`,`shop_id`,`site_id`,`order_status`,\n" +
                "`fulfillment_channel`, `sales_channel`,`order_channel`,`url`,`ship_service_level`,`product_name`,`sku`,`sku_id`,\n" +
                "`asin`,`item_status`,`quantity`,`currency`,`item_price`,`item_tax`,`shipping_price`,`shipping_tax`,`gift_wrap_price`,\n" +
                "`gift_wrap_tax`,`item_promotion_discount`,`ship_promotion_discount`,`ship_city`,`ship_state`,`ship_postal_code`,\n" +
                "`ship_country`,`promotion_ids`,`is_business_order`,`purchase_order_number`,`price_designation`, `is_replacement_order`,`original_order_id`,\n" +
                "`create_date`,`create_id_user`,`modify_date`,`modify_id_user`,`audit_date`,`audit_id_user`,`recording_id`)values";
        // 保存sql后缀
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        for (SalesAmazonFbaTradeReport trade : trdList) {
            sb.append("(");
            StrUtils.appBuider(sb, trade.getAmazonOrderId());
            sb.append(",");
            StrUtils.appBuider(sb, trade.getMerchantOrderId());
            sb.append(",");
            // 构建sql后缀
            sb.append(trade.getDate() + "," + trade.getLastUpdatedDate() + "," + trade.getShopId() + "," + trade.getSiteId());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getOrderStatus());
            sb.append(",");
            StrUtils.appBuider(sb, trade.getFulfillmentChannel());
            //#
            sb.append(",");
            StrUtils.appBuider(sb, trade.getSalesChannel());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getOrderChannel());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getUrl());
            sb.append(",");
            StrUtils.appBuider(sb, trade.getShipServiceLevel());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getProductName());
            sb.append(",");
            StrUtils.appBuider(sb, trade.getSku());
            sb.append("," + trade.getSkuId());
            sb.append(",");
            StrUtils.appBuider(sb, trade.getAsin());
            sb.append(",");
            StrUtils.appBuider(sb, trade.getItemStatus());
            sb.append("," + trade.getQuantity());
            sb.append(",");
            StrUtils.appBuider(sb, trade.getCurrency());
            sb.append(",");
            sb.append("" + trade.getItemPrice() + "," + trade.getItemTax() + "," +
                    "" + trade.getShippingPrice() + "," + trade.getShippingTax()
                    + "," + trade.getGiftWrapPrice() + "," + trade.getGiftWrapTax() + "," + trade.getItemPromotionDiscount()
                    + "," + trade.getShipPromotionDiscount());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getShipCity());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getShipState());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getShipPostalCode());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getShipCountry());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getPromotionIds());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getIsBusinessOrder());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getPurchaseOrderNumber());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getPriceDesignation());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getIsReplacementOrder());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, trade.getOriginalOrderId());
            sb.append(",");
            sb.append(trade.getCreateDate() + ","
                    + trade.getCreateIdUser() + "," + trade.getModifyDate() + ","
                    + trade.getModifyIdUser() + "," + trade.getAuditDate() + ","
                    + trade.getAuditIdUser() + "," + trade.getRecordingId() + "),");
        }
        String sql = sb.toString().substring(0, sb.length() - 1);
        // 构建完整sql
        return sql;
    }
}
