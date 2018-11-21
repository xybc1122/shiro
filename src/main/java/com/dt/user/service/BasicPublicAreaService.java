package com.dt.user.service;

import com.dt.user.model.BasicPublicArea;

import java.util.List;

public interface BasicPublicAreaService {

    /**
     * 查询区域所有相关信息
     * @return
     */
    List<BasicPublicArea> findByListArea();

}
