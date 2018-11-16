package com.dt.user.service.impl;


import com.dt.user.dto.UserDto;
import com.dt.user.mapper.UserMapper;
import com.dt.user.model.UserInfo;
import com.dt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserInfo findByUser(String userName) {
        return userMapper.findByUser(userName);
    }

    @Override
    public List<UserInfo> findByUsers(UserDto userDto) {

        return userMapper.findByUsers(userDto);
    }

    @Override
    public int upUserLandingTime(UserInfo userInfo) {
        return userMapper.upUserLandingTime(userInfo);
    }

    @Override
    public UserInfo getSingleUser(Long id) {
        return userMapper.getSingleUser(id);
    }

    @Override
    public int upUser(Map<String, Object> mapUser) {
        return userMapper.upUser(mapUser);
    }

    @Override
    public int upStaff(Map<String, Object> mapStaff) {
        return userMapper.upStaff(mapStaff);
    }


    @Override
    public List<UserInfo> findByRoleInfo(UserDto userDto) {

        return userMapper.findByRoleInfo(userDto);
    }
}
