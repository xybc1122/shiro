package com.dt.user.utils;

import com.csvreader.CsvReader;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TryUtils {
    /**
     * 封装关闭IO Close
     *
     * @param reader
     * @param isr
     * @param in
     * @param wb
     */
    public static void ioClose(BufferedReader reader, InputStreamReader isr, Workbook wb, CsvReader csvReader, FileInputStream in) {
        try {
            //csv
            if (reader != null) {
                reader.close();
            }
            if (csvReader != null) {
                csvReader.close();
            }
            if (isr != null) {
                isr.close();
            }
            //xls
            if (wb != null) {
                wb.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
