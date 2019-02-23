package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.BasePublicModel.BasicPublicProducts;
import com.dt.user.service.BasePublicService.BasicPublicProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pro")
public class BasicPublicProductsController {
    @Autowired
    private BasicPublicProductsService publicProductsService;

    /**
     * 获取产品类目
     *
     * @return
     */
    @RequestMapping("/findByListProducts")
    public ResponseBase findByListProducts() {
        List<BasicPublicProducts> basicPublicSiteList = publicProductsService.findByProductsInfo();
        return BaseApiService.setResultSuccess(basicPublicSiteList);
    }
}
