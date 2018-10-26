package com.shiro.demoshiro.mapper;

import com.shiro.demoshiro.doman.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.Map;


@Mapper
public interface UserMapper {


    @Select("select uid, user_name,pass_word,state,created_date  from user_info where user_name=#{username}")
    @Results({
            //column数据的字段  property 实体类的字段
            @Result(id = true, column = "uid", property = "uid"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "pass_word", property = "passWord"),
            @Result(column = "state", property = "state"),
            @Result(column = "created_date", property = "createdTime"),
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
