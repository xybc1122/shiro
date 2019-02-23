package com.dt.user.service.impl;

import com.dt.user.mapper.BasePublicMapper.BasicLogisticsmgtTransportPropertyMapper;
import com.dt.user.service.BasePublicService.BasicLogisticsmgtTransportPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicLogisticsmgtTransportPropertyServiceImpl implements BasicLogisticsmgtTransportPropertyService {

    @Autowired
    private BasicLogisticsmgtTransportPropertyMapper propertyMapper;

}
