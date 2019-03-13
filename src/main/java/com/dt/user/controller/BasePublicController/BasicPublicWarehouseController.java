package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.BasePublicModel.BasicPublicWarehouse;
import com.dt.user.service.BasePublicService.BasicPublicWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/war")
public class BasicPublicWarehouseController {
    @Autowired
    private BasicPublicWarehouseService warehouseService;

    @GetMapping("/findByListWar")
    public ResponseBase findByListWar() {
        List<BasicPublicWarehouse> basicPublicSiteList = warehouseService.findByWarehouseInfo();
        return BaseApiService.setResultSuccess(basicPublicSiteList);

    }
}
