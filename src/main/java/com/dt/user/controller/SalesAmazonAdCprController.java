//package com.dt.user.controller;
//
//import com.dt.user.config.BaseApiService;
//import com.dt.user.config.ResponseBase;
//import com.dt.user.model.SalesAmazonAdCpr;
//import com.dt.user.model.UserInfo;
//import com.dt.user.model.UserUpload;
//import com.dt.user.service.SalesAmazonAdCprService;
//import com.dt.user.toos.Constants;
//import com.dt.user.utils.*;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/ad/upload")
//public class SalesAmazonAdCprController {
//    @Autowired
//    private SalesAmazonAdCprService cprService;
//    //用户上传记录实体类
//    private UserUpload userUpload;
//
//    //行数 /报错行数
//    private int count = 1;
//    //没有sku有几行存入
//    private int sumNoSku;
//
//    @Transactional
//    @GetMapping("/file")
//    public ResponseBase saveFileInfo(HttpServletRequest request,@RequestParam("file") MultipartFile file, HttpServletRequest request,
//                                     @RequestParam("sId") String sId, @RequestParam("seId") String seId) {
//        String token = GetCookie.getToken(request);
//        UserInfo user = JwtUtils.jwtUser(token);
//        if (user == null) {
//            return BaseApiService.setResultError("用户无效~");
//        }
//        //指定文件存放路径
//        String saveFilePath = Constants.SAVE_FILE_PATH;
//        count = 1;
//        sumNoSku = 0;
//        //店铺ID
//        Integer shopId = Integer.parseInt(sId);
//        //站点ID
//        Integer siteId = Integer.parseInt(seId);
//        // pId
//        Integer pId = Integer.parseInt(payId);
//        //String contentType = file.getContentType();//图片||文件类型
//        String fileName = file.getOriginalFilename();//图片||文件名字
//        userUpload = FileUtils.uploadOperating(shopId, siteId, fileName, saveFilePath, user);
//        String filePath = "E:\\DT-US-CPR-201811.xlsx";
//        FileInputStream in = null;
//        File files = new File(filePath);
//        Workbook wb = null;
//        try {
//            in = new FileInputStream(filePath);
//            XlsUtils.checkExcel(files);
//            wb = XlsUtils.getWorkbook(in, files);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Sheet sheet = wb.getSheetAt(0);
//        List<SalesAmazonAdCpr> cprList = new ArrayList<>();
//        SalesAmazonAdCpr saCpr;
//        Row row;
//        Cell cell;
//        int line = 1;
//        int lastRowNum = sheet.getLastRowNum(); // 获取总行数
//        int totalNumber = sheet.getRow(0).getPhysicalNumberOfCells(); //获取总列数
//        int sIds = 1;
//        for (int i = line; i <= lastRowNum; i++) {
//            saCpr = new SalesAmazonAdCpr();
//            for (int j = 0; j < totalNumber; j++) {
//                row = sheet.getRow(i);
//                cell = row.getCell(j);
//                switch (sIds) {
//                    case 1:
//                        switch (j) {
//                            case 0:
//                                saCpr.setDate(StrUtils.replaceLong(XlsUtils.getCellValue(cell).trim()));
//                                break;
//                            case 2:
//                                saCpr.setCampaignName(StrUtils.repString(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 3:
//                                saCpr.setAdGroupName(StrUtils.repString(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 4:
//                                saCpr.setAdvertisedSku(StrUtils.repString(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 5:
//                                saCpr.setAdvertisedAsin(StrUtils.repString(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 6:
//                                saCpr.setImpressions(StrUtils.repDouble(XlsUtils.getCellValue(cell).trim()));
//                                break;
//                            case 7:
//                                saCpr.setClicks(StrUtils.repDouble(XlsUtils.getCellValue(cell).trim()));
//                                break;
//                            case 10:
//                                saCpr.setTotalSpend(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 11:
//                                saCpr.setSales(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 13:
//                                saCpr.setRoas(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 14:
//                                saCpr.setOrdersPlaced(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 15:
//                                saCpr.setTotalUnits(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 17:
//                                saCpr.setSameskuUnitsOrdered(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 18:
//                                saCpr.setOtherskuUnitsOrdered(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 19:
//                                saCpr.setSameskuUnitsSales(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
//                                break;
//                            case 20:
//                                saCpr.setOtherskuUnitsSales(StrUtils.repDouble(XlsUtils.getCellValue(cell)));
//                                break;
//                        }
//                        break;
//                }
//            }
//            cprList.add(saCpr);
//        }
//        cprService.AddSalesAmazonAdCprList(cprList);
//
//        return null;
//    }
//}
