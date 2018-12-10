package com.dt.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermsMapper {

    /**
     * 查询获得用户权限列表
     * @return
     */
    @Select("SELECT GROUP_CONCAT(DISTINCT p.perms) AS perms,GROUP_CONCAT(DISTINCT `p_name`)AS pName FROM `system_user_perms` AS p \n" +
            "LEFT JOIN `system_user_perms_role` AS pr ON p.`p_id`=pr.`p_id`\n" +
            "LEFT JOIN system_user_role_user AS ur ON pr.`r_id`=ur.`r_id`\n" +
            "WHERE ur.`u_id`=#{uid}")
    List<String> findByPerms(@Param("uid") Long uid);
}
