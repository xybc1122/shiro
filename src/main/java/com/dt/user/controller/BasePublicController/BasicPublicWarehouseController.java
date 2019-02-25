package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.UserDto;
import com.dt.user.model.BasePublicModel.BasicPublicWarehouse;
import com.dt.user.service.BasePublicService.BasicPublicWarehouseService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/war")
public class BasicPublicWarehouseController {
    @Autowired
    private BasicPublicWarehouseService warehouseService;

    @PostMapping("/findByListWar")
    public ResponseBase findByListWar(@RequestBody UserDto pageDto) {
        if (pageDto.getCurrentPage() != null && pageDto.getPageSize() != null) {
            PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
            List<BasicPublicWarehouse> basicPublicSiteList = warehouseService.findByWarehouseInfo();
            PageInfo<BasicPublicWarehouse> pageInfo = new PageInfo<>(basicPublicSiteList);
            Integer currentPage = pageDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }
}
