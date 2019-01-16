package com.dt.user.service;

import com.dt.user.model.BasicSalesAmazonWarehouse;
import org.apache.ibatis.annotations.Param;

public interface BasicSalesAmazonWarehouseService {

    /**
     * 通过fc 查找站点ID 仓库id
     *
     * @return
     */
    BasicSalesAmazonWarehouse getWarehouse(@Param("fc") String fc);

}
