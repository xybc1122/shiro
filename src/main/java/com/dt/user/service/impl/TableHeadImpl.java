package com.dt.user.service.impl;

import com.dt.user.mapper.TableHeadMapper;
import com.dt.user.model.TableHead;
import com.dt.user.service.TableHeadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TableHeadImpl implements TableHeadService {

    @Autowired
    private TableHeadMapper tableHeadMapper;

    @Override
    public List<TableHead> findByMenuIdHeadList(Long id, Long uid) {
        //创建一个新的数组
        List<TableHead> headNew = new ArrayList<>();
        //接收数据库传来的对象
        List<TableHead> headList = tableHeadMapper.findByHeader(uid);
        for (TableHead head : headList) {
            //判断不为空
            if (StringUtils.isNotBlank(head.getMenuId())) {
                //切割变成数组
                String[] menu_id = head.getMenuId().trim().split(",");
                for (int i = 0; i < menu_id.length; i++) {
                    int menuId = Integer.parseInt(menu_id[i]);
                    if (menuId == id) {
                        headNew.add(head);
                    }
                }
            }
        }
        return headNew;
    }

    @Override
    public List<TableHead> getTableHeadList(Map<String, Object> mapHead) {
        return tableHeadMapper.getTableHeadList(mapHead);
    }

    @Override
    public List<TableHead> findByHeadList() {
        return tableHeadMapper.findByHeadList();
    }
}
