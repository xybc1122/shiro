package com.dt.user.model;

/**
 * 定时请求的对象
 */
public class Timing {
    //上传页面id
    private Long redId;

//    private static volatile Timing timing = null;
    /**
     * 总数
     */
    private Double totalNumber;

    /**
     * 状态
     */
    private String status;

    /**
     * 百分比
     */
    private Integer percentage;

    /**
     * 颜色
     */
    private String color;

    /**
     * msg
     */
    private String msg;

    public Long getRedId() {
        return redId;
    }

    public void setRedId(Long redId) {
        this.redId = redId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void Timing() {

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public Double getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Double totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
