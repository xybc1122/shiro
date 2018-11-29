package com.dt.user.provider;


import com.dt.user.dto.UserDto;
import org.apache.ibatis.jdbc.SQL;

public class RoleProvider {

    public String findByRoleInfo(UserDto userDto) {
        return new SQL() {{

            SELECT("r.rid,r.r_name,GROUP_CONCAT(DISTINCT u.user_name)AS userName FROM user_info AS u");
            INNER_JOIN("user_role AS ur ON ur.`u_id`=u.`uid`");
            LEFT_OUTER_JOIN("role AS r ON ur.`r_id`=r.`rid`");
            LEFT_OUTER_JOIN("role_menu AS rm ON rm.`r_id`=r.`rid`");
            LEFT_OUTER_JOIN("menu AS m ON m.`menu_id`=rm.`m_id`");
            GROUP_BY("r.rid");
        }}.toString();
    }
}
