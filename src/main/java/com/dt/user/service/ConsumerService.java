package com.dt.user.service;

import com.dt.user.config.ResponseBase;
import java.util.concurrent.Future;

public interface ConsumerService {
    /**
     * 封装处理数据Txt
     *
     * @param uuIdName
     * @param saveFilePath
     * @param fileName
     * @param shopId
     * @param uid
     * @param recordingId
     * @param tbId
     * @param aId
     * @return
     */
    Future<ResponseBase> importTxt(String uuIdName, String saveFilePath, String fileName, Long shopId, Long uid, Long
            recordingId, Integer tbId, Integer aId);

    /**
     * 封装处理数据Xls
     *
     * @param uuIdName
     * @param saveFilePath
     * @param fileName
     * @param siteId
     * @param shopId
     * @param uid
     * @param recordingId
     * @param tbId
     * @return
     */
    Future<ResponseBase> importXls(String uuIdName, String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Long
            recordingId, Integer tbId);


    /**
     * 封装处理数据Csv
     * @param uuIdName
     * @param saveFilePath
     * @param fileName
     * @param siteId
     * @param shopId
     * @param uid
     * @param pId
     * @param recordingId
     * @param tbId
     * @param businessTime
     * @return
     */
    Future<ResponseBase> importCsv(String uuIdName, String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Integer
            pId, Long recordingId, Integer tbId, String businessTime);
}