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
    @Select("SELECT u.uid,u.user_name,t.id,t.head_name,t.menu_id,t.`top_type`,t.top_order FROM user_info AS u \n" +
            "INNER JOIN user_role AS ur ON ur.`u_id`=u.`uid` \n" +
            "INNER JOIN role AS r ON r.`rid`=ur.`r_id` \n" +
            "INNER JOIN role_menu AS rm ON rm.`r_id`=r.`rid`\n" +
            "INNER JOIN menu AS m ON m.`menu_id`=rm.`m_id` \n" +
            "INNER JOIN `tb_head_menu` AS tm ON tm.m_id = m.`menu_id` \n" +
            "INNER JOIN `table_head` AS t ON tm.th_id = t.id\n" +
            "WHERE u.uid =#{uid}\n" +
            "GROUP BY t.head_name \n" +
            "ORDER BY top_order ")
    List<TableHead> findByHeader(@Param("uid") Long uid);

    /**
     * 通过mid查询一个数据
     * @param mid
     * @return
     */
    @Select("SELECT GROUP_CONCAT(DISTINCT t.head_name) as headName,GROUP_CONCAT(DISTINCT t.id) as id,GROUP_CONCAT(DISTINCT m.`menu_id`)AS menuId FROM `table_head` AS t\n" +
            "LEFT JOIN `tb_head_menu` AS tm ON tm.th_id=t.id\n" +
            "LEFT JOIN `menu` AS m ON m.`menu_id`=tm.m_id\n" +
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
    @Select("SELECT`id`,`head_name` FROM `table_head`")
    List<TableHead> findByHeadList();
}
