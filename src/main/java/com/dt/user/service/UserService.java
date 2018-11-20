package com.dt.user.service;


import com.dt.user.dto.UserDto;
import com.dt.user.model.UserInfo;
import org.apache.ibatis.annotations.Select;

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


    /**
     * 查询一个角色下的所有用户跟 菜单
     * @param userDto
     * @return
     */
    List<UserInfo> findByRoleInfo(UserDto userDto);
    /**
     * 单个删除或批量删除用户信息
     */
    int delUserInfo(String uidIds);

    /**
     * 查询被删除的用户信息
     */
    List<UserInfo> findByDelUserInfo();
}
