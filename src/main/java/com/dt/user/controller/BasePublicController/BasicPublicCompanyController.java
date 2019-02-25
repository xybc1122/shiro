package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
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
@RequestMapping("company")
public class BasicPublicCompanyController {
    @Autowired
    private BasicPublicCompanyService basicPublicCompanyService;

    /**
     * 获得公司的信息
     * @return
     */
    @PostMapping("/findByListCompany")
    public ResponseBase findByListCompany(@RequestBody UserDto pageDto) {
        if (pageDto.getCurrentPage() != null && pageDto.getPageSize() != null) {
            PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
            List<BasicPublicCompany> basicPublicCompanyList = basicPublicCompanyService.findByListCompany();
            PageInfo<BasicPublicCompany> pageInfo = new PageInfo<>(basicPublicCompanyList);
            Integer currentPage = pageDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }


}
