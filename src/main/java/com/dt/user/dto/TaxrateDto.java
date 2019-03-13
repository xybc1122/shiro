package com.dt.user.dto;

import com.dt.user.model.BasePublicModel.BasicPublicDutiesTaxrate;

/**
 * @ClassName TaxrateDto
 * Description TODO
 * @Author 陈恩惠
 * @Date 2019/3/13 11:09
 **/
public class TaxrateDto extends BasicPublicDutiesTaxrate {
    /**
     * 类名称
     */
    private String productsName;
    /**
     * 国家名称
     */
    private String countryName;

    public String getProductsName() {
        return productsName;
    }

    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
