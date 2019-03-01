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
    public List<CountryDto> findByCountry(CountryDto country) {
        return countryMapper.findByCountry(country);
    }
}
