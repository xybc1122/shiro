package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.dto.ExchangeRateDto;
import com.dt.user.model.BasePublicModel.BasicPublicExchangeRate;
import com.dt.user.provider.BasicPublicExchangeRateProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface BasicPublicExchangeRateMapper {


    //查询汇率信息
    @SelectProvider(type = BasicPublicExchangeRateProvider.class, method = "findRate")
    List<BasicPublicExchangeRate> getRateInfo(ExchangeRateDto rateDto);
}
