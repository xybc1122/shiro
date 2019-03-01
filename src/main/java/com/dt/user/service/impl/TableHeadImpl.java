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
        List<TableHead> head = tableHeadMapper.findByHeader(menuId);
        for (TableHead h : head) {
            int orderIndex = h.getTopOrder().indexOf(",");
            int menuIdIndex = h.getMenuId().indexOf(",");
            //如果有,号的说明是有顺序的 并且两个都要不等于- 1的
            if (orderIndex != -1 && menuIdIndex != -1) {
                String[] strOrder = h.getTopOrder().split(",");
                String[] strMenuId = h.getMenuId().split(",");
                //判断下两个长度是否一样 进入
                if (strOrder.length == strMenuId.length) {
                    for (int i = 0; i < strOrder.length; i++) {
                        Long mId = Long.parseLong(strMenuId[i]);
                        if (mId.equals(menuId)) {
                            h.setTopOrder(strOrder[i]);
                            break;
                        }
                    }
                }
            }
            h.setMenuId(null);
        }
        //接收数据库传来的对象
        return head;
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
        if (menuId == null) {
            return tableHeads;
        }
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
