//package com.dt.user;
//
//
//import com.dt.user.mapper.BasePublicMapper.BasicSalesAmazonWarehouseMapper;
//import com.dt.user.mapper.SalesAmazonAdMapper.SalesAmazonFbaInventoryEndMapper;
//import com.dt.user.mapper.UserUploadMapper;
//import com.dt.user.model.BasePublicModel.BasicSalesAmazonWarehouse;
//import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaInventoryEnd;
//import com.dt.user.model.UserUpload;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class MapperTest {
//    @Autowired
//    private BasicSalesAmazonWarehouseMapper warehouseMapper;
//    @Autowired
//    private UserUploadMapper userUploadMapper;
//    @Autowired
//    private SalesAmazonFbaInventoryEndMapper endMapper;
//
////    @Test
////    public void add() {
////        FinancialSalesBalance s = new FinancialSalesBalance();
////        s.setSkuId(222L);
////        s.setType("2");
////        s.setOrderId("2223");
////        fsb.addInfoGerman(1, s);
////
////    }
//
//    @Test
//    public void add() {
//        BasicSalesAmazonWarehouse s = warehouseMapper.getWarehouse("YYZ2");
//        System.out.println(s);
//    }
//
//    @Test
//    public void end() {
//        List<SalesAmazonFbaInventoryEnd> aList = new ArrayList<>();
//        SalesAmazonFbaInventoryEnd s = new SalesAmazonFbaInventoryEnd();
//        s.setDate(1111L);
//        aList.add(s);
//        endMapper.AddSalesAmazonAdInventoryEndList(aList);
//
//    }
//
//    @Test
//    public void up() {
//        UserUpload s = new UserUpload();
//        s.setRemark("aaa");
//        s.setStatus(0);
//        s.setFilePath("a");
//        s.setId(729L);
//        s.setName("hhhhh");
//        int c = userUploadMapper.upUploadInfo(s);
//        System.out.println(c);
//    }
//}
