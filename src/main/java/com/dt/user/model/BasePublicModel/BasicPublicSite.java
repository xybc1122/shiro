package com.dt.user.model.BasePublicModel;

import com.dt.user.model.ParentSysTemLog;

/**
 * 站点表
 */
public class BasicPublicSite extends ParentSysTemLog {

  private Integer siteId;
  private Integer number;
  private String siteName;
  private String siteNameEng;
  private String siteShortNameEng;
  private String url;
  private Double vat;
  private Long principal;



    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteNameEng() {
        return siteNameEng;
    }

    public void setSiteNameEng(String siteNameEng) {
        this.siteNameEng = siteNameEng;
    }

    public String getSiteShortNameEng() {
        return siteShortNameEng;
    }

    public void setSiteShortNameEng(String siteShortNameEng) {
        this.siteShortNameEng = siteShortNameEng;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Long getPrincipal() {
        return principal;
    }

    public void setPrincipal(Long principal) {
        this.principal = principal;
    }

}
