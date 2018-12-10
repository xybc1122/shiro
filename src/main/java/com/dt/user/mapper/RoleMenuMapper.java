package com.dt.user.mapper;

import com.dt.user.model.RoleMenu;
import com.dt.user.provider.RoleMenuProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMenuMapper {


    /**
     * 通过角色id来查询拥有的菜单
     **/
    @Select("select m_id,r_id,id  from system_user_role_menu where r_id=#{rid}")
    List<RoleMenu> gerRoleMenus(@Param("rid") Long rid);

    /**
     * 添加角色跟菜单
     */
    @Insert("INSERT INTO `system_user_role_menu` (`m_id`, `r_id`) values(#{menuId}, #{rid})")
    int addRoleMenu(@Param("menuId") Long menuId, @Param("rid") Long rid);


    /**
     * 删除角色跟菜单
     */
    @DeleteProvider(type = RoleMenuProvider.class,method="delRoleMenu")
    int delRoleMenu(RoleMenu roleMenu);
}
