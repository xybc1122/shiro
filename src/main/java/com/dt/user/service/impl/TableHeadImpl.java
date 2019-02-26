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
    public List<TableHead> findByMenuIdHeadList(Long menuId) {
        //接收数据库传来的对象

        return tableHeadMapper.findByHeader(menuId);
    }

    @Override
    public List<TableHead> getTableHeadList(Map<String, Object> mapHead) {
        return tableHeadMapper.getTableHeadList(mapHead);
    }

    @Override
    public TableHead getTableHead(Long mid) {
        return tableHeadMapper.getTableHead(mid);
    }

    @Override
    public List<TableHead> findByHeadList(Long menuId) {
        List<TableHead> tableHeads = tableHeadMapper.findByHeadList();
        return headNewList(tableHeads, menuId);

    }

    /**
     * 遍历查找对应的menuId
     *
     * @param headList
     * @param mId
     * @return
     */
    public List<TableHead> headNewList(List<TableHead> headList, Long mId) {
        //创建一个新的数组
        List<TableHead> headNew = new ArrayList<>();
        for (TableHead head : headList) {
            //判断不为空
            if (StringUtils.isNotBlank(head.getMenuId())) {
                //切割变成数组
                String[] menu_id = head.getMenuId().trim().split(",");
                for (int i = 0; i < menu_id.length; i++) {
                    if (StringUtils.isNotBlank(menu_id[i])) {
                        int menuId = Integer.parseInt(menu_id[i]);
                        if (mId == menuId) {
                            headNew.add(head);
                        }
                    }
                }
            }
        }
        return headNew;
    }
}
