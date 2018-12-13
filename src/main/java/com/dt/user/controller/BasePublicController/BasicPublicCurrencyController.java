package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.BasePublicModel.BasicPublicCurrency;
import com.dt.user.service.BasePublicService.BasicPublicCurrencyService;
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
     * 获得货币信息
     *
     * @return
     */
    @GetMapping("/findByListCurrency")
    public ResponseBase findByListCurrency() {
        List<BasicPublicCurrency> basicPublicCurrencies = basicPublicCurrencyService.findByListCurrency();
        return BaseApiService.setResultSuccess(basicPublicCurrencies);
        }
}
