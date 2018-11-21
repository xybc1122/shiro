package com.dt.user.service.impl;

import com.dt.user.mapper.BasicPublicShopMapper;
import com.dt.user.model.BasicPublicShop;
import com.dt.user.service.BasicPublicShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicPublicShopServiceImpl implements BasicPublicShopService {
    @Autowired
    private BasicPublicShopMapper basicPublicShopMapper;

    @Override
    public List<BasicPublicShop> findByListShop() {

        return basicPublicShopMapper.findByListShop();
    }
}
