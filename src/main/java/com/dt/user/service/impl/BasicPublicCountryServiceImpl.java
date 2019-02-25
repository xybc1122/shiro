package com.dt.user.service.impl;

import com.dt.user.dto.CountryDto;
import com.dt.user.mapper.BasePublicMapper.BasicPublicCountryMapper;
import com.dt.user.service.BasePublicService.BasicPublicCountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicPublicCountryServiceImpl implements BasicPublicCountryService {
    @Autowired
    private BasicPublicCountryMapper countryMapper;

    @Override
    public List<CountryDto> findByUsers(CountryDto country) {
        return countryMapper.findByUsers(country);
    }
}
