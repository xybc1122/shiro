package com.dt.user.provider;


import com.dt.user.model.FinancialSalesBalance;

public class FinancialSalesBalanceProvider {


    public String addInfoGerman(int insertNum, FinancialSalesBalance fsb) {
        // sql前缀
        String prefix = "insert into financial_sales_amazon_balance" +
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
                "`modify_date`,`modify_id_user`, `audit_date`, `audit_id_user`)" +
                " values";
        // 保存sql后缀
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        for (int i = 0; i < insertNum; i++) {
            // 构建sql后缀
            sb.append("(" + fsb.getDate() + "," + fsb.getShopId() + "," + fsb.getSiteId() + "," + fsb.getSettlemenId() + "," +
                    "" + fsb.getPaymentTypeId() + ",'" + fsb.getType() + "'," + fsb.getOrderId() + ",'" + fsb.getSku() + "" +
                    "'," + fsb.getSkuId() + ",'" + fsb.getDescription() + "'," + fsb.getoQuantity() + "," + fsb.getQuantity() + "," +
                    "" + fsb.getRefundQuantity() + "," + fsb.getOrderQty() + "," + fsb.getAdjustmentQty() + "," + fsb.getMarketplace() + "" +
                    "" + fsb.getFulfillment() + "," + fsb.getCity() + "," + fsb.getState() + "," + fsb.getPostal() + "," +
                    "" + fsb.getSales() + "," + fsb.getSalePrice() + "," + fsb.getPreSalePrice() + "," + fsb.getStdSalePrice() + "," +
                    "" + fsb.getNewShippingCredits() + "," + fsb.getShippingCredits() + "," + fsb.getGiftwrapCredits() + "," + fsb.getPromotionalRebates() + "," +
                    "" + fsb.getSalesTax() + "," + fsb.getMarketplaceFacilitatorTax() + "," + fsb.getSellingFees() + "," + fsb.getFbaFee() + "," +
                    "" + fsb.getOtherTransactionFees() + "," + fsb.getOther() + "" + "," + fsb.getTotal() + "," + fsb.getServiceFee() + "" +
                    "," + fsb.getTransfer() + "," + fsb.getAdjustment() + "," + fsb.getNewPromotionalRebates() + "," + fsb.getNewShippingFba() + "," +
                    "" + fsb.getStdProductSales() + "," + fsb.getStdSalesOriginal() + "," + fsb.getStdSalesAdd() + "," + fsb.getStdSalesMinus() + "," +
                    fsb.getStdFba() + "," + fsb.getStdFbas() + "," + fsb.getStdFbaOriginal() + "," + fsb.getLightningDealFee() + "," +
                    "" + fsb.getFbaInventoryFee() + "," + fsb.getStatus() + "," + fsb.getCreateDate() + "," + fsb.getCreateIdUser() + "," +
                    "" + fsb.getModifyDate() + "," + fsb.getModifyIdUser() + "," + fsb.getAuditDate() + "," + fsb.getAuditIdUser() + ")");
        }
        // 构建完整sql
        return sb.toString();
    }
}
