package com.dt.user.mapper;
import com.dt.user.dto.UserDto;
import com.dt.user.model.Role;
import com.dt.user.model.UserInfo;
import com.dt.user.provider.RoleProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

@Mapper
public interface RoleMapper {
    //查询角色表的role_sign  角色类型
    @Select("select rid, role_sign from role where rid in(select r_id from user_role where u_id=#{uid})")
    Set<String> getAllRolesByUid(@Param("uid") Long uid);

    /**
     * 查询所有的角色
     * @return
     */
    @Select("SELECT `rid`,`r_name`,`role_sign`FROM `role`")
    List<Role> getRoleList();


    /**
     * 查询一个角色下的所有用户跟 菜单
     *
     * @param userDto
     * @return
     */
    @SelectProvider(type = RoleProvider.class, method = "findByRoleInfo")
    List<UserInfo> findByRoleInfo(UserDto userDto);
}
