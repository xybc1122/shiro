package com.dt.user.mapper;

import com.dt.user.model.UserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserRoleMapper {
    /**
     * 设置角色信息
     *
     * @param userRole
     * @return
     */
    @Insert("insert into user_role(u_id,r_id) values(#{uId}, #{rId})")
    int addUserRole(UserRole userRole);
}
