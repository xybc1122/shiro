package com.dt.user.service.impl;

import com.dt.user.mapper.BasePublicMapper.BasicPublicWarehouseMapper;
import com.dt.user.model.BasePublicModel.BasicPublicWarehouse;
import com.dt.user.service.BasePublicService.BasicPublicWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasicPublicWarehouseServiceImpl implements BasicPublicWarehouseService {
    @Autowired
    private BasicPublicWarehouseMapper warehouseMapper;

    @Override
    public List<BasicPublicWarehouse> findByWarehouseInfo() {
        //一级目录
        List<BasicPublicWarehouse> warehouseList = new ArrayList<>();
        //子目录
        List<BasicPublicWarehouse> childWarehouseList = new ArrayList<>();
        List<BasicPublicWarehouse> warehouses = warehouseMapper.findByWarehouseInfo();
        if (warehouses != null && warehouses.size() > 0) {
            for (int i = 0; i < warehouses.size(); i++) {
                BasicPublicWarehouse warehouse = warehouses.get(i);
                //如果是父目录
                if (warehouse.getIsParent() != null) {
                    if (warehouse.getIsParent() == 1) {
                        warehouseList.add(warehouse);
                    } else {
                        childWarehouseList.add(warehouse);
                    }
                }
            }
            // 为一级目录设置子目录 getChild是递归调用的
            if (warehouseList.size() > 0) {
                for (BasicPublicWarehouse war : warehouseList) {
                    war.setChildWarehouse(getChild(war.getParentWarehouseId(), childWarehouseList));
                }
            }
        }
        return warehouseList;
    }

    private List<BasicPublicWarehouse> getChild(Integer parentWarehouseId, List<BasicPublicWarehouse> childWarehouseList) {
        // 子菜单
        List<BasicPublicWarehouse> childList = new ArrayList<>();
        // 遍历childList，找出所有的根节点和非根节点
        if (childWarehouseList != null && childWarehouseList.size() > 0) {
            for (BasicPublicWarehouse warehouse : childWarehouseList) {
                //如果子菜单跟父菜单ID相同 就设置进去
                if (parentWarehouseId.equals(warehouse.getParentWarehouseId())) {
                    childList.add(warehouse);
                }
            }
        }
        //查询子节点
        if (childList.size() > 0) {
            for (BasicPublicWarehouse war : childList) {
                war.setChildWarehouse(getChild(war.getWarehouseId(), childWarehouseList));
            }
        }
        return childList;
    }
}
