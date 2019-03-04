package com.dt.user.mapper;

import com.dt.user.model.SystemMenuIcon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName SystemMenuIconMapper
 * Description TODO
 * @Author 陈恩惠
 * @Date 2019/3/4 14:32
 **/
@Mapper
public interface SystemMenuIconMapper {
    /**
     * 获得所有icon
     *
     * @return
     */
    @Select("SELECT `i_id`,`icon` FROM `system_menu_icon`")
    List<SystemMenuIcon> getIconList();
}
