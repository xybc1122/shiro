package com.dt.user.service.BasePublicService;

import com.dt.user.model.BasePublicModel.BasicSalesAmazonWarehouse;
import org.apache.ibatis.annotations.Param;

public interface BasicSalesAmazonWarehouseService {

    /**
     * 通过fc 查找站点ID 仓库id
     *
     * @return
     */
    BasicSalesAmazonWarehouse getWarehouse(@Param("fc") String fc);

}
