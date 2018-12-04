package com.dt.user.service;

import com.dt.user.model.TbHeadMenu;

public interface HeadMenuMapperService {

    /**
     * 新增菜单关联数据
     */
    int addHeadMenu(TbHeadMenu tbHeadMenu);

    /**
     * 删除菜单关联
     */
    int delHeadMenu(TbHeadMenu tbHeadMenu);
}
