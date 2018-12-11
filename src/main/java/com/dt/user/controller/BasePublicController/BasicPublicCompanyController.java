package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.BasicPublicCompany;
import com.dt.user.service.BasicPublicCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("company")
public class BasicPublicCompanyController {
    @Autowired
    private BasicPublicCompanyService basicPublicCompanyService;

    /**
     * 获得公司的信息
     *
     * @return
     */
    @GetMapping("/findByListCompany")
    public ResponseBase findByListCompany() {
        List<BasicPublicCompany> basicPublicCompanyList = basicPublicCompanyService.findByListCompany();
        return BaseApiService.setResultSuccess(basicPublicCompanyList);
    }


}
