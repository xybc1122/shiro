package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.BasicPublicArea;
import com.dt.user.model.BasicPublicCurrency;
import com.dt.user.service.BasicPublicAreaService;
import com.dt.user.service.BasicPublicCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class BasicPublicCurrencyController {
    @Autowired
    private BasicPublicCurrencyService basicPublicCurrencyService;

    /**
     * 获得区域的信息
     *
     * @return
     */
    @GetMapping("/findByListCurrency")
    public ResponseBase findByListCurrency() {
        List<BasicPublicCurrency> basicPublicCurrencies = basicPublicCurrencyService.findByListCurrency();
        return BaseApiService.setResultSuccess(basicPublicCurrencies);
        }
}