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
     * 查询获得权限列表
     *
     * @return
     */
    @Select("SELECT DISTINCT m.perms FROM menu AS m\n" +
            "LEFT JOIN role_menu AS rm\n" +
            "ON m.`menu_id`=rm.`m_id`\n" +
            "LEFT JOIN user_role AS ur\n" +
            "ON rm.`r_id`=ur.`r_id`\n" +
            "WHERE ur.`u_id`=#{uid}")
    List<String> findByPermsMenu(@Param("uid") Long uid);

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
            "FROM \n" +
            "menu m\n" +
            "INNER JOIN role_menu rm ON m.menu_id = m_id\n" +
            "INNER JOIN role r ON r.rid= rm.r_id\n" +
            "WHERE rid= #{roleId}")
    List<Menu> findQueryByRoleId(@Param("roleId") Long roleId);

    /**
     * 添加一个菜单到角色组里
     *
     * @param roleMenu
     * @return
     */
    @Insert("INSERT INTO `mydb`.`role_menu` (`m_id`,`r_id`) VALUES (#{mId},#{rId});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int saveMenu(RoleMenu roleMenu);


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
