package com.dt.user.fallbcak;

import com.dt.user.config.ResponseBase;
import com.dt.user.service.Logistics.LogisticsClient;
import org.springframework.stereotype.Component;

/**
 * 针对商品服务，错降级处理
 */
@Component
public class LogisticsClientFallback implements LogisticsClient {


    @Override
    public ResponseBase wayInfo() {

        System.out.println("feign 调用 wayInfo 异常 ");

        return null;
    }
}
