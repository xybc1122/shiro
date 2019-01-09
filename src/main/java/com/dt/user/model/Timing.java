package com.dt.user.model;

/**
 * 定时请求的对象
 */
public class Timing {

    private static Timing timing = null;
    /**
     * 总数
     */
    private Long totalNumber;
    /**
     * 长度进度
     */
    private Long dataLength;
    /**
     * 状态
     */
    private String status;

    private void Timing() {

    }

    public static Timing getInstance() {
        //判断实例是否为空，为空则实例化
        if (null == timing) {
            synchronized (Timing.class) {
                if (null == timing) {
                    timing = new Timing();
                }
            }
        }
        //否则直接返回
        return timing;
    }

    public Long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Long getDataLength() {
        return dataLength;
    }

    public void setDataLength(Long dataLength) {
        this.dataLength = dataLength;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
