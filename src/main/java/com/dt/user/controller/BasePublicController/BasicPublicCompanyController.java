package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.CompanyDto;
import com.dt.user.dto.UserDto;
import com.dt.user.model.BasePublicModel.BasicPublicCompany;
import com.dt.user.service.BasePublicService.BasicPublicCompanyService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class BasicPublicCompanyController {
    @Autowired
    private BasicPublicCompanyService basicPublicCompanyService;

    /**
     * 获得公司的信息
     * @return
     */
    @PostMapping("/findByListCompany")
    public ResponseBase findByListCompany(@RequestBody CompanyDto companyDto) {
        if (companyDto.getCurrentPage() != null && companyDto.getPageSize() != null) {
            PageHelper.startPage(companyDto.getCurrentPage(), companyDto.getPageSize());
            List<CompanyDto> basicPublicCompanyList = basicPublicCompanyService.findByListCompany();
            PageInfo<CompanyDto> pageInfo = new PageInfo<>(basicPublicCompanyList);
            Integer currentPage = companyDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }


}
