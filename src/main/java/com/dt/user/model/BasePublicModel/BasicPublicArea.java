package com.dt.user.model.BasePublicModel;

/**
 * 区域表
 */
public class BasicPublicArea {
    /**
     * 区域ID
     */
    private Long areaId;
    /**
     * 区域编号
     */
    private Long number;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 区域英文
     */
    private String areaNameEng;
    /**
     * 区域英文检称
     */
    private String areaShortNameEng;


    /**
     * 状态ID
     */
    private Long statusId;

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }


    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaNameEng() {
        return areaNameEng;
    }

    public void setAreaNameEng(String areaNameEng) {
        this.areaNameEng = areaNameEng;
    }
    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getAreaShortNameEng() {
        return areaShortNameEng;
    }

    public void setAreaShortNameEng(String areaShortNameEng) {
        this.areaShortNameEng = areaShortNameEng;
    }
}
