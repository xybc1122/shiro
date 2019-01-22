package com.dt.user.service.Logistics.fallbcak;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.service.Logistics.LogisticsClient;
import com.dt.user.service.Logistics.model.LogisticsPage;
import org.springframework.stereotype.Component;

/**
 * 针对商品服务，错降级处理
 */
@Component
public class LogisticsClientFallback implements LogisticsClient {

    @Override
    public ResponseBase wayInfo(LogisticsPage logisticsPage) {
        return BaseApiService.setResultError("feign 调用 wayInfo 服务异常");
    }
}
