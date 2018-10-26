package com.shiro.demoshiro.mapper;

import com.shiro.demoshiro.doman.URole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RolesMapper {

    @Select("select r_name,types,rid from u_role where rid in(select rid from u_user_role where uid=#{uid})")
    @Results({
            @Result(id = true, column = "rid", property = "rId"),
            @Result(column = "r_name", property = "rName"),
            @Result(column = "types", property = "types"),
    })
    List<URole> getAllRolesByuid(int uid);
}
