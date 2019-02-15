package com.dt.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.mapper.BasePublicMapper.BasicPublicAmazonTypeMapper;
import com.dt.user.model.BasePublicModel.BasicSalesAmazonWarehouse;
import com.dt.user.model.FinancialSalesBalance;
import com.dt.user.model.SalesAmazonAd.*;
import com.dt.user.model.Timing;
import com.dt.user.model.UserUpload;
import com.dt.user.service.BasePublicService.BasicPublicSiteService;
import com.dt.user.service.BasePublicService.BasicSalesAmazonCsvTxtXslHeaderService;
import com.dt.user.service.BasePublicService.BasicSalesAmazonSkuService;
import com.dt.user.service.BasePublicService.BasicSalesAmazonWarehouseService;
import com.dt.user.service.ConsumerService;
import com.dt.user.service.FinancialSalesBalanceService;
import com.dt.user.service.SalesAmazonAdService.*;
import com.dt.user.service.UserUploadService;
import com.dt.user.service.WebSocketServer;
import com.dt.user.toos.Constants;
import com.dt.user.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;
import java.util.concurrent.Future;

@Service
public class ConsumerServiceImpl implements ConsumerService {


    @Autowired
    private SalesAmazonFbaBusinessreportService busService;
    @Autowired
    private FinancialSalesBalanceService fsbService;
    @Autowired
    private SalesAmazonAdCprService cprService;

    @Autowired
    private SalesAmazonAdStrService strService;

    @Autowired
    private SalesAmazonAdOarService oarService;

    @Autowired
    private SalesAmazonAHlService hlService;


    @Autowired
    private BasicPublicAmazonTypeMapper typeMapper;
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

    @Autowired
    private SalesAmazonFbaRefundService refundService;

    @Autowired
    private SalesAmazonFbaReceivestockService receivestockService;

    @Autowired
    private SalesAmazonFbaInventoryEndService endService;

    @Autowired
    private UserUploadService userUploadService;
    @Autowired
    private WebSocketServer ws;

    /**
     * 多线程返回接收
     */
    private Future<ResponseBase> future;
    /**
     * 没有sku有几行存入
     */
    private ThreadLocal<Long> numberCount = ThreadLocal.withInitial(() -> 0L);
    /**
     * 真实存入
     */
    private ThreadLocal<Long> count = ThreadLocal.withInitial(() -> 0L);
    /**
     * 没有sku存入
     */
    private ThreadLocal<Integer> sumErrorSku = ThreadLocal.withInitial(() -> 0);
    /**
     * 获取没有SKU的List集合
     */
    private ThreadLocal<List<List<String>>> noSkuList = new ThreadLocal<>();
    /**
     * 实时数据Set集合
     */
    private ThreadLocal<Set<Timing>> timSet = new ThreadLocal<>();
//#######################Txt

    /**
     * 异步处理Txt数据
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
    @Override
    @Transactional
    @Async("executor")
    public Future<ResponseBase> importTxt(String uuIdName, String saveFilePath, String fileName, Long shopId, Long uid, Long recordingId, Integer tbId, Integer aId) {
        future = new AsyncResult<>(threadTxt(uuIdName, saveFilePath, fileName, shopId, uid, recordingId, tbId, aId));
        return future;
    }

    private ResponseBase threadTxt(String uuIdName, String saveFilePath, String fileName, Long shopId, Long uid, Long recordingId, Integer tbId, Integer aId) {
        Timing timing = new Timing();
        ResponseBase responseCsv;
        String filePath = saveFilePath + uuIdName;
        try (InputStreamReader read = streamReader(filePath);
             BufferedReader br = new BufferedReader(read)
        ) {
            //拿到数据库的表头 进行校验
            List<String> head = getHeadInfo(null, tbId, aId);
            //对比头部
            String lineHead = br.readLine();
            List<String> txtHead = Arrays.asList(lineHead.split("\t"));
            boolean isFlg = ArrUtils.eqOrderList(head, txtHead);
            timing.setInfo(fileName, recordingId);
            if (!isFlg) {
                //返回错误信息
                return errorResult(0, "表头信息不一致", recordingId, fileName, timing, "exception", saveFilePath, uuIdName);
            }
            //设置文件总数
            timing.setFileCount(filePath);
            //第一行List头
            List<String> strLineHead = new ArrayList<>();
            strLineHead.add(lineHead);
            //多线程处理
            responseCsv = saveTxt(br, shopId, uid, recordingId, strLineHead, timing, tbId, aId);
            return saveUserUploadInfo(responseCsv, recordingId, fileName, null, 3, saveFilePath, uuIdName);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            String errorMsg = "数据存入失败====>请查找" + (numberCount.get() + 1) + "行错误信息";
            return errorResult(0, errorMsg, recordingId, fileName, timing, "exception", saveFilePath, uuIdName);
        } finally {
            CrrUtils.clearListThread(noSkuList);
            count.set(0L);
            numberCount.set(0L);
        }
    }

    private ResponseBase saveTxt(BufferedReader br, Long shopId, Long uid, Long
            recordingId, List<String> lineHead, Timing timing, Integer tbId, Integer aId) throws IOException {
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
        //计算返回前端的数
        Map<String, Integer> intMap = new HashMap<>();
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
                        sfReceives = salesReceiveStock(i, sfReceives, newLine);
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
            int currentCount = timing.setAttributesTim(index);
            ws.schedule(intMap, currentCount, timSet, timing, 1L);
        }
        //插入数据
        timing.setMsg("正在导入数据库..........");
        ws.sendInfo(JSON.toJSONString(CrrUtils.inCreateSet(timSet, timing)), 1L);
        int countTrad = 0;
        if (safTradList != null) {
            if (safTradList.size() > 0) {
                countTrad = tradeReportService.AddSalesAmazonAdTrdList(safTradList);
            }
        }
        if (safRefundList != null) {
            if (safRefundList.size() > 0) {
                //导入数据库
                countTrad = refundService.AddSalesAmazonAdRefundList(safRefundList);
            }
        }
        if (sfReceivesList != null) {
            if (sfReceivesList.size() > 0) {
                //导入数据库
                countTrad = receivestockService.AddSalesAmazonAdReceivestockList(sfReceivesList);
            }
        }
        if (safEndList != null) {
            if (safEndList.size() > 0) {
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
    public SalesAmazonFbaReceivestock salesReceiveStock(int i, SalesAmazonFbaReceivestock sft, String[] j) {
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

    //#######################Txt
    //#######################Xls
    @Override
    @Transactional
    @Async("executor")
    public Future<ResponseBase> importXls(String uuIdName, String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Long recordingId, Integer tbId) {
        future = new AsyncResult<>(threadXls(uuIdName, saveFilePath, fileName, siteId, shopId, uid,
                recordingId, tbId));
        return future;
    }


    public ResponseBase threadXls(String uuIdName, String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Long
            recordingId, Integer tbId) {
        Timing timing = new Timing();
        String filePath = saveFilePath + uuIdName;
        ResponseBase responseBase;
        //判断文件类型 fileType()
        File file = new File(filePath);
        try (FileInputStream in = new FileInputStream(filePath);
             Workbook wb = XlsUtils.fileType(in, file)) {
            if (wb == null) {
                //返回错误信息
                return errorResult(0, "不是excel文件", recordingId, fileName, timing, "exception", filePath, uuIdName);
            }
            Sheet sheet = wb.getSheetAt(0);
            int totalNumber = sheet.getRow(0).getPhysicalNumberOfCells(); //获取总列数
            //拿到数据库的表头 进行校验 !!!这里还可以优化 暂定
            List<String> head = getHeadInfo(siteId, tbId, null);
            //对比表头
            boolean isFlg = compareHeadXls(totalNumber, sheet, head);
            //必须在 setTiming.add 前设置id
            timing.setInfo(fileName, recordingId);
            //如果表头对比失败
            if (!isFlg) {
                //返回错误信息
                return errorResult(0, "表头信息不一致", recordingId, fileName, timing, "exception", filePath, uuIdName);
            }
            responseBase = saveXls(shopId, siteId, uid, recordingId, totalNumber, head, tbId, sheet, timing);
            return saveUserUploadInfo(responseBase, recordingId, fileName, null, 1, filePath, uuIdName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            String errorMsg = "数据存入失败====>请查找" + (numberCount.get() + 1) + "行错误信息" + e.getMessage();
            return errorResult(0, errorMsg, recordingId, fileName, timing, "exception", filePath, uuIdName);
        } finally {
            CrrUtils.clearListThread(noSkuList);
            count.set(0L);
            numberCount.set(0L);
        }
    }

    /**
     * 读取Cprxls 信息
     *
     * @param shopId
     * @param siteId
     * @param uid
     * @param recordingId
     * @param totalNumber
     * @param sheet
     * @return
     */
    public ResponseBase saveXls(Long shopId, Long siteId, Long uid, Long
            recordingId, int totalNumber, List<String> head, Integer tbId, Sheet sheet, Timing timing) throws IOException {
        // 开始时间
        Long begin = new Date().getTime();
        Row row;
        Cell cell;
        List<SalesAmazonAdCpr> cprList = null;
        List<SalesAmazonAdStr> strList = null;
        List<SalesAmazonAdOar> oarList = null;
        List<SalesAmazonAdHl> hlList = null;
        SalesAmazonAdHl adHl;
        SalesAmazonAdOar adOar;
        SalesAmazonAdCpr saCpr;
        SalesAmazonAdStr adStr;
        List<?> tList = new ArrayList<>();
        if (tbId == 105) {
            cprList = ArrUtils.listT(tList);
        } else if (tbId == 107) {
            strList = ArrUtils.listT(tList);
        } else if (tbId == 106) {
            oarList = ArrUtils.listT(tList);
        } else if (tbId == 125) {
            hlList = ArrUtils.listT(tList);
        }
        int index = 0;
        int line = 1;
        int lastRowNum = sheet.getLastRowNum(); // 获取总行数
        timing.setTotalNumber((double) lastRowNum);
        Map<String, Integer> intMap = new HashMap<>();
        timing.setMsg("正在校验数据..........");
        for (int i = line; i <= lastRowNum; i++) {
            //numberCount++
            CrrUtils.inCreateNumberLong(numberCount);
            //count ++ 成功数量
            CrrUtils.inCreateNumberLong(count);
            row = sheet.getRow(i);
            // 105 cpr
            if (tbId == 105) {
                saCpr = setCpr(shopId, siteId, uid, recordingId);
                for (int j = 0; j < totalNumber; j++) {
                    cell = row.getCell(j);
                    saCpr = setCprPojo(j, saCpr, cell);
                }
                Long skuId = skuService.selSkuId(shopId, siteId, saCpr.getAdvertisedSku());
                //设置没有SKU的信息导入
                if (xslSkuList(skuId, saCpr, row, totalNumber, head) != null) {
                    cprList.add((SalesAmazonAdCpr) xslSkuList(skuId, saCpr, row, totalNumber, head));
                }
                //107 str
            } else if (tbId == 107) {
                adStr = setStr(shopId, siteId, uid, recordingId);
                for (int j = 0; j < totalNumber; j++) {
                    cell = row.getCell(j);
                    adStr = setStrPojo(j, adStr, cell);
                }
                strList.add(adStr);
                //106 oar
            } else if (tbId == 106) {
                adOar = setOar(shopId, siteId, uid, recordingId);
                for (int j = 0; j < totalNumber; j++) {
                    cell = row.getCell(j);
                    adOar = setOarPojo(j, adOar, cell);
                }
                Long skuId = skuService.getAsinSkuId(shopId, siteId, adOar.getOtherAsin());
                //设置没有SKU的信息导入
                if (xslSkuList(skuId, adOar, row, totalNumber, head) != null) {
                    oarList.add((SalesAmazonAdOar) xslSkuList(skuId, adOar, row, totalNumber, head));
                }
            } else if (tbId == 125) {
                adHl = setHl(shopId, siteId, uid, recordingId);
                for (int j = 0; j < totalNumber; j++) {
                    row = sheet.getRow(i);
                    cell = row.getCell(j);
                    adHl = setHlPojo(j, adHl, cell);
                }
                hlList.add(adHl);
            }
            index++;
            //计算百分比
            int currentCount = timing.setAttributesTim(index);
            ws.schedule(intMap, currentCount, timSet, timing, 1L);
        }
        int saveCount = 0;
        //插入数据
        timing.setMsg("正在导入数据库..........");
        ws.sendInfo(JSON.toJSONString(CrrUtils.inCreateSet(timSet, timing)), 1L);
        if (cprList != null) {
            if (cprList.size() > 0) {
                saveCount = cprService.AddSalesAmazonAdCprList(cprList);
            }
        }
        if (strList != null) {
            if (strList.size() > 0) {
                saveCount = strService.AddSalesAmazonAdStrList(strList);
            }
        }
        if (oarList != null) {
            if (oarList.size() > 0) {
                saveCount = oarService.AddSalesAmazonAdOarList(oarList);
            }
        }
        if (hlList != null) {
            if (hlList.size() > 0) {
                saveCount = hlService.AddSalesAmazonAdHlList(hlList);
            }
        }
        if (saveCount > 0) {
            return printCount(begin, timing, count.get(), index);
        }
        return BaseApiService.setResultError("数据存入异常,请检查错误信息");
    }

    /**
     * xsl 获取没有SKU的文件List
     *
     * @param skuId
     * @return
     */
    public Object xslSkuList(Long skuId, Object obj, Row row, int totalNumber, List<String> head) {
        String strXsl;
        if (obj instanceof SalesAmazonAdCpr) {
            SalesAmazonAdCpr cpr = (SalesAmazonAdCpr) obj;
            if (StringUtils.isNotEmpty(cpr.getAdvertisedSku())) {
                strXsl = skuSetting(skuId, row, totalNumber, head);
                if (strXsl == null) {
                    return null;
                }
            }
            cpr.setSkuId(skuId);
            return cpr;
        }
        if (obj instanceof SalesAmazonAdOar) {
            SalesAmazonAdOar oar = (SalesAmazonAdOar) obj;
            if (StringUtils.isNotEmpty(oar.getOtherAsin())) {
                strXsl = skuSetting(skuId, row, totalNumber, head);
                if (strXsl == null) {
                    return null;
                }
            }
            oar.setSkuId(skuId);
            return oar;
        }
        return null;
    }

    /**
     * xls/sku设置
     *
     * @param skuId
     * @return
     */
    public String skuSetting(Long skuId, Row row, int totalNumber, List<String> head) {
        if (skuId == null) {
            CrrUtils.inCreateList(noSkuList);
            //如果等于0 就先设置头
            if (noSkuList.get().size() == 0) {
                noSkuList.get().add(head);
            }
            //count --
            CrrUtils.delCreateNumberLong(count);
            //sumNoSku ++
            CrrUtils.inCreateNumberInteger(sumErrorSku);
            List<String> skuListNo = new ArrayList<>();
            //拿到那一行信息
            for (int i = 0; i < totalNumber; i++) {
                skuListNo.add(row.getCell(i).toString());
            }
            noSkuList.get().add(skuListNo);
            noSkuList.set(noSkuList.get());
            return null;
        }
        return "success";
    }

    /**
     * set pojo cpr
     */
    public SalesAmazonAdCpr setCprPojo(int j, SalesAmazonAdCpr saCpr, Cell cell) {
        String strAdCpr;
        switch (j) {
            case 0:
                saCpr.setDate(lon(cell));
                break;
            case 2:
                strAdCpr = str(cell);
                saCpr.setCampaignName(strAdCpr);
                break;
            case 3:
                strAdCpr = str(cell);
                saCpr.setAdGroupName(strAdCpr);
                break;
            case 4:
                strAdCpr = str(cell);
                saCpr.setAdvertisedSku(strAdCpr);
                break;
            case 5:
                strAdCpr = str(cell);
                saCpr.setAdvertisedAsin(strAdCpr);
                break;
            case 6:
                saCpr.setImpressions(dou(cell));
                break;
            case 7:
                saCpr.setClicks(dou(cell));
                break;
            case 10:
                saCpr.setTotalSpend(dou(cell));
                break;
            case 11:
                saCpr.setSales(dou(cell));
                break;
            case 13:
                saCpr.setRoas(dou(cell));
                break;
            case 14:
                saCpr.setOrdersPlaced(dou(cell));
                break;
            case 15:
                saCpr.setTotalUnits(dou(cell));
                break;
            case 17:
                saCpr.setSameskuUnitsOrdered(dou(cell));
                break;
            case 18:
                saCpr.setOtherskuUnitsOrdered(dou(cell));
                break;
            case 19:
                saCpr.setSameskuUnitsSales(dou(cell));
                break;
            case 20:
                saCpr.setOtherskuUnitsSales(dou(cell));
                break;
        }
        return saCpr;
    }


    /**
     * set pojo str
     */
    public SalesAmazonAdStr setStrPojo(int j, SalesAmazonAdStr adStr, Cell cell) {
        String strAdStr;
        switch (j) {
            case 0:
                adStr.setDate(lon(cell));
                break;
            case 2:
                strAdStr = str(cell);
                adStr.setCampaignName(strAdStr);
                break;
            case 3:
                strAdStr = str(cell);
                adStr.setAdGroupName(strAdStr);
                break;
            case 4:
                strAdStr = str(cell);
                adStr.setTargeting(strAdStr);
                break;
            case 5:
                strAdStr = str(cell);
                adStr.setMatchType(strAdStr);
                break;
            case 6:
                strAdStr = str(cell);
                adStr.setCustomerSearchTerm(strAdStr);
                break;
            case 7:
                adStr.setImpressions(dou(cell));
                break;
            case 8:
                adStr.setClicks(dou(cell));
                break;
            case 11:
                adStr.setTotalSpend(dou(cell));
                break;
            case 12:
                adStr.setSales(dou(cell));
                break;
            case 14:
                adStr.setRoas(dou(cell));
                break;
            case 15:
                adStr.setOrdersPlaced(dou(cell));
                break;
            case 16:
                adStr.setTotalUnits(dou(cell));
                break;
            case 18:
                adStr.setAdvertisedSkuUnitsOrdered(dou(cell));
                break;
            case 19:
                adStr.setOtherSkuUnitsOrdered(dou(cell));
                break;
            case 20:
                adStr.setAdvertisedSkuUnitsSales(dou(cell));
                break;
            case 21:
                adStr.setOtherSkuUnitsSales(dou(cell));
                break;
        }
        return adStr;
    }

    /**
     * set pojo oar
     */
    public SalesAmazonAdOar setOarPojo(int j, SalesAmazonAdOar adOar, Cell cell) {
        String strAdOar;
        switch (j) {
            case 0:
                adOar.setDate(lon(cell));
                break;
            case 2:
                strAdOar = str(cell);
                adOar.setCampaignName(strAdOar);
                break;
            case 3:
                strAdOar = str(cell);
                adOar.setAdGroupName(strAdOar);
                break;
            case 4:
                strAdOar = str(cell);
                adOar.setAdvertisedSku(strAdOar);
                break;
            case 5:
                strAdOar = str(cell);
                adOar.setAdvertisedAsin(strAdOar);
                break;
            case 6:
                strAdOar = str(cell);
                adOar.setTargeting(strAdOar);
                break;
            case 7:
                strAdOar = str(cell);
                adOar.setMatchType(strAdOar);
                break;
            case 8:
                strAdOar = str(cell);
                adOar.setOtherAsin(strAdOar);
                break;
            case 9:
                adOar.setOtherAsinUnits(dou(cell));
                break;
            case 10:
                adOar.setOtherAsinUnitsOrdered(dou(cell));
                break;
            case 11:
                adOar.setOtherAsinUnitsOrderedSales(dou(cell));
                break;
        }
        return adOar;
    }

    /**
     * set pojo hl
     */
    public SalesAmazonAdHl setHlPojo(int j, SalesAmazonAdHl adHl, Cell cell) {
        String strAdHl;
        switch (j) {
            case 0:
                adHl.setDate(lon(cell));
                break;
            case 2:
                strAdHl = str(cell);
                adHl.setCampaignName(strAdHl);
                break;
            case 3:
                adHl.setImpressions(dou(cell));
                break;
            case 4:
                adHl.setClicks(dou(cell));
                break;
            case 5:
                adHl.setCtr(dou(cell));
                break;
            case 6:
                adHl.setCpc(dou(cell));
                break;
            case 7:
                adHl.setSpend(dou(cell));
                break;
            case 8:
                adHl.setAcos(dou(cell));
                break;
            case 9:
                adHl.setRoas(dou(cell));
                break;
            case 10:
                adHl.setTotalSales(dou(cell));
                break;
            case 11:
                adHl.setTotalOrders(dou(cell));
                break;
            case 12:
                adHl.setTotalUnits(dou(cell));
                break;
            case 13:
                adHl.setConversionRate(dou(cell));
                break;
        }
        return adHl;
    }

    /**
     * 封装 String  类型转换
     *
     * @return
     */
    public String str(Object obj) {
        String strObj;
        if (obj instanceof Cell) {
            Cell cell = (Cell) obj;
            strObj = StrUtils.repString(XlsUtils.getCellValue(cell).trim());
            if (strObj.equals("-1")) {
                return null;
            }
            return strObj;
        }
        return null;

    }

    /**
     * 封装 Long  类型转换
     *
     * @param cell
     * @return
     */
    public Long lon(Cell cell) {
        Long lonCell;
        lonCell = StrUtils.replaceLong(XlsUtils.getCellValue(cell).trim());
        if (lonCell == -1L) {
            return null;
        }
        return lonCell;
    }

    /**
     * 封装 Doublie  类型转换
     *
     * @param cell
     * @return
     */
    public Double dou(Cell cell) {
        Double DouCell;
        DouCell = StrUtils.repDouble(XlsUtils.getCellValue(cell).trim());
        if (DouCell == -1.0) {
            return null;
        }
        return DouCell;
    }

    /**
     * 对比xls 表头信息是否一致
     *
     * @param totalNumber
     * @param sheet
     * @return
     */
    public boolean compareHeadXls(int totalNumber, Sheet sheet, List<String> slqHead) {
        Row row;
        Cell cell;
        List<String> twoList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < totalNumber; j++) {
                cell = row.getCell(j);
                twoList.add(cell.toString().trim());
                System.out.println(cell.toString().trim());
            }
        }
        return ArrUtils.eqOrderList(slqHead, twoList);
    }
    //#######################Xls


    //#######################Csv
    @Override
    public Future<ResponseBase> importCsv(String uuIdName, String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Integer pId, Long recordingId, Integer tbId, String businessTime) {
        future = new AsyncResult<>(threadCsv(uuIdName, saveFilePath, fileName, siteId, shopId, uid,
                pId, recordingId, tbId, businessTime));
        return future;
    }

    /**
     * 封装csv店铺选择
     *
     * @param saveFilePath
     * @param fileName
     * @param siteId
     * @param shopId
     * @return
     */
    public ResponseBase threadCsv(String uuIdName, String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Integer
            pId, Long recordingId, Integer tbId, String businessTime) {
        ResponseBase responseCsv;
        List<String> fileHeadList;
        String filePath = saveFilePath + uuIdName;
        String csvJson;
        JSONObject rowJson;
        int row;
        // 创建CSV读对象
        CsvReader csvReader = null;
        //获得头信息长度
        Timing timing = new Timing();
        csvJson = CSVUtil.startReadLine(filePath, siteId, tbId);
        rowJson = JSONObject.parseObject(csvJson);
        row = (Integer) rowJson.get("index");
        //必须在这里设置id
        timing.setInfo(fileName, recordingId);
        if (row == -1) {
            //返回错误信息
            return errorResult(0, "表中真实字段第一行信息比对不上", recordingId, fileName, timing, "exception", filePath, uuIdName);
        }
        //拿到之前的表头信息
        fileHeadList = JSONObject.parseArray(rowJson.get("head").toString(), String.class);
        //拿到数据库的表头 进行校验
        List<String> sqlHeadList = getHeadInfo(siteId, tbId, null);
        //对比表头是否一致
        boolean isFlg = compareHeadCsv(fileHeadList, sqlHeadList);
        if (!isFlg) {
            //返回错误信息
            return errorResult(0, "表头信息不一致", recordingId, fileName, timing, "exception", saveFilePath, uuIdName);
        }
        try (InputStreamReader isr = streamReader(filePath)) {
            csvReader = new CsvReader(isr);
            //设置文件总数
            timing.setFileCount(filePath);
            responseCsv = saveCsv(csvReader, row, shopId, siteId, uid, pId, recordingId, tbId, businessTime, timing);
            return saveUserUploadInfo(responseCsv, recordingId, fileName, fileHeadList, 2, saveFilePath, uuIdName);
        } catch (Exception e) {
            String errorMsg = "数据存入失败====>请查找" + (numberCount.get() + 1) + "行错误信息" + e.getMessage();
            return errorResult(0, errorMsg, recordingId, fileName, timing, "exception", saveFilePath, uuIdName);
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
            CrrUtils.clearListThread(noSkuList);
            count.set(0L);
            numberCount.set(0L);
        }
    }

    /**
     * csv财务数据解析
     *
     * @param row
     * @param sId
     * @param seId
     * @param uid
     * @return
     */
    public ResponseBase saveCsv(CsvReader csvReader, int row, Long sId, Long seId, Long uid, Integer pId, Long
            recordingId, Integer tbId, String businessTime, Timing timing) throws IOException {
        List<FinancialSalesBalance> fsbList = null;
        List<SalesAmazonFbaBusinessreport> sfbList = null;
        // 开始时间
        Long begin = new Date().getTime();
        int index = 0;
        FinancialSalesBalance fb;
        SalesAmazonFbaBusinessreport sfb;

        List<?> tList = new ArrayList<>();
        if (tbId == Constants.FINANCE_ID || tbId == Constants.FINANCE_ID_YY) {
            fsbList = ArrUtils.listT(tList);
        } else if (tbId == Constants.BUSINESS_ID) {
            sfbList = ArrUtils.listT(tList);
        }
        Map<String, Integer> intMap = new HashMap<>();
        timing.setMsg("正在校验数据..........");
        while (csvReader.readRecord()) {
            if (index >= row) {
                //numberCount++
                CrrUtils.inCreateNumberLong(numberCount);
                //count ++ 成功数量
                CrrUtils.inCreateNumberLong(count);
                //85 == 财务上传ID | 104 运营上传
                if (tbId == Constants.FINANCE_ID || tbId == Constants.FINANCE_ID_YY) {
                    fb = saveFinance(setFsb(sId, seId, uid, pId.longValue(), recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //108 == 业务上传ID
                else if (tbId == Constants.BUSINESS_ID) {
                    sfb = saveBusiness(setBusPort(sId, seId, uid, recordingId), csvReader, sId, seId, Long.parseLong(businessTime));
                    if (sfb != null) {
                        sfbList.add(sfb);
                    }
                }
            }
            index++;
            //计算百分比
            int currentCount = timing.setAttributesTim(index);
            ws.schedule(intMap, currentCount, timSet, timing, 1L);
        }
        int number = 0;
        //插入数据
        timing.setMsg("正在导入数据库..........");
        ws.sendInfo(JSON.toJSONString(CrrUtils.inCreateSet(timSet, timing)), 1L);
        //财务
        if (fsbList != null) {
            if (fsbList.size() > 0) {
                //插入数据
                number = fsbService.addInfo(fsbList, tbId);
            }
        }
        //业务
        if (sfbList != null) {
            if (sfbList.size() > 0) {
                //插入数据
                timing.setMsg("正在导入数据库..........");
                number = busService.AddSalesAmazonAdBusList(sfbList);

            }
        }
        if (number != 0) {
            return printCount(begin, timing, count.get(), index);
        }
        return BaseApiService.setResultError("存入数据失败，请检查信息");
    }

    /**
     * 美国 业务存入对象
     */
    public SalesAmazonFbaBusinessreport saveBusiness(SalesAmazonFbaBusinessreport sfb, CsvReader csvReader, Long
            sId, Long seId, Long businessTime) throws IOException {
        sfb.setDate(businessTime);
        String sAsin;
        if (seId.intValue() == 1 || seId.intValue() == 4 || seId.intValue() == 5 || seId.intValue() == 6
                || seId.intValue() == 7 || seId.intValue() == 8 || seId.intValue() == 9) {
            sfb.setfAsin(StrUtils.repString(csvReader.get(0)));
            sAsin = StrUtils.repString(csvReader.get(1));
            sfb.setsAsin(sAsin);
            sfb.setpName(StrUtils.repString(csvReader.get(2)));
            sfb.setSessionsVisit(StrUtils.replaceInteger(csvReader.get(3)));
            sfb.setSessionsPer(StrUtils.repDouble(csvReader.get(4)));
            sfb.setPageViews(StrUtils.replaceInteger(csvReader.get(5)));
            sfb.setBuyBoxPer(StrUtils.repDouble(csvReader.get(7)));
            sfb.setOrder(StrUtils.replaceInteger(csvReader.get(8)));
            sfb.setOrderB2B(StrUtils.replaceInteger(csvReader.get(9)));
            sfb.setSales(StrUtils.repDouble(csvReader.get(12)));
            sfb.setSalesB2B(StrUtils.repDouble(csvReader.get(13)));
            sfb.setOrderItems(StrUtils.replaceInteger(csvReader.get(14)));
            sfb.setOrderItemsB2B(StrUtils.replaceInteger(csvReader.get(15)));
        } else {
            sfb.setfAsin(StrUtils.repString(csvReader.get(0)));
            sAsin = StrUtils.repString(csvReader.get(1));
            sfb.setsAsin(sAsin);
            sfb.setpName(StrUtils.repString(csvReader.get(2)));
            sfb.setSessionsVisit(StrUtils.replaceInteger(csvReader.get(3)));
            sfb.setSessionsPer(StrUtils.repDouble(csvReader.get(4)));
            sfb.setPageViews(StrUtils.replaceInteger(csvReader.get(5)));
            sfb.setBuyBoxPer(StrUtils.repDouble(csvReader.get(7)));
            sfb.setOrder(StrUtils.replaceInteger(csvReader.get(8)));
            sfb.setSales(StrUtils.repDouble(csvReader.get(10)));
            sfb.setOrderItems(StrUtils.replaceInteger(csvReader.get(11)));
        }
        Long skuId = skuService.getAsinSkuId(sId, seId, sAsin);
        String result = skuList(skuId, csvReader, sAsin);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        return sfb;
    }

    /**
     * csv 财务存入对象
     */
    public FinancialSalesBalance saveFinance(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws
            IOException {
        //设置时间类型转换
        DateUtils.setDate(fsb, seId, csvReader);
        fsb.setSettlemenId(StrUtils.repString(csvReader.get(1)));
        String type = StrUtils.repString(csvReader.get(2));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        String skuName = null;
        switch (seId.intValue()) {
            case 1:
                fsb.setOrderId(StrUtils.repString(csvReader.get(3)));
                skuName = StrUtils.repString(csvReader.get(4));
                fsb.setSku(skuName);
                fsb.setDescription(StrUtils.repString(csvReader.get(5)));
                fsb.setoQuantity(StrUtils.replaceLong(csvReader.get(6)));
                fsb.setMarketplace(StrUtils.repString(csvReader.get(7)));
                fsb.setFulfillment(StrUtils.repString(csvReader.get(8)));
                fsb.setCity(StrUtils.repString(csvReader.get(9)));
                fsb.setState(StrUtils.repString(csvReader.get(10)));
                fsb.setPostal(StrUtils.repString(csvReader.get(11)));
                fsb.setSales(StrUtils.replaceDouble(csvReader.get(12)));
                fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get(13)));
                fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get(14)));
                fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get(15)));
                fsb.setSalesTax(StrUtils.replaceDouble(csvReader.get(16)));
                fsb.setMarketplaceFacilitatorTax(StrUtils.replaceDouble(csvReader.get(17)));
                fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get(18)));
                fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get(19)));
                fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get(20)));
                fsb.setOther(StrUtils.replaceDouble(csvReader.get(21)));
                fsb.setTotal(StrUtils.replaceDouble(csvReader.get(22)));
                break;
            case 2:
                fsb.setOrderId(StrUtils.repString(csvReader.get(3)));
                skuName = StrUtils.repString(csvReader.get(4));
                fsb.setSku(skuName);
                fsb.setDescription(StrUtils.repString(csvReader.get(5)));
                fsb.setoQuantity(StrUtils.replaceLong(csvReader.get(6)));
                fsb.setMarketplace(StrUtils.repString(csvReader.get(7)));
                fsb.setFulfillment(StrUtils.repString(csvReader.get(8)));
                fsb.setCity(StrUtils.repString(csvReader.get(9)));
                fsb.setState(StrUtils.repString(csvReader.get(10)));
                fsb.setPostal(StrUtils.repString(csvReader.get(11)));
                fsb.setSales(StrUtils.replaceDouble(csvReader.get(12)));
                fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get(13)));
                fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get(14)));
                fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get(15)));
                fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get(17)));
                fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get(18)));
                fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get(19)));
                fsb.setOther(StrUtils.replaceDouble(csvReader.get(20)));
                fsb.setTotal(StrUtils.replaceDouble(csvReader.get(21)));
                break;
            case 3:
                fsb.setOrderId(StrUtils.repString(csvReader.get(3)));
                skuName = StrUtils.repString(csvReader.get(4));
                fsb.setSku(skuName);
                fsb.setDescription(StrUtils.repString(csvReader.get(5)));
                fsb.setoQuantity(StrUtils.replaceLong(csvReader.get(6)));
                fsb.setMarketplace(StrUtils.repString(csvReader.get(7)));
                fsb.setFulfillment(StrUtils.repString(csvReader.get(8)));
                fsb.setCity(StrUtils.repString(csvReader.get(9)));
                fsb.setState(StrUtils.repString(csvReader.get(10)));
                fsb.setPostal(StrUtils.repString(csvReader.get(11)));
                fsb.setSales(StrUtils.replaceDouble(csvReader.get(12)));
                fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get(13)));
                fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get(14)));
                fsb.setSalesTax(StrUtils.replaceDouble(csvReader.get(15)));
                fsb.setLowValueGoods(StrUtils.replaceDouble(csvReader.get(16)));
                fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get(17)));
                fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get(18)));
                fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get(19)));
                fsb.setOther(StrUtils.replaceDouble(csvReader.get(20)));
                fsb.setTotal(StrUtils.replaceDouble(csvReader.get(21)));
                break;
            case 4:
                fsb.setOrderId(StrUtils.repString(csvReader.get(3)));
                skuName = StrUtils.repString(csvReader.get(4));
                fsb.setSku(skuName);
                fsb.setDescription(StrUtils.repString(csvReader.get(5)));
                fsb.setoQuantity(StrUtils.replaceLong(csvReader.get(6)));
                fsb.setMarketplace(StrUtils.repString(csvReader.get(7)));
                fsb.setFulfillment(StrUtils.repString(csvReader.get(8)));
                fsb.setCity(StrUtils.repString(csvReader.get(9)));
                fsb.setState(StrUtils.repString(csvReader.get(10)));
                fsb.setPostal(StrUtils.repString(csvReader.get(11)));
                fsb.setSales(StrUtils.replaceDouble(csvReader.get(12)));
                fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get(13)));
                fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get(14)));
                fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get(15)));
                fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get(16)));
                fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get(17)));
                fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get(18)));
                fsb.setOther(StrUtils.replaceDouble(csvReader.get(19)));
                fsb.setTotal(StrUtils.replaceDouble(csvReader.get(20)));
                break;
            case 5:
                fsb.setOrderId(StrUtils.repString(csvReader.get(3)));
                skuName = StrUtils.repString(csvReader.get(4));
                fsb.setSku(skuName);
                fsb.setDescription(StrUtils.repString(csvReader.get(5)));
                fsb.setoQuantity(StrUtils.replaceLong(csvReader.get(6)));
                fsb.setMarketplace(StrUtils.repString(csvReader.get(7)));
                fsb.setFulfillment(StrUtils.repString(csvReader.get(8)));
                fsb.setCity(StrUtils.repString(csvReader.get(9)));
                fsb.setState(StrUtils.repString(csvReader.get(10)));
                fsb.setPostal(StrUtils.repString(csvReader.get(11)));
                fsb.setSales(StrUtils.replaceDouble(csvReader.get(12)));
                fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get(13)));
                fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get(14)));
                fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get(15)));
                fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get(16)));
                fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get(17)));
                fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get(18)));
                fsb.setOther(StrUtils.replaceDouble(csvReader.get(19)));
                fsb.setTotal(StrUtils.replaceDouble(csvReader.get(20)));
                break;
            case 6:
                fsb.setOrderId(StrUtils.repString(csvReader.get(3)));
                skuName = StrUtils.repString(csvReader.get(4));
                fsb.setSku(skuName);
                fsb.setDescription(StrUtils.repString(csvReader.get(5)));
                fsb.setoQuantity(StrUtils.replaceLong(csvReader.get(6)));
                fsb.setMarketplace(StrUtils.repString(csvReader.get(7)));
                fsb.setFulfillment(StrUtils.repString(csvReader.get(8)));
                fsb.setCity(StrUtils.repString(csvReader.get(9)));
                fsb.setState(StrUtils.repString(csvReader.get(10)));
                fsb.setPostal(StrUtils.repString(csvReader.get(11)));
                fsb.setSales(StrUtils.replaceDouble(csvReader.get(12)));
                fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get(13)));
                fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get(14)));
                fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get(15)));
                fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get(16)));
                fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get(17)));
                fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get(18)));
                fsb.setOther(StrUtils.replaceDouble(csvReader.get(19)));
                fsb.setTotal(StrUtils.replaceDouble(csvReader.get(20)));
                break;
            case 7:
                fsb.setOrderId(StrUtils.repString(csvReader.get(3)));
                skuName = StrUtils.repString(csvReader.get(4));
                fsb.setSku(skuName);
                fsb.setDescription(StrUtils.repString(csvReader.get(5)));
                fsb.setoQuantity(StrUtils.replaceLong(csvReader.get(6)));
                fsb.setMarketplace(StrUtils.repString(csvReader.get(7)));
                fsb.setFulfillment(StrUtils.repString(csvReader.get(8)));
                fsb.setCity(StrUtils.repString(csvReader.get(9)));
                fsb.setState(StrUtils.repString(csvReader.get(10)));
                fsb.setPostal(StrUtils.repString(csvReader.get(11)));
                fsb.setSales(StrUtils.replaceDouble(csvReader.get(12)));
                fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get(13)));
                fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get(14)));
                fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get(15)));
                fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get(16)));
                fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get(17)));
                fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get(18)));
                fsb.setOther(StrUtils.replaceDouble(csvReader.get(19)));
                fsb.setTotal(StrUtils.replaceDouble(csvReader.get(20)));
                break;
            case 8:
                fsb.setOrderId(StrUtils.repString(csvReader.get(3)));
                skuName = StrUtils.repString(csvReader.get(4));
                fsb.setSku(skuName);
                fsb.setDescription(StrUtils.repString(csvReader.get(5)));
                fsb.setoQuantity(StrUtils.replaceLong(csvReader.get(6)));
                fsb.setMarketplace(StrUtils.repString(csvReader.get(7)));
                fsb.setFulfillment(StrUtils.repString(csvReader.get(8)));
                fsb.setCity(StrUtils.repString(csvReader.get(9)));
                fsb.setState(StrUtils.repString(csvReader.get(10)));
                fsb.setPostal(StrUtils.repString(csvReader.get(11)));
                fsb.setSales(StrUtils.replaceDouble(csvReader.get(12)));
                fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get(13)));
                fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get(14)));
                fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get(15)));
                fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get(16)));
                fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get(17)));
                fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get(18)));
                fsb.setOther(StrUtils.replaceDouble(csvReader.get(19)));
                fsb.setTotal(StrUtils.replaceDouble(csvReader.get(20)));
                break;
            case 9:
                fsb.setOrderId(StrUtils.repString(csvReader.get(3)));
                skuName = StrUtils.repString(csvReader.get(4));
                fsb.setSku(skuName);
                fsb.setDescription(StrUtils.repString(csvReader.get(5)));
                fsb.setoQuantity(StrUtils.replaceLong(csvReader.get(6)));
                fsb.setMarketplace(StrUtils.repString(csvReader.get(7)));
                fsb.setFulfillment(StrUtils.repString(csvReader.get(8)));
                fsb.setCity(StrUtils.repString(csvReader.get(9)));
                fsb.setState(StrUtils.repString(csvReader.get(10)));
                fsb.setPostal(StrUtils.repString(csvReader.get(11)));
                fsb.setSales(StrUtils.replaceDouble(csvReader.get(12)));
                fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get(13)));
                fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get(14)));
                fsb.setPointFee(StrUtils.replaceDouble(csvReader.get(15)));
                fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get(16)));
                fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get(17)));
                fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get(18)));
                fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get(19)));
                fsb.setOther(StrUtils.replaceDouble(csvReader.get(20)));
                fsb.setTotal(StrUtils.replaceDouble(csvReader.get(21)));
                break;
            case 10:
                fsb.setOrderId(StrUtils.repString(csvReader.get(3)));
                skuName = StrUtils.repString(csvReader.get(4));
                fsb.setSku(skuName);
                fsb.setDescription(StrUtils.repString(csvReader.get(5)));
                fsb.setoQuantity(StrUtils.replaceLong(csvReader.get(6)));
                fsb.setMarketplace(StrUtils.repString(csvReader.get(7)));
                fsb.setFulfillment(StrUtils.repString(csvReader.get(8)));
                fsb.setCity(StrUtils.repString(csvReader.get(9)));
                fsb.setState(StrUtils.repString(csvReader.get(10)));
                fsb.setPostal(StrUtils.repString(csvReader.get(11)));
                fsb.setSales(StrUtils.replaceDouble(csvReader.get(12)));
                fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get(13)));
                fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get(14)));
                fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get(15)));
                fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get(16)));
                fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get(17)));
                fsb.setOther(StrUtils.replaceDouble(csvReader.get(18)));
                fsb.setTotal(StrUtils.replaceDouble(csvReader.get(19)));
                break;
        }
        StrUtils.isService(fsb.getType(), fsb);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        String result = skuList(skuId, csvReader, skuName);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        fsb.setSkuId(skuId);
        return fsb;
    }

    /**
     * csv 封装获取没有SKU的文件List
     *
     * @param skuId
     * @param csvReader
     * @return
     */
    public String skuList(Long skuId, CsvReader csvReader, String sku) throws IOException {
        //不为空  判断skuID
        if (StringUtils.isNotEmpty(sku)) {
            String result = exportCsvType(csvReader, skuId);
            if (StringUtils.isEmpty(result)) {
                return null;
            }
        }
        //如果sku是空的  直接返回设置NULL
        return "success";
    }

    /**
     * csv 对比表头返回
     *
     * @return
     */
    public boolean compareHeadCsv
    (List<String> oldHeadList, List<String> sqlHeadList) {
        List<String> headList = new ArrayList<>();
        //转换下头信息
        for (int i = 0; i < oldHeadList.size(); i++) {
            String head = oldHeadList.get(i).replace("\"", "").replace("﻿", "").trim();
            headList.add(head);
            System.out.println(head);
        }
        //如果不一致返回false
        return ArrUtils.eqOrderList(headList, sqlHeadList);
    }

    /**
     * 通用设置CSV 没有sku/typeName导出文件
     *
     * @param csvReader
     * @param skuId
     * @return
     */
    public String exportCsvType(CsvReader csvReader, Long skuId) throws IOException {
        if (skuId == null || skuId == -1) {
            CrrUtils.inCreateList(noSkuList);
            //count --
            CrrUtils.delCreateNumberLong(count);
            //sumNoSku ++
            CrrUtils.inCreateNumberInteger(sumErrorSku);
            List<String> skuListNo = new ArrayList<>();
            for (int i = 0; i < csvReader.getColumnCount(); i++) {
                skuListNo.add(csvReader.get(i).replace(",", "."));
            }
            noSkuList.get().add(skuListNo);
            noSkuList.set(noSkuList.get());
            return null;
        }
        return "success";
    }


    /**
     * csv 设置TypeName
     */
    public boolean setType(String type, Long seId, CsvReader csvReader, FinancialSalesBalance fsb) {
        String typeName;
        try {
            typeName = orderTypeName(type, seId, csvReader);
            if (StringUtils.isNotEmpty(typeName)) {
                fsb.setType(typeName);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * csv 获取没有typeName的文件List
     *
     * @param csvReader
     * @return
     */
    public String orderTypeName(String type, Long seId, CsvReader csvReader) throws IOException {
        String typeName = typeMapper.getTypeName(seId, type);
        //如果数据库查询出来为空
        if (StringUtils.isEmpty(typeName)) {
            String result = exportCsvType(csvReader, -1L);
            return result;
        }
        return typeName;
    }

    //#######################Csv
//##########################################################通用方法

    /**
     * 封装获得编码格式 适用于TXT  CSV
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    private InputStreamReader streamReader(String filePath) throws Exception {
        String fileEncode = EncodingDetect.getJavaEncode(filePath);
        if (fileEncode == null) {
            throw new Exception("filePath加载文件路径不存在");
        }
        return new InputStreamReader(new FileInputStream(filePath), fileEncode);
    }

    /**
     * 封装错误信息
     *
     * @param msg
     * @param recordingId
     * @param fileName
     * @return
     */
    private ResponseBase errorResult(Integer percentage, String msg, Long recordingId, String fileName, Timing timing, String status, String saveFilePath, String uuidName) {
        timing.setInfo(status, percentage, msg);
        CrrUtils.inCreateSet(timSet, timing);
        return saveUserUploadInfo(BaseApiService.setResultError(msg), recordingId, fileName, null, 0, saveFilePath, uuidName);
    }

    /**
     * 封装noSku写入状态
     *
     * @param responseBase
     * @param recordingId
     * @param fileName
     * @param head
     * @param type
     * @return
     */
    private ResponseBase saveUserUploadInfo(ResponseBase responseBase, Long recordingId, String
            fileName, List<String> head, int type, String saveFilePath, String uuidName) {
        if (responseBase.getCode() == 200) {
            if (noSkuList.get() != null && noSkuList.get().size() > 0) {
                if (type == 1) {
                    //写入xlsx 文件写入到服务器的地址   Constants.WRITE_SAVE_FILE_PATH
                    XlsUtils.outPutXssFile(noSkuList.get(), Constants.WRITE_SAVE_FILE_PATH, uuidName);
                } else if (type == 2) {
                    //写入CSV文件到本地
                    CSVUtil.write(head, noSkuList.get(), Constants.WRITE_SAVE_FILE_PATH, uuidName);
                } else if (type == 3) {
                    //写入Txt
                    TxtUtils.writeFileTxt(noSkuList.get(), Constants.WRITE_SAVE_FILE_PATH, uuidName);
                }
                //上传成功 有些skuId 记录上传信息~
                String msg = responseBase.getMsg() + "====>有" + sumErrorSku.get() + "个没有sku文件/数据库没有typeName";
                sumErrorSku.set(0);
                return upUserUpload(2, recordingId, fileName, msg, saveFilePath, uuidName);
            }
            //上传成功 都有skuId~
            return upUserUpload(0, recordingId, fileName, responseBase.getMsg(), saveFilePath, uuidName);
        } else {
            //存入信息报错
            return upUserUpload(1, recordingId, fileName, responseBase.getMsg(), saveFilePath, uuidName);
        }
    }

    /**
     * 封装通用更新方法
     */
    private ResponseBase upUserUpload(int status, Long id, String fileName, String msg, String saveFilePath, String uuIdName) {
        UserUpload upload;
        switch (status) {
            case 0:
                upload = recordInfo(status, msg, id, fileName, saveFilePath, uuIdName);
                return BaseApiService.setResultSuccess(msg, upload);
            case 1:
                upload = recordInfo(status, msg, id, fileName, saveFilePath, uuIdName);
                return BaseApiService.setResultError("error/" + msg, upload);
            case 2:
                int fileIndex = saveFilePath.lastIndexOf("/");
                upload = recordInfo(status, msg, id, "NO" + fileName, saveFilePath.substring(0, fileIndex) + "SkuNo/", uuIdName);
                return BaseApiService.setResultSuccess(msg, upload);
            case 3:
                upload = recordInfo(status, msg, id, fileName, saveFilePath, uuIdName);
                return BaseApiService.setResultError(msg, upload);
            case 4:
                break;
        }
        return null;
    }

    /**
     * 封装更新信息
     *
     * @param status
     * @param msg
     */
    private UserUpload recordInfo(Integer status, String msg, Long id, String fileName, String saveFilePath, String uuIdName) {
        UserUpload upload = new UserUpload(id, new Date().getTime());
        if (status == 2) {
            upload.setUuidName(uuIdName);
            upload.setFilePath(saveFilePath);
        }
        upload.setName(fileName);
        upload.setStatus(status);
        upload.setRemark(msg);
        userUploadService.upUploadInfo(upload);
        return upload;

    }


    /**
     * 封装通用打印输出语句 方法
     *
     * @param
     * @return
     */
    public ResponseBase printCount(Long begin, Timing timing, Long successNumber, int index) {
        timing.setInfo("success", "数据导入成功..........");
        ws.sendInfo(JSON.toJSONString(CrrUtils.inCreateSet(timSet, timing)), 1L);
        // 结束时间
        Long end = new Date().getTime();
        return BaseApiService.setResultSuccess("总共" + index + "条数据/ 真实数据" + successNumber + "条数据插入成功/====>失败 " + sumErrorSku.get() + "条/花费时间 : " + (end - begin) / 1000 + " s");
    }

    /**
     * 封装通用获得头信息对比
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
        CrrUtils.inCreateList(noSkuList);
        //count --
        CrrUtils.delCreateNumberLong(count);
        //sumNoSku ++
        CrrUtils.inCreateNumberInteger(sumErrorSku);
        if (noSkuList.get().size() == 0) {
            noSkuList.get().add(head);
        }
        List<String> skuListNo = new ArrayList<>();
        skuListNo.add(line);
        noSkuList.get().add(skuListNo);
        noSkuList.set(noSkuList.get());
    }
//##########################################################通用方法

//###############设置表头

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

//###############设置表头


}
