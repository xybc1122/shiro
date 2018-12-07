package com.dt.user.utils;

import com.csvreader.CsvReader;
import com.dt.user.mapper.FinancialSalesBalanceMapper;
import com.dt.user.model.FinancialSalesBalance;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.Date;


public class CSVUtil {
    @Autowired
    private FinancialSalesBalanceMapper financialSalesBalanceMapper;


    public static void main(String[] args) {

//        int dayIndex = date.indexOf(":");
//        String day = date.substring(0, dayIndex);
//        String dateMonth = date.substring(dayIndex + 1);
//        int indexMonth = dateMonth.indexOf(":");
//        String month = dateMonth.substring(0, indexMonth);
//        String dateYear = dateMonth.substring(indexMonth + 1);
//        int indexYear = dateYear.indexOf(" ");
//        String year = dateYear.substring(0, indexYear);
//
//        //取时分秒
//        int lastIndex = date.indexOf(" ");
//        String lastDate = date.substring(lastIndex);
//        StringBuilder sb = new StringBuilder();
//        sb.append(year).append(":").append(month).append(":").append(day).append(lastDate);
//        System.out.println(sb);
        // 测试导入
   /*

        // 测试导入
      read(filePath);*/
//        filePath(filePath);
        String filePath = "E:/201810月31-201812月1CustomTransaction.csv";
        CSVUtil s = new CSVUtil();
        s.saveGermanInfo(filePath, startReadLine(filePath));

//        read();

    }

    /**
     * 查询从第几行开始读
     *
     * @param filePath 导入路径
     * @return
     * @throws Exception
     */
    public void saveGermanInfo(String filePath, int row) {
        File file = new File(filePath);
        InputStreamReader isr;
        FinancialSalesBalance financialSalesBalance = new FinancialSalesBalance();
        // 创建CSV读对象
        CsvReader csvReader = null;
        int index = 0;
        try {
            isr = new InputStreamReader(new FileInputStream(file), "GBK");
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
                    }
                }
                if (index >= row - 1) {
                    financialSalesBalance.setDate(DateUtils.getGermanTime(csvReader.get("Datum/Uhrzeit")));
                    financialSalesBalance.setSettlemenId(csvReader.get("Abrechnungsnummer"));
                    financialSalesBalance.setType(csvReader.get("Typ"));
                    financialSalesBalance.setOrderId(csvReader.get("Bestellnummer"));
                    financialSalesBalance.setSku(csvReader.get("SKU"));
                    financialSalesBalance.setDescription(csvReader.get("Beschreibung"));
                    financialSalesBalance.setoQuantity(Long.parseLong(csvReader.get("Menge")));
                    financialSalesBalance.setMarketplace(csvReader.get("Marketplace"));
                    financialSalesBalance.setFulfillment(csvReader.get("Versand"));
                    financialSalesBalance.setState(csvReader.get("Bundesland"));
                    financialSalesBalance.setPostal(csvReader.get("Postleitzahl"));
                    financialSalesBalance.setSales(StringUtils.replaceString(csvReader.get("Ums?tze")));
                    financialSalesBalance.setShippingCredits(StringUtils.replaceString(csvReader.get("Gutschrift für Versandkosten")));
                    financialSalesBalance.setGiftwrapCredits(StringUtils.replaceString(csvReader.get("Gutschrift für Geschenkverpackung")));
                    financialSalesBalance.setPromotionalRebates(StringUtils.replaceString(csvReader.get("Rabatte aus Werbeaktionen")));
                    financialSalesBalance.setSellingFees(StringUtils.replaceString(csvReader.get("Verkaufsgebühren")));
                    financialSalesBalance.setFbaFee(StringUtils.replaceString(csvReader.get("Gebühren zu Versand durch Amazon")));
                    financialSalesBalance.setOtherTransactionFees(StringUtils.replaceString(csvReader.get("Andere Transaktionsgebühren")));
                    financialSalesBalance.setOther(StringUtils.replaceString(csvReader.get("Andere")));
                    financialSalesBalance.setTotal(StringUtils.replaceString(csvReader.get("Gesamt")));
                    financialSalesBalance.setCreateDate(new Date().getTime());
                    financialSalesBalance.setCreateIdUser(1L);

                    //System.out.println(StringUtils.replaceString(csvReader.get("Andere Transaktionsgebühren")));
                    // System.out.println(financialSalesBalance);
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }
    }
//        if (row == -1) {
//            System.out.println("请查看字段名Datum/Uhrzeit是否正确");
//        }
    //

    /**
     * 从第几行开始读
     *
     * @param filePath 文件路径
     */
    public static int startReadLine(String filePath) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                index++;
                if (item[0].equals("Datum/Uhrzeit")) {
                    return index;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    public static void read() {

        String filePath = "E:/201810月31-201812月1CustomTransaction.csv";

        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath);

            // 读表头
            csvReader.readHeaders();
            while (csvReader.readRecord()) {
                // 读一整行
//                System.out.println(csvReader.getRawRecord());
                // 读这行的某一列
                System.out.println(csvReader.get("Abrechnungsnummer"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
