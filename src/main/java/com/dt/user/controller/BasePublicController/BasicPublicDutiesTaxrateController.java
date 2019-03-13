package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.TaxrateDto;
import com.dt.user.model.BasePublicModel.BasicPublicDutiesTaxrate;
import com.dt.user.service.BasePublicService.BasicPublicDutiesTaxrateService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName BasicPublicDutiesTaxrateController
 * Description TODO
 * @Author 陈恩惠
 * @Date 2019/3/13 15:02
 **/
@RestController
@RequestMapping("/tax")
public class BasicPublicDutiesTaxrateController {
    @Autowired
    private BasicPublicDutiesTaxrateService taxrateService;

    @PostMapping("/findByListTax")
    public ResponseBase findByListTax(@RequestBody TaxrateDto taxrateDto) {
        if (taxrateDto.getCurrentPage() != null && taxrateDto.getPageSize() != null) {
            PageHelper.startPage(taxrateDto.getCurrentPage(), taxrateDto.getPageSize());
            List<TaxrateDto> taxrateDtoList = taxrateService.findByListTaxrate(taxrateDto);
            PageInfo<TaxrateDto> pageInfo = new PageInfo<>(taxrateDtoList);
            Integer currentPage = taxrateDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }
}
