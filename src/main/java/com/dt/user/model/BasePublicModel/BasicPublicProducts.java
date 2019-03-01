package com.dt.user.model.BasePublicModel;

import com.dt.user.model.ParentSysTemLog;
import com.dt.user.model.SystemLogStatus;
import java.util.List;

/**
 * 产品类目
 */
public class BasicPublicProducts extends ParentSysTemLog {

    private Long productsId;
    private Long number;
    private String productsName;
    private Long parentProductsId;
    private String productsPath;
    private Long isParent;

    //状态对象
    private SystemLogStatus systemLogStatus;

    public SystemLogStatus getSystemLogStatus() {
        return systemLogStatus;
    }

    public void setSystemLogStatus(SystemLogStatus systemLogStatus) {
        this.systemLogStatus = systemLogStatus;
    }
    // 子目录
    private List<BasicPublicProducts> childMenus;

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public List<BasicPublicProducts> getChildMenus() {
        return childMenus;
    }

    public void setChildMenus(List<BasicPublicProducts> childMenus) {
        this.childMenus = childMenus;
    }

    public Long getProductsId() {
        return productsId;
    }

    public void setProductsId(Long productsId) {
        this.productsId = productsId;
    }


    public String getProductsName() {
        return productsName;
    }

    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    public Long getParentProductsId() {
        return parentProductsId;
    }

    public void setParentProductsId(Long parentProductsId) {
        this.parentProductsId = parentProductsId;
    }

    public String getProductsPath() {
        return productsPath;
    }

    public void setProductsPath(String productsPath) {
        this.productsPath = productsPath;
    }

    public Long getIsParent() {
        return isParent;
    }

    public void setIsParent(Long isParent) {
        this.isParent = isParent;
    }


}
