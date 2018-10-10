package com.shiro.demoshiro.mapper;

import com.shiro.demoshiro.doman.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.Map;


@Mapper
public interface UserMapper {


    @Select("select uid, username,password,state from user_info where username=#{username}")
    @Results({
            @Result(id = true, column = "uid", property = "uid"),
            @Result(column = "username", property = "username"),
            @Result(column = "uid", property = "roles",
                    many = @Many(
                            select = "com.shiro.demoshiro.mapper.RolesMapper.getAllRolesByuid",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    User findByUser(String username);


    @MapKey("uid") //指定对象的id作为Map的KEY
    @Select("select * from user_info")
    Map<Integer, User> findByUserMap();

}
