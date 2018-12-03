package com.dt.user.provider;

import org.apache.ibatis.jdbc.SQL;


public class RoleMenuProvider {


    public String delRoleMenu(Long rid, Long menuIds) {
        return new SQL() {{
            if (menuIds == null) {
                DELETE_FROM("`role_menu`");
                WHERE("r_id=" + rid);
            } else {
                DELETE_FROM("`role_menu`");
                WHERE("r_id=" + rid + " and m_id=" + menuIds);
            }
        }}.toString();
    }
}
