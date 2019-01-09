package com.dt.user.model;

import java.util.List;

public class UserUpload {

    public UserUpload() {

    }
    private List<UserUpload> uploadSuccessList;
    private Long id;
    private Long uid;
    /**
     * 上传文件名称
     */
    private String name;
    private Long createDate;
    private Long delDate;
    private Long delDateId;
    /**
     * 上传服务器路径
     */
    private String filePath;

    /**
     * 写入服务器路径
     */
    private String writeFilePath;
    /**
     * 上传备注
     */
    private String remark;

    /**
     * 上传状态0代表成功,1代表失败,2代表上传成功后 有些没有skuId的没上传
     */
    private Integer status;
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 站点ID
     */
    private Long siteId;

    /**
     * payId
     */
    private Integer pId;

    /**
     * tbId 通过这ID获取是哪一个菜单的上传
     */
    private Integer tbId;

    /**
     * 记录表ID
     *
     * @return
     */
    private Long recordingId;
    /**
     * 洲ID
     */
    private Integer areaId;

    public List<UserUpload> getUploadSuccessList() {
        return uploadSuccessList;
    }

    public void setUploadSuccessList(List<UserUpload> uploadSuccessList) {
        this.uploadSuccessList = uploadSuccessList;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getDelDate() {
        return delDate;
    }

    public void setDelDate(Long delDate) {
        this.delDate = delDate;
    }

    public Long getDelDateId() {
        return delDateId;
    }

    public void setDelDateId(Long delDateId) {
        this.delDateId = delDateId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getWriteFilePath() {
        return writeFilePath;
    }

    public void setWriteFilePath(String writeFilePath) {
        this.writeFilePath = writeFilePath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Integer getTbId() {
        return tbId;
    }

    public void setTbId(Integer tbId) {
        this.tbId = tbId;
    }

    public Long getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(Long recordingId) {
        this.recordingId = recordingId;
    }
}
