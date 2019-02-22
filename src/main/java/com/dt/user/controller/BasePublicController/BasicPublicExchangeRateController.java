package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.PageDto;
import com.dt.user.model.BasePublicModel.BasicPublicExchangeRate;
import com.dt.user.model.BasePublicModel.BasicPublicSite;
import com.dt.user.service.BasePublicService.BasicPublicExchangeRateService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/rate")
@RestController
public class BasicPublicExchangeRateController {
    @Autowired
    private BasicPublicExchangeRateService rateService;

    @RequestMapping("/findByListRate")
    public ResponseBase findByListRate(@RequestBody PageDto pageDto) {
        if (pageDto.getCurrentPage() != null && pageDto.getPageSize() != null) {
            PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
            List<BasicPublicExchangeRate> basicPublicSiteList = rateService.getRateInfo();
            PageInfo<BasicPublicExchangeRate> pageInfo = new PageInfo<>(basicPublicSiteList);
            Integer currentPage = pageDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }
}
