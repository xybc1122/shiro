package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.BasicPublicCompany;
import com.dt.user.model.BasicPublicShop;
import com.dt.user.service.BasicPublicShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("shop")
public class BasicPublicShopController {
    @Autowired
    private BasicPublicShopService basicPublicShopService;

    /**
     * 获得公司的信息
     *
     * @return
     */
    @GetMapping("/findByListShop")
    public ResponseBase findByListShop() {
        List<BasicPublicShop> basicPublicShopList = basicPublicShopService.findByListShop();
        return BaseApiService.setResultSuccess(basicPublicShopList);
    }

}
