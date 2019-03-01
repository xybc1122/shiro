package com.dt.user.model.BasePublicModel;


import com.dt.user.model.ParentSysTemLog;

/**
 * 公司表
 */
public class BasicPublicCompany extends ParentSysTemLog {
    /**
     * 公司ID
     */
    private Integer companyId;
    /**
     * 公司编号
     */
    private Integer number;
    /**
     * 公司全称
     */
    private String companyName;
    /**
     * 公司全称英文
     */
    private String companyNameEng;
    /**
     * 公司简称
     */
    private String companyShortName;
    /**
     * 公司简称英文
     */
    private String companyShortNameEng;
    /**
     * 信用代码
     */
    private String creditCode;
    /**
     * 开户行
     */
    private String bankOfDeposit;
    /**
     * 银行账号
     */
    private String bankAccount;
    /**
     * 账号类型
     */
    private String accountType;
    /**
     * 公司地址
     */
    private String companyAddress;
    /**
     * 公司地址英文
     */
    private String companyAddressEng;

    /**
     * 公司电话
     */
    private String telPhone;

    /**
     * 公司短代码
     */
    private String companyShortCode;

    public String getCompanyShortCode() {
        return companyShortCode;
    }

    public void setCompanyShortCode(String companyShortCode) {
        this.companyShortCode = companyShortCode;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNameEng() {
        return companyNameEng;
    }

    public void setCompanyNameEng(String companyNameEng) {
        this.companyNameEng = companyNameEng;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public String getCompanyShortNameEng() {
        return companyShortNameEng;
    }

    public void setCompanyShortNameEng(String companyShortNameEng) {
        this.companyShortNameEng = companyShortNameEng;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getBankOfDeposit() {
        return bankOfDeposit;
    }

    public void setBankOfDeposit(String bankOfDeposit) {
        this.bankOfDeposit = bankOfDeposit;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyAddressEng() {
        return companyAddressEng;
    }

    public void setCompanyAddressEng(String companyAddressEng) {
        this.companyAddressEng = companyAddressEng;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

}
