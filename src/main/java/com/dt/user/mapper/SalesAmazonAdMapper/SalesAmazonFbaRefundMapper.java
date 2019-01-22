package com.dt.user.mapper.SalesAmazonAdMapper;

import com.dt.user.model.SalesAmazonAd.SalesAmazonFbaRefund;
import com.dt.user.provider.SalesAmazonFbaRefundProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface SalesAmazonFbaRefundMapper {



    /**
     * 存入退货数据
     *
     * @return
     */
    @InsertProvider(type = SalesAmazonFbaRefundProvider.class, method = "addRefund")
    int AddSalesAmazonAdRefundList(@Param("refundList") List<SalesAmazonFbaRefund> refundList);
}
