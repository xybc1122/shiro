package com.dt.user.mapper;

import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;
import com.dt.user.provider.MenuProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MenuMapper {


    /**
     * 查找用户所拥有的所有菜单
     *
     * @param
     * @return
     */
    @SelectProvider(type = MenuProvider.class, method = "findQueryMenuList")
    List<Menu> queryMenuList(UserInfo userInfo);

    /**
     * 根据角色id查询角色所拥有的菜单
     *
     * @param roleId
     * @return
     */
    @Select("SELECT \n" +
            "m.menu_id,m.`name`,parent_id,url,icon,menu_order\n" +
            "FROM system_user_menu m\n" +
            "INNER JOIN system_user_role_menu rm ON m.menu_id = m_id\n" +
            "INNER JOIN system_user_role r ON r.rid= rm.r_id\n" +
            "WHERE rid= #{roleId}")
    List<Menu> findQueryByRoleId(@Param("roleId") Long roleId);


    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    @InsertProvider(type = MenuProvider.class, method = "addMenu")
    int addMenu(@Param("menuList") List<Menu> menu);


}
