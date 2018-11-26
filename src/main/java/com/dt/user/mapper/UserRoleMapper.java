package com.dt.user.mapper;

import com.dt.user.model.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 移除角色
     */
    @Delete("DELETE FROM `user_role`WHERE r_id = #{rid} and u_id= #{uid}")
    int delUserRole(@Param("rid") Long rid, @Param("uid") Long uid);
}
