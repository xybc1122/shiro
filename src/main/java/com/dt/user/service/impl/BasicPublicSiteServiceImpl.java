package com.dt.user.service.impl;

import com.dt.user.dto.SiteDto;
import com.dt.user.mapper.BasePublicMapper.BasicPublicSiteMapper;
import com.dt.user.model.BasePublicModel.BasicPublicSite;
import com.dt.user.service.BasePublicService.BasicPublicSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicPublicSiteServiceImpl implements BasicPublicSiteService {

    @Autowired
    private BasicPublicSiteMapper basicPublicSiteMapper;

    @Override
    public List<SiteDto> findBySiteList(SiteDto siteDto) {
        return basicPublicSiteMapper.findBySiteList(siteDto);
    }

    @Override
    public List<BasicPublicSite> getShopIdTakeSiteList(Long sId) {
        return basicPublicSiteMapper.getShopIdTakeSiteList(sId);
    }

    @Override
    public Integer getSiteId(String url) {
        return basicPublicSiteMapper.getSiteId(url);
    }
}
