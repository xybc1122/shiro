package com.dt.user.service.impl;

import com.dt.user.mapper.HrArchivesEmployeeMapper;
import com.dt.user.model.HrArchivesEmployee;
import com.dt.user.service.HrArchivesEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HrArchivesEmployeeImpl implements HrArchivesEmployeeService {

    @Autowired
    private HrArchivesEmployeeMapper hrMapper;

    @Override
    public List<HrArchivesEmployee> getHrList() {
        return hrMapper.getHrList();
    }

    @Override
    public int bindHrInfo(Long uid, Long sid) {
        return hrMapper.bindHrInfo(uid, sid);
    }

    @Override
    public int upHrInfo(Map<String, Object> mapStaff) {
        return hrMapper.upHrInfo(mapStaff);
    }
}
