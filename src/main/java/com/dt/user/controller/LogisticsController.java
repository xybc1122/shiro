package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.service.Logistics.LogisticsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/logistics")
@RestController
public class LogisticsController {

    @Autowired
    private LogisticsClient logisticsClient;


    /**
     * 获得物流实时数据
     *
     * @return
     */
    @GetMapping("/info")
    public ResponseBase getWayInfo() {
        return logisticsClient.wayInfo();
    }
}
