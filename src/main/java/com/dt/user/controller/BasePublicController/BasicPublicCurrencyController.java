package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.CurrencyDto;
import com.dt.user.dto.UserDto;
import com.dt.user.model.BasePublicModel.BasicPublicCurrency;
import com.dt.user.service.BasePublicService.BasicPublicCurrencyService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class BasicPublicCurrencyController {
    @Autowired
    private BasicPublicCurrencyService basicPublicCurrencyService;

    /**
     * 获得货币信息
     *
     * @return
     */
    @PostMapping("/findByListCurrency")
    public ResponseBase findByListCurrency(@RequestBody CurrencyDto currencyDto) {
        if (currencyDto.getCurrentPage() != null && currencyDto.getPageSize() != null) {
            PageHelper.startPage(currencyDto.getCurrentPage(), currencyDto.getPageSize());
            List<CurrencyDto> basicPublicCurrencies = basicPublicCurrencyService.findByListCurrency();
            PageInfo<CurrencyDto> pageInfo = new PageInfo<>(basicPublicCurrencies);
            Integer currentPage = currencyDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }
}
