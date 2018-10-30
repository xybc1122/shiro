package com.dt.user.mapper;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface RolesMapper {
    //查询角色表的role_sign  角色类型
    @Select("select role_sign from role where rid in(select r_id from user_role where u_id=#{uid})")
    Set<String> getAllRolesByUid(@Param("uid") Long uid);
}
