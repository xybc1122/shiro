package com.dt.user.service.impl;

import com.csvreader.CsvReader;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.BasePublicModel.BasicSalesAmazonWarehouse;
import com.dt.user.model.FinancialSalesBalance;
import com.dt.user.model.SalesAmazonAd.*;
import com.dt.user.model.Timing;
import com.dt.user.service.BasePublicService.BasicPublicSiteService;
import com.dt.user.service.BasePublicService.BasicSalesAmazonCsvTxtXslHeaderService;
import com.dt.user.service.BasePublicService.BasicSalesAmazonSkuService;
import com.dt.user.service.BasePublicService.BasicSalesAmazonWarehouseService;
import com.dt.user.service.ConsumerService;
import com.dt.user.service.SalesAmazonAdService.*;
import com.dt.user.toos.Constants;
import com.dt.user.utils.ArrUtils;
import com.dt.user.utils.CrrUtils;
import com.dt.user.utils.DateUtils;
import com.dt.user.utils.StrUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private BasicSalesAmazonWarehouseService warehouseService;

    @Autowired
    private BasicPublicSiteService siteService;
    @Autowired
    private BasicSalesAmazonSkuService skuService;
    @Autowired
    private BasicSalesAmazonCsvTxtXslHeaderService headService;
    @Autowired
    private SalesAmazonFbaTradeReportService tradeReportService;
    /**
     * 多线程返回接收
     */
    private Future<ResponseBase> future;
    @Autowired
    private SalesAmazonFbaRefundService refundService;

    @Autowired
    private SalesAmazonFbaReceivestockService receivestockService;

    @Autowired
    private SalesAmazonFbaInventoryEndService endService;


    //没有sku有几行存入
    ThreadLocal<Long> numberCount = ThreadLocal.withInitial(() -> 0L);
    //真实存入函数
    ThreadLocal<Long> count = ThreadLocal.withInitial(() -> 0L);
    //没有sku有几行存入
    ThreadLocal<Integer> sumErrorSku = ThreadLocal.withInitial(() -> 0);
    //获取没有SKU的List集合 并发List 容器
    ThreadLocal<List<List<String>>> noSkuList = new ThreadLocal<>();

    //获取没有SKU的List集合 并发List 容器
    private static ThreadLocal<Set<Timing>> timSet = new ThreadLocal<>();

    /**
     * 返回错误数据存入接口实例
     *
     * @return
     */
    @Override
    public List<List<String>> writeNoListSku() {
        return noSkuList.get();
    }

    /**
     * 返回setTiming实例
     *
     * @return
     */
    @Override
    public ThreadLocal<Set<Timing>> timingWrite() {
        return timSet;
    }

    /**
     * 线程池处理Txt Data数据
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
    @Override
    @Transactional
    @Async("executor")
    public Future<ResponseBase> dealWithTxtData(BufferedReader br, Long shopId, Long uid, Long recordingId, List<String> strLineHead, Timing timing, Integer tbId, Integer aId) throws IOException {
        future = new AsyncResult<>(saveTxt(br, shopId, uid, recordingId, strLineHead, timing, tbId, aId));
        return future;
    }

    /**
     * 线程池 处理数据CSv
     *
     * @throws IOException
     */
    public Future<ResponseBase> dealWithCsvData(CsvReader csvReader, int row, Long shopId, Long siteId, Long uid, Integer pId, Long recordingId, Integer tbId, String businessTime, Timing timing) {
        // future = new AsyncResult<>(saveCsv(csvReader, row, shopId, siteId, uid, pId, recordingId, tbId, businessTime, timing));
        return future;
    }

    private ResponseBase saveTxt(BufferedReader br, Long shopId, Long uid, Long
            recordingId, List<String> lineHead, Timing timing, Integer tbId, Integer aId) throws IOException {
        //每次进来之前清空当前线程List里面的数据
        CrrUtils.delList(noSkuList);
        // 开始时间
        Long begin = new Date().getTime();
        List<SalesAmazonFbaReceivestock> sfReceivesList = null;
        List<SalesAmazonFbaRefund> safRefundList = null;
        List<SalesAmazonFbaTradeReport> safTradList = null;
        List<SalesAmazonFbaInventoryEnd> safEndList = null;
        SalesAmazonFbaTradeReport sftPort;
        SalesAmazonFbaRefund sfRefund;
        SalesAmazonFbaReceivestock sfReceives;
        SalesAmazonFbaInventoryEnd sfEnd;
        String line;
        int index = 0;
        timing.setMsg("正在校验数据..........");
        List<?> tList = new ArrayList<>();
        switch (tbId) {
            case 109:
                safTradList = ArrUtils.listT(tList);
                break;
            case 110:
                safRefundList = ArrUtils.listT(tList);
                break;
            case 113:
                sfReceivesList = ArrUtils.listT(tList);
                break;
            case 114:
                safEndList = ArrUtils.listT(tList);
                break;
        }
        while ((line = br.readLine()) != null) {
            //numberCount++
            CrrUtils.inCreateNumberLong(numberCount);
            //count ++ 成功数量
            CrrUtils.inCreateNumberLong(count);
            // 一次读入一行数据
            String[] newLine = line.split("\t", -1);
            switch (tbId) {
                //订单报告
                case 109:
                    sftPort = setTraPort(shopId, uid, recordingId);
                    for (int i = 0; i < newLine.length; i++) {
                        sftPort = saveTradeReport(i, sftPort, newLine, shopId);
                        if (sftPort == null) {
                            //先拿到这一行信息 newLine
                            exportTxtType(lineHead, line);
                            break;
                        }
                    }
                    if (sftPort != null) {
                        safTradList.add(sftPort);
                    }
                    break;
                //退货报告
                case 110:
                    sfRefund = setRefund(shopId, uid, recordingId);
                    for (int i = 0; i < newLine.length; i++) {
                        sfRefund = salesAmazonFbaRefund(i, sfRefund, newLine, shopId, aId);
                        if (sfRefund == null) {
                            //先拿到这一行信息 newLine
                            exportTxtType(lineHead, line);
                            break;
                        }
                    }
                    if (sfRefund != null) {
                        safRefundList.add(sfRefund);
                    }
                    break;
                //接收库存
                case 113:
                    sfReceives = setReceives(shopId, uid, recordingId);
                    for (int i = 0; i < newLine.length; i++) {
                        sfReceives = salesReceivestock(i, sfReceives, newLine);
                        if (sfReceives == null) {
                            //先拿到这一行信息 newLine
                            exportTxtType(lineHead, line);
                            break;
                        }
                    }
                    if (sfReceives != null) {
                        sfReceivesList.add(sfReceives);
                    }
                    break;
                //期末库存
                case 114:
                    sfEnd = setEnd(shopId, uid, recordingId);
                    for (int i = 0; i < newLine.length; i++) {
                        sfEnd = salesEnd(i, sfEnd, newLine);
                        if (sfEnd == null) {
                            //先拿到这一行信息 newLine
                            exportTxtType(lineHead, line);
                            break;
                        }
                    }
                    if (sfEnd != null) {
                        safEndList.add(sfEnd);
                    }
                    break;
            }
            index++;
            //计算百分比
            timing.setAttributesTim(index);
            CrrUtils.inCreateSet(timSet, timing);
        }
        int countTrad = 0;
        if (safTradList != null) {
            if (safTradList.size() > 0) {
                //插入数据
                timing.setMsg("正在导入数据库..........");
                countTrad = tradeReportService.AddSalesAmazonAdTrdList(safTradList);
            }
        }
        if (safRefundList != null) {
            if (safRefundList.size() > 0) {
                //插入数据
                timing.setMsg("正在导入数据库..........");
                //导入数据库
                countTrad = refundService.AddSalesAmazonAdRefundList(safRefundList);

            }
        }
        if (sfReceivesList != null) {
            if (sfReceivesList.size() > 0) {
                //插入数据
                timing.setMsg("正在导入数据库..........");
                //导入数据库
                countTrad = receivestockService.AddSalesAmazonAdReceivestockList(sfReceivesList);
            }
        }
        if (safEndList != null) {
            if (safEndList.size() > 0) {
                //插入数据
                timing.setMsg("正在导入数据库..........");
                //导入数据库
                countTrad = endService.AddSalesAmazonAdInventoryEndList(safEndList);
            }
        }
        if (countTrad > 0) {
            return printCount(begin, timing, count.get(), index);
        }
        return BaseApiService.setResultError("数据存入异常,请检查错误信息");
    }

    /**
     * 通用打印输出语句 方法
     *
     * @param
     * @return
     */
    public ResponseBase printCount(Long begin, Timing timing, Long successNumber, int index) {
        timing.setInfo("success", "数据导入成功..........");
        CrrUtils.inCreateSet(timSet, timing);
        // 结束时间
        Long end = new Date().getTime();
        return BaseApiService.setResultSuccess("总共" + index + "条数据/" + successNumber + "条数据插入成功/====>失败 " + sumErrorSku.get() + "条/花费时间 : " + (end - begin) / 1000 + " s");
    }

    /**
     * 通用获得头信息对比
     *
     * @param seId
     * @param tbId
     * @return
     */
    public List<String> getHeadInfo(Long seId, int tbId, Integer areaId) {
        //85 tbId 跟 104 tbId头信息一致
        if (tbId == Constants.FINANCE_ID_YY) {
            return headService.headerList(seId, Constants.FINANCE_ID, areaId);
        }
        return headService.headerList(seId, tbId, areaId);
    }

    /**
     * 期末库存信息存入
     *
     * @param sft
     * @param j
     * @return
     * @throws IOException
     */
    public SalesAmazonFbaInventoryEnd salesEnd(int i, SalesAmazonFbaInventoryEnd sft, String[] j) {
        switch (i) {
            case 0:
                sft.setDate(DateUtils.getTime(j[i], Constants.ORDER_RETURN));
                break;
            case 1:
                sft.setFnsku(StrUtils.repString(j[i]));
                break;
            case 2:
                sft.setSku(StrUtils.repString(j[i]));
                break;
            case 3:
                sft.setProductName(StrUtils.repString(j[i]));
                break;
            case 4:
                sft.setQuantity(StrUtils.replaceInteger(j[i]));
                break;
            case 5:
                String fc = StrUtils.repString(j[i]);
                if (StringUtils.isEmpty(fc)) {
                    return null;
                }
                sft.setFc(fc);
                BasicSalesAmazonWarehouse warehouse = warehouseService.getWarehouse(fc);
                if (warehouse == null) {
                    return null;
                }
                if (warehouse.getSiteId() == null || warehouse.getAmazonWarehouseId() == null) {
                    return null;
                }
                sft.setSiteId(warehouse.getSiteId());
                sft.setAwId(warehouse.getAmazonWarehouseId());
                break;
            case 6:
                sft.setDisposition(StrUtils.repString(j[i]));
                break;
            case 7:
                sft.setCountry(StrUtils.repString(j[i]));
                break;
        }
        return sft;
    }

    /**
     * 接收订单信息存入
     *
     * @param sft
     * @param j
     * @return
     * @throws IOException
     */
    public SalesAmazonFbaReceivestock salesReceivestock(int i, SalesAmazonFbaReceivestock sft, String[] j) {
        switch (i) {
            case 0:
                sft.setDate(DateUtils.getTime(j[i], Constants.ORDER_RETURN));
                break;
            case 1:
                sft.setFnsku(StrUtils.repString(j[i]));
                break;
            case 2:
                sft.setSku(StrUtils.repString(j[i]));
                break;
            case 3:
                sft.setProductName(StrUtils.repString(j[i]));
                break;
            case 4:
                sft.setQuantity(StrUtils.replaceInteger(j[i]));
                break;
            case 5:
                sft.setFbaShipmentId(StrUtils.repString(j[i]));
                break;
            case 6:
                String fc = StrUtils.repString(j[i]);
                if (StringUtils.isEmpty(fc)) {
                    return null;
                }
                sft.setFc(fc);
                BasicSalesAmazonWarehouse warehouse = warehouseService.getWarehouse(fc);
                if (warehouse == null) {
                    return null;
                }
                if (warehouse.getSiteId() == null || warehouse.getAmazonWarehouseId() == null) {
                    return null;
                }
                sft.setSiteId(warehouse.getSiteId());
                sft.setAwId(warehouse.getAmazonWarehouseId());
                break;
        }
        return sft;
    }

    /**
     * 退货报告信息存入
     *
     * @param sft
     * @param j
     * @return
     * @throws IOException
     */
    public SalesAmazonFbaRefund salesAmazonFbaRefund(int i, SalesAmazonFbaRefund sft, String[] j, Long sId, Integer aId) {
        switch (i) {
            case 0:
                sft.setDate(DateUtils.getTime(j[i], Constants.ORDER_RETURN));
                break;
            case 1:
                String oId = StrUtils.repString(j[i]);
                if (StringUtils.isEmpty(oId)) {
                    return null;
                }
                sft.setOrderId(oId);
                //查询 获得site Id
                SalesAmazonFbaTradeReport serviceReport = tradeReportService.getReport(sId, oId);
                if (serviceReport == null) {
                    return null;
                }
                //如果有一个是空的 就返回null
                if (serviceReport.getDate() == null || serviceReport.getSiteId() == null) {
                    return null;
                }
                sft.setSiteId(serviceReport.getSiteId());
                sft.setPurchaseDate(serviceReport.getDate());
                break;
            case 2:
                sft.setSku(StrUtils.repString(j[i]));
                break;
            case 3:
                sft.setsAsin(StrUtils.repString(j[i]));
                boolean isFlgId = skuEqAsin(sft.getSku(), sft.getsAsin(), sId, sft.getSiteId(), sft);
                if (!isFlgId) {
                    return null;
                }
                break;
            case 4:
                sft.setFnsku(StrUtils.repString(j[i]));
                break;
            case 5:
                sft.setpName(StrUtils.repString(j[i]));
                break;
            case 6:
                sft.setQuantity(StrUtils.replaceInteger(j[i]));
                break;
            case 7:
                sft.setFc(StrUtils.repString(j[i]));
                break;
            case 8:
                sft.setDetailedDisposition(StrUtils.repString(j[i]));
                break;
            case 9:
                sft.setReason(StrUtils.repString(j[i]));
                break;
            case 10:
                if (aId == 4 && sft.getSiteId() == 9) {
                    sft.setLicensePlateNumber(StrUtils.repString(j[i]));
                } else {
                    sft.setRefundStaus(StrUtils.repString(j[i]));
                }
                break;
            case 11:
                if (aId == 4 && sft.getSiteId() == 9) {
                    sft.setCustomerRemarks(StrUtils.repString(j[i]));
                } else {
                    sft.setLicensePlateNumber(StrUtils.repString(j[i]));
                }
                break;
            case 12:
                sft.setCustomerRemarks(StrUtils.repString(j[i]));
                break;
        }
        return sft;
    }

    /**
     * 订单报告信息存入
     *
     * @param sft
     * @param j
     * @return
     * @throws IOException
     */
    public SalesAmazonFbaTradeReport saveTradeReport(int i, SalesAmazonFbaTradeReport sft, String[] j, Long sId) {
        switch (i) {
            case 0:
                sft.setAmazonOrderId(StrUtils.repString(j[i]));
                break;
            case 1:
                sft.setMerchantOrderId(StrUtils.repString(j[i]));
                break;
            case 2:
                sft.setDate(DateUtils.getTime(j[i], Constants.ORDER_RETURN));
                break;
            case 3:
                sft.setLastUpdatedDate(DateUtils.getTime(j[i], Constants.ORDER_RETURN));
                break;
            case 4:
                sft.setOrderStatus(StrUtils.repString(j[i]));
                break;
            case 5:
                sft.setFulfillmentChannel(StrUtils.repString(j[i]));
                break;
            case 6:
                String siteUrl = StrUtils.repString(j[i]);
                sft.setSalesChannel(siteUrl);
                //查询 获得site Id
                Long siteId = siteService.getSiteId(siteUrl);
                if (siteId == null) {
                    return null;
                }
                sft.setSiteId(siteId);
                break;
            case 7:
                sft.setOrderChannel(StrUtils.repString(j[i]));
                break;
            case 8:
                sft.setUrl(StrUtils.repString(j[i]));
                break;
            case 9:
                sft.setShipServiceLevel(StrUtils.repString(j[i]));
                break;
            case 10:
                sft.setProductName(StrUtils.repString(j[i]));
                break;
            case 11:
                sft.setSku(StrUtils.repString(j[i]));
                break;
            case 12:
                sft.setAsin(StrUtils.repString(j[i]));
                boolean isFlgId = skuEqAsin(sft.getSku(), sft.getAsin(), sId, sft.getSiteId(), sft);
                if (!isFlgId) {
                    return null;
                }
                break;
            case 13:
                sft.setItemStatus(StrUtils.repString(j[i]));
                break;
            case 14:
                sft.setQuantity(StrUtils.replaceInteger(j[i]));
                break;
            case 15:
                sft.setCurrency(StrUtils.repString(j[i]));
                break;
            case 16:
                sft.setItemPrice(StrUtils.repDouble(j[i]));
                break;
            case 17:
                sft.setItemTax(StrUtils.repDouble(j[i]));
                break;
            case 18:
                sft.setShippingPrice(StrUtils.repDouble(j[i]));
                break;
            case 19:
                sft.setShippingPrice(StrUtils.repDouble(j[i]));
                break;
            case 20:
                sft.setGiftWrapPrice(StrUtils.repDouble(j[i]));
                break;
            case 21:
                sft.setGiftWrapTax(StrUtils.repDouble(j[i]));
                break;
            case 22:
                sft.setItemPromotionDiscount(StrUtils.repDouble(j[i]));
                break;
            case 23:
                sft.setShipPromotionDiscount(StrUtils.repDouble(j[i]));
                break;
            case 24:
                sft.setShipCity(StrUtils.repString(j[i]));
                break;
            case 25:
                sft.setShipState(StrUtils.repString(j[i]));
                break;
            case 26:
                sft.setShipPostalCode(StrUtils.repString(j[i]));
                break;
            case 27:
                sft.setShipCountry(StrUtils.repString(j[i]));
                break;
            case 28:
                sft.setPromotionIds(StrUtils.repString(j[i]));
                break;
            case 29:
                sft.setIsBusinessOrder(StrUtils.repString(j[i]));
                break;
            case 30:
                sft.setPurchaseOrderNumber(StrUtils.repString(j[i]));
                break;
            case 31:
                sft.setPriceDesignation(StrUtils.repString(j[i]));
                break;
            case 32:
                sft.setIsReplacementOrder(StrUtils.repString(j[i]));
                break;
            case 33:
                sft.setOriginalOrderId(StrUtils.repString(j[i]));
                break;
        }
        return sft;
    }

    /**
     * 洲业务 sku asin  业务对比获得sku
     *
     * @param sku
     * @param asin
     */
    public boolean skuEqAsin(String sku, String asin, Long sId, Long seId, Object obj) {
        if (StringUtils.isNotEmpty(sku) && StringUtils.isNotEmpty(asin)) {
            //查询skuId
            Long skuId = skuService.selSkuId(sId, seId, sku);
            Long asinId = skuService.getAsinSkuId(sId, seId, asin);
            if (skuId == null || asinId == null) {
                return false;
            }
            //订单
            if (obj instanceof SalesAmazonFbaTradeReport) {
                SalesAmazonFbaTradeReport sftReport = (SalesAmazonFbaTradeReport) obj;
                if (skuId.equals(asinId)) {
                    sftReport.setSkuId(skuId);
                    return true;
                }
            }
            //退货
            if (obj instanceof SalesAmazonFbaRefund) {
                SalesAmazonFbaRefund sftRefund = (SalesAmazonFbaRefund) obj;
                if (skuId.equals(asinId)) {
                    sftRefund.setSkuId(skuId);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 通用设置Txt 没有sku/导出文件
     *
     * @return
     */
    public void exportTxtType(List<String> head, String line) {
        List<List<String>> skuNoIdList = CrrUtils.inCreateList(noSkuList);
        //count --
        CrrUtils.delCreateNumberLong(count);
        //sumNoSku ++
        CrrUtils.inCreateNumberInteger(sumErrorSku);
        if (skuNoIdList.size() == 0) {
            skuNoIdList.add(head);
        }
        List<String> skuListNo = new ArrayList<>();
        skuListNo.add(line);
        skuNoIdList.add(skuListNo);
        noSkuList.set(skuNoIdList);
    }

//################通用设置表头

    /**
     * 期末库存通用对象
     */
    public SalesAmazonFbaInventoryEnd setEnd(Long sId, Long uid, Long recordingId) {
        return new SalesAmazonFbaInventoryEnd(sId, new Date().getTime(), uid, recordingId);
    }

    /**
     * 接收库存通用对象
     */
    public SalesAmazonFbaReceivestock setReceives(Long sId, Long uid, Long recordingId) {
        return new SalesAmazonFbaReceivestock(sId, new Date().getTime(), uid, recordingId);
    }

    /**
     * 退货报告通用对象
     */
    public SalesAmazonFbaRefund setRefund(Long sId, Long uid, Long recordingId) {
        return new SalesAmazonFbaRefund(sId, new Date().getTime(), uid, recordingId);
    }

    /**
     * 订单报告通用对象
     */
    public SalesAmazonFbaTradeReport setTraPort(Long sId, Long uid, Long recordingId) {
        return new SalesAmazonFbaTradeReport(sId, new Date().getTime(), uid, recordingId);
    }

    /**
     * 业务报告通用对象
     */
    public SalesAmazonFbaBusinessreport setBusPort(Long sId, Long seId, Long uid, Long recordingId) {
        return new SalesAmazonFbaBusinessreport(sId, seId, new Date().getTime(), uid, recordingId);
    }

    /**
     * 财务设置通用对象
     */
    public FinancialSalesBalance setFsb(Long sId, Long seId, Long uid, Long pId, Long recordingId) {
        return new FinancialSalesBalance(sId, seId, pId, new Date().getTime(), uid, recordingId);
    }

    /**
     * H1设置通用对象
     */
    public SalesAmazonAdHl setHl(Long sId, Long seId, Long uid, Long recordingId) {
        return new SalesAmazonAdHl(sId, seId, new Date().getTime(), uid, recordingId);
    }

    /**
     * Cpr设置通用对象
     */
    public SalesAmazonAdCpr setCpr(Long sId, Long seId, Long uid, Long recordingId) {
        return new SalesAmazonAdCpr(sId, seId, new Date().getTime(), uid, recordingId);
    }

    /**
     * Oar设置通用对象
     */
    public SalesAmazonAdOar setOar(Long sId, Long seId, Long uid, Long recordingId) {
        return new SalesAmazonAdOar(sId, seId, new Date().getTime(), uid, recordingId);
    }

    /**
     * Str设置通用对象
     */
    public SalesAmazonAdStr setStr(Long sId, Long seId, Long uid, Long recordingId) {
        return new SalesAmazonAdStr(sId, seId, new Date().getTime(), uid, recordingId);
    }

    //################通用设置表头


}
