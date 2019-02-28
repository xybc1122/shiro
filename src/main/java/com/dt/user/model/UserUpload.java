package com.dt.user.model;

import java.util.List;

public class UserUpload {


    public UserUpload() {

    }

    public UserUpload(Long id, Long uid) {
        this.id = id;
        this.uid = uid;
    }

    /**
     * 存入记录信息集合数据
     */
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
    private String uuidName;
    /**
     * 上传状态0代表成功,1代表失败,2代表上传成功后 有些没有skuId的没上传
     */
    private Integer status;
    /**
     * 店铺ID
     */
    private Integer shopId;
    /**
     * 站点ID
     */
    private Integer siteId;

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
    /**
     * 业务报告时间
     */
    private String businessTime;

    public UserUpload(Long uid, String name, Long createDate, Long delDate, Long delDateId, String filePath, String writeFilePath, String remark, Integer status, Integer shopId, Integer siteId, Integer pId, Integer tbId, Long recordingId, Integer areaId, String businessTime) {
        this.uid = uid;
        this.name = name;
        this.createDate = createDate;
        this.delDate = delDate;
        this.delDateId = delDateId;
        this.filePath = filePath;
        this.writeFilePath = writeFilePath;
        this.remark = remark;
        this.status = status;
        this.shopId = shopId;
        this.siteId = siteId;
        this.pId = pId;
        this.tbId = tbId;
        this.recordingId = recordingId;
        this.areaId = areaId;
        this.businessTime = businessTime;
    }

    public String getUuidName() {
        return uuidName;
    }

    public void setUuidName(String uuidName) {
        this.uuidName = uuidName;
    }

    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

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

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }
}
