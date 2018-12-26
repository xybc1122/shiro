package com.dt.user.utils;

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
    public static void ioClose(BufferedReader reader, InputStreamReader isr, Workbook wb, FileInputStream in) {
        try {
            //csv
            if (reader != null) {
                reader.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (wb != null) {
                wb.close();
            }
            //xls
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
