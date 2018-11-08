package com.dt.user.provider;


import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;
import org.apache.ibatis.jdbc.SQL;

/**
 * video类构建动态sql语句
 */
public class MenuProvider {

    public String findQueryMenuList(final UserInfo user) {
        return new SQL() {{
            //代表超级管理员
            if (user.getStatus() == 1) {
                SELECT("  menu_id,`name`,parent_id,url,icon,menu_order");
                FROM("menu");
                ORDER_BY("menu_order asc");
            }
            //代表用户
            if (user.getStatus() == 0) {
                SELECT("m.* FROM user_info AS u");
                INNER_JOIN("user_role AS ur ON u.uid=ur.u_id");
                INNER_JOIN("role AS r  ON r.rid=ur.r_id");
                INNER_JOIN("role_menu AS rm ON r.rid=rm.r_id");
                INNER_JOIN("menu AS m  ON m.menu_id=rm.m_id");
                WHERE("u.uid=" + user.getUid());
                GROUP_BY("m.menu_id");
                ORDER_BY("m.`menu_order` asc");
            }
        }}.toString();
    }

    public String addMenu(final Menu menu) {
        return null;
    }
}
