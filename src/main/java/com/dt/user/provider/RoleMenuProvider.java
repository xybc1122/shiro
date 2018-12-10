package com.dt.user.provider;

import com.dt.user.model.RoleMenu;
import org.apache.ibatis.jdbc.SQL;


public class RoleMenuProvider {


    public String delRoleMenu(RoleMenu roleMenu) {
        return new SQL() {{
            if (roleMenu.getmId() == null) {
                DELETE_FROM("`system_user_role_menu`");
                WHERE("r_id=" + roleMenu.getrId());
            } else {
                DELETE_FROM("`system_user_role_menu`");
                WHERE("r_id=" + roleMenu.getrId() + " and m_id=" + roleMenu.getmId());
            }
        }}.toString();
    }
}
