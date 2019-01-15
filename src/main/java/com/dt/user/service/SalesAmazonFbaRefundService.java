package com.dt.user.service;
import com.dt.user.model.SalesAmazonFbaRefund;
import java.util.List;

public interface SalesAmazonFbaRefundService {



    /**
     * 存入退货数据
     *
     * @return
     */
    int AddSalesAmazonAdRefundList(List<SalesAmazonFbaRefund> refundList);
}
