package com.dt.user.service;


import com.dt.user.dto.UserDto;
import com.dt.user.model.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserService {
    //登陆查询用户
    UserInfo findByUser(String userName);


    //查询账号管理信息
    List<UserInfo> findByUsers(UserDto userDto);

    //更新登陆时间
    int upUserLandingTime(UserInfo userInfo);


    /**
     * 通过 id查询 用户
     */
    UserInfo getSingleUser(Long id);

    /**
     * 更新用户信息
     * @param mapUser
     * @return
     */

    int upUser(Map<String, Object> mapUser);

    /**
     * 更新员工表信息
     */
    int upStaff(Map<String, Object> mapStaff);
}
