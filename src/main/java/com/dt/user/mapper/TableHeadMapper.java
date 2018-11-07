package com.dt.user.mapper;

import com.dt.user.model.TableHead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface TableHeadMapper {

    /**
     * 查询所有表头
     * @return
     */
    @Select("select id,head_name,menu_id from `table_head`")
    List<TableHead> findByHeader();
}
