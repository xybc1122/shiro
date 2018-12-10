package com.dt.user.controller;

import com.csvreader.CsvReader;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.FinancialSalesBalance;
import com.dt.user.service.FinancialSalesBalanceService;
import com.dt.user.utils.CSVUtil;
import com.dt.user.utils.DateUtils;
import com.dt.user.utils.FileUtils;
import com.dt.user.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

@RestController
@RequestMapping("/upload")
public class FinancialSalesBalanceController {

    @Autowired
    private FinancialSalesBalanceService financialSalesBalanceService;

    /**
     * 查询从第几行开始读
     * @return
     * @throws Exception
     */
    @Transactional
    @PostMapping("/germany")
    @Async("executor")
    public ResponseBase germanyInfo(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String contentType = file.getContentType();//图片||文件类型
        String fileName = file.getOriginalFilename();//图片||文件名字
        //指定文件存放路径，可以是相对路径或者绝对路径
        String filePath = "D:\\";
        try {
            FileUtils.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
        }
//        String filePath = "E:/201810月31-201812月1CustomTransaction.csv";
//        int file = filePath.indexOf(".");
//        String typeFile = filePath.substring(file + 1);
//        System.out.println(typeFile);
//        switch (typeFile) {
//            //csv文件操作
//            case "csv":
//                saveCSV(filePath);
//                break;
//            case "":
//                break;
//        }


        return null;
    }


    public void saveCSV(String filePath) {
        FinancialSalesBalance financialSalesBalance = new FinancialSalesBalance();
        //获得头信息长度
        int row = CSVUtil.startReadLine(filePath);
        InputStreamReader isr = null;
        // 创建CSV读对象
        CsvReader csvReader = null;
        int index = 0;
        try {
            isr = new InputStreamReader(new FileInputStream(new File(filePath)), "GBK");
            csvReader = new CsvReader(isr);
            while (csvReader.readRecord()) {
                //如果正确设置表头
                if (index == (row - 2)) {
                    //设置表头
                    csvReader.readHeaders();
                    //拿到表头信息 对比数据库的表头 如果不一致 抛出报错信息 不执行下去
                    String[] head = csvReader.getRawRecord().split(",");
                    for (int i = 0; i < head.length; i++) {
                        System.out.println(head[i]);
                        //return BaseApiService.setResultError("数据库字段不一致~");
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
                    financialSalesBalanceService.addInfoGerman(financialSalesBalance);
                }
                index++;
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
    }
}
