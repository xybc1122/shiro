package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.model.BasePublicModel.BasicPublicProducts;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import java.util.List;
@Mapper
public interface BasicPublicProductsMapper{

    /**
     * 查询类目信息
     *
     * @return
     */
    @Select("SELECT `products_id`,`number`,`products_name`,`parent_products_id`,\n" +
            "`products_path`,`is_parent`,status_id \n" +
            "FROM `basic_public_products`")
    @Results({
            //数据库字段映射 //数据库字段映射 column数据库字段 property Java 字段
            @Result(column = "status_id", property = "systemLogStatus",
                    one = @One(
                            select = "com.dt.user.mapper.SystemLogStatusMapper.findSysStatusInfo",
                            fetchType = FetchType.EAGER
                    )
            )
    })
    List<BasicPublicProducts> findByProductsInfo();
}
