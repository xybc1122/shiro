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
    List<UserInfo> findByUsers(UserDto pageDto);

    //更新登陆时间
    int upUserLandingTime(UserInfo userInfo);


    /**
     * 通过 id查询 用户
     */
    UserInfo getSingleUser(Long id);

    /**
     * 更新用户信息
     *
     * @return
     */

    int upUser(Map<String, Object> userMap);

    /**
     * 单个删除或批量删除用户信息
     */
    int delUserInfo(String uidIds);

    /**
     * 单个恢复或批量恢复用户信息
     */
    int reUserInfo(@Param("uidIds") String uidIds);

    /**
     * 查询被删除的用户信息
     */
    List<UserInfo> findByDelUserInfo();

    /**
     * 注册用户验证用户是否存在
     */

    UserInfo getUserName(String userName);

    /**
     * 新增一个用户
     */
    int saveUserInfo(UserInfo userInfo);

    /**
     * 查找所有用户信息
     */
    List<UserInfo> getByUsers();

    /**
     * 首次登陆 用户密码更新
     * @return
     */
    int upUserPwd(@Param("uid") Long uid, @Param("pwd") String pwd);

}
