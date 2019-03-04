package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.SystemMenuIcon;
import com.dt.user.service.SystemMenuIconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName SystemMenuIconController
 * Description TODO
 * @Author 陈恩惠
 * @Date 2019/3/4 14:36
 **/
@RestController
@RequestMapping("/icon")
public class SystemMenuIconController {
    @Autowired
    private SystemMenuIconService iconService;

    /**
     * 获得所有图标列表
     *
     * @return
     */
    @GetMapping("/getIconInfo")
    public ResponseBase getIconInfo() {
        List<SystemMenuIcon> iconList = iconService.getIconList();
        return BaseApiService.setResultSuccess(iconList);
    }
}
