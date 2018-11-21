package com.dt.user.service;

import com.dt.user.model.BasicPublicCompany;

import java.util.List;

public interface BasicPublicCompanyService {


    /**
     * 查询公司所有相关信息
     * @return
     */
    List<BasicPublicCompany> findByListCompany();
}
