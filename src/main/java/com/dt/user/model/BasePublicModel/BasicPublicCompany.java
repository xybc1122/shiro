package com.dt.user.model.BasePublicModel;


/**
 * 公司表
 */
public class BasicPublicCompany{
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
    private String fullName;
    /**
     * 公司全称英文
     */
    private String fullNameEng;
    /**
     * 公司简称
     */
    private String shortName;
    /**
     * 公司简称英文
     */
    private String shortNameEng;
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
    private String address;
    /**
     * 公司地址英文
     */
    private String addressEng;

    /**
     * 公司电话
     */
    private String telPhone;
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

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullNameEng() {
        return fullNameEng;
    }

    public void setFullNameEng(String fullNameEng) {
        this.fullNameEng = fullNameEng;
    }

    public String getShortNameEng() {
        return shortNameEng;
    }

    public void setShortNameEng(String shortNameEng) {
        this.shortNameEng = shortNameEng;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressEng() {
        return addressEng;
    }

    public void setAddressEng(String addressEng) {
        this.addressEng = addressEng;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}
