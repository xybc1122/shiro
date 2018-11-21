package com.dt.user.service.impl;

import com.dt.user.mapper.BasicPublicAreaMapper;
import com.dt.user.model.BasicPublicArea;
import com.dt.user.service.BasicPublicAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicPublicAreaServiceImpl implements BasicPublicAreaService {

    @Autowired
    private BasicPublicAreaMapper basicPublicAreaMapper;

    @Override
    public List<BasicPublicArea> findByListArea() {
        return basicPublicAreaMapper.findByListArea();
    }
}
