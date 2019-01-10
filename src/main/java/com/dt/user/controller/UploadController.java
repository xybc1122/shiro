package com.dt.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.mapper.BasePublicMapper.BasicPublicAmazonTypeMapper;
import com.dt.user.model.*;
import com.dt.user.service.*;
import com.dt.user.service.BasePublicService.BasicPublicSiteService;
import com.dt.user.service.BasePublicService.BasicSalesAmazonCsvTxtXslHeaderService;
import com.dt.user.service.BasePublicService.BasicSalesAmazonSkuService;
import com.dt.user.toos.Constants;
import com.dt.user.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private FinancialSalesBalanceService fsbService;

    @Autowired
    private BasicSalesAmazonCsvTxtXslHeaderService headService;

    @Autowired
    private BasicSalesAmazonSkuService skuService;

    @Autowired
    private UserUploadService userUploadService;

    @Autowired
    private SalesAmazonAdCprService cprService;

    @Autowired
    private SalesAmazonAdStrService strService;

    @Autowired
    private SalesAmazonAdOarService oarService;

    @Autowired
    private SalesAmazonAHlService hlService;

    @Autowired
    private SalesAmazonFbaBusinessreportService busService;

    @Autowired
    private BasicPublicSiteService siteService;

    @Autowired
    private SalesAmazonFbaTradeReportService tradeReportService;

    @Autowired
    private BasicPublicAmazonTypeMapper typeMapper;
    //获取没有SKU的List集合 并发List 容器
    private CopyOnWriteArrayList<List<String>> skuNoIdList = new CopyOnWriteArrayList<>();
    //行数 /报错行数
    ThreadLocal<Long> count = ThreadLocal.withInitial(() -> 0L);
    //没有sku有几行存入
    ThreadLocal<Integer> sumErrorSku = ThreadLocal.withInitial(() -> 0);
    //读写锁
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    //读锁
    Lock readLock = readWriteLock.readLock();
    //写锁
    Lock writeLock = readWriteLock.writeLock();


    //并发++
    public void inCreateSumNoSku() {
        Integer sumSku = sumErrorSku.get();
        sumSku++;
        sumErrorSku.set(sumSku);
    }

    public void inCreateCount() {
        Long myCount = count.get();
        myCount++;
        count.set(myCount);
    }

    public void delCreateCount() {
        Long typeCount = count.get();
        typeCount--;
        count.set(typeCount);
    }

    /**
     * 定时请求的状态
     *
     * @return
     */
    @GetMapping("/timing")
    public ResponseBase timingStatus() {
        return BaseApiService.setResultSuccess("");
    }

    @GetMapping("/downloadCommonFile")
    public ResponseBase downloadFile(@RequestParam("fileId") String fileId, HttpServletRequest request, HttpServletResponse response) {
        String path = "D:/";
        try {
            FileUtils.downloadFile(path, response, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/file")
    public ResponseBase saveFileInfo(HttpServletRequest request, @RequestParam("sId") String sId,
                                     @RequestParam("seId") String seId, @RequestParam("payId") String payId,
                                     @RequestParam("menuId") String menuId, @RequestParam("areaId") String areaId) {
        String token = GetCookie.getToken(request);
        UserInfo user = JwtUtils.jwtUser(token);
        if (user == null) {
            return BaseApiService.setResultError("用户无效~");
        }
        MultipartFile file;
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("files");
        //记录用户上传信息~
        boolean isUpload = true;
        UserUpload upload;
        String msg = "上传成功~";
        int fileCount = 0;
        List<UserUpload> uploadList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < files.size(); i++) {
                file = files.get(i);
                //指定文件存放路径
                String saveFilePath = Constants.SAVE_FILE_PATH;
                String contentType = file.getContentType();//图片||文件类型
                String fileName = file.getOriginalFilename();//图片||文件名字
                try {
                    FileUtils.uploadFile(file.getBytes(), saveFilePath, fileName);
                } catch (Exception e) {
                    isUpload = false;
                    msg = "上传失败~" + fileName;
                    fileCount++;
                    sb.append(fileName);
                }
                //店铺ID
                Integer shopId = null;
                if (StringUtils.isNotBlank(sId)) {
                    shopId = Integer.parseInt(sId);
                }
                //站点ID
                Integer siteId = null;
                if (StringUtils.isNotBlank(seId)) {
                    siteId = Integer.parseInt(seId);
                }
                Integer tbId = null;
                if (StringUtils.isNotBlank(menuId)) {
                    tbId = Integer.parseInt(menuId);
                }
                // pId
                Integer pId = null;
                if (StringUtils.isNotBlank(payId)) {
                    pId = Integer.parseInt(payId);
                }
                // pId
                Integer aId = null;
                if (StringUtils.isNotBlank(areaId)) {
                    aId = Integer.parseInt(areaId);
                }
                int status = isUpload ? 0 : 4;
                //记录用户上传信息~
                upload = uploadOperating(new UserUpload(), siteId, shopId, fileName, saveFilePath, user, pId, status, msg, tbId, aId);
                if (isUpload) {
                    uploadList.add(upload);
                }
                isUpload = true;
            }
            String getMsg = "上传了" + files.size() + "个文件/" + "其中" + fileCount + "个文件失败~ 失败文件名字" + sb.toString() + "";
            return BaseApiService.setResultSuccess(getMsg, uploadList);
        } catch (Exception e) {
            return BaseApiService.setResultError("上传程序出错,所有文件停止处理,请重新上传~", uploadList);
        }
    }

    /**
     * 数据处理接口
     *
     * @return
     */
    @PostMapping("/addInfo")
    @Transactional
    public ResponseBase redFileInfo(@RequestBody UserUpload upload) {
        List<ResponseBase> responseBaseList = new ArrayList<>();
        int baseNum = upload.getUploadSuccessList().size();
        //获取上传状态集合
        Set<Timing> setUpListData = new HashSet<>();
        ResponseBase responseBase;
        if (baseNum > 0) {
            for (int i = 0; i < baseNum; i++) {
                UserUpload userUpload = upload.getUploadSuccessList().get(i);
                int fileIndex = userUpload.getName().lastIndexOf(".");
                String typeFile = userUpload.getName().substring(fileIndex + 1);
                if (typeFile.equals("csv")) {
                    responseBase = importCsv(userUpload.getFilePath(), userUpload.getName(), userUpload.getSiteId(), userUpload.getShopId(), userUpload.getUid(), userUpload.getpId(), userUpload.getId(), userUpload.getTbId());
                    responseBaseList.add(responseBase);
                } else if (typeFile.equals("xlsx") || typeFile.equals("xls")) {
                    responseBase = importXls(userUpload.getFilePath(), userUpload.getName(), userUpload.getSiteId(), userUpload.getShopId(), userUpload.getUid(), userUpload.getId(), userUpload.getTbId());
                    responseBaseList.add(responseBase);
                } else if (typeFile.equals("txt")) {
                    responseBase = importTxt(userUpload.getFilePath(), userUpload.getName(), userUpload.getShopId(), userUpload.getUid(), userUpload.getId(), userUpload.getTbId(), userUpload.getAreaId(), setUpListData);
                    responseBaseList.add(responseBase);
                    // System.out.println("txt");
                }
            }
        }
        return BaseApiService.setResultSuccess(responseBaseList);
    }


    /**
     * 设置文件总数
     *
     * @param filePath
     */
    public void setFileCount(String filePath, Timing timing) {
        //首先获得行数
        Double sumCount = TxtUtils.readFile(filePath);
        if (sumCount != 0.0) {
            //获得总行数
            timing.setTotalNumber(sumCount);
        }
    }

    //###############################封装Txt
    public ResponseBase importTxt(String saveFilePath, String fileName, Long shopId, Long uid, Long recordingId, Integer tbId, Integer aId, Set<Timing> setTiming) {
        Timing timing = new Timing();
        ResponseBase responseCsv;
        String filePath = saveFilePath + fileName;
        try (InputStreamReader read = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(read)
        ) {
            //拿到数据库的表头 进行校验
            List<String> head = getHeadInfo(null, tbId, aId);
            //对比头部
            String lineHead = br.readLine();
            List<String> txtHead = Arrays.asList(lineHead.split("\t"));
            boolean isFlg = ArrUtils.eqOrderList(head, txtHead);
            if (!isFlg) {
                return saveUserUploadInfo(BaseApiService.setResultError("表头信息不一致"), recordingId, fileName, null, 3);
            }
            writeLock.lock();
            try {
                //设置文件总数
                setFileCount(filePath, timing);
                //第一行List头
                List<String> strLineHead = new ArrayList<>();
                strLineHead.add(lineHead);
                responseCsv = saveTxt(br, shopId, uid, recordingId, strLineHead, timing, setTiming);
                return saveUserUploadInfo(responseCsv, recordingId, fileName, null, 3);
            } finally {
                writeLock.unlock();
            }
        } catch (IOException e) {
            //System.out.println(e.getMessage() + "-------------------------");
            timing.setStatus("exception");
            setTiming.add(timing);
            responseCsv = BaseApiService.setResultError("第" + count.get() + "行信息错误,数据存入失败~");
            return saveUserUploadInfo(responseCsv, recordingId, fileName, null, 0);
        } finally {
            count.set(0L);
        }
    }

    /**
     * 封装Txt
     */
    public ResponseBase saveTxt(BufferedReader br, Long shopId, Long uid, Long recordingId, List<String> lineHead, Timing timing, Set<Timing> setTiming) throws IOException {
        // 开始时间
        Long begin = new Date().getTime();
        List<SalesAmazonFbaTradeReport> sfbTradList = new ArrayList<>();
        SalesAmazonFbaTradeReport sftPort;
        SalesAmazonFbaTradeReport tradeReport;
        String line;
        v1:
        while ((line = br.readLine()) != null) {
            //count ++
            inCreateCount();
            sftPort = setTraPort(shopId, uid, recordingId);
            // 一次读入一行数据
            String[] newLine = line.split("\t", -1);
            for (int i = 0; i < newLine.length; i++) {
                tradeReport = saveTradeReport(i, sftPort, newLine, shopId);
                if (tradeReport == null) {
                    //先拿到这一行信息 newLine
                    exportTxtType(lineHead, line);
                    continue v1;
                }
            }
            if (sftPort != null) {
                sfbTradList.add(sftPort);
            }
            //计算百分比
            int percentage = (int) ((count.get() / timing.getTotalNumber()) * 100);
            timing.setPercentage(percentage);
            if (percentage == 60) {
                //设置颜色
                timing.setColor("#8e71c7");
            }
            setTiming.add(timing);
        }
        int countTrad;
        if (sfbTradList.size() > 0) {
            //插入数据
            timing.setMsg("正在导入数据库..........");
            countTrad = tradeReportService.AddSalesAmazonAdTrdList(sfbTradList);
            if (countTrad > 0) {
                timing.setStatus("success");
                timing.setMsg("数据导入成功..........");
                setTiming.add(timing);
                return printCount(countTrad, begin);
            }
        }
        return BaseApiService.setResultError("Txt存入异常~");
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
    public ResponseBase importCsv(String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Integer pId, Long id, Integer tbId) {
        ResponseBase responseCsv;
        List<String> oldHeadList;
        String filePath = saveFilePath + fileName;
        ResponseBase comparisonBase;
        String csvJson;
        JSONObject rowJson;
        int row;
        // 创建CSV读对象
        CsvReader csvReader = null;
        //获得头信息长度
        csvJson = CSVUtil.startReadLine(filePath, siteId, tbId);
        rowJson = JSONObject.parseObject(csvJson);
        row = (Integer) rowJson.get("index");
        if (row == -1) {
            return upUserUpload(3, id, fileName, Constants.MSG_ERROR);
        }
        oldHeadList = JSONObject.parseArray(rowJson.get("head").toString(), String.class);
        //对比表头是否一致
        comparisonBase = comparison(oldHeadList, siteId, tbId);
        if (comparisonBase != null) {
            return saveUserUploadInfo(comparisonBase, id, fileName, null, 2);
        }

        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(filePath)), StandardCharsets.UTF_8)) {
            csvReader = new CsvReader(isr);
            writeLock.lock();
            try {
                //设置文件总数
                //Timing.setFileCount(filePath);
                responseCsv = saveCsv(csvReader, row, shopId, siteId, uid, pId.longValue(), id, tbId, 1L);
                return saveUserUploadInfo(responseCsv, id, fileName, oldHeadList, 2);
            } finally {
                writeLock.unlock();
            }
        } catch (Exception e) {
            //System.out.println(e.getMessage() + "-------------------------");
            //Timing.getInstance().setStatus("error");
            responseCsv = BaseApiService.setResultError("第" + count.get() + "行信息错误,数据存入失败~");
            return saveUserUploadInfo(responseCsv, id, fileName, null, 0);
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
            count.set(0L);
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
    public ResponseBase saveCsv(CsvReader csvReader, int row, Long sId, Long seId, Long uid, Long pId, Long recordingId, Integer tbId, Long dateTime) throws IOException {
        List<FinancialSalesBalance> fsbList = null;
        List<SalesAmazonFbaBusinessreport> sfbList = null;
        // 开始时间
        Long begin = new Date().getTime();
        int index = 0;
        FinancialSalesBalance fb;
        SalesAmazonFbaBusinessreport sfb;
        if (tbId == null) {
            return BaseApiService.setResultError("没有tbId------");
        }
        List<?> tList = new ArrayList<>();
        if (tbId == Constants.FINANCE_ID || tbId == Constants.FINANCE_ID_YY) {
            fsbList = ArrUtils.listT(tList);
        } else if (tbId == Constants.BUSINESS_ID) {
            sfbList = ArrUtils.listT(tList);
        }
        while (csvReader.readRecord()) {
            inCreateCount();
            if (index >= row) {
                //85 == 财务上传ID | 104 运营上传
                if (tbId == Constants.FINANCE_ID || tbId == Constants.FINANCE_ID_YY) {
                    fb = saveFinance(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //108 == 业务上传ID
                else if (tbId == Constants.BUSINESS_ID) {
                    sfb = saveBusiness(setBusPort(sId, seId, uid, recordingId), csvReader, sId, seId, dateTime);
                    if (sfb != null) {
                        sfbList.add(sfb);
                    }
                }
            }
            index++;
            //获得count
            //Timing.getInstance().setDataLength(count.get());
        }
        int number = 0;
        //财务
        if (fsbList != null) {
            if (fsbList.size() > 0) {
                number = fsbService.addInfo(fsbList, tbId);
            }
        }
        //业务
        if (sfbList != null) {
            if (sfbList.size() > 0) {
                number = busService.AddSalesAmazonAdBusList(sfbList);
            }
        }
        if (number != 0) {
            // 结束时间
            Long end = new Date().getTime();
            Long successSum = count.get() - row;
            Long sum = successSum + sumErrorSku.get();
            return BaseApiService.setResultSuccess("总共" + sum + "条数据/" + successSum + "条数据插入成功/失败 " + sumErrorSku.get() + "条/花费时间 : " + (end - begin) / 1000 + " s");
        }
        return BaseApiService.setResultError("表里的skuID全部不一致 请修改~");
    }

    /**
     * 封装更新信息
     *
     * @param status
     * @param msg
     */
    public UserUpload recordInfo(Integer status, String msg, Long id, String fileName) {
        UserUpload upload = new UserUpload();
        upload.setId(id);
        upload.setUid(new Date().getTime());
        upload.setName(fileName);
        if (status != 0) {
            if (status == 3) {
                upload.setStatus(status);
            }
            if (status == 2) {
                upload.setStatus(status);
            }
            if (status == 1) {
                upload.setStatus(status);
            }
            upload.setRemark(msg);
            userUploadService.upUploadInfo(upload);
        }
        return upload;

    }

    /**
     * 通过记录用户上传信息操作
     *
     * @param siteId
     * @param shopId
     * @param fileName
     * @param saveFilePath
     * @param user
     * @return
     */
    public UserUpload uploadOperating(UserUpload upload, Integer siteId, Integer shopId,
                                      String fileName, String saveFilePath,
                                      UserInfo user, Integer pId, Integer status,
                                      String msg, Integer tbId, Integer aId) {
        //存入文件名字
        upload.setName(fileName);
        //存入上传时间
        upload.setCreateDate(new Date().getTime());
        //用户ID
        upload.setUid(user.getUid());
        //上传服务器路径
        upload.setFilePath(saveFilePath);
        //站点ID
        if (siteId != null) {
            upload.setSiteId(siteId.longValue());
        }
        //店铺ID
        if (shopId != null) {
            upload.setShopId(shopId.longValue());
        }
        upload.setAreaId(aId);
        //付款类型ID
        upload.setpId(pId);
        //上传状态
        upload.setStatus(status);
        //上传信息
        upload.setRemark(msg);

        upload.setTbId(tbId);
        userUploadService.addUserUploadInfo(upload);
        return upload;
    }

    /**
     * CSV头部比较返回
     *
     * @param headArr
     * @param seId
     * @param id
     * @return
     * @throws IOException
     */
    public ResponseBase comparison(List<String> headArr, Long seId, int id) {
        List<String> headList = new ArrayList<>();
        //比较头部
        if (!compareHeadCsv(headList, headArr, getHeadInfo(seId, id, null))) {
            return BaseApiService.setResultError("CSV文件表头信息不一致/请检查~");
        }
        return null;
    }

    /**
     * 设置时间转换类型
     *
     * @param fsb
     * @param seId
     * @param csvReader
     * @throws IOException
     */
    public void setDate(FinancialSalesBalance fsb, Long seId, CsvReader csvReader) throws IOException {
        switch (seId.intValue()) {
            case 1:
                fsb.setDate(DateUtils.getTime(csvReader.get(0), Constants.USA_TIME));
                break;
            case 2:
                fsb.setDate(DateUtils.getTime(csvReader.get(0), Constants.CANADA_TIME));
                break;
            case 3:
                fsb.setDate(DateUtils.getTime(csvReader.get(0), Constants.AUSTRALIA_TIME));
                break;
            case 4:
                fsb.setDate(DateUtils.getTime(csvReader.get(0), Constants.UNITED_KINGDOM_TIME));
                break;
            case 5:
                fsb.setDate(DateUtils.getTime(csvReader.get(0), Constants.GERMAN_TIME));
                break;
            case 6:
                fsb.setDate(DateUtils.getFranceTime(csvReader.get(0), Constants.FRANCE_TIME));
                break;
            case 7:
                fsb.setDate(DateUtils.getItalyTime(csvReader.get(0), Constants.ITALY_TIME));
                break;
            case 8:
                fsb.setDate(DateUtils.getTime(csvReader.get(0), Constants.SPAIN_TIME));
                break;
            case 9:
                fsb.setDate(DateUtils.getTime(csvReader.get(0), Constants.JAPAN_TIME));
                break;
            case 10:
                fsb.setDate(DateUtils.getTime(csvReader.get(0), Constants.MEXICO_TIME));
                break;
        }
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
                sft.setDate(DateUtils.getTime(j[i], Constants.NORTH_AMERICA));
                break;
            case 3:
                sft.setLastUpdatedDate(DateUtils.getTime(j[i], Constants.NORTH_AMERICA));
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
        }
        return sft;
    }

    /**
     * 洲业务 sku asin  业务对比获得sku
     *
     * @param sku
     * @param asin
     */
    public boolean skuEqAsin(String sku, String asin, Long sId, Long seId, SalesAmazonFbaTradeReport sft) {
        if (StringUtils.isNotEmpty(sku) && StringUtils.isNotEmpty(asin)) {
            //查询skuId
            Long skuId = skuService.selSkuId(sId, seId, sku);
            Long asinId = skuService.getAsinSkuId(sId, seId, asin);
            if (skuId == null || asinId == null) {
                return false;
            }
            if (skuId.equals(asinId)) {
                sft.setSkuId(skuId);
                return true;
            }
        }
        return false;
    }

    /**
     * csv 财务存入对象
     */
    public FinancialSalesBalance saveFinance(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws IOException {
        //设置时间类型转换
        setDate(fsb, seId, csvReader);
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
     * 美国 业务存入对象
     */
    public SalesAmazonFbaBusinessreport saveBusiness(SalesAmazonFbaBusinessreport sfb, CsvReader csvReader, Long sId, Long seId, Long dateTime) throws IOException {
        sfb.setDate(dateTime);
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

    //10月
    //11月


    /**
     * 通用设置Txt 没有sku/导出文件
     *
     * @return
     */
    public void exportTxtType(List<String> head, String line) {
        //count --
        delCreateCount();
        //sumNoSku ++
        inCreateSumNoSku();
        if (skuNoIdList.size() == 0) {
            skuNoIdList.add(head);
        }
        List<String> skuListNo = new ArrayList<>();
        skuListNo.add(line);
        skuNoIdList.add(skuListNo);
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
            //count --
            delCreateCount();
            //sumNoSku ++
            inCreateSumNoSku();
            List<String> skuListNo = new ArrayList<>();
            for (int i = 0; i < csvReader.getColumnCount(); i++) {
                skuListNo.add(csvReader.get(i).replace(",", "."));
            }
            skuNoIdList.add(skuListNo);
            return null;
        }
        return "success";
    }

    /**
     * csv 获取没有SKU的文件List
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
     * csv 对比表头返回
     *
     * @param headList
     * @return
     */
    public boolean compareHeadCsv(List<String> headList, List<String> oldHeadList, List<String> fBalanceHead) {
        //拿到表头信息 对比数据库的表头 如果不一致 抛出报错信息 不执行下去
        for (int i = 0; i < oldHeadList.size(); i++) {
            String head = oldHeadList.get(i).replace("\"", "").replace("﻿", "").trim();
            headList.add(head);
            System.out.println(head);
        }
        //如果不一致返回false
        return ArrUtils.eqOrderList(headList, fBalanceHead);
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

//################通用设置表头

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


    //###############################封装xls

    /**
     * 广告上传
     *
     * @param saveFilePath
     * @param fileName
     * @param siteId
     * @param shopId
     * @param uid
     * @param recordingId
     * @param tbId
     * @return
     */
    public ResponseBase importXls(String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Long recordingId, Integer tbId) {
        String filePath = saveFilePath + fileName;
        ResponseBase responseBase = null;
        //判断文件类型 fileType()
        File file = new File(filePath);
        try (FileInputStream in = new FileInputStream(filePath);
             Workbook wb = fileType(in, file)) {
            if (wb == null) {
                return BaseApiService.setResultError("不是excel文件~");
            }
            Sheet sheet = wb.getSheetAt(0);
            int line = 1;
            int totalNumber = sheet.getRow(0).getPhysicalNumberOfCells(); //获取总列数
            //拿到数据库的表头 进行校验
            List<String> head = getHeadInfo(siteId, tbId, null);
            //对比表头
            ResponseBase headResponse = contrastHeadMethod(totalNumber, sheet, head);
            //如果表头对比失败
            if (headResponse != null) {
                return headResponse;
            }
            int lastRowNum = sheet.getLastRowNum(); // 获取总行数
            writeLock.lock();
            try {
                switch (tbId) {
                    //Cpr
                    case 105:
                        responseBase = readTableCpr(line, lastRowNum, shopId, siteId, uid, recordingId, totalNumber, sheet, head);
                        break;
                    // STR
                    case 107:
                        responseBase = readTableStr(line, lastRowNum, shopId, siteId, uid, recordingId, totalNumber, sheet);
                        break;
                    //OAR
                    case 106:
                        responseBase = readTableOar(line, lastRowNum, shopId, siteId, uid, recordingId, totalNumber, sheet, head);
                        break;
                    //HL
                    case 125:
                        responseBase = readTableHl(line, lastRowNum, shopId, siteId, uid, recordingId, totalNumber, sheet);
                        break;
                }
                return saveUserUploadInfo(responseBase, recordingId, fileName, null, 1);
            } finally {
                writeLock.unlock();
            }
        } catch (FileNotFoundException e) {
            return BaseApiService.setResultError("FileInputStream 异常~");
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            responseBase = BaseApiService.setResultError("第" + count.get() + "行信息错误,数据存入失败~");
            //如果报错更新状态
            responseBase = saveUserUploadInfo(responseBase, recordingId, fileName, null, 0);
            return responseBase;
        } finally {
            count.set(0L);
        }
    }

    /**
     * 读取HL 信息
     *
     * @param line
     * @param lastRowNum
     * @param shopId
     * @param siteId
     * @param uid
     * @param recordingId
     * @param totalNumber
     * @param sheet
     * @return
     */
    public ResponseBase readTableHl(int line, int lastRowNum, Long shopId, Long siteId, Long uid, Long recordingId,
                                    int totalNumber, Sheet sheet) {
        List<SalesAmazonAdHl> hlList;
        SalesAmazonAdHl adHl;
        Row row;
        Cell cell;
        // 开始时间
        Long begin = new Date().getTime();
        hlList = new ArrayList<>();
        for (int i = line; i <= lastRowNum; i++) {
            //count ++
            inCreateCount();
            adHl = setHl(shopId, siteId, uid, recordingId);
            for (int j = 0; j < totalNumber; j++) {
                row = sheet.getRow(i);
                cell = row.getCell(j);
                adHl = setHlPojo(j, adHl, cell);
            }
            hlList.add(adHl);
        }
        if (hlList.size() > 0) {
            int countHl = hlService.AddSalesAmazonAdHlList(hlList);
            return printCount(countHl, begin);
        }
        return null;
    }

    /**
     * 读取Oar信息
     *
     * @return
     */
    public ResponseBase readTableOar(int line, int lastRowNum, Long shopId, Long siteId, Long uid, Long recordingId,
                                     int totalNumber, Sheet sheet, List<String> head) {
        List<SalesAmazonAdOar> oarList;
        SalesAmazonAdOar adOar;
        // 开始时间
        Long begin = new Date().getTime();
        Row row = null;
        Cell cell;
        oarList = new ArrayList<>();
        for (int i = line; i <= lastRowNum; i++) {
            //count ++
            inCreateCount();
            adOar = setOar(shopId, siteId, uid, recordingId);
            for (int j = 0; j < totalNumber; j++) {
                row = sheet.getRow(i);
                cell = row.getCell(j);
                adOar = setOarPojo(j, adOar, cell);
            }
            Long skuId = skuService.getAsinSkuId(shopId, siteId, adOar.getOtherAsin());
            //设置没有SKU的信息导入
            if (xslSkuList(skuId, adOar, row, totalNumber, head) != null) {
                oarList.add((SalesAmazonAdOar) xslSkuList(skuId, adOar, row, totalNumber, head));
            }
        }
        if (oarList.size() > 0) {
            int countOar = oarService.AddSalesAmazonAdOarList(oarList);
            return printCount(countOar, begin);
        }
        return null;
    }

    /**
     * 读取Str信息
     *
     * @return
     */
    public ResponseBase readTableStr(int line, int lastRowNum, Long shopId, Long siteId, Long uid, Long recordingId,
                                     int totalNumber, Sheet sheet) {
        List<SalesAmazonAdStr> strList;
        SalesAmazonAdStr adStr;
        // 开始时间
        Long begin = new Date().getTime();
        Row row;
        Cell cell;
        strList = new ArrayList<>();
        for (int i = line; i <= lastRowNum; i++) {
            //count ++
            inCreateCount();
            adStr = setStr(shopId, siteId, uid, recordingId);
            for (int j = 0; j < totalNumber; j++) {
                row = sheet.getRow(i);
                cell = row.getCell(j);
                adStr = setStrPojo(j, adStr, cell);
            }
            strList.add(adStr);
        }
        if (strList.size() > 0) {
            int countStr = strService.AddSalesAmazonAdStrList(strList);
            return printCount(countStr, begin);
        }
        return null;
    }

    /**
     * 读取Cprxls 信息
     *
     * @param line
     * @param lastRowNum
     * @param shopId
     * @param siteId
     * @param uid
     * @param recordingId
     * @param totalNumber
     * @param sheet
     * @return
     */
    public ResponseBase readTableCpr(int line, int lastRowNum, Long shopId, Long siteId, Long uid, Long recordingId,
                                     int totalNumber, Sheet sheet, List<String> head) {
        List<SalesAmazonAdCpr> cprList;
        SalesAmazonAdCpr saCpr;
        Row row = null;
        Cell cell;
        // 开始时间
        Long begin = new Date().getTime();
        cprList = new ArrayList<>();
        for (int i = line; i <= lastRowNum; i++) {
            //count ++
            inCreateCount();
            saCpr = setCpr(shopId, siteId, uid, recordingId);
            for (int j = 0; j < totalNumber; j++) {
                row = sheet.getRow(i);
                cell = row.getCell(j);
                saCpr = setCprPojo(j, saCpr, cell);
            }
            Long skuId = skuService.selSkuId(shopId, siteId, saCpr.getAdvertisedSku());
            //设置没有SKU的信息导入
            if (xslSkuList(skuId, saCpr, row, totalNumber, head) != null) {
                cprList.add((SalesAmazonAdCpr) xslSkuList(skuId, saCpr, row, totalNumber, head));
            }
        }
        if (cprList.size() > 0) {
            int countCpr = cprService.AddSalesAmazonAdCprList(cprList);
            //Timing.getInstance().setStatus("success");
            return printCount(countCpr, begin);
        }
        return null;
    }

    /**
     * 通用更新方法
     */
    public ResponseBase upUserUpload(int status, Long id, String fileName, String msg) {
        UserUpload upload;
        switch (status) {
            case 0:
                upload = recordInfo(status, msg, id, fileName);
                return BaseApiService.setResultSuccess(msg, upload);
            case 1:
                upload = recordInfo(status, msg, id, fileName);
                return BaseApiService.setResultError("error" + msg, upload);
            case 2:
                upload = recordInfo(2, msg, id, fileName);
                return BaseApiService.setResultSuccess(msg, upload);
            case 3:
                upload = recordInfo(status, msg, id, fileName);
                return BaseApiService.setResultError(msg, upload);
            case 4:
                break;
        }
        return null;
    }

    /**
     * 通用打印输出语句 方法
     *
     * @param s
     * @return
     */
    public ResponseBase printCount(int s, Long begin) {
        if (s > 0) {
            // 结束时间
            Long end = new Date().getTime();
            return BaseApiService.setResultSuccess(count.get() - 1 + "条数据插入成功~花费时间 : " + (end - begin) / 1000 + " s");
        }
        return null;
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
     * 判断文件类型
     */
    public Workbook fileType(FileInputStream in, File file) {
        try {
            //判断文件是否是excel
            XlsUtils.checkExcel(file);
            //判断Excel的版本,获取Workbook
            return XlsUtils.getWorkbook(in, file);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 比较头部 Txt
     *
     * @param totalNumber
     * @param sheet
     * @param head
     * @return
     */
    public ResponseBase contrastHeadMethod(int totalNumber, Sheet sheet, List<String> head) {
        //如果对比正常 返回 Null
        if (!compareHeadXls(totalNumber, sheet, head)) {
            //更新
            return BaseApiService.setResultError(Constants.MSG_XLS);
        }
        return null;
    }

    /**
     * 对比xls 表头信息是否一致
     *
     * @param totalNumber
     * @param sheet
     * @return
     */
    public boolean compareHeadXls(int totalNumber, Sheet sheet, List<String> fBalanceHead) {
        Row row;
        Cell cell;
        List<String> twoList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < totalNumber; j++) {
                row = sheet.getRow(i);
                cell = row.getCell(j);
                twoList.add(cell.toString().trim());
                System.out.println(cell.toString().trim());
            }
        }
        return ArrUtils.eqOrderList(fBalanceHead, twoList);
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
            //如果等于0 就先设置头
            if (skuNoIdList.size() == 0) {
                skuNoIdList.add(head);
            }
            //count -- sumNoSku ++
            delCreateCount();
            inCreateSumNoSku();
            List<String> skuListNo = new ArrayList<>();
            //拿到那一行信息
            for (int i = 0; i < totalNumber; i++) {
                skuListNo.add(XlsUtils.getCellValue(row.getCell(i)));
            }
            skuNoIdList.add(skuListNo);
            return null;
        }
        return "success";
    }

    /**
     * 记录xlsx文件 的上传信息
     *
     * @param responseBase
     * @param id
     * @return
     */
    public ResponseBase saveUserUploadInfo(ResponseBase responseBase, Long recordingId, String fileName, List<String> head, int type) {
        try {
            if (responseBase.getCode() == 200) {
                if (skuNoIdList.size() != 0) {
                    if (type == 1) {
                        //写入xlsx 文件写入到服务器的地址   Constants.WRITE_SAVE_FILE_PATH
                        XlsUtils.outPutXssFile(skuNoIdList, Constants.WRITE_SAVE_FILE_PATH, fileName);
                    } else if (type == 2) {
                        //写入CSV文件到本地
                        CSVUtil.write(head, skuNoIdList, Constants.WRITE_SAVE_FILE_PATH, fileName);
                    } else if (type == 3) {
                        TxtUtils.writeFileTxt(skuNoIdList, Constants.WRITE_SAVE_FILE_PATH, fileName);
                        //写入Txt
                    }
                    //上传成功 有些skuId 记录上传信息~
                    String msg = responseBase.getMsg() + "----有" + sumErrorSku.get() + "个没有sku文件/数据库没有typeName";
                    sumErrorSku.set(0);
                    return upUserUpload(2, recordingId, fileName, msg);
                }
                //上传成功 都有skuId~
                return upUserUpload(0, recordingId, fileName, responseBase.getMsg());
            } else {
                //存入信息报错
                return upUserUpload(1, recordingId, fileName, responseBase.getMsg());
            }
        } finally {
            //清空数据
            skuNoIdList.clear();
        }
    }

}
