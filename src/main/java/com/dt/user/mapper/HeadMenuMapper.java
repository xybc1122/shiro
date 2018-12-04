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
    @Insert("INSERT INTO `tb_head_menu`(`m_id`,`th_id`)\n" +
            "VALUES (#{mId},#{thId})")
    int addHeadMenu(TbHeadMenu tbHeadMenu);

    /**
     * 删除菜单关联
     */
    @Delete("DELETE\n" +
            "FROM `tb_head_menu`\n" +
            "WHERE `th_id` = #{thId} AND m_id=#{mId};\n")
    int delHeadMenu(TbHeadMenu tbHeadMenu);
}
