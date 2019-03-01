package com.dt.user.service.impl;

import com.dt.user.mapper.BasePublicMapper.BasicPublicUnitMapper;
import com.dt.user.service.BasePublicService.BasicPublicMeasurementUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicPublicMeasurementUnitImpl implements BasicPublicMeasurementUnitService {
    @Autowired
    private BasicPublicUnitMapper unitMapper;

}
