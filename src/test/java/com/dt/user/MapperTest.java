package com.dt.user;


import com.dt.user.mapper.BasicSalesAmazonWarehouseMapper;
import com.dt.user.mapper.UserUploadMapper;
import com.dt.user.model.BasicSalesAmazonWarehouse;
import com.dt.user.model.UserUpload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {
    @Autowired
    private BasicSalesAmazonWarehouseMapper warehouseMapper;
    @Autowired
    private UserUploadMapper userUploadMapper;

//    @Test
//    public void add() {
//        FinancialSalesBalance s = new FinancialSalesBalance();
//        s.setSkuId(222L);
//        s.setType("2");
//        s.setOrderId("2223");
//        fsb.addInfoGerman(1, s);
//
//    }

    @Test
    public void add() {
        BasicSalesAmazonWarehouse s = warehouseMapper.getWarehouse("YYZ2");
        System.out.println(s);
    }

    @Test
    public void up() {
        UserUpload s = new UserUpload();
        s.setRemark("aaa");
        s.setStatus(0);
        s.setFilePath("a");
        s.setId(729L);
        s.setName("hhhhh");
        int c = userUploadMapper.upUploadInfo(s);
        System.out.println(c);
    }
}
