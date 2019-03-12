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

    // 子目录
    private List<BasicPublicProducts> childPros;
    //状态对象
    private SystemLogStatus systemLogStatus;

    public SystemLogStatus getSystemLogStatus() {
        return systemLogStatus;
    }

    public void setSystemLogStatus(SystemLogStatus systemLogStatus) {
        this.systemLogStatus = systemLogStatus;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public List<BasicPublicProducts> getChildPros() {
        return childPros;
    }

    public void setChildPros(List<BasicPublicProducts> childPros) {
        this.childPros = childPros;
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
