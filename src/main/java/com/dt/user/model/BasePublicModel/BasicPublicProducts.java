package com.dt.user.model.BasePublicModel;

import java.util.List;

/**
 * 产品类目
 */
public class BasicPublicProducts {

    private Long productsId;
    private Long productsNumber;
    private String productsName;
    private Long parentProductsId;
    private String productsPath;
    private Long isParent;
    private String remark;
    private Integer status;
    private Long createDate;
    private Long createIdUser;
    private Long modifyDate;
    private Long modifyIdUser;
    private Long auditDate;
    private Long auditIdUser;
    // 子目录
    private List<BasicPublicProducts> childMenus;

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

    public Long getProductsNumber() {
        return productsNumber;
    }

    public void setProductsNumber(Long productsNumber) {
        this.productsNumber = productsNumber;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getCreateIdUser() {
        return createIdUser;
    }

    public void setCreateIdUser(Long createIdUser) {
        this.createIdUser = createIdUser;
    }

    public Long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getModifyIdUser() {
        return modifyIdUser;
    }

    public void setModifyIdUser(Long modifyIdUser) {
        this.modifyIdUser = modifyIdUser;
    }

    public Long getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Long auditDate) {
        this.auditDate = auditDate;
    }

    public Long getAuditIdUser() {
        return auditIdUser;
    }

    public void setAuditIdUser(Long auditIdUser) {
        this.auditIdUser = auditIdUser;
    }
}
