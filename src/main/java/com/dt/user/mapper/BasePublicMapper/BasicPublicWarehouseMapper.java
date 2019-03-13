package com.dt.user.mapper.BasePublicMapper;


import com.dt.user.model.BasePublicModel.BasicPublicWarehouse;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface BasicPublicWarehouseMapper {


    /**
     * 查询仓库信息
     */
    @Select("SELECT`warehouse_id`,`number`,`warehouse_name`,parent_warehouse_id,`warehouse_address`,\n" +
            "`principal`,is_parent,status_id\n" +
            "FROM`basic_public_warehouse`")
    @Results({
            //数据库字段映射 //数据库字段映射 column数据库字段 property Java 字段
            @Result(column = "status_id", property = "systemLogStatus",
                    one = @One(
                            select = "com.dt.user.mapper.SystemLogStatusMapper.findSysStatusInfo",
                            fetchType = FetchType.EAGER
                    )
            )
    })
    List<BasicPublicWarehouse> findByWarehouseInfo();
}
