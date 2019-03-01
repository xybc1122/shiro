package com.dt.user.dto;

import com.dt.user.model.BasePublicModel.BasicPublicProduct;


public class ProductDto extends BasicPublicProduct {

    /**
     * 计量 单位名称
     */
    private String unitName;
    /**
     * 物料类型 名称
     */
    private String itemTypName;
    /**
     * 物料属性名称
     */
    private String itemAttributeName;
    /**
     * 类目名称
     */
    private String productsName;
    /**
     * HS Code
     */
    private String hsCode;



    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getProductsName() {
        return productsName;
    }

    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    public String getItemAttributeName() {
        return itemAttributeName;
    }

    public String getItemTypName() {
        return itemTypName;
    }

    public void setItemTypName(String itemTypName) {
        this.itemTypName = itemTypName;
    }

    public void setItemAttributeName(String itemAttributeName) {
        this.itemAttributeName = itemAttributeName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }


}
