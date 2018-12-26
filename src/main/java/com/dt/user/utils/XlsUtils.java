package com.dt.user.utils;

import com.dt.user.model.SalesAmazonAdCpr;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsUtils {

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * 判断Excel的版本,获取Workbook
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbook(InputStream in, File file) throws IOException {
        Workbook wb = null;
        if (file.getName().endsWith(EXCEL_XLS)) {  //Excel 2003
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {  // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /**
     * 判断文件是否是excel
     *
     * @throws Exception
     */
    public static void checkExcel(File file) throws Exception {
        if (!file.exists()) {
            throw new Exception("文件不存在");
        }
        if (!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))) {
            throw new Exception("文件不是Excel");
        }
    }



//    public static void s(String filePath) {
//        Workbook wb = null;
//        FileInputStream in = null;
//        in = new FileInputStream(filePath);
//        File files = new File(filePath);
//        wb = XlsUtils.getWorkbook(in, files);
//    }
//    public static void main(String[] args) throws Exception {
//        String filePath = "E:\\DT-US-CPR-201811.xlsx";
//        FileInputStream in = new FileInputStream(filePath);
//        File file = new File(filePath);
//        checkExcel(file);
//        Workbook wb = getWorkbook(in, file);
//        Sheet sheet = wb.getSheetAt(0);
//        List<SalesAmazonAdCpr> b = new ArrayList<>();
//        SalesAmazonAdCpr saCpr;
//        Row row;
//        Cell cell;
//        int line = 1;
//        int lastRowNum = sheet.getLastRowNum(); // 获取总行数
//        int totalNumber = sheet.getRow(0).getPhysicalNumberOfCells(); //获取总列数
//        int sId = 1;
//        for (int i = line; i <= lastRowNum; i++) {
//            saCpr = new SalesAmazonAdCpr();
//            for (int j = 0; j < totalNumber; j++) {
//                row = sheet.getRow(i);
//                cell = row.getCell(j);
//                switch (sId) {
//                    case 1:
//                        switch (j) {
//                            case 0:
//                                saCpr.setDate(StrUtils.replaceLong(getCellValue(cell).trim()));
//                                break;
//                            case 2:
//                                saCpr.setCampaignName(StrUtils.repString(getCellValue(cell)));
//                                break;
//                            case 3:
//                                saCpr.setAdGroupName(StrUtils.repString(getCellValue(cell)));
//                                break;
//                            case 4:
//                                saCpr.setAdvertisedSku(StrUtils.repString(getCellValue(cell)));
//                                break;
//                            case 5:
//                                saCpr.setAdvertisedAsin(StrUtils.repString(getCellValue(cell)));
//                                break;
//                            case 6:
//                                saCpr.setImpressions(StrUtils.repDouble(getCellValue(cell).trim()));
//                                break;
//                            case 7:
//                                saCpr.setClicks(StrUtils.repDouble(getCellValue(cell).trim()));
//                                break;
//                            case 10:
//                                saCpr.setTotalSpend(StrUtils.repDouble(getCellValue(cell)));
//                                break;
//                            case 11:
//                                saCpr.setSales(StrUtils.repDouble(getCellValue(cell)));
//                                break;
//                            case 13:
//                                saCpr.setRoas(StrUtils.repDouble(getCellValue(cell)));
//                                break;
//                            case 14:
//                                saCpr.setOrdersPlaced(StrUtils.repDouble(getCellValue(cell)));
//                                break;
//                            case 15:
//                                saCpr.setTotalUnits(StrUtils.repDouble(getCellValue(cell)));
//                                break;
//                            case 17:
//                                saCpr.setSameskuUnitsOrdered(StrUtils.repDouble(getCellValue(cell)));
//                                break;
//                            case 18:
//                                saCpr.setSameskuUnitsOrdered(StrUtils.repDouble(getCellValue(cell)));
//                                break;
//                            case 19:
//                                saCpr.setSameskuUnitsSales(StrUtils.repDouble(getCellValue(cell)));
//                                break;
//                            case 20:
//                                saCpr.setOtherskuUnitsSales(StrUtils.repDouble(getCellValue(cell)));
//                                break;
//                        }
//                        System.out.println(saCpr);
//                        break;
//                }
//            }
//            b.add(saCpr);
//        }
//        System.out.println(b);
//    }

    public static String getCellValue(Cell cell) {
        String str = null;
        if (cell == null) {
            return str;
        }
        switch (cell.getCellType()) {
            case STRING:
                str = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    str = String.valueOf(cell.getDateCellValue().getTime());
                } else {
                    str = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                str = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                str = String.valueOf(cell.getCellFormula());
                break;
            case BLANK:
                System.out.println();
                break;
            default:
                System.out.println();
        }
        return str;
    }

}