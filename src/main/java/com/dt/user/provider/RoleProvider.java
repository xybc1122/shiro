package com.dt.user.provider;


import com.dt.user.dto.PageDto;
import org.apache.ibatis.jdbc.SQL;

public class RoleProvider {

    public String findByRoleInfo(PageDto pageDto) {
        return new SQL() {{
            SELECT("r.rid,r.r_name,GROUP_CONCAT(DISTINCT u.user_name)AS userName,GROUP_CONCAT(DISTINCT u.uid)AS uIds FROM system_user_info AS u");
            INNER_JOIN("system_user_role_user AS ur ON ur.`u_id`=u.`uid`");
            LEFT_OUTER_JOIN("system_user_role AS r ON ur.`r_id`=r.`rid`");
            LEFT_OUTER_JOIN("system_user_role_menu AS rm ON rm.`r_id`=r.`rid`");
            LEFT_OUTER_JOIN("system_user_menu AS m ON m.`menu_id`=rm.`m_id`");
            GROUP_BY("r.rid");
        }}.toString();
    }
}
