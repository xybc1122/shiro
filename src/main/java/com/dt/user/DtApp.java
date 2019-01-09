package com.dt.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
// 开启异步调用功能 线程池
@EnableAsync
public class DtApp {

    public static void main(String[] args) {
        SpringApplication.run(DtApp.class, args);
    }
}
