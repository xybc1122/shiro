package com.dt.user.provider;

import com.dt.user.dto.ProductDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class BasicPublicProductProvider {


    public String findProduct(ProductDto productDto) {
        return new SQL() {{
            SELECT("p.`product_id`,p.`product_code`,p.`product_name`,p.`model`,\n" +
                    " p.`qty_per_box`,p.`product_sku`,p.`products_id`,\n" +
                    " p.`length_cm`,p.`width_cm`,p.`height_cm`,\n" +
                    " p.`gw_kg`,p.`nw_kg`,p.`volume_m3`,p.`length_in`,\n" +
                    " p.`width_in`,p.`height_in`,p.`volume_cuft`,p.`made_in`,\n" +
                    " p.`hs_code_id`,p.`status_id`,ia.item_attribute_name,it.item_typ_name,ps.products_name,hc.hs_code, u.unit_name\n" +
                    "FROM `basic_public_product` AS p");
            LEFT_OUTER_JOIN("`basic_public_unit` AS u ON u.unit_id=p.`unit_id`");
            LEFT_OUTER_JOIN("`basic_public_item_attribute` AS ia ON ia.item_attribute_id = p.`item_attribute_id`");
            LEFT_OUTER_JOIN("`basic_public_item_type` AS it ON it.`item_typ_id` = p.`item_typ_id`");
            LEFT_OUTER_JOIN("`basic_public_products` AS ps ON ps.`products_id`=p.`products_id`");
            LEFT_OUTER_JOIN("`basic_export_hs_code` AS hc ON hc.`hs_code_id`=p.`hs_code_id`");
            if (productDto.getSystemLogStatus() != null) {
                LEFT_OUTER_JOIN("`system_log_status` AS ls ON ls.status_id=p.`status_id`");
                //备注
                if (StringUtils.isNotBlank(productDto.getSystemLogStatus().getRemark())) {
                    WHERE("ls.remark=#{systemLogStatus.remark}");
                }
                //状态
                if (productDto.getSystemLogStatus().getStatus() != null) {
                    WHERE("ls.status=#{systemLogStatus.status}");
                }
                //创建时间
                if (productDto.getSystemLogStatus().getCreateDate() != null) {
                    WHERE("ls.create_date=#{systemLogStatus.createDate}");
                }
                //创建人
                if (productDto.getSystemLogStatus().getCreateUser() != null) {
                    WHERE("ls.create_user=#{systemLogStatus.createUser}");
                }
                //修改日期
                if (productDto.getSystemLogStatus().getModifyDate() != null) {
                    WHERE("ls.modify_date=#{systemLogStatus.modifyDate}");
                }
                //修改人
                if (productDto.getSystemLogStatus().getModifyUser() != null) {
                    WHERE("ls.modify_user=#{systemLogStatus.modifyUser}");
                }
                //审核时间
                if (productDto.getSystemLogStatus().getAuditDate() != null) {
                    WHERE("ls.audit_date=#{systemLogStatus.auditDate}");
                }
                //审核人
                if (productDto.getSystemLogStatus().getAuditUser() != null) {
                    WHERE("ls.audit_user=#{systemLogStatus.auditUser}");
                }
            }
            //产品代码
            if (StringUtils.isNotBlank(productDto.getProductCode())) {
                WHERE("p.product_code=#{productCode}");
            }
            //产品名称
            if (StringUtils.isNotBlank(productDto.getProductName())) {
                WHERE("p.product_name=#{productName}");
            }
            //规格型号
            if (StringUtils.isNotBlank(productDto.getModel())) {
                WHERE("p.model=#{model}");
            }
            //每箱数量
            if (productDto.getQtyPerBox() != null) {
                WHERE("p.qty_per_box=#{qtyPerBox}");
            }
            //产品SKU
            if (StringUtils.isNotBlank(productDto.getProductSku())) {
                WHERE("p.product_sku=#{productSku}");
            }
            //长度CM
            if (productDto.getLengthCm() != null) {
                WHERE("p.length_cm=#{lengthCm}");
            }
            //宽度CM
            if (productDto.getWidthCm() != null) {
                WHERE("p.width_cm=#{widthCm}");
            }
            //高度CM
            if (productDto.getHeightCm() != null) {
                WHERE("p.height_cm=#{heightCm}");
            }
            //毛重KG
            if (productDto.getGwKg() != null) {
                WHERE("p.gw_kg=#{gwKg}");
            }
            //净重
            if (productDto.getNwKg() != null) {
                WHERE("p.nw_kg=#{nwKg}");
            }
            //体积m
            if (productDto.getVolumeM3() != null) {
                WHERE("p.volume_m3=#{volumeM3}");
            }
            //长度英寸
            if (productDto.getLengthIn() != null) {
                WHERE("p.length_in=#{lengthIn}");
            }
            //宽度英寸
            if (productDto.getQtyPerBox() != null) {
                WHERE("p.width_in=#{widthIn}");
            }
            //高度英寸
            if (productDto.getHeightIn() != null) {
                WHERE("p.height_in=#{heightIn}");
            }
            //体积立方英尺
            if (productDto.getVolumeCuft() != null) {
                WHERE("p.volume_cuft=#{volumeCuft}");
            }

            //产地
            if (StringUtils.isNotBlank(productDto.getMadeIn())) {
                WHERE("p.made_in=#{madeIn}");
            }
            //计量 单位名称
            if (StringUtils.isNotBlank(productDto.getUnitName())) {
                WHERE("u.unit_name=#{unitName}");
            }
            //物料类型 名称
            if (StringUtils.isNotBlank(productDto.getItemTypName())) {
                WHERE("it.item_typ_name=#{itemTypName}");
            }
            // 物料属性名称
            if (StringUtils.isNotBlank(productDto.getItemAttributeName())) {
                WHERE("ia.item_attribute_name=#{itemAttributeName}");
            }
            // 类目名称
            if (StringUtils.isNotBlank(productDto.getProductName())) {
                WHERE("ps.products_name=#{productsName}");
            }
            //  HS Code
            if (StringUtils.isNotBlank(productDto.getHsCode())) {
                WHERE("hc.hs_code=#{hsCode}");
            }
        }}.toString();
    }
}
