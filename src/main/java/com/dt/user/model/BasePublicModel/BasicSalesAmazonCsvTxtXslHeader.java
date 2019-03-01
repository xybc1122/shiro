package com.dt.user.model.BasePublicModel;


import com.dt.user.model.ParentSysTemLog;

public class BasicSalesAmazonCsvTxtXslHeader extends ParentSysTemLog {
    /**
     * 站点ID
     */
    private Long siteId;
    /**
     * 对应表头字段
     */
    private String importTemplet;
    /**
     * 位置标识
     */
    private Integer position;
    /**
     * 标识ID
     */
    private Long id;

    private Integer isImport;
    /**
     * 哪儿数据类型上传标识  1代表财务
     */
    private Integer tbId;
    /**
     * 开或者关
     */
    private Boolean openClose;

    public Boolean getOpenClose() {
        return openClose;
    }

    public void setOpenClose(Boolean openClose) {
        this.openClose = openClose;
    }

    public Integer getTbId() {
        return tbId;
    }

    public void setTbId(Integer tbId) {
        this.tbId = tbId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getImportTemplet() {
        return importTemplet;
    }

    public void setImportTemplet(String importTemplet) {
        this.importTemplet = importTemplet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getIsImport() {
        return isImport;
    }

    public void setIsImport(Integer isImport) {
        this.isImport = isImport;
    }
}
