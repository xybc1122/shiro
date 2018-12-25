package com.dt.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.mapper.BasePublicMapper.BasicPublicAmazonTypeMapper;
import com.dt.user.model.FinancialSalesBalance;
import com.dt.user.model.UserInfo;
import com.dt.user.model.UserUpload;
import com.dt.user.service.BasePublicService.BasicSalesAmazonCsvTxtXslHeaderService;
import com.dt.user.service.BasePublicService.BasicSalesAmazonSkuService;
import com.dt.user.service.FinancialSalesBalanceService;
import com.dt.user.service.UserUploadService;
import com.dt.user.toos.Constants;
import com.dt.user.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/upload")
public class FinancialSalesBalanceController {

    @Autowired
    private FinancialSalesBalanceService financialSalesBalanceService;

    @Autowired
    private BasicSalesAmazonCsvTxtXslHeaderService salesAmazonCsvTxtXslHeaderService;

    @Autowired
    private BasicSalesAmazonSkuService skuService;

    @Autowired
    private UserUploadService userUploadService;

    @Autowired
    private BasicPublicAmazonTypeMapper bAmazonTypeMapper;
    //获取没有SKU的List集合
    private volatile List<List<String>> skuNoIdList;
    //行数 /报错行数
    private volatile int count;
    //没有sku有几行存入
    private volatile int sumNoSku;
    //线程锁
    private Lock lock = new ReentrantLock();

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
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/file")
    public ResponseBase saveFileInfo(@RequestParam("file") MultipartFile file, HttpServletRequest request, @RequestParam("sId") String sId, @RequestParam("seId") String seId, @RequestParam("payId") String payId) {
        String token = GetCookie.getToken(request);
        UserInfo user = JwtUtils.jwtUser(token);
        if (user == null) {
            return BaseApiService.setResultError("用户无效~");
        }
        //指定文件存放路径
        String saveFilePath = Constants.SAVE_FILE_PATH;
        //String contentType = file.getContentType();//图片||文件类型
        String fileName = file.getOriginalFilename();//图片||文件名字
        try {
            FileUtils.uploadFile(file.getBytes(), saveFilePath, fileName);
        } catch (Exception e) {
            return BaseApiService.setResultError("上传失败" + e.getMessage());
        }
        //店铺ID
        Integer shopId = Integer.parseInt(sId);
        //站点ID
        Integer siteId = Integer.parseInt(seId);
        // pId
        Integer pId = Integer.parseInt(payId);
        //记录用户上传信息~
        UserUpload upload = uploadOperating(new UserUpload(), siteId, shopId, fileName, saveFilePath, user, pId, 0, "上传成功");
        return BaseApiService.setResultSuccess("上传成功~", upload);
    }

    @PostMapping("/addInfo")
    @Transactional
    public synchronized ResponseBase redFileInfo(@RequestBody UserUpload userUpload) {
        count = 1;
        sumNoSku = 0;
        int fileIndex = userUpload.getName().lastIndexOf(".");
        String typeFile = userUpload.getName().substring(fileIndex + 1);

        switch (typeFile) {
            case "csv":
                return shopSelection(userUpload.getFilePath(), userUpload.getName(), userUpload.getSiteId(), userUpload.getShopId(), userUpload.getUid(), userUpload.getpId(), userUpload.getId());
        }
        return null;
    }

    /**
     * 封装店铺选择
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
     * 通用读取信息
     *
     * @param filePath 文件绝对路径
     * @param row      读取到真正的头行数
     * @param sId      店铺ID
     * @param seId     站点ID
     * @param head     表头信息
     * @return
     */
    public ResponseBase switchCountry(String filePath, int row, Long sId, Long seId, Long uid, String fileName, List<String> head, Integer pId, Long id) {
        ResponseBase responseCsv = saveCsv(filePath, row, sId, seId, uid, head, pId.longValue());
        if (responseCsv.getCode() == 200) {
            if (skuNoIdList.size() != 0) {
                //文件写入到服务器的地址
                String skuNoPath = Constants.WRITE_SAVE_FILE_PATH;
                //写入CSV文件到本地
                CSVUtil.write(head, skuNoIdList, skuNoPath, fileName);
                //上传成功 有些skuId 记录上传信息~
                String msg = responseCsv.getMsg() + "----有" + sumNoSku + "个没有sku文件";
                recordInfo(2, msg, id);
                return BaseApiService.setResultSuccess(msg, false);
            }
            //上传成功 都有skuId~
            return BaseApiService.setResultSuccess(responseCsv.getMsg());
        } else {
            //存入信息报错
            recordInfo(1, responseCsv.getMsg(), id);
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
     * 数据解析
     *
     * @param filePath
     * @param row
     * @param sId
     * @param seId
     * @param uid
     * @return
     */
    public ResponseBase saveCsv(String filePath, int row, Long sId, Long seId, Long uid, List<String> head, Long pId) {
        // 开始时间
        Long begin = new Date().getTime();
        boolean isFlg;
        InputStreamReader isr = null;
        // 创建CSV读对象
        CsvReader csvReader = null;
        int index = 0;
        FinancialSalesBalance fb;
        FinancialSalesBalance fsb = new FinancialSalesBalance();
        try {
            //设置编码格式 ,日文解码shift_jis
            String coding = seId == 9 ? "shift_jis" : "GBK";
            isr = new InputStreamReader(new FileInputStream(new File(filePath)), coding);
            csvReader = new CsvReader(isr);
            List<FinancialSalesBalance> fsbList = new ArrayList<>();
            skuNoIdList = new ArrayList<>();
            List<String> headList = new ArrayList<>();
            //如果表里没有别的数据 第一行就是头
            fsb.setCreateDate(new Date().getTime());
            fsb.setSiteId(seId);
            fsb.setShopId(sId);
            fsb.setCreateIdUser(uid);
            if (row == 0) {
                csvReader.readHeaders();
                //比较头部
                isFlg = compareHead(headList, head, seId);
                if (!isFlg) {
                    return BaseApiService.setResultError("CSV文件表头信息不一致/请检查~");
                }
            }
            while (csvReader.readRecord()) {
                //count ++
                count++;
                //如果是多行的
                if (index == (row - 1)) {
                    csvReader.readHeaders();
                    //比较头部
                    isFlg = compareHead(headList, head, seId);
                    if (!isFlg) {
                        return BaseApiService.setResultError("CSV文件表头信息不一致/请检查~");
                    }
                }
                //如果正确 通过站点ID 判断 存入 哪个站点数据
                //美国站
                if (index >= row && seId == 1L) {
                    fb = usaDepositObject(setFsb(sId, seId, uid, pId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //加拿大站
                else if (index >= row && seId == 2L) {
                    fb = canadaDepositObject(setFsb(sId, seId, uid, pId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }   //澳大利亚站
                else if (index >= row && seId == 3L) {
                    fb = australiaDepositObject(setFsb(sId, seId, uid, pId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //英国站
                else if (index >= row && seId == 4L) {
                    fb = unitedKingdomDepositObject(setFsb(sId, seId, uid, pId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //德国站
                else if (index >= row && seId == 5L) {
                    fb = germanDepositObject(setFsb(sId, seId, uid, pId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }
                //法国
                else if (index >= row && seId == 6L) {
                    fb = franceDepositObject(setFsb(sId, seId, uid, pId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                } //意大利
                else if (index >= row && seId == 7L) {
                    fb = italyDepositObject(setFsb(sId, seId, uid, pId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                }  //西班牙
                else if (index >= row && seId == 8L) {
                    fb = spainDepositObject(setFsb(sId, seId, uid, pId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                } //日本
                else if (index >= row && seId == 9L) {
                    fb = japanDepositObject(setFsb(sId, seId, uid, pId), csvReader, sId, seId, uid);
                    if (fb != null) {
                        fsbList.add(fb);
                    }
                } //墨西哥
                else if (index >= row && seId == 10L) {
                    fb = mexicoDepositObject(setFsb(sId, seId, uid, pId), csvReader, sId, seId, uid);
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
                    return BaseApiService.setResultSuccess((count - row - 1) + "条数据插入成功~花费时间 : " + (end - begin) / 1000 + " s");
                }
            }
            return BaseApiService.setResultError("表里的skuID全部不一致 请修改~");
        } catch (Exception e) {
            return BaseApiService.setResultError("第" + count + "行信息错误,数据存入失败~");
        } finally {
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
     * 墨西哥存入对象
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
     * 日本存入对象
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
     * 法国存入对象
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
     * 西班牙存入对象
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
     * 意大利存入对象
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
     * 英国存入对象
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
     * 澳洲存入对象
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
     * 加拿大存入对象
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
     * 美国存入对象
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
     * 德国存入对象
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
     * 获取没有SKU的文件List
     *
     * @param skuId
     * @param csvReader
     * @param fsb
     * @return
     */
    public FinancialSalesBalance skuList(Long skuId, CsvReader csvReader, FinancialSalesBalance fsb) throws IOException {
        if (StringUtils.isNotEmpty(fsb.getSku())) {
            if (skuId == null) {
                //count -- NoSku ++
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
     * 设置通用对象
     */
    public FinancialSalesBalance setFsb(Long sId, Long seId, Long uid, Long pId) {
        return new FinancialSalesBalance(sId, seId, pId, new Date().getTime(), uid);
    }

    /**
     * 设置TypeName
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
     * 获取没有typeName的文件List
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
     * 对比表头返回
     *
     * @param headList
     * @param seId
     * @return
     */
    public boolean compareHead(List<String> headList, List<String> oldHeadList, Long seId) {
        //拿到表头信息 对比数据库的表头 如果不一致 抛出报错信息 不执行下去
        for (int i = 0; i < oldHeadList.size(); i++) {
            String head = oldHeadList.get(i).replace("\"", "");
            headList.add(head);
            System.out.println(head);
        }
        //拿到数据库里的表头信息
        List<String> fBalanceHead = salesAmazonCsvTxtXslHeaderService.headerList(seId);
        //如果不一致返回false
        return ArrUtils.equalList(headList, fBalanceHead);
    }
    //###############################封装exe
}
