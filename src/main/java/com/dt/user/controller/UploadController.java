package com.dt.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.UserUploadDto;
import com.dt.user.mapper.BasePublicMapper.BasicPublicAmazonTypeMapper;
import com.dt.user.model.FinancialSalesBalance;
import com.dt.user.model.SalesAmazonAdCpr;
import com.dt.user.model.UserInfo;
import com.dt.user.model.UserUpload;
import com.dt.user.service.BasePublicService.BasicSalesAmazonCsvTxtXslHeaderService;
import com.dt.user.service.BasePublicService.BasicSalesAmazonSkuService;
import com.dt.user.service.FinancialSalesBalanceService;
import com.dt.user.service.SalesAmazonAdCprService;
import com.dt.user.service.UserUploadService;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
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
    private BasicPublicAmazonTypeMapper bAmazonTypeMapper;
    //获取没有SKU的List集合
    private volatile List<List<String>> skuNoIdList;
    //行数 /报错行数
    private volatile int count;
    //没有sku有几行存入
    private volatile int sumNoSku;
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
    public ResponseBase saveFileInfo(HttpServletRequest request, @RequestParam("sId") String sId, @RequestParam("seId") String seId, @RequestParam("payId") String payId) {
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
            // pId
            Integer pId;
            if (StringUtils.isBlank(payId)) {
                pId = 0;
            } else {
                pId = Integer.parseInt(payId);
            }
            int status = isUpload ? 0 : 4;
            //记录用户上传信息~
            upload = uploadOperating(new UserUpload(), siteId, shopId, fileName, saveFilePath, user, pId, status, msg);
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
     * @param uploadDto
     * @return
     */
    @PostMapping("/addInfo")
    @Transactional
    public ResponseBase redFileInfo(@RequestBody UserUploadDto uploadDto) {
        List<ResponseBase> responseBaseList = new ArrayList<>();
        int baseNum = uploadDto.getUploadSuccessList().size();
        ResponseBase responseBase;
        if (baseNum > 0) {
            for (int i = 0; i < baseNum; i++) {
                UserUpload userUpload = uploadDto.getUploadSuccessList().get(i);
                int fileIndex = userUpload.getName().lastIndexOf(".");
                String typeFile = userUpload.getName().substring(fileIndex + 1);
                if (typeFile.equals("csv")) {
                    responseBase = shopSelection(userUpload.getFilePath(), userUpload.getName(), userUpload.getSiteId(), userUpload.getShopId(), userUpload.getUid(), userUpload.getpId(), userUpload.getId());
                    responseBaseList.add(responseBase);
                } else if (typeFile.equals("xlsx") || typeFile.equals("xls")) {
                    try {
                        responseBase = uploadAd(userUpload.getFilePath(), userUpload.getName(), userUpload.getSiteId(), userUpload.getShopId(), userUpload.getUid(), userUpload.getId(), 2);
                        responseBaseList.add(xlsxSaveUserUploadInfo(responseBase, userUpload.getId(), userUpload.getName()));
                    } finally {
                        writeLock.unlock();
                    }
                }
            }
        }
        return BaseApiService.setResultSuccess(responseBaseList);
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
                //文件写入到服务器的地址
                String skuNoPath = Constants.WRITE_SAVE_FILE_PATH;
                //写入xlsx
                XlsUtils.outPutXssFile(skuNoIdList, skuNoPath, fileName);
                //上传成功 有些skuId 记录上传信息~
                String msg = responseBase.getMsg() + "----有" + sumNoSku + "个没有sku文件/数据库没有typeName";
                recordInfo(2, msg, id);
                sumNoSku = 0;
                return BaseApiService.setResultSuccess(msg, false);
            }
            //上传成功 都有skuId~
            return BaseApiService.setResultSuccess(responseBase.getMsg());
        } else {
            //存入信息报错
            recordInfo(1, responseBase.getMsg(), id);
            return BaseApiService.setResultError("error");
        }
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
    public ResponseBase shopSelection(String saveFilePath, String fileName, Long siteId, Long shopId, Long uid, Integer pId, Long id) {
        String filePath = saveFilePath + fileName;
        //获得头信息长度
        String csvJson = CSVUtil.startReadLine(filePath, siteId);
        JSONObject rowJson = JSONObject.parseObject(csvJson);
        int row = (Integer) rowJson.get("index");
        if (row == -1) {
            String msg = "存入数据失败,请检查表头第一行是否正确/请检查上传的站点~";
            recordInfo(3, msg, id);
            return BaseApiService.setResultError(msg);
        }
        List<String> oldHeadList = JSONObject.parseArray(rowJson.get("head").toString(), String.class);
        return switchCountry(filePath, row, shopId, siteId, uid, fileName, oldHeadList, pId, id);
    }

    /**
     * csv通用读取信息
     *
     * @param filePath    文件绝对路径
     * @param row         读取到真正的头行数
     * @param sId         店铺ID
     * @param seId        站点ID
     * @param head        表头信息
     * @param recordingId 记录表里面的ID
     * @return
     */
    public ResponseBase switchCountry(String filePath, int row, Long sId, Long seId, Long uid, String fileName, List<String> head, Integer pId, Long recordingId) {
        ResponseBase responseCsv;
        try {
            responseCsv = saveCsv(filePath, row, sId, seId, uid, head, pId.longValue(), recordingId);
        } finally {
            writeLock.unlock();
        }
        if (responseCsv.getCode() == 200) {
            if (skuNoIdList.size() != 0) {
                //文件写入到服务器的地址
                String skuNoPath = Constants.WRITE_SAVE_FILE_PATH;
                //写入CSV文件到本地
                CSVUtil.write(head, skuNoIdList, skuNoPath, fileName);
                //上传成功 有些skuId 记录上传信息~
                String msg = responseCsv.getMsg() + "----有" + sumNoSku + "个没有sku文件/数据库没有typeName";
                recordInfo(2, msg, recordingId);
                sumNoSku = 0;
                return BaseApiService.setResultSuccess(msg, false);
            }
            //上传成功 都有skuId~
            return BaseApiService.setResultSuccess(responseCsv.getMsg());
        } else {
            //存入信息报错
            recordInfo(1, responseCsv.getMsg(), recordingId);
            return BaseApiService.setResultError("error");
        }

    }

    /**
     * 封装更新信息
     *
     * @param status
     * @param msg
     */
    public void recordInfo(Integer status, String msg, Long id) {
        UserUpload userUpload = new UserUpload();
        userUpload.setId(id);
        if (status == 3) {
            userUpload.setStatus(status);
        }
        if (status == 2) {
            userUpload.setStatus(status);
        }
        if (status == 1) {
            userUpload.setStatus(status);
        }
        userUpload.setRemark(msg);
        userUploadService.upUploadInfo(userUpload);
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
    public UserUpload uploadOperating(UserUpload upload, Integer siteId, Integer shopId, String fileName, String saveFilePath, UserInfo user, Integer pId, Integer status, String msg) {
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
        userUploadService.addUserUploadInfo(upload);
        return upload;
    }


    /**
     * csv数据解析
     *
     * @param filePath
     * @param row
     * @param sId
     * @param seId
     * @param uid
     * @return
     */
    public ResponseBase saveCsv(String filePath, int row, Long sId, Long seId, Long uid, List<String> headArr, Long pId, Long recordingId) {
        count = 1;
        // 开始时间
        Long begin = new Date().getTime();
        InputStreamReader isr = null;
        // 创建CSV读对象
        CsvReader csvReader = null;
        int index = 0;
        FinancialSalesBalance fb;
        try {
            //设置编码格式 ,日文解码shift_jis
            String coding = seId == 9 ? "shift_jis" : "GBK";
            isr = new InputStreamReader(new FileInputStream(new File(filePath)), coding);
            csvReader = new CsvReader(isr);
            List<FinancialSalesBalance> fsbList = new ArrayList<>();
            List<String> headList = new ArrayList<>();
            ResponseBase comparisonBase;
            //row==0 第一行就是头
            if (row == 0) {
                comparisonBase = comparison(csvReader, headList, headArr, seId, 1);
                if (comparisonBase != null) {
                    return comparisonBase;
                }
            }
            //读锁
            writeLock.lock();
            skuNoIdList = new ArrayList<>();
            while (csvReader.readRecord()) {
                //count ++
                count++;
                //如果是多行的
                if (index == (row - 1)) {
                    comparisonBase = comparison(csvReader, headList, headArr, seId, 1);
                    if (comparisonBase != null) {
                        return comparisonBase;
                    }
                }
                //如果正确 通过站点ID 判断 存入 哪个站点数据
                //美国站
                if (index >= row && seId == 1L) {
                    fb = usaDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //加拿大站
                else if (index >= row && seId == 2L) {
                    fb = canadaDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }   //澳大利亚站
                else if (index >= row && seId == 3L) {
                    fb = australiaDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //英国站
                else if (index >= row && seId == 4L) {
                    fb = unitedKingdomDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //德国站
                else if (index >= row && seId == 5L) {
                    fb = germanDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //法国
                else if (index >= row && seId == 6L) {
                    fb = franceDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                } //意大利
                else if (index >= row && seId == 7L) {
                    fb = italyDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }  //西班牙
                else if (index >= row && seId == 8L) {
                    fb = spainDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                } //日本
                else if (index >= row && seId == 9L) {
                    fb = japanDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                } //墨西哥
                else if (index >= row && seId == 10L) {
                    fb = mexicoDepositObject(setFsb(sId, seId, uid, pId, recordingId), csvReader, sId, seId, uid);
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
                    int sum = (count - row - 1);
                    return BaseApiService.setResultSuccess(sum + "条数据插入成功~花费时间 : " + (end - begin) / 1000 + " s");
                }
            }
            return BaseApiService.setResultError("表里的skuID全部不一致 请修改~");
        } catch (Exception e) {
            return BaseApiService.setResultError("第" + count + "行信息错误,数据存入失败~");
        } finally {
            count = 1;
            if (csvReader != null) {
                csvReader.close();
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 头部比较返回
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
    public FinancialSalesBalance mexicoDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("fecha/hora"), Constants.MEXICO_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("Id. de liquidación")));
        String type = StrUtils.replaceString(csvReader.get("tipo "));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.replaceString(csvReader.get("Id. del pedido")));
        String skuName = StrUtils.replaceString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.replaceString(csvReader.get("descripción")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("cantidad")));
        fsb.setMarketplace(StrUtils.replaceString(csvReader.get("marketplace")));
        fsb.setFulfillment(StrUtils.replaceString(csvReader.get("cumplimiento")));
        fsb.setCity(StrUtils.replaceString(csvReader.get("ciudad del pedido")));
        fsb.setState(StrUtils.replaceString(csvReader.get("estado del pedido")));
        fsb.setPostal(StrUtils.replaceString(csvReader.get("código postal del pedido")));
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
    public FinancialSalesBalance japanDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("日付/時間"), Constants.JAPAN_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("決済番号")));
        String type = StrUtils.replaceString(csvReader.get("トランザクションの種類"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.replaceString(csvReader.get("注文番号")));
        String skuName = StrUtils.replaceString(csvReader.get("SKU"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.replaceString(csvReader.get("説明")));
        fsb.setMarketplace(StrUtils.replaceString(csvReader.get("Amazon 出品サービス")));
        fsb.setFulfillment(StrUtils.replaceString(csvReader.get("フルフィルメント")));
        fsb.setCity(StrUtils.replaceString(csvReader.get("市町村")));
        fsb.setState(StrUtils.replaceString(csvReader.get("都道府県")));
        fsb.setPostal(StrUtils.replaceString(csvReader.get("郵便番号")));
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
    public FinancialSalesBalance franceDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getFranceTime(csvReader.get("date/heure"), Constants.FRANCE_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("numéro de versement")));
        String type = StrUtils.replaceString(csvReader.get("type"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.replaceString(csvReader.get("numéro de la commande")));
        String skuName = StrUtils.replaceString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.replaceString(csvReader.get("description")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("quantité")));
        fsb.setMarketplace(StrUtils.replaceString(csvReader.get("Marketplace")));
        fsb.setFulfillment(StrUtils.replaceString(csvReader.get("traitement")));
        fsb.setCity(StrUtils.replaceString(csvReader.get("ville d'où provient la commande")));
        fsb.setState(StrUtils.replaceString(csvReader.get("Région d'où provient la commande")));
        fsb.setPostal(StrUtils.replaceString(csvReader.get("code postal de la commande")));
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
    public FinancialSalesBalance spainDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("fecha y hora"), Constants.SPAIN_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("identificador de pago")));
        String type = StrUtils.replaceString(csvReader.get("tipo"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.replaceString(csvReader.get("número de pedido")));
        String skuName = StrUtils.replaceString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.replaceString(csvReader.get("descripción")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("cantidad")));
        fsb.setMarketplace(StrUtils.replaceString(csvReader.get("web de Amazon")));
        fsb.setFulfillment(StrUtils.replaceString(csvReader.get("gestión logística")));
        fsb.setCity(StrUtils.replaceString(csvReader.get("ciudad de procedencia del pedido")));
        fsb.setState(StrUtils.replaceString(csvReader.get("comunidad autónoma de procedencia del pedido")));
        fsb.setPostal(StrUtils.replaceString(csvReader.get("código postal de procedencia del pedido")));
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
    public FinancialSalesBalance italyDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getItalyTime(csvReader.get("Data/Ora:"), Constants.ITALY_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("Numero pagamento")));
        String type = StrUtils.replaceString(csvReader.get("Tipo"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.replaceString(csvReader.get("Numero ordine")));
        String skuName = StrUtils.replaceString(csvReader.get("SKU"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.replaceString(csvReader.get("Descrizione")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("Quantità")));
        fsb.setMarketplace(StrUtils.replaceString(csvReader.get("Marketplace")));
        fsb.setFulfillment(StrUtils.replaceString(csvReader.get("Gestione")));
        fsb.setCity(StrUtils.replaceString(csvReader.get("Città di provenienza dell'ordine")));
        fsb.setState(StrUtils.replaceString(csvReader.get("Provincia di provenienza dell'ordine")));
        fsb.setPostal(StrUtils.replaceString(csvReader.get("CAP dell'ordine")));
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
    public FinancialSalesBalance unitedKingdomDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.UNITED_KINGDOM_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("settlement id")));
        String type = StrUtils.replaceString(csvReader.get("type"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.replaceString(csvReader.get("order id")));
        String skuName = StrUtils.replaceString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.replaceString(csvReader.get("description")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("quantity")));
        fsb.setMarketplace(StrUtils.replaceString(csvReader.get("marketplace")));
        fsb.setFulfillment(StrUtils.replaceString(csvReader.get("fulfillment")));
        fsb.setCity(StrUtils.replaceString(csvReader.get("order city")));
        fsb.setState(StrUtils.replaceString(csvReader.get("order state")));
        fsb.setPostal(StrUtils.replaceString(csvReader.get("order postal")));
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
    public FinancialSalesBalance australiaDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.AUSTRALIA_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("settlement id")));
        String type = StrUtils.replaceString(csvReader.get("type"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.replaceString(csvReader.get("order id")));
        String skuName = StrUtils.replaceString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.replaceString(csvReader.get("description")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("quantity")));
        fsb.setMarketplace(StrUtils.replaceString(csvReader.get("marketplace")));
        fsb.setFulfillment(StrUtils.replaceString(csvReader.get("fulfillment")));
        fsb.setCity(StrUtils.replaceString(csvReader.get("order city")));
        fsb.setState(StrUtils.replaceString(csvReader.get("order state")));
        fsb.setPostal(StrUtils.replaceString(csvReader.get("order postal")));
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
    public FinancialSalesBalance canadaDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.CANADA_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("settlement id")));
        String type = StrUtils.replaceString(csvReader.get("type"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.replaceString(csvReader.get("order id")));
        String skuName = StrUtils.replaceString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.replaceString(csvReader.get("description")));
        fsb.setMarketplace(StrUtils.replaceString(csvReader.get("marketplace")));
        fsb.setFulfillment(StrUtils.replaceString(csvReader.get("fulfillment")));
        fsb.setCity(StrUtils.replaceString(csvReader.get("order city")));
        fsb.setState(StrUtils.replaceString(csvReader.get("order state")));
        fsb.setPostal(StrUtils.replaceString(csvReader.get("order postal")));
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
    public FinancialSalesBalance usaDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.USA_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("settlement id")));
        String type = StrUtils.replaceString(csvReader.get("type"));
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.replaceString(csvReader.get("order id")));
        String skuName = StrUtils.replaceString(csvReader.get("sku"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.replaceString(csvReader.get("description")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("quantity")));
        fsb.setMarketplace(StrUtils.replaceString(csvReader.get("marketplace")));
        fsb.setFulfillment(StrUtils.replaceString(csvReader.get("fulfillment")));
        fsb.setCity(StrUtils.replaceString(csvReader.get("order city")));
        fsb.setState(StrUtils.replaceString(csvReader.get("order state")));
        fsb.setPostal(StrUtils.replaceString(csvReader.get("order postal")));
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
    public FinancialSalesBalance germanDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("Datum/Uhrzeit"), Constants.GERMAN_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("Abrechnungsnummer")));
        String type = StrUtils.replaceString(csvReader.get("Typ"));
        //Type类型转换
        if (StringUtils.isEmpty(type)) {
            fsb.setType(type);
        } else if (!setType(type, seId, csvReader, fsb)) {
            return null;
        }
        fsb.setOrderId(StrUtils.replaceString(csvReader.get("Bestellnummer")));
        String skuName = StrUtils.replaceString(csvReader.get("SKU"));
        fsb.setSku(skuName);
        fsb.setDescription(StrUtils.replaceString(csvReader.get("Beschreibung")));
        fsb.setoQuantity(StrUtils.replaceLong(csvReader.get("Menge")));
        fsb.setMarketplace(StrUtils.replaceString(csvReader.get("Marketplace")));
        fsb.setFulfillment(StrUtils.replaceString(csvReader.get("Versand")));
        fsb.setState(StrUtils.replaceString(csvReader.get("Bundesland")));
        fsb.setPostal(StrUtils.replaceString(csvReader.get("Postleitzahl")));
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
                //count -- NoSku ++sumNoSku
                count--;
                sumNoSku++;
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
     * xsl 获取没有SKU的文件List
     *
     * @param skuId
     * @return
     */
    public SalesAmazonAdCpr cprSkuList(Long skuId, SalesAmazonAdCpr cpr, Row row, int totalNumber, List<String> head) {
        if (StringUtils.isNotEmpty(cpr.getAdvertisedSku())) {
            if (skuId == null) {
                if (skuNoIdList.size() == 0) {
                    skuNoIdList.add(head);
                }
                //count -- NoSku ++sumNoSku
                count--;
                sumNoSku++;
                List<String> skuListNo = new ArrayList<>();
                //拿到那一行信息
                for (int i = 0; i < totalNumber; i++) {
                    skuListNo.add(XlsUtils.getCellValue(row.getCell(i)));
                }
                skuNoIdList.add(skuListNo);
                return null;
            }
        }
        cpr.setSkuId(skuId);
        return cpr;
    }

    /**
     * 财务设置通用对象
     */
    public FinancialSalesBalance setFsb(Long sId, Long seId, Long uid, Long pId, Long recordingId) {
        return new FinancialSalesBalance(sId, seId, pId, new Date().getTime(), uid, recordingId);
    }

    /**
     * Cpr设置通用对象
     */
    public SalesAmazonAdCpr setCpr(Long sId, Long seId, Long uid, Long recordingId) {
        return new SalesAmazonAdCpr(sId, seId, new Date().getTime(), uid, recordingId);
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
        String typeName = bAmazonTypeMapper.getTypeName(seId, type);
        //如果数据库查询出来为空
        if (StringUtils.isEmpty(typeName)) {
            //count --
            count--;
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

    public List<String> getHeadInfo(Long seId, int id) {
        return salesAmazonCsvTxtXslHeaderService.headerList(seId, id);
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
        count = 1;
        String filePath = saveFilePath + fileName;
        FileInputStream in;
        try {
            in = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            return BaseApiService.setResultError("FileInputStream 异常~");
        }
        File file = new File(filePath);
        Workbook wb;
        //判断文件类型
        wb = fileType(in, file);
        if (wb == null) {
            return BaseApiService.setResultError("不是excel文件~");
        }
        Sheet sheet = wb.getSheetAt(0);
        int line = 1;
        int totalNumber = sheet.getRow(0).getPhysicalNumberOfCells(); //获取总列数
        //拿到数据库的表头 进行校验
        List<String> head = getHeadInfo(siteId, 2);
        //对比表头
        if (contrastHeadMethod(totalNumber, sheet, head) != null) {
            return contrastHeadMethod(totalNumber, sheet, head);
        }
        int lastRowNum = sheet.getLastRowNum(); // 获取总行数
        try {
            switch (tbId) {
                //Cpr
                case 2:
                    return readTableCpr(line, lastRowNum, shopId, siteId, uid, recordingId, totalNumber, sheet, tbId, head);
                // STR
                    case 3:
            }
        } catch (Exception e) {
            return BaseApiService.setResultError("第" + count + "行信息错误,数据存入失败~");
        } finally {
            count = 1;
            TryUtils.ioClose(null, null, wb, in);
        }
        return null;
    }

    /**
     * 通用读取xls 信息
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
                                     int totalNumber, Sheet sheet, int tbId, List<String> head) {
        List<SalesAmazonAdCpr> cprList = null;
        SalesAmazonAdCpr saCpr;
        Row row = null;
        Cell cell;
        // 开始时间
        Long begin = new Date().getTime();
        //写锁
        writeLock.lock();
        skuNoIdList = new ArrayList<>();
        if (tbId == 2) {
            cprList = new ArrayList<>();
        }
        for (int i = line; i <= lastRowNum; i++) {
            count++;
            saCpr = setCpr(shopId, siteId, uid, recordingId);
            for (int j = 0; j < totalNumber; j++) {
                row = sheet.getRow(i);
                cell = row.getCell(j);
                saCpr = setCprPojo(j, saCpr, cell);
            }
            Long skuId = skuService.selSkuId(shopId, siteId, saCpr.getAdvertisedSku());
            //设置没有SKU的信息导入
            if (cprSkuList(skuId, saCpr, row, totalNumber, head) != null) {
                cprList.add(cprSkuList(skuId, saCpr, row, totalNumber, head));
            }
        }
        if (cprList.size() > 0) {
            int countCpr = cprService.AddSalesAmazonAdCprList(cprList);
            if (countCpr > 0) {
                // 结束时间
                Long end = new Date().getTime();
                return BaseApiService.setResultSuccess(count - 1 + "条数据插入成功~花费时间 : " + (end - begin) / 1000 + " s");
            }
        }
        return null;
    }

    /**
     * set pojo cpr
     */
    public SalesAmazonAdCpr setCprPojo(int j, SalesAmazonAdCpr saCpr, Cell cell) {
        switch (j) {
            case 0:
                saCpr.setDate(StrUtils.replaceLong(XlsUtils.getCellValue(cell).trim()));
                break;
            case 2:
                saCpr.setCampaignName(StrUtils.repString(XlsUtils.getCellValue(cell)));
                break;
            case 3:
                saCpr.setAdGroupName(StrUtils.repString(XlsUtils.getCellValue(cell)));
                break;
            case 4:
                saCpr.setAdvertisedSku(StrUtils.repString(XlsUtils.getCellValue(cell)));
                break;
            case 5:
                saCpr.setAdvertisedAsin(StrUtils.repString(XlsUtils.getCellValue(cell)));
                break;
            case 6:
                saCpr.setImpressions(StrUtils.repDouble(XlsUtils.getCellValue(cell).trim()));
                break;
            case 7:
                saCpr.setClicks(StrUtils.repDouble(XlsUtils.getCellValue(cell).trim()));
                break;
            case 10:
                saCpr.setTotalSpend(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
                break;
            case 11:
                saCpr.setSales(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
                break;
            case 13:
                saCpr.setRoas(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
                break;
            case 14:
                saCpr.setOrdersPlaced(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
                break;
            case 15:
                saCpr.setTotalUnits(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
                break;
            case 17:
                saCpr.setSameskuUnitsOrdered(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
                break;
            case 18:
                saCpr.setOtherskuUnitsOrdered(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
                break;
            case 19:
                saCpr.setSameskuUnitsSales(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
                break;
            case 20:
                saCpr.setOtherskuUnitsSales(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
                break;
        }
        return saCpr;
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
            String msg = "xlsx/xls文件表头信息不一致/请检查~";
            return BaseApiService.setResultError(msg);
        }
        return null;
    }
}
