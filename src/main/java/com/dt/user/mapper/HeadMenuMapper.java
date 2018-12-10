package com.dt.user.mapper;


import com.dt.user.model.TbHeadMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HeadMenuMapper {

    /**
     * 新增菜单关联数据
     */
    @Insert("INSERT INTO `system_user_menu_field`(`m_id`,`field_id`)\n" +
            "VALUES (#{mId},#{thId})")
    int addHeadMenu(TbHeadMenu tbHeadMenu);

    /**
     * 删除菜单关联
     */
    @Delete("DELETE FROM `system_user_menu_field`\n" +
            "WHERE `field_id` = #{thId} AND m_id=#{mId};\n")
    int delHeadMenu(TbHeadMenu tbHeadMenu);
}
