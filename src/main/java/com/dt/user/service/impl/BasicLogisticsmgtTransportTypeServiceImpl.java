package com.dt.user.service.impl;

import com.dt.user.mapper.BasePublicMapper.BasicLogisticsmgtTransportTypeMapper;
import com.dt.user.service.BasePublicService.BasicLogisticsmgtTransportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicLogisticsmgtTransportTypeServiceImpl implements BasicLogisticsmgtTransportTypeService {
    @Autowired
    private BasicLogisticsmgtTransportTypeMapper typeMapper;

}
