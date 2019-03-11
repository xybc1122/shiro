package com.dt.user.mapper;

import com.dt.user.dto.TableHeadDto;
import com.dt.user.model.TableHead;
import com.dt.user.provider.TableHeadProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
import java.util.Map;

@Mapper
public interface TableHeadMapper {

    /**
     * 根据菜单ID 查询对应显示的表头
     * 如果一对一 直接放t.id 就会 直接放到 嵌套对象里显示 需要t.id as h_id  给个别名
     *
     * @return
     */
    @Select("SELECT t.id as h_id ,t.menu_id,t.id,t.head_name,t.`top_type`,t.top_order,t.is_fixed ,t.input_type FROM `system_user_table_head` AS t\n" +
            "LEFT JOIN `system_user_menu_field` AS mf ON mf.field_id= t.`id`\n" +
            "WHERE  mf.m_id = #{mId}")
    @Results({
            //数据库字段映射 //数据库字段映射 column数据库字段 property Java 字段
            @Result(column = "h_id", property = "statusOptions",
                    one = @One(
                            select = "com.dt.user.mapper.AccountStatusOptionsMapper.getAccountStatusOptionsInfo",
                            fetchType = FetchType.EAGER
                    )
            )
    })
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
    @Select("SELECT`id`,`head_name`,`menu_id`,top_order,top_type FROM `system_user_table_head`")
    List<TableHead> findByHeadList();

    /**
     * 查询引用表头信息
     */
    @Select("SELECT`id`,`head_name`,`menu_id`,top_order,top_type FROM `system_user_table_head` where is_reference =true")
    List<TableHead> getIsReference();

    /**
     * 更新菜单下的头 排序
     *
     * @return
     */
    @UpdateProvider(type = TableHeadProvider.class, method = "upHeadSort")
    int upHeadSort(@Param("newTopOrder") String[] newTopOrder, @Param("id") Long id);
}
