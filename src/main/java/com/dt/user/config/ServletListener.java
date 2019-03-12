package com.dt.user.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @ClassName ServletListener
 * Description TODO
 * @Author 陈恩惠
 * @Date 2019/3/7 10:03
 * 项目启动执行
 **/
@Configuration
public class ServletListener extends BaseApiService implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //清空当前库的所有缓存  这里设计有问题
        baseRedisService.delAll();
    }
}
