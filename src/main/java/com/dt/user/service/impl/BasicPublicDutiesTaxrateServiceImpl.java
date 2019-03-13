package com.dt.user.service.impl;

import com.dt.user.dto.TaxrateDto;
import com.dt.user.mapper.BasePublicMapper.BasicPublicDutiesTaxrateMapper;
import com.dt.user.service.BasePublicService.BasicPublicDutiesTaxrateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName BasicPublicDutiesTaxrateServiceImpl
 * Description TODO
 * @Author 陈恩惠
 * @Date 2019/3/13 15:01
 **/
@Service
public class BasicPublicDutiesTaxrateServiceImpl implements BasicPublicDutiesTaxrateService {
    @Autowired
    private BasicPublicDutiesTaxrateMapper taxrateMapper;

    @Override
    public List<TaxrateDto> findByListTaxrate(TaxrateDto taxrateDto) {
        return taxrateMapper.findByListTaxrate(taxrateDto);
    }
}
