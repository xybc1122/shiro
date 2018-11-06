package com.dt.user.mapper;

import com.dt.user.model.Menu;
import com.dt.user.model.UserInfo;
import com.dt.user.provider.MenuProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

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
     * 查找用户的菜单
     *
     * @param
     * @return
     */
    @SelectProvider(type = MenuProvider.class, method = "findQueryMenuList")
    List<Menu> queryMenuList(UserInfo userInfo);


    /**
     * 通过用户id 查询菜单
     */
    @Select("select m.* FROM user_info AS u inner join user_role AS ur ON u.uid=ur.u_id" +
            "inner join role AS r  ON r.rid=ur.r_id" +
            "inner join role_menu AS rm ON r.rid=rm.r_id" +
            "inner join menu AS m  ON m.menu_id=rm.m_id" +
            "where u.uid=#{uid}  group by  m.menu_id  order by m.order asc")
    List<Menu> findByIdMenuList(@Param("uid") Long uid);


}
