package com.dt.user.utils;

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
        return 0;
    }
}
