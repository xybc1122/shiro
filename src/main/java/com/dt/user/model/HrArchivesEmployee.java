package com.dt.user.model;

import java.io.Serializable;

/**
 * 员工表
 */
public class HrArchivesEmployee implements Serializable {
    /**
     * 员工ID
     */
    private Long sId;
    /**
     * 用户ID
     */
    private Long uId;
    /**
     * 职位ID
     */
    private Long ptId;
    /**
     * 员工编号
     */
    private String number;
    /**
     * 员工姓名
     */
    private String employeeName;
    /**
     * 员工姓名拼音
     */
    private String employeeNamePinyin;
    /**
     * 员工英文名
     */
    private String employeeNameEng;
    /**
     * 性别(0男；1女)
     */
    private Integer sex;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 员工手机
     */
    private long mobilePhone;
    /**
     * 民族ID
     */
    private long nationId;
    /**
     * 员工状态(1在职；0离职)
     */
    private Integer status;

    public Long getsId() {
        return sId;
    }

    public void setsId(Long sId) {
        this.sId = sId;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public Long getPtId() {
        return ptId;
    }

    public void setPtId(Long ptId) {
        this.ptId = ptId;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeNamePinyin() {
        return employeeNamePinyin;
    }

    public void setEmployeeNamePinyin(String employeeNamePinyin) {
        this.employeeNamePinyin = employeeNamePinyin;
    }

    public String getEmployeeNameEng() {
        return employeeNameEng;
    }

    public void setEmployeeNameEng(String employeeNameEng) {
        this.employeeNameEng = employeeNameEng;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public long getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(long mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public long getNationId() {
        return nationId;
    }

    public void setNationId(long nationId) {
        this.nationId = nationId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
