package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.model.BasePublicModel.BasicPublicWarehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicPublicWarehouseMapper {


    /**
     * 查询仓库信息
     */
    @Select("SELECT`warehouse_id`,`warehouse_number`,`warehouse_name`,`warehouse_address`,\n" +
            "`principal`,`remark`, `status`,`create_date`,`create_id_user`,\n" +
            "`modify_date`,`modify_id_user`, `audit_date`,`audit_id_user`\n" +
            "FROM`basic_public_warehouse`")
    List<BasicPublicWarehouse> findByWarehouseInfo();
}
