package com.dt.user.model.SalesAmazonAd;

/**
 * 订单报告表
 */
public class SalesAmazonFbaTradeReport {

    private Long id;
    private String amazonOrderId;
    private String merchantOrderId;
    private Long date;
    private Long lastUpdatedDate;
    private Integer shopId;
    private Integer siteId;
    private String orderStatus;
    private String fulfillmentChannel;
    private String salesChannel;
    private String orderChannel;
    private String url;
    private String shipServiceLevel;
    private String productName;
    private String sku;
    private Long skuId;
    private String asin;
    private String itemStatus;
    private Integer quantity;
    private String currency;
    private Double itemPrice;
    private Double itemTax;
    private Double shippingPrice;
    private Double shippingTax;
    private Double giftWrapPrice;
    private Double giftWrapTax;
    private Double itemPromotionDiscount;
    private Double shipPromotionDiscount;
    private String shipCity;
    private String shipState;
    private String shipPostalCode;
    private String shipCountry;
    private String promotionIds;
    private String isBusinessOrder;
    private String purchaseOrderNumber;
    private String priceDesignation;
    private String isReplacementOrder;
    private String originalOrderId;
    private Long createDate;
    private Long createIdUser;
    private Long modifyDate;
    private Long modifyIdUser;
    private Long auditDate;
    private Long auditIdUser;
    private Long recordingId;



    public SalesAmazonFbaTradeReport() {

    }

    public SalesAmazonFbaTradeReport(Integer shopId, Long createDate, Long createIdUser, Long recordingId) {
        this.shopId = shopId;
        this.createDate = createDate;
        this.createIdUser = createIdUser;
        this.recordingId = recordingId;
    }

    public String getIsReplacementOrder() {
        return isReplacementOrder;
    }

    public void setIsReplacementOrder(String isReplacementOrder) {
        this.isReplacementOrder = isReplacementOrder;
    }

    public String getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(String originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    public Long getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(Long recordingId) {
        this.recordingId = recordingId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Long lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getFulfillmentChannel() {
        return fulfillmentChannel;
    }

    public void setFulfillmentChannel(String fulfillmentChannel) {
        this.fulfillmentChannel = fulfillmentChannel;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(String orderChannel) {
        this.orderChannel = orderChannel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShipServiceLevel() {
        return shipServiceLevel;
    }

    public void setShipServiceLevel(String shipServiceLevel) {
        this.shipServiceLevel = shipServiceLevel;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getItemTax() {
        return itemTax;
    }

    public void setItemTax(Double itemTax) {
        this.itemTax = itemTax;
    }

    public Double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(Double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public Double getShippingTax() {
        return shippingTax;
    }

    public void setShippingTax(Double shippingTax) {
        this.shippingTax = shippingTax;
    }

    public Double getGiftWrapPrice() {
        return giftWrapPrice;
    }

    public void setGiftWrapPrice(Double giftWrapPrice) {
        this.giftWrapPrice = giftWrapPrice;
    }

    public Double getGiftWrapTax() {
        return giftWrapTax;
    }

    public void setGiftWrapTax(Double giftWrapTax) {
        this.giftWrapTax = giftWrapTax;
    }

    public Double getItemPromotionDiscount() {
        return itemPromotionDiscount;
    }

    public void setItemPromotionDiscount(Double itemPromotionDiscount) {
        this.itemPromotionDiscount = itemPromotionDiscount;
    }

    public Double getShipPromotionDiscount() {
        return shipPromotionDiscount;
    }

    public void setShipPromotionDiscount(Double shipPromotionDiscount) {
        this.shipPromotionDiscount = shipPromotionDiscount;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getShipState() {
        return shipState;
    }

    public void setShipState(String shipState) {
        this.shipState = shipState;
    }

    public String getShipPostalCode() {
        return shipPostalCode;
    }

    public void setShipPostalCode(String shipPostalCode) {
        this.shipPostalCode = shipPostalCode;
    }

    public String getShipCountry() {
        return shipCountry;
    }

    public void setShipCountry(String shipCountry) {
        this.shipCountry = shipCountry;
    }

    public String getPromotionIds() {
        return promotionIds;
    }

    public void setPromotionIds(String promotionIds) {
        this.promotionIds = promotionIds;
    }

    public String getIsBusinessOrder() {
        return isBusinessOrder;
    }

    public void setIsBusinessOrder(String isBusinessOrder) {
        this.isBusinessOrder = isBusinessOrder;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getPriceDesignation() {
        return priceDesignation;
    }

    public void setPriceDesignation(String priceDesignation) {
        this.priceDesignation = priceDesignation;
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

    @Override
    public String toString() {
        return "SalesAmazonFbaTradeReport{" +
                "id=" + id +
                ", amazonOrderId='" + amazonOrderId + '\'' +
                ", merchantOrderId='" + merchantOrderId + '\'' +
                ", date=" + date +
                ", lastUpdatedDate=" + lastUpdatedDate +
                ", shopId=" + shopId +
                ", siteId=" + siteId +
                ", orderStatus='" + orderStatus + '\'' +
                ", fulfillmentChannel='" + fulfillmentChannel + '\'' +
                ", salesChannel='" + salesChannel + '\'' +
                ", orderChannel='" + orderChannel + '\'' +
                ", url='" + url + '\'' +
                ", shipServiceLevel='" + shipServiceLevel + '\'' +
                ", productName='" + productName + '\'' +
                ", sku='" + sku + '\'' +
                ", skuId=" + skuId +
                ", asin='" + asin + '\'' +
                ", itemStatus='" + itemStatus + '\'' +
                ", quantity=" + quantity +
                ", currency='" + currency + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemTax=" + itemTax +
                ", shippingPrice=" + shippingPrice +
                ", shippingTax=" + shippingTax +
                ", giftWrapPrice=" + giftWrapPrice +
                ", giftWrapTax=" + giftWrapTax +
                ", itemPromotionDiscount=" + itemPromotionDiscount +
                ", shipPromotionDiscount=" + shipPromotionDiscount +
                ", shipCity='" + shipCity + '\'' +
                ", shipState='" + shipState + '\'' +
                ", shipPostalCode='" + shipPostalCode + '\'' +
                ", shipCountry='" + shipCountry + '\'' +
                ", promotionIds='" + promotionIds + '\'' +
                ", isBusinessOrder='" + isBusinessOrder + '\'' +
                ", purchaseOrderNumber='" + purchaseOrderNumber + '\'' +
                ", priceDesignation='" + priceDesignation + '\'' +
                ", createDate=" + createDate +
                ", createIdUser=" + createIdUser +
                ", modifyDate=" + modifyDate +
                ", modifyIdUser=" + modifyIdUser +
                ", auditDate=" + auditDate +
                ", auditIdUser=" + auditIdUser +
                ", recordingId=" + recordingId +
                '}';
    }
}
