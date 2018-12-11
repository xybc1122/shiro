package com.dt.user.mapper;

import com.dt.user.model.TableHead;
import com.dt.user.provider.TableHeadProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface TableHeadMapper {

    /**
     * 根据用户id 查询对应显示的表头
     *
     * @return
     */
    @Select("SELECT u.uid,u.user_name,t.id,t.head_name,t.menu_id,t.`top_type`,t.top_order FROM system_user_info AS u \n" +
            "INNER JOIN system_user_role_user AS ur ON ur.`u_id`=u.`uid` \n" +
            "INNER JOIN system_user_role AS r ON r.`rid`=ur.`r_id` \n" +
            "INNER JOIN system_user_role_menu AS rm ON rm.`r_id`=r.`rid`\n" +
            "INNER JOIN system_user_menu AS m ON m.`menu_id`=rm.`m_id` \n" +
            "INNER JOIN `system_user_menu_field` AS tf ON tf.m_id = m.`menu_id` \n" +
            "INNER JOIN `system_user_table_head` AS t ON tf.field_id = t.id\n" +
            "WHERE u.uid =#{uid}\n" +
            "GROUP BY t.head_name \n" +
            "ORDER BY top_order ")
    List<TableHead> findByHeader(@Param("uid") Long uid);

    /**
     * 通过mid查询一个数据
     * @param mid
     * @return
     */
    @Select("SELECT GROUP_CONCAT(DISTINCT t.head_name) as headName,GROUP_CONCAT(DISTINCT t.id) as id,GROUP_CONCAT(DISTINCT m.`menu_id`)AS menuId FROM `system_user_table_head` AS t\n" +
            "LEFT JOIN `system_user_menu_field` AS tf ON tf.field_id=t.id\n" +
            "LEFT JOIN `system_user_menu` AS m ON m.`menu_id`=tf.m_id\n" +
            "WHERE  m.`menu_id`=#{mId}")
    TableHead getTableHead(@Param("mId")Long mid);
    /**
     * 根据菜单id查询对应显示的表头
     */
    @SelectProvider(type = TableHeadProvider.class,method = "showTableHead")
    List<TableHead> getTableHeadList(Map<String, Object> mapHead);

    /**
     * 查询所有表头信息
     */
    @Select("SELECT`id`,`head_name` FROM `system_user_table_head`")
    List<TableHead> findByHeadList();
}
