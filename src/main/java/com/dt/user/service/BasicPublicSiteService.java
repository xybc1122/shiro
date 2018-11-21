package com.dt.user.service;

import com.dt.user.model.BasicPublicSite;

import java.util.List;

public interface BasicPublicSiteService {


    /**
     * 查询所有站点信息
     * @return
     */
    List<BasicPublicSite> findBySiteList();

}
