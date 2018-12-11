package com.dt.user.controller;

import com.csvreader.CsvReader;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.FinancialSalesBalance;
import com.dt.user.service.FinancialSalesBalanceService;
import com.dt.user.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/upload")
public class FinancialSalesBalanceController {

    @Autowired
    private FinancialSalesBalanceService financialSalesBalanceService;

    /**
     * 德国csv表操作
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional
    @PostMapping("/germany")
    public ResponseBase germanyInfo(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        //String contentType = file.getContentType();//图片||文件类型
        String fileName = file.getOriginalFilename();//图片||文件名字
        //指定文件存放路径
        String saveFilePath = "D:/csv/";
        try {
            FileUtils.uploadFile(file.getBytes(), saveFilePath, fileName);
        } catch (FileNotFoundException e) {
            return BaseApiService.setResultError("上传失败" + e.getMessage());
        } catch (IOException e) {
            return BaseApiService.setResultError("上传失败" + e.getMessage());
        } catch (Exception e) {
            return BaseApiService.setResultError("上传失败" + e.getMessage());
        }
        String filePath = saveFilePath + fileName;
        //获得头信息长度
        int row = CSVUtil.startReadLine(filePath);
        if (row == 0) {
            throw new Exception("存入数据失败,请检查Datum/Uhrzeit是否正确");
        }
        int fileIndex = filePath.indexOf(".");
        String typeFile = filePath.substring(fileIndex + 1);
        switch (typeFile) {
            //csv文件操作
            case "csv":
                boolean isCsv = saveGermanyCSV(filePath, row);
                if (isCsv) {
                    return BaseApiService.setResultSuccess("数据存入成功~");
                }
                FileUtils.deleteFile(filePath);
                return BaseApiService.setResultError("数据存入失败~");
            case "":
                break;
        }
        return null;
    }
    /**
     * 德国数据配置解析
     *
     * @param filePath
     */
    public boolean saveGermanyCSV(String filePath, int row) {
        FinancialSalesBalance financialSalesBalance = new FinancialSalesBalance();
        boolean isFlg;
        InputStreamReader isr = null;
        // 创建CSV读对象
        CsvReader csvReader = null;
        int index = 0;
        try {
            isr = new InputStreamReader(new FileInputStream(new File(filePath)), "GBK");
            csvReader = new CsvReader(isr);
            List<FinancialSalesBalance> financialSalesBalanceList = new ArrayList<>();
            while (csvReader.readRecord()) {
                //如果正确设置表头
                if (index == (row - 2)) {
                    //设置表头
                    csvReader.readHeaders();
                    //拿到表头信息 对比数据库的表头 如果不一致 抛出报错信息 不执行下去
                    String[] head = csvReader.getRawRecord().split(",");
                    List<String> headList = Arrays.asList(head);
                    List<String> fBalanceHead = new ArrayList<>();
                    isFlg = ArrUtils.equalList(headList, fBalanceHead);
                    if (!isFlg) {
                        return false;
                    }
                }
                if (index >= row - 1) {
                    financialSalesBalance.setDate(DateUtils.getGermanTime(csvReader.get("Datum/Uhrzeit")));
                    financialSalesBalance.setSettlemenId(StrUtils.replaceString(csvReader.get("Abrechnungsnummer")));
                    financialSalesBalance.setType(StrUtils.replaceString(csvReader.get("Typ")));
                    financialSalesBalance.setOrderId(StrUtils.replaceString(csvReader.get("Bestellnummer")));
                    financialSalesBalance.setSku(StrUtils.replaceString(csvReader.get("SKU")));
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
                    //UserId
                    financialSalesBalance.setCreateIdUser(1L);
                    financialSalesBalanceList.add(financialSalesBalance);
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
}
