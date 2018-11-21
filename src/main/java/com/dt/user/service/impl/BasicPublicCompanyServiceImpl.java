package com.dt.user.service.impl;

import com.dt.user.mapper.BasicPublicCompanyMapper;
import com.dt.user.model.BasicPublicCompany;
import com.dt.user.service.BasicPublicCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicPublicCompanyServiceImpl implements BasicPublicCompanyService {
    @Autowired
    private BasicPublicCompanyMapper basicPublicCompanyMapper;

    @Override
    public List<BasicPublicCompany> findByListCompany() {
        return basicPublicCompanyMapper.findByListCompany();
    }
}
