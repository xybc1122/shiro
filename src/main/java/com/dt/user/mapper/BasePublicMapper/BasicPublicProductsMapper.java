package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.model.BasePublicModel.BasicPublicProducts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicPublicProductsMapper {

    /**
     * 查询类目信息
     * @return
     */
    @Select("SELECT `products_id`,`products_number`,`products_name`,`Parent_products_id`,\n" +
            "`products_path`,`is_parent`,`remark`,`status`,`create_date`,`create_id_user`,\n" +
            "`modify_date`,`modify_id_user`,`audit_date`,`audit_id_user`\n" +
            "FROM `basic_public_products`")
    List<BasicPublicProducts> findByProductsInfo();
}
