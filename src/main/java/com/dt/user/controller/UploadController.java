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
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Future;
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
    private SalesAmazonFbaRefundService refundService;

    @Autowired
    private SalesAmazonFbaReceivestockService receivestockService;

    @Autowired
    private BasicSalesAmazonWarehouseService warehouseService;
    /**
     * 多线程返回接收
     */
    private Future<ResponseBase> future;

    @Autowired
    private BasicPublicAmazonTypeMapper typeMapper;
    //获取没有SKU的List集合 并发List 容器
    private CopyOnWriteArrayList<List<String>> skuNoIdList = new CopyOnWriteArrayList<>();

    private CopyOnWriteArraySet<Timing> setTiming = new CopyOnWriteArraySet<>();
    //没有sku有几行存入
    ThreadLocal<Long> numberCount = ThreadLocal.withInitial(() -> 0L);
    //真实存入函数
    ThreadLocal<Long> count = ThreadLocal.withInitial(() -> 0L);
    //没有sku有几行存入
    ThreadLocal<Integer> sumErrorSku = ThreadLocal.withInitial(() -> 0);

    //读写锁
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    //读锁
    Lock readLock = readWriteLock.readLock();
    //写锁
    Lock writeLock = readWriteLock.writeLock();


    public void inCreateNumberCount() {
        Long myNumberCount = numberCount.get();
        myNumberCount++;
        numberCount.set(myNumberCount);
    }

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
    public ResponseBase timingStatus(@RequestParam("redIds") String redIds) {
        Set<Timing> set = new HashSet<>();
        String[] ids = redIds.split(",");
        List<String> arrayList = new ArrayList<>(Arrays.asList(ids));
        if (setTiming.size() > 0) {
            //如果两个长度不够
            if (arrayList.size() != setTiming.size()) {
                int length = setTiming.size() - arrayList.size();
                for (int k = 0; k < length; k++) {
                    arrayList.add("0");
                }
            }
            for (Timing t : setTiming) {
                for (int j = 0; j < arrayList.size(); j++) {
                    if (t.getRedId().equals(Long.parseLong(arrayList.get(j)))) {
                        set.add(t);
                        break;
                    }
                }
            }
        }
        //查询数据
        return BaseApiService.setResultSuccess(set);
    }

    @PostMapping("/downloadCommonFile")
    public ResponseBase downloadFile(HttpServletRequest
                                             request, HttpServletResponse response, @RequestBody Map<String, Object> fileMap) {
        String filePath = (String) fileMap.get("filePath");
        try {
            FileUtils.downloadFile(filePath, response, request);
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
                                     @RequestParam("menuId") String menuId,
                                     @RequestParam("areaId") String areaId, @RequestParam("businessTime") String businessTime) {
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
        int fileCount = 0;
        String msg;
        List<UserUpload> uploadList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < files.size(); i++) {
                file = files.get(i);
                //指定文件存放路径
                String saveFilePath = Constants.SAVE_FILE_PATH;
                // String contentType = file.getContentType();//图片||文件类型
                String fileName = file.getOriginalFilename();//图片||文件名字
                try {
                    FileUtils.uploadFile(file.getBytes(), saveFilePath, fileName);
                    msg = "上传成功~";
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
                //菜单ID
                Integer tbId = null;
                if (StringUtils.isNotBlank(menuId)) {
                    tbId = Integer.parseInt(menuId);
                }
                // 付款类型选择ID
                Integer pId = null;
                if (StringUtils.isNotBlank(payId)) {
                    pId = Integer.parseInt(payId);
                }
                // 洲ID
                Integer aId = null;
                if (StringUtils.isNotBlank(areaId)) {
                    aId = Integer.parseInt(areaId);
                }
                int status = isUpload ? 0 : 4;
                //记录用户上传信息~
                upload = uploadOperating(new UserUpload(), siteId, shopId, fileName, saveFilePath, user, pId, status, msg, tbId, aId, businessTime);
                if (isUpload) {
                    uploadList.add(upload);
                }
                isUpload = true;
            }
            String getMsg = "上传了" + files.size() + "个文件/" + "其中" + fileCount + "个文件失败~ 失败文件名字" + sb.toString() + "";
            return BaseApiService.setResultSuccess(getMsg, uploadList);
        } catch (Exception e) {
            return BaseApiService.setResultError("上传异常,请检查问题", uploadList);
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
        ResponseBase responseBase;
        if (baseNum > 0) {
            for (int i = 0; i < baseNum; i++) {
                UserUpload userUpload = upload.getUploadSuccessList().get(i);
                int fileIndex = userUpload.getName().lastIndexOf(".");
                String typeFile = userUpload.getName().substring(fileIndex + 1);
                if (typeFile.equals("csv")) {
                    responseBase = importCsv(userUpload.getFilePath(), userUpload.getName(), userUpload.getSiteId(), userUpload.getShopId(), userUpload.getUid(),
                            userUpload.getpId(), userUpload.getId(), userUpload.getTbId(), userUpload.getBusinessTime());
                    responseBaseList.add(responseBase);
                } else if (typeFile.equals("xlsx") || typeFile.equals("xls")) {
                    responseBase = importXls(userUpload.getFilePath(), userUpload.getName(), userUpload.getSiteId(), userUpload.getShopId(), userUpload.getUid(), userUpload.getId(), userUpload.getTbId());
                    responseBaseList.add(responseBase);
                } else if (typeFile.equals("txt")) {
                    responseBase = importTxt(userUpload.getFilePath(), userUpload.getName(), userUpload.getShopId(), userUpload.getUid(), userUpload.getId(), userUpload.getTbId(), userUpload.getAreaId());
                    responseBaseList.add(responseBase);
                    // System.out.println("txt");
                }
                System.out.println("删除前" + setTiming.size());
                for (Timing t : setTiming) {
                    if (t.getRedId().equals(userUpload.getId())) {
                        setTiming.remove(t);
                        break;
                    }
                }
                System.out.println("删除后" + setTiming.size());
            }
        }
        return BaseApiService.setResultSuccess(responseBaseList);
    }

    /**
     * 封装错误信息
     *
     * @param msg
     * @param recordingId
     * @param fileName
     * @return
     */
    public ResponseBase errorResult(Integer percentage, String msg, Long recordingId, String fileName, Timing timing, String status, String saveFilePath) {
        timing.setInfo(status, percentage, msg);
        setTiming.add(timing);
        return saveUserUploadInfo(BaseApiService.setResultError(msg), recordingId, fileName, null, 0, saveFilePath);
    }

    /**
     * 封装获得编码格式 适用于TXT  CSV
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public InputStreamReader streamReader(String filePath) throws Exception {
        String fileEncode = EncodingDetect.getJavaEncode(filePath);
        if (fileEncode == null) {
            throw new Exception("filePath加载文件路径不存在");
        }
        return new InputStreamReader(new FileInputStream(filePath), fileEncode);
    }

    //###############################封装Txt
    public ResponseBase importTxt(String saveFilePath, String fileName, Long shopId, Long uid, Long
            recordingId, Integer tbId, Integer aId) {
        Timing timing = new Timing();
        ResponseBase responseCsv;
        String filePath = saveFilePath + fileName;
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
                return errorResult(0, "表头信息不一致", recordingId, fileName, timing, "exception", saveFilePath);
            }
            //设置文件总数
            timing.setFileCount(filePath);
            //第一行List头
            List<String> strLineHead = new ArrayList<>();
            strLineHead.add(lineHead);
            //多线程处理
            responseCsv = dealWithTxtData(br, shopId, uid, recordingId, strLineHead, timing, tbId, aId).get();
            return saveUserUploadInfo(responseCsv, recordingId, fileName, null, 3, saveFilePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            String errorMsg = "数据存入失败====>请查找" + (numberCount.get() + 1) + "行错误信息";
            return errorResult(0, errorMsg, recordingId, fileName, timing, "exception", saveFilePath);
        } finally {
            count.set(0L);
            numberCount.set(0L);
        }
    }

    /**
     * 线程池 处理数据Xls
     *
     * @throws IOException
     */
    @Async("executor")
    public Future<ResponseBase> dealWithXlsData(Long shopId, Long siteId, Long uid, Long
            recordingId, int totalNumber, List<String> head, Integer tbId, Sheet sheet, Timing timing) {
        future = new AsyncResult<>(readTable(shopId, siteId, uid, recordingId, totalNumber, head, tbId, sheet, timing));
        return future;
    }

    /**
     * 线程池 处理数据 Txt
     */
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
    @Async("executor")
    public Future<ResponseBase> dealWithCsvData(CsvReader csvReader, int row, Long shopId, Long siteId, Long uid, Integer pId, Long recordingId, Integer tbId, String businessTime, Timing timing) throws IOException {
        future = new AsyncResult<>(saveCsv(csvReader, row, shopId, siteId, uid, pId, recordingId, tbId, businessTime, timing));
        return future;
    }


    /**
     * 封装Txt
     */
    public ResponseBase saveTxt(BufferedReader br, Long shopId, Long uid, Long
            recordingId, List<String> lineHead, Timing timing, Integer tbId, Integer aId) throws IOException {
        // 开始时间
        Long begin = new Date().getTime();
        List<SalesAmazonFbaReceivestock> sfReceivesList = null;
        List<SalesAmazonFbaRefund> safRefundList = null;
        List<SalesAmazonFbaTradeReport> safTradList = null;
        SalesAmazonFbaTradeReport sftPort;
        SalesAmazonFbaRefund sfRefund;
        SalesAmazonFbaReceivestock sfReceives;
        List<?> tList = new ArrayList<>();
        if (tbId == 109) {
            safTradList = ArrUtils.listT(tList);
        } else if (tbId == 110) {
            safRefundList = ArrUtils.listT(tList);
        } else if (tbId == 113) {
            sfReceivesList = ArrUtils.listT(tList);
        }
        String line;
        int index = 0;
        timing.setMsg("正在校验数据..........");
        while ((line = br.readLine()) != null) {
            inCreateNumberCount();
            //count ++ 成功数量
            inCreateCount();
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
                        sfReceives = salesReceivestock(i, sfReceives, newLine, shopId, aId);
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
            }
            index++;
            //计算百分比
            timing.setAttributesTim(index);
            setTiming.add(timing);
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
        if (countTrad > 0) {
            return printCount(begin, timing, count.get(), index);
        }
        return BaseApiService.setResultError("数据存入异常,请检查错误信息");
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
    public ResponseBase importCsv(String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Integer
            pId, Long recordingId, Integer tbId, String businessTime) {
        ResponseBase responseCsv;
        List<String> fileHeadList;
        String filePath = saveFilePath + fileName;
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
            return errorResult(0, "表中真实字段第一行信息比对不上", recordingId, fileName, timing, "exception", filePath);
        }
        //拿到之前的表头信息
        fileHeadList = JSONObject.parseArray(rowJson.get("head").toString(), String.class);
        //拿到数据库的表头 进行校验
        List<String> sqlHeadList = getHeadInfo(siteId, tbId, null);
        //对比表头是否一致
        boolean isFlg = compareHeadCsv(fileHeadList, sqlHeadList);
        if (!isFlg) {
            //返回错误信息
            return errorResult(0, "表头信息不一致", recordingId, fileName, timing, "exception", saveFilePath);
        }
        try (InputStreamReader isr = streamReader(filePath)) {
            csvReader = new CsvReader(isr);
            //设置文件总数
            timing.setFileCount(filePath);
            responseCsv = dealWithCsvData(csvReader, row, shopId, siteId, uid, pId, recordingId, tbId, businessTime, timing).get();
            return saveUserUploadInfo(responseCsv, recordingId, fileName, fileHeadList, 2, saveFilePath);
        } catch (Exception e) {
            String errorMsg = "数据存入失败====>请查找" + (numberCount.get() + 1) + "行错误信息" + e.getMessage();
            return errorResult(0, errorMsg, recordingId, fileName, timing, "exception", saveFilePath);
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
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
        timing.setMsg("正在校验数据..........");
        while (csvReader.readRecord()) {
            if (index >= row) {
                inCreateNumberCount();
                inCreateCount();
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
            timing.setAttributesTim(index);
            setTiming.add(timing);
        }
        int number = 0;
        //财务
        if (fsbList != null) {
            if (fsbList.size() > 0) {
                //插入数据
                timing.setMsg("正在导入数据库..........");
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
     * 封装更新信息
     *
     * @param status
     * @param msg
     */
    public UserUpload recordInfo(Integer status, String msg, Long id, String fileName, String saveFilePath) {
        UserUpload upload = new UserUpload();
        upload.setId(id);
        upload.setUid(new Date().getTime());
        if (status != 0) {
            if (status == 3) {
                upload.setStatus(status);
            }
            if (status == 2) {
                upload.setName(fileName);
                upload.setFilePath(saveFilePath);
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
                                      String msg, Integer tbId, Integer aId, String businessTime) {
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
        //菜单信息
        upload.setTbId(tbId);
        //业务报告时间信息
        if (!businessTime.equals("undefined")) {
            upload.setBusinessTime(businessTime);
        }
        userUploadService.addUserUploadInfo(upload);
        return upload;
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
     * 接收订单信息存入
     *
     * @param sft
     * @param j
     * @return
     * @throws IOException
     */
    public SalesAmazonFbaReceivestock salesReceivestock(int i, SalesAmazonFbaReceivestock sft, String[] j, Long sId, Integer aId) {
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
     * csv 财务存入对象
     */
    public FinancialSalesBalance saveFinance(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws
            IOException {
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
    public ResponseBase importXls(String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Long
            recordingId, Integer tbId) {
        Timing timing = new Timing();
        String filePath = saveFilePath + fileName;
        ResponseBase responseBase;
        //判断文件类型 fileType()
        File file = new File(filePath);
        try (FileInputStream in = new FileInputStream(filePath);
             Workbook wb = fileType(in, file)) {
            if (wb == null) {
                //返回错误信息
                return errorResult(0, "不是excel文件", recordingId, fileName, timing, "exception", filePath);
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
                return errorResult(0, "表头信息不一致", recordingId, fileName, timing, "exception", filePath);
            }
            responseBase = dealWithXlsData(shopId, siteId, uid, recordingId, totalNumber, head, tbId, sheet, timing).get();
            return saveUserUploadInfo(responseBase, recordingId, fileName, null, 1, filePath);
        } catch (Exception e) {
            String errorMsg = "数据存入失败====>请查找" + (numberCount.get() + 1) + "行错误信息" + e.getMessage();
            return errorResult(0, errorMsg, recordingId, fileName, timing, "exception", filePath);
        } finally {
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
    public ResponseBase readTable(Long shopId, Long siteId, Long uid, Long
            recordingId, int totalNumber, List<String> head, Integer tbId, Sheet sheet, Timing timing) {
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
        timing.setMsg("正在校验数据..........");
        for (int i = line; i <= lastRowNum; i++) {
            inCreateNumberCount();
            //count ++
            inCreateCount();
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
            timing.setAttributesTim(index);
            setTiming.add(timing);
        }
        int saveCount = 0;
        if (cprList != null) {
            if (cprList.size() > 0) {
                //插入数据
                timing.setMsg("正在导入数据库..........");
                saveCount = cprService.AddSalesAmazonAdCprList(cprList);
            }
        }
        if (strList != null) {
            if (strList.size() > 0) {
                //插入数据
                timing.setMsg("正在导入数据库..........");
                saveCount = strService.AddSalesAmazonAdStrList(strList);
            }
        }
        if (oarList != null) {
            if (oarList.size() > 0) {
                //插入数据
                timing.setMsg("正在导入数据库..........");
                saveCount = oarService.AddSalesAmazonAdOarList(oarList);
            }
        }
        if (hlList != null) {
            if (hlList.size() > 0) {
                //插入数据
                timing.setMsg("正在导入数据库..........");
                saveCount = hlService.AddSalesAmazonAdHlList(hlList);
            }
        }
        if (saveCount > 0) {
            return printCount(begin, timing, count.get(), index);
        }
        return BaseApiService.setResultError("数据存入异常,请检查错误信息");
    }

    /**
     * 通用更新方法
     */
    public ResponseBase upUserUpload(int status, Long id, String fileName, String msg, String saveFilePath) {
        UserUpload upload;
        switch (status) {
            case 0:
                upload = recordInfo(status, msg, id, fileName, saveFilePath);
                return BaseApiService.setResultSuccess(msg, upload);
            case 1:
                upload = recordInfo(status, msg, id, fileName, saveFilePath);
                return BaseApiService.setResultError("error/" + msg, upload);
            case 2:
                int fileIndex = saveFilePath.lastIndexOf("/");
                upload = recordInfo(status, msg, id, "NO" + fileName,     saveFilePath.substring(0,fileIndex) + "SkuNo/");
                return BaseApiService.setResultSuccess(msg, upload);
            case 3:
                upload = recordInfo(status, msg, id, fileName, saveFilePath);
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
    public ResponseBase printCount(Long begin, Timing timing, Long successNumber, int index) {
        timing.setInfo("success", "数据导入成功..........");
        setTiming.add(timing);
        // 结束时间
        Long end = new Date().getTime();
        return BaseApiService.setResultSuccess("总共" + index + "条数据/" + successNumber + "条数据插入成功/====>失败 " + sumErrorSku.get() + "条/花费时间 : " + (end - begin) / 1000 + " s");
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
     * 写入状态
     *
     * @param responseBase
     * @param recordingId
     * @param fileName
     * @param head
     * @param type
     * @return
     */
    public ResponseBase saveUserUploadInfo(ResponseBase responseBase, Long recordingId, String
            fileName, List<String> head, int type, String saveFilePath) {
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
                        //写入Txt
                        TxtUtils.writeFileTxt(skuNoIdList, Constants.WRITE_SAVE_FILE_PATH, fileName);
                    }
                    //上传成功 有些skuId 记录上传信息~
                    String msg = responseBase.getMsg() + "====>有" + sumErrorSku.get() + "个没有sku文件/数据库没有typeName";
                    sumErrorSku.set(0);
                    return upUserUpload(2, recordingId, fileName, msg, saveFilePath);
                }
                //上传成功 都有skuId~
                return upUserUpload(0, recordingId, fileName, responseBase.getMsg(), saveFilePath);
            } else {
                //存入信息报错
                return upUserUpload(1, recordingId, fileName, responseBase.getMsg(), saveFilePath);
            }
        } finally {
            //清空数据
            skuNoIdList.clear();
        }
    }

}
