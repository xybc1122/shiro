package com.dt.user.service.impl;

import com.dt.user.dto.CountryDto;
import com.dt.user.dto.ProductDto;
import com.dt.user.mapper.BasePublicMapper.BasicPublicProductMapper;
import com.dt.user.service.BasePublicService.BasicPublicProductService;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicPublicProductServiceImpl implements BasicPublicProductService {
    @Autowired
    private BasicPublicProductMapper productMapper;

    @Override
    public List<ProductDto> findProductInfo(ProductDto productDto) {
        return productMapper.findProductInfo(productDto);
    }
}
