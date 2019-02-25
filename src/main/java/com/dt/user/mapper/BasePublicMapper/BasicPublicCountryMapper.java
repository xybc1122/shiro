package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.dto.CountryDto;
import com.dt.user.provider.BasicPublicCountryProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface BasicPublicCountryMapper {

    /**
     * 查询获得国家信息
     */
    @SelectProvider(type = BasicPublicCountryProvider.class, method = "findCountry")
    List<CountryDto> findByUsers(CountryDto country);




}
