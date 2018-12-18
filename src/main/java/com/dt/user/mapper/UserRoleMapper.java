package com.dt.user.mapper;

import com.dt.user.model.UserRole;
import com.dt.user.provider.UserRoleProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    /**
     * 设置角色信息
     *
     * @return
     */
    @InsertProvider(type = UserRoleProvider.class, method = "addUserRole")
    int addUserRole(@Param("userRoleList") List<UserRole> userRoleList);


    /**
     * 移除角色
     */
    @Delete("DELETE FROM `system_user_role_user`WHERE r_id = #{rid} and u_id= #{uid}")
    int delUserRole(@Param("rid") Long rid, @Param("uid") Long uid);
}
