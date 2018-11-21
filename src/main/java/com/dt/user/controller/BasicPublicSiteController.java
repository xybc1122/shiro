package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.BasicPublicSite;
import com.dt.user.service.BasicPublicSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("site")
public class BasicPublicSiteController {

    @Autowired
    private BasicPublicSiteService basicPublicSiteService;

    /**
     * 获得站点的信息
     *
     * @return
     */
    @GetMapping("/findByListSite")
    public ResponseBase findByListSite() {
        List<BasicPublicSite> basicPublicSiteList = basicPublicSiteService.findBySiteList();
        return BaseApiService.setResultSuccess(basicPublicSiteList);
    }

}
