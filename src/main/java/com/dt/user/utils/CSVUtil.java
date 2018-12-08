package com.dt.user.utils;

import com.csvreader.CsvReader;
import java.io.*;


public class CSVUtil {
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
