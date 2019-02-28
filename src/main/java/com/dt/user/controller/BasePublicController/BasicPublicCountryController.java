package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.CountryDto;
import com.dt.user.service.BasePublicService.BasicPublicCountryService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/country")
@RestController
public class BasicPublicCountryController {

    @Autowired
    private BasicPublicCountryService countryService;

    @PostMapping("/findCountryInfo")
    public ResponseBase findCountryInfo(@RequestBody CountryDto countryDto) {
        if (countryDto.getCurrentPage() != null && countryDto.getPageSize() != null) {
            PageHelper.startPage(countryDto.getCurrentPage(), countryDto.getPageSize());
            List<CountryDto> countryDtoList = countryService.findByUsers(countryDto);
            PageInfo<CountryDto> pageInfo = new PageInfo<>(countryDtoList);
            Integer currentPage = countryDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }

}
