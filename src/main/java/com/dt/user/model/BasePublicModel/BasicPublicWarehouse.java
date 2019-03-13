package com.dt.user.model.BasePublicModel;

import com.dt.user.model.ParentSysTemLog;

import java.util.List;

/**
 * 仓库
 */
public class BasicPublicWarehouse extends ParentSysTemLog {

    private Integer warehouseId;
    private Integer number;
    private String warehouseName;
    private String warehouseAddress;
    private String principal;
    private Integer isParent;
    private Integer parentWarehouseId;
    // 子目录
    private List<BasicPublicWarehouse> childWarehouse;

    public List<BasicPublicWarehouse> getChildWarehouse() {
        return childWarehouse;
    }

    public void setChildWarehouse(List<BasicPublicWarehouse> childWarehouse) {
        this.childWarehouse = childWarehouse;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public Integer getParentWarehouseId() {
        return parentWarehouseId;
    }

    public void setParentWarehouseId(Integer parentWarehouseId) {
        this.parentWarehouseId = parentWarehouseId;
    }
}
