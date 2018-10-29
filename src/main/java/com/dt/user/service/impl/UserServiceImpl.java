package com.dt.user.service.impl;


import com.dt.user.mapper.UserMapper;
import com.dt.user.model.UserInfo;
import com.dt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserInfo findByUser(String userName) {
        return userMapper.findByUser(userName);
    }
}
