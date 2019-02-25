package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
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
@RequestMapping("/currency")
public class BasicPublicCurrencyController {
    @Autowired
    private BasicPublicCurrencyService basicPublicCurrencyService;

    /**
     * 获得货币信息
     *
     * @return
     */
    @PostMapping("/findByListCurrency")
    public ResponseBase findByListCurrency(@RequestBody UserDto pageDto) {
        if (pageDto.getCurrentPage() != null && pageDto.getPageSize() != null) {
            PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
            List<BasicPublicCurrency> basicPublicCurrencies = basicPublicCurrencyService.findByListCurrency();
            PageInfo<BasicPublicCurrency> pageInfo = new PageInfo<>(basicPublicCurrencies);
            Integer currentPage = pageDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }
}
