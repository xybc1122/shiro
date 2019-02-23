package com.dt.user.service.impl;

import com.dt.user.mapper.BasePublicMapper.BasicPublicProvinceCityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicPublicProvinceCityServiceImpl {

    @Autowired
    private BasicPublicProvinceCityMapper cityMapper;
}
