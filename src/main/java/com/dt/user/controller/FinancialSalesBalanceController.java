package com.dt.user.controller;

import com.alibaba.fastjson.JSONArray;
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
    private List<List<String>> noSkuIdList;
    //获取CSV数据库表头信息
    private List<String> headList;

    /**
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional
    @PostMapping("/file")
    public ResponseBase saveFileInfo(@RequestParam("file") MultipartFile file, HttpServletRequest request, @RequestParam("sId") String sId, @RequestParam("seId") String seId) throws Exception {
        Integer shopId = Integer.parseInt(sId);
        Integer siteId = Integer.parseInt(seId);
        String token = GetCookie.getToken(request);
        UserInfo user = JwtUtils.jwtUser(token);
        if (user == null) {
            return BaseApiService.setResultError("token无效~~");
        }
        //String contentType = file.getContentType();//图片||文件类型
        String fileName = file.getOriginalFilename();//图片||文件名字
        //指定文件存放路径
        String saveFilePath = "D:/csv/";
        try {
            FileUtils.uploadFile(file.getBytes(), saveFilePath, fileName);
        } catch (Exception e) {
            return BaseApiService.setResultError("上传失败" + e.getMessage());
        }
        String filePath = saveFilePath + fileName;
        //获得头信息长度
        int row = CSVUtil.startReadLine(filePath);
        if (row == 0) {
            throw new Exception("存入数据失败,请检查表头第一行是否正确");
        }
        int fileIndex = filePath.indexOf(".");
        String typeFile = filePath.substring(fileIndex + 1);
        switch (seId) {
            //1 美国操作
            case "1":
                return switchCountry(typeFile, filePath, row, shopId, siteId, user, fileName);
            case "2":
            case "3":
            case "4":
                return switchCountry(typeFile, filePath, row, shopId, siteId, user, fileName);
            case "5":
                //5 德国操作
                return switchCountry(typeFile, filePath, row, shopId, siteId, user, fileName);
            case "6":
            case "7":
            case "8":
            case "9":
            case "10":
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
     * @return
     */
    public ResponseBase switchCountry(String typeFile, String filePath, int row, Integer sId, Integer seId, UserInfo user, String fileName) {
        switch (typeFile) {
            case "csv":
                boolean isCsv = saveCSV(filePath, row, sId.longValue(), seId.longValue(), user.getUid());
                if (isCsv) {
                    if (noSkuIdList.size() != 0) {
                        String skuNoPath = "D:/skuNo/";
                        //写入CSV文件到本地
                        CSVUtil.write(headList, noSkuIdList, skuNoPath, fileName);
                        return BaseApiService.setResultSuccess("数据存入成功~", false);
                    }
                    return BaseApiService.setResultSuccess("数据存入成功~", true);
                }
                FileUtils.deleteFile(filePath);
                return BaseApiService.setResultError("数据存入失败--请检查字段~");
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
    public boolean saveCSV(String filePath, int row, Long sId, Long seId, Long uid) {
        boolean isFlg;
        InputStreamReader isr = null;
        // 创建CSV读对象
        CsvReader csvReader = null;
        int index = 0;
        try {
            isr = new InputStreamReader(new FileInputStream(new File(filePath)), "GBK");
            csvReader = new CsvReader(isr);
            List<FinancialSalesBalance> financialSalesBalanceList = new ArrayList<>();
            noSkuIdList = new ArrayList<>();
            headList = new ArrayList<>();
            FinancialSalesBalance fb;
            while (csvReader.readRecord()) {
                //如果正确设置表头
                if (index == (row - 2)) {
                    //设置表头
                    csvReader.readHeaders();
                    //拿到表头信息 对比数据库的表头 如果不一致 抛出报错信息 不执行下去
                    String[] head = csvReader.getRawRecord().split(",");
                    for (int i = 0; i < head.length; i++) {
                        headList.add(head[i].replace("\"", ""));
                        //System.out.println(head[i].replace("\"", ""));
                    }
                    //拿到数据库里的表头信息
                    List<String> fBalanceHead = salesAmazonCsvTxtXslHeaderService.headerList(seId);
                    //如果不一致返回false
                    isFlg = ArrUtils.equalList(headList, fBalanceHead);
                    if (!isFlg) {
                        return false;
                    }
                }
                //如果正确 通过站点ID 判断 存入 哪个站点数据
                //美国站
                if (index >= row - 1 && seId == 1L) {
                    fb = usaDepositObject(new FinancialSalesBalance(), csvReader, sId, seId, uid);
                    if (fb != null) {
                        financialSalesBalanceList.add(fb);
                    }
                }
                //加拿大站
                else if (index >= row - 1 && seId == 2L) {
                    fb = canadaDepositObject(new FinancialSalesBalance(), csvReader, sId, seId, uid);
                    if (fb != null) {
                        financialSalesBalanceList.add(fb);
                    }
                }
                //德国站
                else if (index >= row - 1 && seId == 5L) {
                    fb = germanDepositObject(new FinancialSalesBalance(), csvReader, sId, seId, uid);
                    if (fb != null) {
                        financialSalesBalanceList.add(fb);
                    }
                }
                index++;
            }
            int count = financialSalesBalanceService.addInfoGerman(financialSalesBalanceList);
            if (count != 0) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        return false;
    }

    /**
     * 加拿大存入对象
     *
     * @param financialSalesBalance
     * @param csvReader
     * @param sId
     * @param seId
     * @param uid
     * @return
     * @throws IOException
     */
    public FinancialSalesBalance canadaDepositObject(FinancialSalesBalance financialSalesBalance, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        financialSalesBalance.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.CANADA_TIME));
        financialSalesBalance.setSettlemenId(StrUtils.replaceString(csvReader.get("settlement id")));
        financialSalesBalance.setType(StrUtils.replaceString(csvReader.get("type")));
        financialSalesBalance.setOrderId(StrUtils.replaceString(csvReader.get("order id")));
        String skuName = StrUtils.replaceString(csvReader.get("sku"));
        financialSalesBalance.setSku(skuName);
        financialSalesBalance.setDescription(StrUtils.replaceString(csvReader.get("description")));
        financialSalesBalance.setMarketplace(StrUtils.replaceString(csvReader.get("marketplace")));
        financialSalesBalance.setFulfillment(StrUtils.replaceString(csvReader.get("fulfillment")));
        financialSalesBalance.setCity(StrUtils.replaceString(csvReader.get("order city")));
        financialSalesBalance.setState(StrUtils.replaceString(csvReader.get("order state")));
        financialSalesBalance.setPostal(StrUtils.replaceString(csvReader.get("order postal")));
        financialSalesBalance.setSales(StrUtils.replaceDouble(csvReader.get("product sales")));
        financialSalesBalance.setShippingCredits(StrUtils.replaceDouble(csvReader.get("shipping credits")));
        financialSalesBalance.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("promotional rebates")));
        financialSalesBalance.setSellingFees(StrUtils.replaceDouble(csvReader.get("selling fees")));
        financialSalesBalance.setFbaFee(StrUtils.replaceDouble(csvReader.get("fba fees")));
        financialSalesBalance.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("other transaction fees")));
        financialSalesBalance.setOther(StrUtils.replaceDouble(csvReader.get("other")));
        financialSalesBalance.setTotal(StrUtils.replaceDouble(csvReader.get("total")));
        financialSalesBalance.setoQuantity(StrUtils.replaceLong(csvReader.get("quantity")));
        financialSalesBalance.setSiteId(seId);
        financialSalesBalance.setShopId(sId);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        financialSalesBalance.setSkuId(skuId);
        //UserId
        financialSalesBalance.setCreateIdUser(uid);
        return financialSalesBalance;
    }

    /**
     * 美国存入对象
     */
    public FinancialSalesBalance usaDepositObject(FinancialSalesBalance financialSalesBalance, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        financialSalesBalance.setDate(DateUtils.getTime(csvReader.get("date/time"), Constants.USA_TIME));
        financialSalesBalance.setSettlemenId(StrUtils.replaceString(csvReader.get("settlement id")));
        financialSalesBalance.setType(StrUtils.replaceString(csvReader.get("type")));
        financialSalesBalance.setOrderId(StrUtils.replaceString(csvReader.get("order id")));
        String skuName = StrUtils.replaceString(csvReader.get("sku"));
        financialSalesBalance.setSku(skuName);
        financialSalesBalance.setDescription(StrUtils.replaceString(csvReader.get("description")));
        financialSalesBalance.setMarketplace(StrUtils.replaceString(csvReader.get("marketplace")));
        financialSalesBalance.setFulfillment(StrUtils.replaceString(csvReader.get("fulfillment")));
        financialSalesBalance.setCity(StrUtils.replaceString(csvReader.get("order city")));
        financialSalesBalance.setState(StrUtils.replaceString(csvReader.get("order state")));
        financialSalesBalance.setPostal(StrUtils.replaceString(csvReader.get("order postal")));
        financialSalesBalance.setSales(StrUtils.replaceDouble(csvReader.get("product sales")));
        financialSalesBalance.setShippingCredits(StrUtils.replaceDouble(csvReader.get("shipping credits")));
        financialSalesBalance.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get("gift wrap credits")));
        financialSalesBalance.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("promotional rebates")));
        financialSalesBalance.setSales(StrUtils.replaceDouble(csvReader.get("sales tax collected")));
        financialSalesBalance.setMarketplaceFacilitatorTax(StrUtils.replaceDouble(csvReader.get("Marketplace Facilitator Tax")));
        financialSalesBalance.setSellingFees(StrUtils.replaceDouble(csvReader.get("selling fees")));
        financialSalesBalance.setFbaFee(StrUtils.replaceDouble(csvReader.get("fba fees")));
        financialSalesBalance.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("other transaction fees")));
        financialSalesBalance.setOther(StrUtils.replaceDouble(csvReader.get("other")));
        financialSalesBalance.setTotal(StrUtils.replaceDouble(csvReader.get("total")));
        financialSalesBalance.setCreateDate(new Date().getTime());
        financialSalesBalance.setSiteId(seId);
        financialSalesBalance.setShopId(sId);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        financialSalesBalance.setSkuId(skuId);
        //UserId
        financialSalesBalance.setCreateIdUser(uid);
        return financialSalesBalance;
    }

    /**
     * 德国存入对象
     */
    public FinancialSalesBalance germanDepositObject(FinancialSalesBalance financialSalesBalance, CsvReader csvReader, Long sId, Long seId, Long uid) throws IOException {
        financialSalesBalance.setDate(DateUtils.getGermanTime(csvReader.get("Datum/Uhrzeit")));
        financialSalesBalance.setSettlemenId(StrUtils.replaceString(csvReader.get("Abrechnungsnummer")));
        financialSalesBalance.setType(StrUtils.replaceString(csvReader.get("Typ")));
        financialSalesBalance.setOrderId(StrUtils.replaceString(csvReader.get("Bestellnummer")));
        String skuName = StrUtils.replaceString(csvReader.get("SKU"));
        financialSalesBalance.setSku(skuName);
        financialSalesBalance.setDescription(StrUtils.replaceString(csvReader.get("Beschreibung")));
        financialSalesBalance.setoQuantity(StrUtils.replaceLong(csvReader.get("Menge")));
        financialSalesBalance.setMarketplace(StrUtils.replaceString(csvReader.get("Marketplace")));
        financialSalesBalance.setFulfillment(StrUtils.replaceString(csvReader.get("Versand")));
        financialSalesBalance.setState(StrUtils.replaceString(csvReader.get("Bundesland")));
        financialSalesBalance.setPostal(StrUtils.replaceString(csvReader.get("Postleitzahl")));
        financialSalesBalance.setSales(StrUtils.replaceDouble(csvReader.get("Ums?tze")));
        financialSalesBalance.setShippingCredits(StrUtils.replaceDouble(csvReader.get("Gutschrift für Versandkosten")));
        financialSalesBalance.setGiftwrapCredits(StrUtils.replaceDouble(csvReader.get("Gutschrift für Geschenkverpackung")));
        financialSalesBalance.setPromotionalRebates(StrUtils.replaceDouble(csvReader.get("Rabatte aus Werbeaktionen")));
        financialSalesBalance.setSellingFees(StrUtils.replaceDouble(csvReader.get("Verkaufsgebühren")));
        financialSalesBalance.setFbaFee(StrUtils.replaceDouble(csvReader.get("Gebühren zu Versand durch Amazon")));
        financialSalesBalance.setOtherTransactionFees(StrUtils.replaceDouble(csvReader.get("Andere Transaktionsgebühren")));
        financialSalesBalance.setOther(StrUtils.replaceDouble(csvReader.get("Andere")));
        financialSalesBalance.setTotal(StrUtils.replaceDouble(csvReader.get("Gesamt")));
        financialSalesBalance.setCreateDate(new Date().getTime());
        financialSalesBalance.setSiteId(seId);
        financialSalesBalance.setShopId(sId);
        Long skuId = skuService.selSkuId(sId, seId, skuName);
        //UserId
        financialSalesBalance.setCreateIdUser(uid);
        return skuList(skuId,csvReader,financialSalesBalance);
    }

    /**
     * 获取没有SKU的文件List
     * @param skuId
     * @param csvReader
     * @param financialSalesBalance
     * @return
     */
    public FinancialSalesBalance skuList(Long skuId, CsvReader csvReader, FinancialSalesBalance financialSalesBalance) throws IOException {
        if (skuId == null) {
            List<String> skuListNo = new ArrayList();
            for (int i = 0; i < csvReader.getColumnCount(); i++) {
                skuListNo.add(csvReader.get(i).replace(",", "."));
            }
            noSkuIdList.add(skuListNo);
            return null;
        }
        financialSalesBalance.setSkuId(skuId);
        return financialSalesBalance;
    }

}
