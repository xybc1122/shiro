package com.dt.user.service.BasePublicService;

import com.dt.user.dto.ProductDto;

import java.util.List;

public interface BasicPublicProductService {


    /**
     * 查询产品物料信息
     */
    List<ProductDto> findProductInfo(ProductDto productDto);
}
