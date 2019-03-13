package com.dt.user.service.BasePublicService;
import com.dt.user.model.BasePublicModel.BasicPublicWarehouse;

import java.util.List;

public interface BasicPublicWarehouseService {

    /**
     * 查询仓库信息
     */
    List<BasicPublicWarehouse> findByWarehouseInfo();

}
