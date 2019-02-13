package com.dt.user.controller.UserServiceController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.HrArchivesEmployee;
import com.dt.user.service.HrArchivesEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class HrArchivesController {
    @Autowired
    private HrArchivesEmployeeService hrService;

    /**
     * 获取员工信息 还没被注册的
     *
     * @return
     */
    @GetMapping("/getStaff")
    public ResponseBase getStaff() {
        List<HrArchivesEmployee> staffList = hrService.getHrList();
        return BaseApiService.setResultSuccess(staffList);
    }
}
