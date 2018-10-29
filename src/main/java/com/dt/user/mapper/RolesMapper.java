package com.dt.user.mapper;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface RolesMapper {

    @Select("select role_sign from role where rid in(select rid from user_role where u_id=#{uid})")
    @Results({
            @Result(id = true, column = "rid", property = "rid"),
            @Result(column = "r_name", property = "rName"),
            @Result(column = "role_sign", property = "roleSign"),
    })
    Set<String> getAllRolesByUid(@Param("uid") Long uid);
}
