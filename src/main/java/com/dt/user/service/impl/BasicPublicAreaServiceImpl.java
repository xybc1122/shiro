package com.dt.user.service.impl;

import com.dt.user.dto.AreaDto;
import com.dt.user.mapper.BasePublicMapper.BasicPublicAreaMapper;
import com.dt.user.model.BasePublicModel.BasicPublicArea;
import com.dt.user.service.BasePublicService.BasicPublicAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicPublicAreaServiceImpl implements BasicPublicAreaService {

    @Autowired
    private BasicPublicAreaMapper basicPublicAreaMapper;

    @Override
    public List<AreaDto> findByListArea() {
        return basicPublicAreaMapper.findByListArea();
    }
}
