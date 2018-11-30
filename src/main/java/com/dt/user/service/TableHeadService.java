package com.dt.user.service;

import com.dt.user.model.TableHead;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TableHeadService {

    List<TableHead> findByMenuIdHeadList(Long id,Long uid);

    /**
     * 根据菜单id查询对应显示的表头
     */
    TableHead getTableHeadList(@Param("mid") Long mid);
}
