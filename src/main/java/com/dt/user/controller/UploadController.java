package com.dt.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.mapper.BasePublicMapper.BasicPublicAmazonTypeMapper;
import com.dt.user.model.*;
import com.dt.user.service.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private FinancialSalesBalanceService financialSalesBalanceService;

    @Autowired
    private BasicSalesAmazonCsvTxtXslHeaderService salesAmazonCsvTxtXslHeaderService;

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
    private BasicPublicAmazonTypeMapper typeMapper;
    //获取没有SKU的List集合 并发List 容器
    private CopyOnWriteArrayList skuNoIdList;
    //行数 /报错行数
    ThreadLocal<Integer> count = ThreadLocal.withInitial(() -> 1);
    //没有sku有几行存入
    ThreadLocal<Integer> sumNoSku = ThreadLocal.withInitial(() -> 0);
    //读写锁
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    //读锁
    Lock readLock = readWriteLock.readLock();
    //写锁
    Lock writeLock = readWriteLock.writeLock();

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
    public ResponseBase saveFileInfo(HttpServletRequest request, @RequestParam("sId") String sId, @RequestParam("seId") String seId, @RequestParam("payId") String payId, @RequestParam("menuId") String menuId) {
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
            Integer shopId = Integer.parseInt(sId);
            //站点ID
            Integer siteId = Integer.parseInt(seId);
            Integer tbId = Integer.parseInt(menuId);
            // pId
            Integer pId;
            if (StringUtils.isBlank(payId)) {
                pId = 0;
            } else {
                pId = Integer.parseInt(payId);
            }
            int status = isUpload ? 0 : 4;
            //记录用户上传信息~
            upload = uploadOperating(new UserUpload(), siteId, shopId, fileName, saveFilePath, user, pId, status, msg, tbId);
            if (isUpload) {
                uploadList.add(upload);
            }
            isUpload = true;
        }
        String getMsg = "上传了" + files.size() + "个文件/" + "其中" + fileCount + "个文件失败~ 失败文件名字" + sb.toString() + "";
        return BaseApiService.setResultSuccess(getMsg, uploadList);
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
                    responseBase = shopSelection(userUpload.getFilePath(), userUpload.getName(), userUpload.getSiteId(), userUpload.getShopId(), userUpload.getUid(), userUpload.getpId(), userUpload.getId(), userUpload.getTbId());
                    responseBaseList.add(responseBase);
                } else if (typeFile.equals("xlsx") || typeFile.equals("xls")) {
                    responseBase = uploadAd(userUpload.getFilePath(), userUpload.getName(), userUpload.getSiteId(), userUpload.getShopId(), userUpload.getUid(), userUpload.getId(), userUpload.getTbId());
                    responseBaseList.add(responseBase);
                }
            }
        }
        return BaseApiService.setResultSuccess(responseBaseList);
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
    public ResponseBase shopSelection(String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Integer pId, Long id, Integer tbId) {
        ResponseBase responseCsv = null;
        List<String> oldHeadList = null;
        String filePath = saveFilePath + fileName;
        //如果是财务导入数据 拿到头信息 对比
        if (tbId == 85) {
            //获得头信息长度
            String csvJson = CSVUtil.startReadLine(filePath, siteId);
            JSONObject rowJson = JSONObject.parseObject(csvJson);
            int row = (Integer) rowJson.get("index");
            if (row == -1) {
                String msg = "存入数据失败,请检查表头第一行是否正确/请检查上传的站点~";
                return upUserUpload(3, id, fileName, msg);
            }
            oldHeadList = JSONObject.parseArray(rowJson.get("head").toString(), String.class);
        }else{

        }
//           comparisonBase = comparison(csvReader, headList, oldHeadList, seId, tbId);
//            if (comparisonBase != null) {
//                return comparisonBase;
//
        }
        writeLock.lock();
        skuNoIdList = new CopyOnWriteArrayList();
        try {
            switch (tbId) {
                //财务CSV
                case 85:
                    responseCsv = saveCsvFsb(filePath, row, shopId, siteId, uid, oldHeadList, pId.longValue(), id, tbId);
                    break;
                case 108:
                    saveCsvBusiness(filePath, row, shopId, siteId, uid, pId.longValue(), id, tbId);
                    break;

            }
            return csvSaveUserUploadInfo(responseCsv, id, fileName, oldHeadList);
        } catch (Exception e) {
            responseCsv = BaseApiService.setResultError("第" + count.get() + "行信息错误,数据存入失败~");
            responseCsv = csvSaveUserUploadInfo(responseCsv, id, fileName, null);
            return responseCsv;
        } finally {
            count.set(1);
            writeLock.unlock();
        }
    }


    /**
     * csv 业务报告录入
     *
     * @param filePath
     * @param row
     * @param sId
     * @param seId
     * @param uid
     * @param headArr
     * @param pId
     * @param recordingId
     * @param tbId
     * @return
     * @throws IOException
     */
    public ResponseBase saveCsvBusiness(String filePath, int row, Long sId, Long seId, Long uid, Long pId, Long recordingId, Integer tbId) throws IOException {

    }

    /**
     * csv财务数据解析
     *
     * @param filePath
     * @param row
     * @param sId
     * @param seId
     * @param uid
     * @return
     */
    public ResponseBase saveCsvFsb(String filePath, int row, Long sId, Long seId, Long uid, List<String> headArr, Long pId, Long recordingId, Integer tbId) throws IOException {
        // 开始时间
        Long begin = new Date().getTime();
        InputStreamReader isr;
        // 创建CSV读对象
        CsvReader csvReader;
        int index = 0;
        FinancialSalesBalance fb;
        //设置编码格式 ,日文解码shift_jis
        String coding = seId == 9 ? "shift_jis" : "GBK";
        isr = new InputStreamReader(new FileInputStream(new File(filePath)), coding);
        csvReader = new CsvReader(isr);
        List<FinancialSalesBalance> fsbList = new ArrayList<>();
        List<String> headList = new ArrayList<>();
        ResponseBase comparisonBase;
        try {
            //row==0 第一行就是头
            if (row == 0) {
                comparisonBase = comparison(csvReader, headList, headArr, seId, tbId);
                if (comparisonBase != null) {
                    return comparisonBase;
                }
            }
            while (csvReader.readRecord()) {
                //count ++
                inCreateCount();
                //如果是多行的
                if (index == (row - 1)) {
                    comparisonBase = comparison(csvReader, headList, headArr, seId, tbId);
                    if (comparisonBase != null) {
                        return comparisonBase;
                    }
                }
                //如果正确 通过站点ID 判断 存入 哪个站点数据
                //美国站
                if (index >= row && seId == 1L) {
                    fb = usaDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //加拿大站
                else if (index >= row && seId == 2L) {
                    fb = canadaDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }   //澳大利亚站
                else if (index >= row && seId == 3L) {
                    fb = australiaDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //英国站
                else if (index >= row && seId == 4L) {
                    fb = unitedKingdomDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //德国站
                else if (index >= row && seId == 5L) {
                    fb = germanDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //法国
                else if (index >= row && seId == 6L) {
                    fb = franceDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                } //意大利
                else if (index >= row && seId == 7L) {
                    fb = italyDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }  //西班牙
                else if (index >= row && seId == 8L) {
                    fb = spainDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                } //日本
                else if (index >= row && seId == 9L) {
                    fb = japanDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                } //墨西哥
                else if (index >= row && seId == 10L) {
                    fb = mexicoDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                index++;
            }
            if (fsbList.size() > 0) {
                int number = financialSalesBalanceService.addInfo(fsbList);
                if (number != 0) {
                    // 结束时间
                    Long end = new Date().getTime();
                    int sum = (count.get() - row - 1);
                    return BaseApiService.setResultSuccess(sum + "条数据插入成功~花费时间 : " + (end - begin) / 1000 + " s");
                }
            }
            return BaseApiService.setResultError("表里的skuID全部不一致 请修改~");
        } finally {
            TryUtils.ioClose(null, isr, null, csvReader, null);

        }
    }

    //并发++
    public void inCreateSumNoSku() {
        Integer sumSku = sumNoSku.get();
        sumSku++;
        sumNoSku.set(sumSku);
    }

    //并发--
    public void delSumNoSku() {
        Integer delSumSku = sumNoSku.get();
        delSumSku--;
        sumNoSku.set(delSumSku);
    }

    public void inCreateCount() {
        Integer myCount = count.get();
        myCount++;
        count.set(myCount);
    }

    public void delCreateCount() {
        Integer typeCount = count.get();
        typeCount--;
        count.set(typeCount);
    }

    /**
     * 记录csv文件 的上传信息
     *
     * @param responseCsv
     * @param recordingId
     * @return
     */
    public ResponseBase csvSaveUserUploadInfo(ResponseBase responseCsv, Long recordingId, String fileName, List<String> head) {
        if (responseCsv.getCode() == 200) {
            if (skuNoIdList.size() != 0) {
                //文件写入到服务器的地址
                String skuNoPath = Constants.WRITE_SAVE_FILE_PATH;
                //写入CSV文件到本地
                CSVUtil.write(head, skuNoIdList, skuNoPath, fileName);
                //上传成功 有些skuId 记录上传信息~
                String msg = responseCsv.getMsg() + "----有" + sumNoSku.get() + "个没有sku文件/数据库没有typeName";
                sumNoSku.set(0);
                return upUserUpload(2, recordingId, fileName, msg);
            }
            //上传成功 都有skuId~
            return upUserUpload(0, recordingId, fileName, responseCsv.getMsg());
        } else {
            //存入信息报错
            return upUserUpload(1, recordingId, fileName, responseCsv.getMsg());
        }
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
    public UserUpload uploadOperating(UserUpload upload, Integer siteId, Integer shopId, String fileName, String saveFilePath, UserInfo user, Integer pId, Integer status, String msg, Integer tbId) {
        //存入文件名字
        upload.setName(fileName);
        //存入上传时间
        upload.setCreateDate(new Date().getTime());
        //用户ID
        upload.setUid(user.getUid());
        //上传服务器路径
        upload.setFilePath(saveFilePath);
        //站点ID
        upload.setSiteId(siteId.longValue());
        //店铺ID
        upload.setShopId(shopId.longValue());
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
     * @param csvReader
     * @param headList
     * @param headArr
     * @param seId
     * @param id
     * @return
     * @throws IOException
     */
    public ResponseBase comparison(CsvReader csvReader, List<String> headList, List<String> headArr, Long seId, int id) throws IOException {
        csvReader.readHeaders();
        //比较头部
        if (!compareHeadCsv(headList, headArr, getHeadInfo(seId, id))) {
            return BaseApiService.setResultError("CSV文件表头信息不一致/请检查~");
        }
        return null;
    }

    /**
     * csv墨西哥存入对象
     */
    public FinancialSalesBalance mexicoDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("fecha/hora"), Constants.MEXICO_TIME));
        fsb.setSettlemenId(StrUtils.repString(csvReader.get("Id. de liquidación")));
        String type = StrUtils.repString(csvReader.get("tipo "));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.repString(csvReader.get("Id. del pedido")));
        String skuName = StrUtils.repString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.repString(csvReader.get("descripción")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("cantidad")));
        fsb.setMarketplace(StrUtils.repString(csvReader.get("marketplace")));
        fsb.setFulfillment(StrUtils.repString(csvReader.get("cumplimiento")));
        fsb.setCity(StrUtils.repString(csvReader.get("ciudad del pedido")));
        fsb.setState(StrUtils.repString(csvReader.get("estado del pedido")));
        fsb.setPostal(StrUtils.repString(csvReader.get("código postal del pedido")));
        fsb.setSales(StrUtils.replaceDouble(csvReader.get("ventas de productos")));
        fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get("créditos de envío")));
        fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get("descuentos promocionales")));
        fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get("tarifas de venta")));
        fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get("tarifas fba")));
        fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("tarifas de otra transacción")));
        fsb.setOther(StrUtils.replaceDouble(csvReader.get("otro")));
        fsb.setTotal(StrUtils.replaceDouble(csvReader.get("total")));
        StrUtils.isService(fsb.getType(), fsb);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * csv 日本存入对象
     */
    public FinancialSalesBalance japanDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("日付/時間"), Constants.JAPAN_TIME));
        fsb.setSettlemenId(StrUtils.repString(csvReader.get("決済番号")));
        String type = StrUtils.repString(csvReader.get("トランザクションの種類"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.repString(csvReader.get("注文番号")));
        String skuName = StrUtils.repString(csvReader.get("SKU"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.repString(csvReader.get("説明")));
        fsb.setMarketplace(StrUtils.repString(csvReader.get("Amazon 出品サービス")));
        fsb.setFulfillment(StrUtils.repString(csvReader.get("フルフィルメント")));
        fsb.setCity(StrUtils.repString(csvReader.get("市町村")));
        fsb.setState(StrUtils.repString(csvReader.get("都道府県")));
        fsb.setPostal(StrUtils.repString(csvReader.get("郵便番号")));
        fsb.setSales(StrUtils.replaceDouble(csvReader.get("商品売上")));
        fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get("配送料")));
        fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get("ギフト包装手数料")));
        fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("プロモーション割引額")));
        fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get("手数料")));
        fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get("FBA 手数料")));
        fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("トランザクションに関するその他の手数料")));
        fsb.setOther(StrUtils.replaceDouble(csvReader.get("その他")));
        fsb.setTotal(StrUtils.replaceDouble(csvReader.get("合計")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("数量")));
        StrUtils.isService(fsb.getType(), fsb);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * csv 法国存入对象
     */
    public FinancialSalesBalance franceDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws IOException {
        fsb.setDate(DateUtils.getFranceTime(csvReader.get("date/heure"), Constants.FRANCE_TIME));
        fsb.setSettlemenId(StrUtils.repString(csvReader.get("numéro de versement")));
        String type = StrUtils.repString(csvReader.get("type"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.repString(csvReader.get("numéro de la commande")));
        String skuName = StrUtils.repString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.repString(csvReader.get("description")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("quantité")));
        fsb.setMarketplace(StrUtils.repString(csvReader.get("Marketplace")));
        fsb.setFulfillment(StrUtils.repString(csvReader.get("traitement")));
        fsb.setCity(StrUtils.repString(csvReader.get("ville d'où provient la commande")));
        fsb.setState(StrUtils.repString(csvReader.get("Région d'où provient la commande")));
        fsb.setPostal(StrUtils.repString(csvReader.get("code postal de la commande")));
        fsb.setSales(StrUtils.replaceDouble(csvReader.get("ventes de produits")));
        fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get("crédits d'expédition")));
        fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get("crédits sur l'emballage cadeau")));
        fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("Rabais promotionnels")));
        fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get("frais de vente")));
        fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get("Frais Expédié par Amazon")));
        fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("autres frais de transaction")));
        fsb.setOther(StrUtils.replaceDouble(csvReader.get("autre")));
        fsb.setTotal(StrUtils.replaceDouble(csvReader.get("total")));
        StrUtils.isService(fsb.getType(), fsb);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * csv 西班牙存入对象
     */
    public FinancialSalesBalance spainDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("fecha y hora"), Constants.SPAIN_TIME));
        fsb.setSettlemenId(StrUtils.repString(csvReader.get("identificador de pago")));
        String type = StrUtils.repString(csvReader.get("tipo"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.repString(csvReader.get("número de pedido")));
        String skuName = StrUtils.repString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.repString(csvReader.get("descripción")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("cantidad")));
        fsb.setMarketplace(StrUtils.repString(csvReader.get("web de Amazon")));
        fsb.setFulfillment(StrUtils.repString(csvReader.get("gestión logística")));
        fsb.setCity(StrUtils.repString(csvReader.get("ciudad de procedencia del pedido")));
        fsb.setState(StrUtils.repString(csvReader.get("comunidad autónoma de procedencia del pedido")));
        fsb.setPostal(StrUtils.repString(csvReader.get("código postal de procedencia del pedido")));
        fsb.setSales(StrUtils.replaceDouble(csvReader.get("ventas de productos")));
        fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get("abonos de envío")));
        fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get("abonos de envoltorio para regalo")));
        fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("devoluciones promocionales")));
        fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get("tarifas de venta")));
        fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get("tarifas de Logística de Amazon")));
        fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("tarifas de otras transacciones")));
        fsb.setOther(StrUtils.replaceDouble(csvReader.get("otro")));
        fsb.setTotal(StrUtils.replaceDouble(csvReader.get("total")));
        StrUtils.isService(fsb.getType(), fsb);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * csv 意大利存入对象
     */
    public FinancialSalesBalance italyDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws IOException {
        fsb.setDate(DateUtils.getItalyTime(csvReader.get("Data/Ora:"), Constants.ITALY_TIME));
        fsb.setSettlemenId(StrUtils.repString(csvReader.get("Numero pagamento")));
        String type = StrUtils.repString(csvReader.get("Tipo"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.repString(csvReader.get("Numero ordine")));
        String skuName = StrUtils.repString(csvReader.get("SKU"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.repString(csvReader.get("Descrizione")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("Quantità")));
        fsb.setMarketplace(StrUtils.repString(csvReader.get("Marketplace")));
        fsb.setFulfillment(StrUtils.repString(csvReader.get("Gestione")));
        fsb.setCity(StrUtils.repString(csvReader.get("Città di provenienza dell'ordine")));
        fsb.setState(StrUtils.repString(csvReader.get("Provincia di provenienza dell'ordine")));
        fsb.setPostal(StrUtils.repString(csvReader.get("CAP dell'ordine")));
        fsb.setSales(StrUtils.replaceDouble(csvReader.get("Vendite")));
        fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get("Accrediti per le spedizioni")));
        fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get("Accrediti per confezioni regalo")));
        fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("Sconti promozionali")));
        fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get("Commissioni di vendita")));
        fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get("Costi del servizio Logistica di Amazon")));
        fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("Altri costi relativi alle transazioni")));
        fsb.setOther(StrUtils.replaceDouble(csvReader.get("Altro")));
        fsb.setTotal(StrUtils.replaceDouble(csvReader.get("totale")));
        StrUtils.isService(fsb.getType(), fsb);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * csv 英国存入对象
     */
    public FinancialSalesBalance unitedKingdomDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.UNITED_KINGDOM_TIME));
        fsb.setSettlemenId(StrUtils.repString(csvReader.get("settlement id")));
        String type = StrUtils.repString(csvReader.get("type"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.repString(csvReader.get("order id")));
        String skuName = StrUtils.repString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.repString(csvReader.get("description")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("quantity")));
        fsb.setMarketplace(StrUtils.repString(csvReader.get("marketplace")));
        fsb.setFulfillment(StrUtils.repString(csvReader.get("fulfillment")));
        fsb.setCity(StrUtils.repString(csvReader.get("order city")));
        fsb.setState(StrUtils.repString(csvReader.get("order state")));
        fsb.setPostal(StrUtils.repString(csvReader.get("order postal")));
        fsb.setSales(StrUtils.replaceDouble(csvReader.get("product sales")));
        fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get("postage credits")));
        fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get("gift wrap credits")));
        fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("promotional rebates")));
        fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get("selling fees")));
        fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get("fba fees")));
        fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("other transaction fees")));
        fsb.setOther(StrUtils.replaceDouble(csvReader.get("other")));
        fsb.setTotal(StrUtils.replaceDouble(csvReader.get("total")));
        StrUtils.isService(fsb.getType(), fsb);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * csv 澳洲存入对象
     */
    public FinancialSalesBalance australiaDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.AUSTRALIA_TIME));
        fsb.setSettlemenId(StrUtils.repString(csvReader.get("settlement id")));
        String type = StrUtils.repString(csvReader.get("type"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.repString(csvReader.get("order id")));
        String skuName = StrUtils.repString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.repString(csvReader.get("description")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("quantity")));
        fsb.setMarketplace(StrUtils.repString(csvReader.get("marketplace")));
        fsb.setFulfillment(StrUtils.repString(csvReader.get("fulfillment")));
        fsb.setCity(StrUtils.repString(csvReader.get("order city")));
        fsb.setState(StrUtils.repString(csvReader.get("order state")));
        fsb.setPostal(StrUtils.repString(csvReader.get("order postal")));
        fsb.setSales(StrUtils.replaceDouble(csvReader.get("product sales")));
        fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get("shipping credits")));
        fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get("gift wrap credits")));
        fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("promotional rebates")));
        fsb.setSalesTax(StrUtils.replaceDouble(csvReader.get("sales tax collected")));
        fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get("selling fees")));
        fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get("fba fees")));
        fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("other transaction fees")));
        fsb.setOther(StrUtils.replaceDouble(csvReader.get("other")));
        fsb.setTotal(StrUtils.replaceDouble(csvReader.get("total")));
        StrUtils.isService(fsb.getType(), fsb);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * csv 加拿大存入对象
     */
    public FinancialSalesBalance canadaDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.CANADA_TIME));
        fsb.setSettlemenId(StrUtils.repString(csvReader.get("settlement id")));
        String type = StrUtils.repString(csvReader.get("type"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.repString(csvReader.get("order id")));
        String skuName = StrUtils.repString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.repString(csvReader.get("description")));
        fsb.setMarketplace(StrUtils.repString(csvReader.get("marketplace")));
        fsb.setFulfillment(StrUtils.repString(csvReader.get("fulfillment")));
        fsb.setCity(StrUtils.repString(csvReader.get("order city")));
        fsb.setState(StrUtils.repString(csvReader.get("order state")));
        fsb.setPostal(StrUtils.repString(csvReader.get("order postal")));
        fsb.setSales(StrUtils.replaceDouble(csvReader.get("product sales")));
        fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get("shipping credits")));
        fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("promotional rebates")));
        fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get("selling fees")));
        fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get("fba fees")));
        fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("other transaction fees")));
        fsb.setOther(StrUtils.replaceDouble(csvReader.get("other")));
        fsb.setTotal(StrUtils.replaceDouble(csvReader.get("total")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("quantity")));
        StrUtils.isService(fsb.getType(), fsb);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * csv 美国存入对象
     */
    public FinancialSalesBalance usaDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.USA_TIME));
        fsb.setSettlemenId(StrUtils.repString(csvReader.get("settlement id")));
        String type = StrUtils.repString(csvReader.get("type"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.repString(csvReader.get("order id")));
        String skuName = StrUtils.repString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.repString(csvReader.get("description")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("quantity")));
        fsb.setMarketplace(StrUtils.repString(csvReader.get("marketplace")));
        fsb.setFulfillment(StrUtils.repString(csvReader.get("fulfillment")));
        fsb.setCity(StrUtils.repString(csvReader.get("order city")));
        fsb.setState(StrUtils.repString(csvReader.get("order state")));
        fsb.setPostal(StrUtils.repString(csvReader.get("order postal")));
        fsb.setSales(StrUtils.replaceDouble(csvReader.get("product sales")));
        fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get("shipping credits")));
        fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get("gift wrap credits")));
        fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("promotional rebates")));
        fsb.setSalesTax(StrUtils.replaceDouble(csvReader.get("sales tax collected")));
        fsb.setMarketplaceFacilitatorTax(StrUtils.replaceDouble(csvReader.get("Marketplace Facilitator Tax")));
        fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get("selling fees")));
        fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get("fba fees")));
        fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("other transaction fees")));
        fsb.setOther(StrUtils.replaceDouble(csvReader.get("other")));
        fsb.setTotal(StrUtils.replaceDouble(csvReader.get("total")));
        StrUtils.isService(fsb.getType(), fsb);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * csv 德国存入对象
     */
    public FinancialSalesBalance germanDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("Datum/Uhrzeit"), Constants.GERMAN_TIME));
        fsb.setSettlemenId(str(csvReader.get("Abrechnungsnummer")));
        String type = StrUtils.repString(csvReader.get("Typ"));
        //Type类型转换
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.repString(csvReader.get("Bestellnummer")));
        String skuName = StrUtils.repString(csvReader.get("SKU"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.repString(csvReader.get("Beschreibung")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("Menge")));
        fsb.setMarketplace(StrUtils.repString(csvReader.get("Marketplace")));
        fsb.setFulfillment(StrUtils.repString(csvReader.get("Versand")));
        fsb.setState(StrUtils.repString(csvReader.get("Bundesland")));
        fsb.setPostal(StrUtils.repString(csvReader.get("Postleitzahl")));
        fsb.setSales(StrUtils.replaceDouble(csvReader.get("Ums?tze")));
        fsb.setShippingCredits(StrUtils.replaceDouble(csvReader.get("Gutschrift für Versandkosten")));
        fsb.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get("Gutschrift für Geschenkverpackung")));
        fsb.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("Rabatte aus Werbeaktionen")));
        fsb.setSellingFees(StrUtils.replaceDouble(csvReader.get("Verkaufsgebühren")));
        fsb.setFbaFee(StrUtils.replaceDouble(csvReader.get("Gebühren zu Versand durch Amazon")));
        fsb.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("Andere Transaktionsgebühren")));
        fsb.setOther(StrUtils.replaceDouble(csvReader.get("Andere")));
        fsb.setTotal(StrUtils.replaceDouble(csvReader.get("Gesamt")));
        StrUtils.isService(fsb.getType(), fsb);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * csv 获取没有SKU的文件List
     *
     * @param skuId
     * @param csvReader
     * @param fsb
     * @return
     */
    public FinancialSalesBalance skuList(Long skuId, CsvReader csvReader, FinancialSalesBalance fsb) throws IOException {
        if (StringUtils.isNotEmpty(fsb.getSku())) {
            if (skuId == null) {
                //count -- sumNoSku ++
                delCreateCount();
                inCreateSumNoSku();
                List<String> skuListNo = new ArrayList<>();
                for (int i = 0; i < csvReader.getColumnCount(); i++) {
                    skuListNo.add(csvReader.get(i).replace(",", "."));
                }
                skuNoIdList.add(skuListNo);
                return null;
            }
        }
        fsb.setSkuId(skuId);
        return fsb;
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
            //count --
            delCreateCount();
            List<String> typeListNo = new ArrayList<>();
            for (int i = 0; i < csvReader.getColumnCount(); i++) {
                typeListNo.add(csvReader.get(i).replace(",", "."));
            }
            skuNoIdList.add(typeListNo);
            return null;
        }
        return typeName;
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
            String head = oldHeadList.get(i).replace("\"", "").trim();
            headList.add(head);
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
    public List<String> getHeadInfo(Long seId, int tbId) {
        return salesAmazonCsvTxtXslHeaderService.headerList(seId, tbId);
    }


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
    public ResponseBase uploadAd(String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Long recordingId, Integer tbId) {
        String filePath = saveFilePath + fileName;
        FileInputStream in = null;
        ResponseBase responseBase = null;
        Workbook wb = null;
        try {
            in = new FileInputStream(filePath);
            File file = new File(filePath);
            //判断文件类型
            wb = fileType(in, file);
            if (wb == null) {
                return BaseApiService.setResultError("不是excel文件~");
            }
            Sheet sheet = wb.getSheetAt(0);
            int line = 1;
            int totalNumber = sheet.getRow(0).getPhysicalNumberOfCells(); //获取总列数
            //拿到数据库的表头 进行校验
            List<String> head = getHeadInfo(siteId, tbId);
            //对比表头
            ResponseBase headResponse = contrastHeadMethod(totalNumber, sheet, head);
            //如果表头对比失败
            if (headResponse != null) {
                return headResponse;
            }
            int lastRowNum = sheet.getLastRowNum(); // 获取总行数
            writeLock.lock();
            skuNoIdList = new CopyOnWriteArrayList();
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
                return xlsxSaveUserUploadInfo(responseBase, recordingId, fileName);
            } finally {
                writeLock.unlock();
            }
        } catch (FileNotFoundException e) {
            return BaseApiService.setResultError("FileInputStream 异常~");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseBase = BaseApiService.setResultError("第" + count.get() + "行信息错误,数据存入失败~");
            //如果报错更新状态
            responseBase = xlsxSaveUserUploadInfo(responseBase, recordingId, fileName);
            return responseBase;
        } finally {
            count.set(1);
            TryUtils.ioClose(null, null, wb, null, in);
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
     * 比较头部 xls
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
        return "ok";
    }

    /**
     * 记录xlsx文件 的上传信息
     *
     * @param responseBase
     * @param id
     * @return
     */
    public ResponseBase xlsxSaveUserUploadInfo(ResponseBase responseBase, Long id, String fileName) {
        if (responseBase.getCode() == 200) {
            if (skuNoIdList.size() != 0) {
                //写入xlsx 文件写入到服务器的地址   Constants.WRITE_SAVE_FILE_PATH
                XlsUtils.outPutXssFile(skuNoIdList, Constants.WRITE_SAVE_FILE_PATH, fileName);
                //上传成功 有些skuId 记录上传信息~
                String msg = responseBase.getMsg() + "----有" + sumNoSku.get() + "个没有sku文件/数据库没有typeName";
                sumNoSku.set(0);
                return upUserUpload(2, id, fileName, msg);
            }
            //上传成功 都有skuId~
            return upUserUpload(0, id, fileName, responseBase.getMsg());
        } else {
            //存入信息报错
            return upUserUpload(1, id, fileName, responseBase.getMsg());
        }
    }

}
