package com.dt.user.service;




import com.dt.user.dto.UserDto;
import com.dt.user.model.Role;
import com.dt.user.model.UserInfo;

import java.util.List;
import java.util.Set;

public interface RoleService {
    //查询角色信息
    Set<String> getAllRolesByUid(Long uid);

    List<Role> getRoleList();

    /**
     * 查询一个角色下的所有用户跟 菜单
     * @param pageDto
     * @return
     */
    List<UserInfo> findByRoleInfo(UserDto pageDto);


    /**
     * 通过角色名字去查询数据库是否有重复
     */
    String findByRoleName(String rName);
}
