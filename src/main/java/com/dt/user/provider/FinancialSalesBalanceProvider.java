package com.dt.user.provider;


import com.dt.user.model.FinancialSalesBalance;
import com.dt.user.toos.Constants;
import com.dt.user.utils.StrUtils;

import java.util.List;
import java.util.Map;

public class FinancialSalesBalanceProvider {

    @SuppressWarnings("unchecked")
    public String addInfo(Map<String, Object> infoMap) {
        List<FinancialSalesBalance> fsbList = (List<FinancialSalesBalance>) infoMap.get("fsbList");
        Integer tbId = (Integer) infoMap.get("tbId");
        String db = "financial_sales_amazon_balance";
        if (tbId == Constants.FINANCE_ID_YY) {
            db = "sales_amazon_fba_balance";
        }
        // sql前缀
        String prefix = "insert into " + db +
                "(`date`,`shop_id`,`site_id`,`settlemen_id`," +
                "`payment_type_id`,`type`,`order_id`,`sku`," +
                "`sku_id`,`description`,`o_quantity`,`quantity`," +
                "`refund_quantity`,`order_qty`,`adjustment_qty`,`marketplace`," +
                "`fulfillment`,`city`,`state`,`postal`," +
                "`sales`,`sale_price`,`pre_sale_price`,`std_sale_price`," +
                "`new_shipping_credits`,`shipping_credits`,`giftwrap_credits`,`promotional_rebates`," +
                "`sales_tax`,`marketplace_facilitator_tax`,`selling_fees`,`fba_fee`," +
                "`other_transaction_fees`, `other`,`total`,`service_fee`," +
                "`transfer`,`adjustment`,`new_promotional_rebates`,`new_shipping_fba`," +
                "`std_product_sales`,`std_sales_original`,`std_sales_add`,`std_sales_minus`," +
                "`std_fba`,`std_fbas`,`std_fba_original`,`lightning_deal_fee`," +
                " `fba_inventory_fee`,`status`,`create_date`,`create_id_user`," +
                "`modify_date`,`modify_id_user`, `audit_date`, `audit_id_user`,`recording_id`,`point_fee`,`low_value_goods`)" +
                " values";
        // 保存sql后缀
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        for (int i = 0; i < fsbList.size(); i++) {
            FinancialSalesBalance fsb = fsbList.get(i);
            // 构建sql后缀
            sb.append("(" + fsb.getDate() + "," + fsb.getShopId() + "," + fsb.getSiteId() + ",");
            //#
            StrUtils.appBuider(sb, fsb.getSettlemenId());
            sb.append(",");
            sb.append(fsb.getPaymentTypeId());
            //#
            sb.append(",");
            StrUtils.appBuider(sb, fsb.getType());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, fsb.getOrderId());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, fsb.getSku());
            sb.append(",");
            sb.append(fsb.getSkuId());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, fsb.getDescription());
            sb.append(",");
            sb.append("" + fsb.getoQuantity() + "," + fsb.getQuantity() + "," +
                    "" + fsb.getRefundQuantity() + "," + fsb.getOrderQty() + "," + fsb.getAdjustmentQty() + ",");
            //#
            StrUtils.appBuider(sb, fsb.getMarketplace());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, fsb.getFulfillment());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, fsb.getCity());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, fsb.getState());
            sb.append(",");
            //#
            StrUtils.appBuider(sb, fsb.getPostal());
            sb.append(",");
            sb.append("" + fsb.getSales() + "," + fsb.getSalePrice() + "," + fsb.getPreSalePrice() + "," + fsb.getStdSalePrice() + "," +
                    "" + fsb.getNewShippingCredits() + "," + fsb.getShippingCredits() + "," + fsb.getGiftwrapCredits() + "," + fsb.getPromotionalRebates() + "," +
                    "" + fsb.getSalesTax() + "," + fsb.getMarketplaceFacilitatorTax() + "," + fsb.getSellingFees() + "," + fsb.getFbaFee() + "," +
                    "" + fsb.getOtherTransactionFees() + "," + fsb.getOther() + "" + "," + fsb.getTotal() + "," + fsb.getServiceFee() + "" +
                    "," + fsb.getTransfer() + "," + fsb.getAdjustment() + "," + fsb.getNewPromotionalRebates() + "," + fsb.getNewShippingFba() + "," +
                    "" + fsb.getStdProductSales() + "," + fsb.getStdSalesOriginal() + "," + fsb.getStdSalesAdd() + "," + fsb.getStdSalesMinus() + "," +
                    fsb.getStdFba() + "," + fsb.getStdFbas() + "," + fsb.getStdFbaOriginal() + "," + fsb.getLightningDealFee() + "," +
                    "" + fsb.getFbaInventoryFee() + "," + fsb.getStatus() + "," + fsb.getCreateDate() + "," + fsb.getCreateIdUser() + "," +
                    "" + fsb.getModifyDate() + "," + fsb.getModifyIdUser() + "," + fsb.getAuditDate() + "," + fsb.getAuditIdUser() + "," + fsb.getRecordingId() + "," + fsb.getPointFee() + "," + fsb.getLowValueGoods() + "),")
            ;
        }
        String sql = sb.toString().substring(0, sb.length() - 1);
        // 构建完整sql
        return sql;
    }
}
