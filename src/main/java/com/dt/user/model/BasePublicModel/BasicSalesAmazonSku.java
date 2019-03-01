package com.dt.user.model.BasePublicModel;


import com.dt.user.model.ParentSysTemLog;

public class BasicSalesAmazonSku extends ParentSysTemLog {

  private Long skuId;
  private String shopId;
  private String siteId;
  private String sku;
  private Long sAsin;
  private String productId;
  private Long classId;
  private Double lengthCm;
  private Double widthCm;
  private Double heightCm;
  private Double gwKg;
  private Double nwKg;
  private Double volumeM3;


  public Long getSkuId() {
    return skuId;
  }

  public void setSkuId(Long skuId) {
    this.skuId = skuId;
  }

  public String getShopId() {
    return shopId;
  }

  public void setShopId(String shopId) {
    this.shopId = shopId;
  }

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public Long getsAsin() {
    return sAsin;
  }

  public void setsAsin(Long sAsin) {
    this.sAsin = sAsin;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public Long getClassId() {
    return classId;
  }

  public void setClassId(Long classId) {
    this.classId = classId;
  }

  public Double getLengthCm() {
    return lengthCm;
  }

  public void setLengthCm(Double lengthCm) {
    this.lengthCm = lengthCm;
  }

  public Double getWidthCm() {
    return widthCm;
  }

  public void setWidthCm(Double widthCm) {
    this.widthCm = widthCm;
  }

  public Double getHeightCm() {
    return heightCm;
  }

  public void setHeightCm(Double heightCm) {
    this.heightCm = heightCm;
  }

  public Double getGwKg() {
    return gwKg;
  }

  public void setGwKg(Double gwKg) {
    this.gwKg = gwKg;
  }

  public Double getNwKg() {
    return nwKg;
  }

  public void setNwKg(Double nwKg) {
    this.nwKg = nwKg;
  }

  public Double getVolumeM3() {
    return volumeM3;
  }

  public void setVolumeM3(Double volumeM3) {
    this.volumeM3 = volumeM3;
  }

}
