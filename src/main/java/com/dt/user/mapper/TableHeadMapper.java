package com.dt.user.mapper;

import com.dt.user.model.TableHead;
import com.dt.user.provider.TableHeadProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface TableHeadMapper {

    /**
     * 根据菜单ID 查询对应显示的表头
     *
     * @return
     */
    @Select("SELECT t.id,t.head_name,t.`top_type`,t.top_order FROM `system_user_table_head` AS t\n" +
            "LEFT JOIN `system_user_menu_field` AS mf ON mf.field_id= t.`id`\n" +
            "WHERE  mf.m_id = #{mId} ORDER BY top_order")
    List<TableHead> findByHeader(@Param("mId") Long mId);

    /**
     * 通过mid查询一个数据
     *
     * @param mid
     * @return
     */
    @Select("SELECT GROUP_CONCAT(DISTINCT t.head_name) as headName,GROUP_CONCAT(DISTINCT t.id) as id,GROUP_CONCAT(DISTINCT m.`menu_id`)AS menuId FROM `system_user_table_head` AS t\n" +
            "LEFT JOIN `system_user_menu_field` AS tf ON tf.field_id=t.id\n" +
            "LEFT JOIN `system_user_menu` AS m ON m.`menu_id`=tf.m_id\n" +
            "WHERE  m.`menu_id`=#{mId}")
    TableHead getTableHead(@Param("mId") Long mid);

    /**
     * 根据多个菜单id查询对应显示的表头 in
     */
    @SelectProvider(type = TableHeadProvider.class, method = "showTableHead")
    List<TableHead> getTableHeadList(Map<String, Object> mapHead);

    /**
     * 查询所有表头信息 然后service 层通过mid来区别对应的表头信息
     */
    @Select("SELECT`id`,`head_name`,`menu_id` FROM `system_user_table_head`")
    List<TableHead> findByHeadList();
}
