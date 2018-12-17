package com.dt.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.FinancialSalesBalance;
import com.dt.user.model.UserInfo;
import com.dt.user.service.BasePublicService.BasicSalesAmazonCsvTxtXslHeaderService;
import com.dt.user.service.BasePublicService.BasicSalesAmazonSkuService;
import com.dt.user.service.FinancialSalesBalanceService;
import com.dt.user.toos.Constants;
import com.dt.user.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/upload")
public class FinancialSalesBalanceController {

    @Autowired
    private FinancialSalesBalanceService financialSalesBalanceService;

    @Autowired
    private BasicSalesAmazonCsvTxtXslHeaderService salesAmazonCsvTxtXslHeaderService;

    @Autowired
    private BasicSalesAmazonSkuService skuService;
    //获取没有SKU的List集合
    private List<List<String>> skuNoIdList;
    //报错行数
    private int errCount;

    /**
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional
    @PostMapping("/file")
    public ResponseBase saveFileInfo(@RequestParam("file") MultipartFile file, HttpServletRequest request, @RequestParam("sId") String sId, @RequestParam("seId") String seId) {
        //指定文件存放路径
        String saveFilePath = Constants.SAVEFILEPATH;
        errCount = 0;
        Integer shopId = Integer.parseInt(sId);
        Integer siteId = Integer.parseInt(seId);
        String token = GetCookie.getToken(request);
        UserInfo user = JwtUtils.jwtUser(token);
        if (user == null) {
            return BaseApiService.setResultError("token无效~~");
        }
        //String contentType = file.getContentType();//图片||文件类型
        String fileName = file.getOriginalFilename();//图片||文件名字
        //电兔
        int fileShopNameDt = fileName.indexOf("电兔");
        //宏名
        int fileShopNameHm = fileName.indexOf("宏名");
        //诚夕
        int fileShopNameCx = fileName.indexOf("诚夕");
        if (fileShopNameDt == -1 && shopId == 1) {
            return BaseApiService.setResultError("不是电兔的文件/请注意操作~");
        } else if (fileShopNameHm == -1 && shopId == 2) {
            return BaseApiService.setResultError("不是宏名的文件/请注意操作~");
        } else if (fileShopNameCx == -1 && shopId == 3) {
            return BaseApiService.setResultError("不是诚夕的文件/请注意操作~");
        } else {
            return shopSelection(file, saveFilePath, fileName, siteId, shopId, user);
        }
    }

    /**
     * 封装店铺选择
     *
     * @param file
     * @param saveFilePath
     * @param fileName
     * @param siteId
     * @param shopId
     * @param user
     * @return
     */
    public ResponseBase shopSelection(MultipartFile file, String saveFilePath, String fileName, Integer siteId, Integer shopId, UserInfo user) {
        try {
            FileUtils.uploadFile(file.getBytes(), saveFilePath, fileName);
        } catch (Exception e) {
            return BaseApiService.setResultError("上传失败" + e.getMessage());
        }
        String filePath = saveFilePath + fileName;
        //获得头信息长度
        String csvJson = CSVUtil.startReadLine(filePath, siteId);
        JSONObject rowJson = JSONObject.parseObject(csvJson);
        int row = (Integer) rowJson.get("index");
        if (row == -1) {
            return BaseApiService.setResultError("存入数据失败,请检查表头第一行是否正确/请检查上传的站点~");
        }
        List<String> oldHeadList = JSONObject.parseArray(rowJson.get("head").toString(), String.class);
        int fileIndex = filePath.indexOf(".");
        String typeFile = filePath.substring(fileIndex + 1);
        switch (siteId) {
            //1 美国操作
            case 1:
                return switchCountry(typeFile, filePath, row, shopId, siteId, user, fileName, oldHeadList);
            case 2:
                return switchCountry(typeFile, filePath, row, shopId, siteId, user, fileName, oldHeadList);
            case 3:
            case 4:
            case 5:
                //5 德国操作
                return switchCountry(typeFile, filePath, row, shopId, siteId, user, fileName, oldHeadList);
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                break;
        }
        return null;
    }

    /**
     * 通用读取信息
     *
     * @param typeFile File文件的.后缀
     * @param filePath 文件绝对路径
     * @param row      读取到真正的头行数
     * @param sId      店铺ID
     * @param seId     站点ID
     * @param user     角色ID
     * @param head     表头信息
     * @return
     */
    public ResponseBase switchCountry(String typeFile, String filePath, int row, Integer sId, Integer seId, UserInfo user, String fileName, List<String> head) {
        switch (typeFile) {
            case "csv":
                ResponseBase responseCsv = saveCSV(filePath, row, sId.longValue(), seId.longValue(), user.getUid(), head);
                if (responseCsv.getCode() == 200) {
                    if (skuNoIdList.size() != 0) {
                        String skuNoPath = "D:/skuNo/";
                        //写入CSV文件到本地
                        CSVUtil.write(head, skuNoIdList, skuNoPath, fileName);
                        return BaseApiService.setResultSuccess("数据存入成功~", false);
                    }
                    return BaseApiService.setResultSuccess("数据存入成功~", true);
                } else {
                    FileUtils.deleteFile(filePath);
                    return responseCsv;
                }
            case "txt":
                break;

            case "xls":
                break;
            case "xlsx":
                break;
        }
        return null;
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
    public ResponseBase saveCSV(String filePath, int row, Long sId, Long seId, Long uid, List<String> head) {
        // 开始时间
        Long begin = new Date().getTime();
        boolean isFlg;
        InputStreamReader isr = null;
        // 创建CSV读对象
        CsvReader csvReader = null;
        int index = 0;
        FinancialSalesBalance fb = null;
        try {
            isr = new InputStreamReader(new FileInputStream(new File(filePath)), "GBK");
            csvReader = new CsvReader(isr);
            List<FinancialSalesBalance> financialSalesBalanceList = new ArrayList<>();
            skuNoIdList = new ArrayList<>();
            List<String> headList = new ArrayList<>();
            //如果表里没有别的数据 第一行就是头
            if (row == 0) {
                csvReader.readHeaders();
                //比较头部
                isFlg = compareHead(headList, head, seId);
                if (!isFlg) {
                    return BaseApiService.setResultError("CSV文件表头信息不一致/请检查~");
                }
            }
            while (csvReader.readRecord()) {
                errCount++;
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
                    fb = usaDepositObject(new FinancialSalesBalance(), csvReader, sId, seId, uid);
                    if (fb != null) {
                        financialSalesBalanceList.add(fb);
                    }
                }
                //加拿大站
                else if (index >= row && seId == 2L) {
                    fb = canadaDepositObject(new FinancialSalesBalance(), csvReader, sId, seId, uid);
                    if (fb != null) {
                        financialSalesBalanceList.add(fb);
                    }
                }
                //德国站
                else if (index >= row && seId == 5L) {
                    fb = germanDepositObject(new FinancialSalesBalance(), csvReader, sId, seId, uid);
                    if (fb != null) {
                        financialSalesBalanceList.add(fb);
                    }
                }
                index++;
            }
            if (financialSalesBalanceList.size() > 0) {
                int count = financialSalesBalanceService.addInfoGerman(financialSalesBalanceList.size(), fb);
                if (count != 0) {
                    // 结束时间
                    Long end = new Date().getTime();
                    // 耗时
                    System.out.println(errCount+"条数据插入花费时间 : " + (end - begin) / 1000 + " s");
                    System.out.println("插入完成");
                    return BaseApiService.setResultSuccess("添加数据成功~");
                }
            }
            return BaseApiService.setResultError("表里的skuID全部不一致 请修改~");
        } catch (Exception e) {
            return BaseApiService.setResultError("第" + errCount + "行信息错误,数据存入失败~" + e.getMessage());
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
     * 加拿大存入对象
     *
     * @param fsb
     * @param csvReader
     * @param sId
     * @param seId
     * @param uid
     * @return
     * @throws IOException
     */
    public FinancialSalesBalance canadaDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.CANADA_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("settlement id")));
        fsb.setType(StrUtils.replaceString(csvReader.get("type")));
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
        fsb.setCreateDate(new Date().getTime());
        fsb.setSiteId(seId);
        fsb.setShopId(sId);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        //UserId
        fsb.setCreateIdUser(uid);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * 美国存入对象
     */
    public FinancialSalesBalance usaDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.USA_TIME));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("settlement id")));
        fsb.setType(StrUtils.replaceString(csvReader.get("type")));
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
        fsb.setCreateDate(new Date().getTime());
        fsb.setSiteId(seId);
        fsb.setShopId(sId);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        //UserId
        fsb.setCreateIdUser(uid);
        return skuList(skuId, csvReader, fsb);
    }

    /**
     * 德国存入对象
     */
    public FinancialSalesBalance germanDepositObject(FinancialSalesBalance fsb, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        fsb.setDate(DateUtils.getGermanTime(csvReader.get("Datum/Uhrzeit")));
        fsb.setSettlemenId(StrUtils.replaceString(csvReader.get("Abrechnungsnummer")));
        fsb.setType(StrUtils.replaceString(csvReader.get("Typ")));
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
        fsb.setCreateDate(new Date().getTime());
        fsb.setSiteId(seId);
        fsb.setShopId(sId);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        //UserId
        fsb.setCreateIdUser(uid);
        return skuList(skuId, csvReader, fsb);
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
        if (skuId == null) {
            List<String> skuListNo = new ArrayList();
            for (int i = 0; i < csvReader.getColumnCount(); i++) {
                skuListNo.add(csvReader.get(i).replace(",", "."));
            }
            skuNoIdList.add(skuListNo);
            return null;
        }
        fsb.setSkuId(skuId);
        return fsb;
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
            headList.add(oldHeadList.get(i).replace("\"", ""));
            //System.out.println(head[i].replace("\"", ""));
        }
        //拿到数据库里的表头信息
        List<String> fBalanceHead = salesAmazonCsvTxtXslHeaderService.headerList(seId);
        //如果不一致返回false
        return ArrUtils.equalList(headList, fBalanceHead);
    }
    /**
     * 设置readHeaders
     */

}
