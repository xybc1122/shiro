package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.dto.CountryDto;
import com.dt.user.provider.BasicPublicCountryProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface BasicPublicCountryMapper {

    /**
     * 查询获得国家信息
     */
    @SelectProvider(type = BasicPublicCountryProvider.class, method = "findCountry")
    @Results({
            @Result(column = "status_id", property = "systemLogStatus",
                    one = @One(
                            select = "com.dt.user.mapper.SystemLogStatusMapper.findSysStatusInfo",
                            fetchType = FetchType.EAGER
                    )
            )
    })
    List<CountryDto> findByCountry(CountryDto country);




}
