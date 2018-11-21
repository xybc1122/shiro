package com.dt.user.service.impl;

import com.dt.user.mapper.BasicPublicSiteMapper;
import com.dt.user.model.BasicPublicSite;
import com.dt.user.service.BasicPublicSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicPublicSiteServiceImpl implements BasicPublicSiteService {

    @Autowired
    private BasicPublicSiteMapper basicPublicSiteMapper;

    @Override
    public List<BasicPublicSite> findBySiteList() {
        return basicPublicSiteMapper.findBySiteList();
    }
}
