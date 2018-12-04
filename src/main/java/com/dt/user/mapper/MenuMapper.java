package com.dt.user.mapper;

import com.dt.user.model.Menu;
import com.dt.user.model.RoleMenu;
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
            "FROM menu m\n" +
            "INNER JOIN role_menu rm ON m.menu_id = m_id\n" +
            "INNER JOIN role r ON r.rid= rm.r_id\n" +
            "WHERE rid= #{roleId}")
    List<Menu> findQueryByRoleId(@Param("roleId") Long roleId);


    /**
     * 新增一个菜单
     *
     * @param menu
     * @return
     */
    @InsertProvider(type = MenuProvider.class, method = "addMenu")
    @Options(useGeneratedKeys = true, keyProperty = "menuId", keyColumn = "menu_id")
    int addMenu(Menu menu);

    /**
     * * 获取菜单表信息
     */
    @Select("select menu_id,`name`,parent_id,url,icon,menu_order,perms from menu where parent_id=0 ORDER BY menu_order ASC")
    List<Menu> findMenuList();


}
