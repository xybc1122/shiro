package com.dt.user.service.impl;

import com.dt.user.mapper.BasePublicMapper.BasicLogisticsmgtTransportCompanyMapper;
import com.dt.user.service.BasePublicService.BasicLogisticsmgtTransportCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicLogisticsmgtTransportCompanyServiceImpl implements BasicLogisticsmgtTransportCompanyService {
    @Autowired
    private BasicLogisticsmgtTransportCompanyMapper companyMapper;
}
