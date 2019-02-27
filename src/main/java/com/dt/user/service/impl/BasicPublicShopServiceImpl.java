package com.dt.user.service.impl;

import com.dt.user.dto.ShopDto;
import com.dt.user.mapper.BasePublicMapper.BasicPublicShopMapper;
import com.dt.user.model.BasePublicModel.BasicPublicShop;
import com.dt.user.service.BasePublicService.BasicPublicShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicPublicShopServiceImpl implements BasicPublicShopService {
    @Autowired
    private BasicPublicShopMapper basicPublicShopMapper;

    @Override
    public List<ShopDto> findByListShop() {

        return basicPublicShopMapper.findByListShop();
    }

    @Override
    public List<BasicPublicShop> getByListShopName() {
        return basicPublicShopMapper.getByListShopName();
    }
}
