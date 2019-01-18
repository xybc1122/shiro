package com.dt.user.service.Logistics;

import com.dt.user.config.ResponseBase;
import com.dt.user.fallbcak.LogisticsClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 物流服务客户端
 */
@FeignClient(name = "wl-service", fallback = LogisticsClientFallback.class)
public interface LogisticsClient {


    @GetMapping("/api/v1/way/wayList")
    ResponseBase wayInfo();

}
