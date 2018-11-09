package com.dt.user.service;


import com.dt.user.dto.UserDto;
import com.dt.user.model.UserInfo;

import java.util.List;

public interface UserService {
    //登陆查询用户
    UserInfo findByUser(String userName);


    //查询账号管理信息
    List<UserInfo> findByUsers(UserDto userDto);

}
