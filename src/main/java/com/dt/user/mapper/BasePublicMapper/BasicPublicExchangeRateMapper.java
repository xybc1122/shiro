package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.dto.ExchangeRateDto;
import com.dt.user.provider.BasicPublicExchangeRateProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface BasicPublicExchangeRateMapper {


    //查询汇率信息
    @SelectProvider(type = BasicPublicExchangeRateProvider.class, method = "findRate")
    @Results({
            //数据库字段映射 //数据库字段映射 column数据库字段 property Java 字段
            @Result(column = "status_id", property = "systemLogStatus",
                    one = @One(
                            select = "com.dt.user.mapper.SystemLogStatusMapper.findSysStatusInfo",
                            fetchType = FetchType.EAGER
                    )
            )
    })
    List<ExchangeRateDto> getRateInfo(ExchangeRateDto rateDto);
}
