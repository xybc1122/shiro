package com.shiro.demoshiro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 解决前后端跨域问题
 */
@Configuration//表明是个配置文件
public class Cors extends WebMvcConfigurerAdapter {

    /**
     * WebMvcConfigurerAdapter方法过期 但是这几个版本不影响使用
     * 解决前端联调 跨域的问题
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")//设置哪个url路径允许跨域
                .allowedOrigins("*")//设置静态资源的跨域路径 | 如果是线上 设置成线上的域名
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")//跨域的方法
                .allowCredentials(true).maxAge(3600);
    }
}
