package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.BasePublicModel.BasicPublicShop;
import com.dt.user.service.BasePublicService.BasicPublicShopService;
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
     * 获得店铺信息
     *
     * @return
     */
    @GetMapping("/findByListShop")
    public ResponseBase findByListShop() {
        List<BasicPublicShop> basicPublicShopList = basicPublicShopService.findByListShop();
        return BaseApiService.setResultSuccess(basicPublicShopList);
    }

}
