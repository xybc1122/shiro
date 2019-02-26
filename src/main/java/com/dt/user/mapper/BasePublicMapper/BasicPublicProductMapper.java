package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.dto.ProductDto;
import com.dt.user.provider.BasicPublicProductProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface BasicPublicProductMapper {
    /**
     * 查询产品物料信息
     */
    @SelectProvider(type = BasicPublicProductProvider.class, method = "findProduct")
    List<ProductDto> findProductInfo(ProductDto productDto);

}
