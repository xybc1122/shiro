package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.TableHead;
import com.dt.user.service.TableHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TableHeadController {
    @Autowired
    private TableHeadService tableHeadService;

    @GetMapping("/head")
    public ResponseBase findByHead(@RequestParam("menu_id") Long id) {
        List<TableHead> headList = tableHeadService.findByMenuIdHeadList(id);
        return BaseApiService.setResultSuccess(headList);
    }
}
