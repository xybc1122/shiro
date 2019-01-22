package com.dt.user.service.Logistics;

import com.dt.user.config.ResponseBase;
import com.dt.user.service.Logistics.fallbcak.LogisticsClientFallback;
import com.dt.user.service.Logistics.model.LogisticsPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 物流服务客户端
 */
@FeignClient(name = "wl-service", fallback = LogisticsClientFallback.class)
public interface LogisticsClient {


    @RequestMapping("/api/v1/wayList")
    ResponseBase wayInfo(LogisticsPage logisticsPage);

}
