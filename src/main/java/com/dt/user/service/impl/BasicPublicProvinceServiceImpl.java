package com.dt.user.service.impl;

import com.dt.user.mapper.BasePublicMapper.BasicPublicProvinceMapper;
import com.dt.user.service.BasePublicService.BasicPublicProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicPublicProvinceServiceImpl implements BasicPublicProvinceService {

    @Autowired
    private BasicPublicProvinceMapper provinceMapper;

}
