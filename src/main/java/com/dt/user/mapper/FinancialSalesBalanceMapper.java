package com.dt.user.mapper;

import com.dt.user.model.FinancialSalesBalance;
import com.dt.user.provider.FinancialSalesBalanceProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FinancialSalesBalanceMapper {
    /**
     * 插入德国数据
     *
     * @return
     */
    @InsertProvider(type = FinancialSalesBalanceProvider.class, method = "addInfoGerman")
    int addInfoGerman(int size, FinancialSalesBalance fb);
}
