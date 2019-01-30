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
     * 通过一个用户ID查询所有角色id
     *
     * @return
     */
    @Select("SELECT `rid` FROM `system_user_role` AS r\n" +
            "LEFT JOIN `system_user_role_user` AS ur ON ur.`r_id`=r.`rid`\n" +
            "WHERE ur.`u_id`= #{uId}")
    List<Long> getRoleIdList(@Param("uId") Long uId);
}
