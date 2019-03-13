package com.dt.user.provider;


import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;
import com.dt.user.utils.StrUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * video类构建动态sql语句
 */
public class MenuProvider {

    public String findQueryMenuList(final UserInfo user) {
        return new SQL() {{
            //代表超级管理员
            if (user.getStatus() == 1) {
                SELECT("menu_id,`name`,parent_id,url,icon,menu_order");
                FROM("system_user_menu");
                WHERE("type=" + user.getType());
                ORDER_BY("menu_order asc");
            }
            //代表用户
            else if (user.getStatus() == 0) {
                SELECT("m.menu_id,m.`name`,m.parent_id,m.url,m.icon,m.menu_order FROM system_user_info AS u");
                LEFT_OUTER_JOIN("system_user_role_user AS ur ON u.uid=ur.u_id");
                LEFT_OUTER_JOIN("system_user_role AS r ON r.rid=ur.r_id");
                LEFT_OUTER_JOIN("system_user_role_menu AS rm ON r.rid=rm.r_id");
                LEFT_OUTER_JOIN("system_user_menu AS m  ON m.menu_id=rm.m_id");
                WHERE("u.uid=" + user.getUid() + " and m.type=" + user.getType());
                GROUP_BY("m.menu_id");
                ORDER_BY("m.`menu_order` asc");
            }
        }}.toString();
    }

    @SuppressWarnings("unchecked")
    public String addMenu(final Map<String, Object> menuMap) {
        List<Menu> menus = (List<Menu>) menuMap.get("menuList");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `system_user_menu`\n" +
                "(`name`,`parent_id`,`url`,`icon`,`menu_order`)values");
        for (Menu m : menus) {
            sb.append("(");
            StrUtils.appBuider(sb, m.getName());
            sb.append(",");
            sb.append(m.getParentId() + ",");
            StrUtils.appBuider(sb, m.getUrl());
            sb.append(",");
            StrUtils.appBuider(sb, m.getIcon());
            sb.append(",");
            sb.append(m.getMenuOrder());
            sb.append("),");
        }
        String sql = sb.toString().substring(0, sb.length() - 1);
        return sql;
    }

    /**
     * 更新菜单信息
     *
     * @param menu
     * @return
     */
    public String upMenu(Menu menu) {
        return new SQL() {{
            UPDATE("`system_user_menu`");
            if (StringUtils.isNotBlank(menu.getName())) {
                SET("`name`=#{name}" + "'" + menu.getName() + "'");
            }
            if (menu.getParentId() != null) {
                SET("`parent_id`=#{parentId}");
            }
            if (StringUtils.isNotBlank(menu.getUrl())) {
                SET("`url`=#{url}");
            }
            if (StringUtils.isNotBlank(menu.getIcon())) {
                SET("`icon`=#{icon}");
            }
            if (menu.getMenuOrder() != null) {
                SET("`menu_order`=#{menuOrder}");
            }
            if (menu.getType() != null) {
                SET("`type`=#{type}");
            }
            WHERE("menu_id=#{menuId}");
        }}.toString();

    }

}
