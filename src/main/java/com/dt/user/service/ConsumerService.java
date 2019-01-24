package com.dt.user.service;

import com.csvreader.CsvReader;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.Timing;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Future;

public interface ConsumerService {
    /**
     * 没有SKU接口存储
     */
    CopyOnWriteArrayList<List<String>> writeNoListSku();

    /**
     * 实时数据信息接口
     *
     * @return
     */
    CopyOnWriteArraySet<Timing> timingWrite();

    /**
     * 多线程处理Txt
     *
     * @param br
     * @param shopId
     * @param uid
     * @param recordingId
     * @param strLineHead
     * @param timing
     * @param tbId
     * @param aId
     * @return
     * @throws IOException
     */
    Future<ResponseBase> dealWithTxtData(BufferedReader br, Long shopId, Long uid,
                                         Long recordingId,
                                         List<String> strLineHead, Timing timing,
                                         Integer tbId, Integer aId,List<List<String>> skuNoIdList) throws IOException;

    /**
     * 多线程处理Csv
     * @param csvReader
     * @param row
     * @param shopId
     * @param siteId
     * @param uid
     * @param pId
     * @param recordingId
     * @param tbId
     * @param businessTime
     * @param timing
     * @return
     */
    Future<ResponseBase> dealWithCsvData(CsvReader csvReader, int row, Long shopId, Long siteId, Long uid, Integer pId, Long recordingId, Integer tbId, String businessTime, Timing timing);
}