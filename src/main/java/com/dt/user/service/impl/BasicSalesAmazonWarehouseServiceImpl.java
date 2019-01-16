package com.dt.user.service.impl;

import com.dt.user.mapper.BasicSalesAmazonWarehouseMapper;
import com.dt.user.model.BasicSalesAmazonWarehouse;;
import com.dt.user.service.BasicSalesAmazonWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicSalesAmazonWarehouseServiceImpl implements BasicSalesAmazonWarehouseService{
    @Autowired
    private BasicSalesAmazonWarehouseMapper warehouseMapper;

    @Override
    public BasicSalesAmazonWarehouse getWarehouse(String fc) {
        return warehouseMapper.getWarehouse(fc);
    }
}
