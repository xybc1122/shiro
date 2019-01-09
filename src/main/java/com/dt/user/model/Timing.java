package com.dt.user.model;

import com.dt.user.utils.TxtUtils;

/**
 * 定时请求的对象
 */
public class Timing {

    private static volatile Timing timing = null;
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
    /**
     * 设置上传当前数
     */


    /**
     * 设置文件总数
     *
     * @param filePath
     */
    public static void setFileCount(String filePath) {
        //首先获得行数
        Long sumCount = TxtUtils.readFile(filePath);
        if (sumCount != 0L) {
            //获得总行数
            Timing.getInstance().setTotalNumber(sumCount);
        }
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
