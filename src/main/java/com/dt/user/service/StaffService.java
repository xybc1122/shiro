package com.dt.user.service;

import com.dt.user.model.Staff;

import java.util.List;

public interface StaffService {


    /**
     * 获得员工信息 没有关联用户的
     */
    List<Staff> GetStaffList();
}
