package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.Staff;
import com.dt.user.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

    /**
     * 获取员工信息 还没被注册的
     * @return
     */
    @GetMapping("/getStaff")
    public ResponseBase getStaff() {
        List<Staff> staffList = staffService.GetStaffList();

        return BaseApiService.setResultSuccess(staffList);
    }
}
