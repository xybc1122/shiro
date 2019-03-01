package com.dt.user.service.BasePublicService;

import com.dt.user.dto.CountryDto;

import java.util.List;

public interface BasicPublicCountryService {


    /**
     * 查询获得国家信息
     */
    List<CountryDto> findByCountry(CountryDto country);
}
