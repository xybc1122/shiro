package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.dto.ProductDto;
import com.dt.user.provider.BasicPublicProductProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface BasicPublicProductMapper {
    /**
     * 查询产品物料信息
     */
    @SelectProvider(type = BasicPublicProductProvider.class, method = "findProduct")
    @Results({
            //数据库字段映射 //数据库字段映射 column数据库字段 property Java 字段
            @Result(column = "status_id", property = "systemLogStatus",
                    one = @One(
                            select = "com.dt.user.mapper.SystemLogStatusMapper.findSysStatusInfo",
                            fetchType = FetchType.EAGER
                    )
            )
    })
    List<ProductDto> findProductInfo(ProductDto productDto);

}
