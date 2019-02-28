package com.dt.user.utils;

import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvWriter;
import com.dt.user.toos.Constants;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class CSVUtil {
    /**
     * 从第几行开始读
     *
     * @param filePath 文件路径
     */
    public static String startReadLine(String filePath, Integer site, int tbId) {
        JSONObject readJson = new JSONObject();
        try (InputStreamReader read = FileUtils.streamReader(filePath);
             BufferedReader reader = new BufferedReader(read)) {
            //设置编码格式 ,日文解码shift_jis
            String line;
            int index = 1;
            while ((line = reader.readLine()) != null) {
                if (tbId == 85 || tbId == 104) {
                    //CSV格式文件为逗号分隔符文件，这里根据逗号切分
                    String item[] = line.split(",");
                    String itemHead = item[0].replace("\"", "");
                    //1美国
                    if (itemHead.equals("date/time") && site == 1) {
                        return headJson(readJson, line, index);
                        //2加拿大
                    } else if (itemHead.equals("date/time") && site == 2) {
                        return headJson(readJson, line, index);
                        //3澳大利亚
                    } else if (itemHead.equals("date/time") && site == 3) {
                        return headJson(readJson, line, index);
                        //4英国
                    } else if (itemHead.equals("date/time") && site == 4) {
                        return headJson(readJson, line, index);
                        //5德国
                    } else if (itemHead.equals("Datum/Uhrzeit") && site == 5) {
                        return headJson(readJson, line, index);
                        //6 法国
                    } else if (itemHead.equals("date/heure") && site == 6) {
                        return headJson(readJson, line, index);
                        // 7 意大利
                    } else if (itemHead.equals("Data/Ora:") && site == 7) {
                        return headJson(readJson, line, index);
                        //8 西班牙
                    } else if (itemHead.equals("fecha y hora") && site == 8) {
                        return headJson(readJson, line, index);
                        // 9 日本
                    } else if (itemHead.equals("日付/時間") && site == 9) {
                        return headJson(readJson, line, index);
                    }//10 墨西哥
                    else if (itemHead.equals("fecha/hora") && site == 10) {
                        return headJson(readJson, line, index);
                    }
                    index++;
                } else {
                    return headJson(readJson, line, index);
                }
            }
            readJson.put("index", -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readJson.toJSONString();
    }

    /**
     * 封装获得CSV表头信息的JSON 数据
     *
     * @param readJson
     * @param line
     * @param index
     * @return
     */
    public static String headJson(JSONObject readJson, String line, int index) {
        String[] headArr = line.split(",");
        readJson.put("head", headArr);
        readJson.put("index", index);
        return readJson.toJSONString();
    }

    /**
     * 写入操作
     *
     * @param headerList    表头 信息
     * @param isNoSkuIdList 表体信息
     * @param filePath      路径
     */
    public static void write(List<String> headerList, List<List<String>> isNoSkuIdList, String filePath, String uuidName) {
        FileUtils.mkdirFile(filePath);
        CsvWriter csvWriter = null;
        try {
            // 创建CSV写对象
            csvWriter = new CsvWriter(filePath + uuidName, ',', Charset.forName("GBK"));
            String[] headers = new String[headerList.size()];
            headers = headerList.toArray(headers);
            // 先写表头
            csvWriter.writeRecord(headers);
            //循环表内容
            for (int i = 0; i < isNoSkuIdList.size(); i++) {
                String[] skuIdArr = new String[isNoSkuIdList.get(i).size()];
                skuIdArr = isNoSkuIdList.get(i).toArray(skuIdArr);
                csvWriter.writeRecord(skuIdArr);
            }
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (csvWriter != null) {
                csvWriter.close();
            }
        }
    }
}
