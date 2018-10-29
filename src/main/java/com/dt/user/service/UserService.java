package com.dt.user.service;


import com.dt.user.model.UserInfo;

public interface UserService {
    //登陆查询用户
    UserInfo findByUser(String userName);

}
