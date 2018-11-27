package com.dt.user.mapper;

import com.dt.user.model.TableHead;
import com.dt.user.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TableHeadMapper {

    /**
     * 根据用户id 查询对应显示的表头
     *
     * @return
     */
    @Select("SELECT u.uid,u.user_name,t.id,t.head_name,t.menu_id,t.`top_type`,t.top_order FROM user_info AS u\n" +
            "INNER JOIN user_role AS ur ON ur.`u_id`=u.`uid`\n" +
            "INNER JOIN role AS r ON r.`rid`=ur.`r_id`\n" +
            "INNER JOIN tb_head_role AS tr ON tr.`rid`=r.`rid`\n" +
            "INNER JOIN table_head AS t ON tr.`th_id`=t.id\n" +
            "WHERE u.uid =#{uid} GROUP BY t.head_name ORDER BY top_order")
    List<TableHead> findByHeader(@Param("uid") Long uid);
}
