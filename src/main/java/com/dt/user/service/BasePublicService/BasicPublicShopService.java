package com.dt.user.service.BasePublicService;

import com.dt.user.model.BasePublicModel.BasicPublicShop;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BasicPublicShopService {


    /**
     * 查询店铺所有相关信息
     * @return
     */
    List<BasicPublicShop> findByListShop();

    /**
     * 查询店铺名字
     * @return
     */
    List<BasicPublicShop> getByListShopName();
}
