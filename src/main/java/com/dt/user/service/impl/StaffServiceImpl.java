package com.dt.user.service.impl;

import com.dt.user.mapper.StaffMapper;
import com.dt.user.model.Staff;
import com.dt.user.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;

    @Override
    public List<Staff> GetStaffList() {
        return staffMapper.GetStaffList();
    }

    @Override
    public int upStaffInfo(Long uid, Long sid) {
        return staffMapper.upStaffInfo(uid, sid);
    }
}
