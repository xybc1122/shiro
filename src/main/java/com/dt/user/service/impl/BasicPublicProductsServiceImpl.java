package com.dt.user.service.impl;

import com.dt.user.mapper.BasePublicMapper.BasicPublicProductsMapper;
import com.dt.user.model.BasePublicModel.BasicPublicProducts;
import com.dt.user.service.BasePublicService.BasicPublicProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasicPublicProductsServiceImpl implements BasicPublicProductsService {
    @Autowired
    private BasicPublicProductsMapper productsMapper;

    @Override
    public List<BasicPublicProducts> findByProductsInfo() {
        //一级目录
        List<BasicPublicProducts> productsList = new ArrayList<>();
        //子目录
        List<BasicPublicProducts> childProductsList = new ArrayList<>();
        List<BasicPublicProducts> products = productsMapper.findByProductsInfo();
        if (products != null && products.size() > 0) {
            for (int i = 0; i < products.size(); i++) {
                BasicPublicProducts publicProducts = products.get(i);
                //如果是父目录
                if (publicProducts.getIsParent() != null) {
                    if (publicProducts.getIsParent() == 1) {
                        productsList.add(publicProducts);
                    } else {
                        childProductsList.add(publicProducts);
                    }
                }
            }
            // 为一级目录设置子目录 getChild是递归调用的
            if (productsList.size() > 0) {
                for (BasicPublicProducts pro : productsList) {
                    pro.setChildPros(getChild(pro.getProductsId(), childProductsList));
                }
            }
        }
        return productsList;
    }
    private List<BasicPublicProducts> getChild(Long productsId, List<BasicPublicProducts> childProductsList) {
        // 子菜单
        List<BasicPublicProducts> childList = new ArrayList<>();
        // 遍历childList，找出所有的根节点和非根节点
        if (childProductsList != null && childProductsList.size() > 0) {
            for (BasicPublicProducts products : childProductsList) {
                //如果子菜单跟父菜单ID相同 就设置进去
                if (productsId.equals(products.getParentProductsId())) {
                    childList.add(products);
                }
            }
        }
        //查询子节点
        if (childList.size() > 0) {
            for (BasicPublicProducts pro : childList) {
                pro.setChildPros(getChild(pro.getProductsId(), childProductsList));
            }
        }
        return childList;
    }
}
