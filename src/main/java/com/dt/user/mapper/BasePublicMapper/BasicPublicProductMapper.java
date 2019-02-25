package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.dto.CountryDto;
import com.dt.user.provider.BasicPublicProductProvider;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface BasicPublicProductMapper {
    /**
     * 查询产品物料信息
     */
    @SelectProvider(type = BasicPublicProductProvider.class, method = "findProduct")
    List<CountryDto> findProductInfo(Product product);

}
