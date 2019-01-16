package com.dt.user;


import com.dt.user.mapper.BasicSalesAmazonWarehouseMapper;
import com.dt.user.model.BasicSalesAmazonWarehouse;
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
}
