package com.dt.user.service.BasePublicService;

import com.dt.user.model.BasePublicModel.BasicPublicCompany;

import java.util.List;

public interface BasicPublicCompanyService {


    /**
     * 查询公司所有相关信息
     * @return
     */
    List<BasicPublicCompany> findByListCompany();
}
