package com.dt.user.provider;
import java.util.List;
import java.util.Map;

public class TableHeadProvider {

    @SuppressWarnings("unchecked")
    public String showTableHead(Map<String, Object> mapHead) {
        List<Integer>ids = (List<Integer>) mapHead.get("menuIds");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT GROUP_CONCAT(DISTINCT t.head_name)as headName," +
                "GROUP_CONCAT(DISTINCT t.id)as ids," +
                "GROUP_CONCAT(DISTINCT m.`menu_id`)AS menuId,m.`name` \n" +
                "FROM `system_user_menu` AS m \n" +
                "LEFT JOIN `system_user_menu_field` AS tf ON tf.m_id=m.menu_id \n" +
                "LEFT JOIN `system_user_table_head` AS t ON t.id=tf.field_id  \n"+
                "WHERE m.`menu_id` in (");
        for (Integer id : ids) {
            if (ids.indexOf(id) > 0)
                sql.append(",");
            sql.append("'").append(id).append("'");
        }
        sql.append(")\n");
        sql.append("GROUP BY m.`menu_id`");
        return sql.toString();
    }
}
