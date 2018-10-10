package com.shiro.demoshiro.mapper;

import com.shiro.demoshiro.doman.URole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RolesMapper {

    @Select("select * from u_role where id in(select rid from u_user_role where uid=#{uid})")
    List<URole> getAllRolesByuid(int uid);
}
