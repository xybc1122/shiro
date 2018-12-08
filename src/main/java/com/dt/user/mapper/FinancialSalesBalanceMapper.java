package com.dt.user.mapper;

import com.dt.user.model.FinancialSalesBalance;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FinancialSalesBalanceMapper {
    /**
     * 插入德国数据
     * @param financialSalesBalance
     * @return
     */
    @Insert("INSERT INTO `financial_sales_balance`(`date`,`shop_id`,`site_id`,`settlemen_id`,`payment_type_id`,`type`,`order_id`,\n" +
            "             `sku`,`sku_id`,`description`,`o_quantity`,`quantity`,`refund_quantity`,`order_qty`,`adjustment_qty`,`marketplace`,\n" +
            "             `fulfillment`,`city`,`state`,`postal`,`sales`,`sale_price`,`pre_sale_price`,`std_sale_price`,`new_shipping_credits`,\n" +
            "             `shipping_credits`,`giftwrap_credits`,`promotional_rebates`,`sales_tax`,`marketplace_facilitator_tax`,`selling_fees`,`fba_fee`, `other_transaction_fees`, `other`,\n" +
            "             `total`,`service_fee`,`transfer`,`adjustment`,`new_promotional_rebates`,`new_shipping_fba`,`std_product_sales`,`std_sales_original`,`std_sales_add`,\n" +
            "             `std_sales_minus`,`std_fba`,`std_fbas`,`std_fba_original`,`lightning_deal_fee`, `fba_inventory_fee`,`status`, `create_date`,`create_id_user`,\n" +
            "             `modify_date`,`modify_id_user`, `audit_date`, `audit_id_user`)\n" +
            "VALUES (#{date},#{shopId},#{siteId},#{settlemenId},#{paymentTypeId},#{type},#{orderId},#{sku},#{skuId},#{description},#{oQuantity},#{quantity}" +
            ",#{refundQuantity},#{orderQty},#{adjustmentQty},#{marketplace},#{fulfillment},#{city},#{state},#{postal},#{sales},#{salePrice},#{preSalePrice},#{stdSalePrice},#{newShippingCredits},#{shippingCredits},#{giftwrapCredits}" +
            ",#{promotionalRebates},#{salesTax},#{marketplaceFacilitatorTax},#{sellingFees},#{fbaFee},#{otherTransactionFees},#{other}," +
            "#{total},#{serviceFee},#{transfer},#{adjustment},#{newPromotionalRebates},#{newShippingFba}," +
            "#{stdProductSales},#{stdSalesOriginal},#{stdSalesAdd},#{stdSalesMinus},#{stdFba},#{stdFbas}," +
            "#{stdFbaOriginal},#{lightningDealFee},#{fbaInventoryFee},#{status},#{createDate},#{createIdUser}," +
            "#{modifyDate},#{modifyIdUser},#{auditDate},#{auditIdUser});")
    int addInfoGerman(FinancialSalesBalance financialSalesBalance);
}
